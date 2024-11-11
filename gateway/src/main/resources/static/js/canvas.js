class Canvas {
    constructor() {
        this.canvas = document.getElementById('canvas');
        this.components = new Map();
        this.connections = new Set();
        this.isConnecting = false;
        this.sourceComponent = null;

        this.initializeEventListeners();
    }

    initializeEventListeners() {
        this.canvas.addEventListener('mousemove', this.handleMouseMove.bind(this));
        this.canvas.addEventListener('click', this.handleCanvasClick.bind(this));
    }

    addComponent(component) {
        const id = 'component-' + Date.now();
        const elem = document.createElement('div');
        elem.className = 'canvas-component';
        elem.id = id;
        elem.innerHTML = `
            <div class="icon">${component.icon}</div>
            <span>${component.name}</span>
            <div class="actions">
                <button class="connect-btn" title="Conectar">⚡</button>
                <button class="delete-btn" title="Eliminar">×</button>
            </div>
        `;

        elem.style.left = component.x + 'px';
        elem.style.top = component.y + 'px';

        this.canvas.appendChild(elem);
        this.components.set(id, { elem, connections: new Set() });

        this.makeComponentDraggable(elem);

        // Event listeners for buttons
        const deleteBtn = elem.querySelector('.delete-btn');
        const connectBtn = elem.querySelector('.connect-btn');

        deleteBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.removeComponent(id);
        });

        connectBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.handleConnectClick(elem);
        });
    }

    handleConnectClick(elem) {
        if (this.isConnecting) {
            if (this.sourceComponent !== elem && !this.areComponentsConnected(this.sourceComponent, elem)) {
                this.createConnection(this.sourceComponent, elem);
            }
            this.endConnectionMode();
        } else {
            this.startConnectionMode(elem);
        }
    }

    areComponentsConnected(comp1, comp2) {
        const comp1Data = this.components.get(comp1.id);
        const comp2Data = this.components.get(comp2.id);

        for (let connectionId of comp1Data.connections) {
            const connection = document.getElementById(connectionId);
            if (!connection) continue;

            const [id1, id2] = this.getConnectionEndpoints(connection);
            if ((id1 === comp1.id && id2 === comp2.id) ||
                (id1 === comp2.id && id2 === comp1.id)) {
                return true;
            }
        }
        return false;
    }

    getConnectionEndpoints(connection) {
        return [connection.dataset.source, connection.dataset.target];
    }

    startConnectionMode(elem) {
        this.isConnecting = true;
        this.sourceComponent = elem;
        elem.classList.add('connecting');
        this.createTemporaryLine();
    }

    endConnectionMode() {
        if (this.sourceComponent) {
            this.sourceComponent.classList.remove('connecting');
        }
        this.isConnecting = false;
        this.sourceComponent = null;
        this.removeTemporaryLine();
    }

    removeComponent(id) {
        const component = this.components.get(id);
        if (component) {
            // Remove all connections
            component.connections.forEach(connectionId => {
                const line = document.getElementById(connectionId);
                if (line) line.remove();
                this.connections.delete(connectionId);
            });

            // Remove references from other components
            this.components.forEach(comp => {
                comp.connections.forEach(connectionId => {
                    if (document.getElementById(connectionId)?.dataset.source === id ||
                        document.getElementById(connectionId)?.dataset.target === id) {
                        comp.connections.delete(connectionId);
                    }
                });
            });

            // Remove the component element
            component.elem.remove();
            this.components.delete(id);
        }
    }

    makeComponentDraggable(elem) {
        let isDragging = false;
        let currentX;
        let currentY;
        let initialX;
        let initialY;
        let xOffset = 0;
        let yOffset = 0;

        elem.addEventListener('mousedown', startDragging);

        function startDragging(e) {
            if (e.target.closest('.connect-btn, .delete-btn')) {
                return;
            }

            isDragging = true;
            initialX = e.clientX - xOffset;
            initialY = e.clientY - yOffset;

            if (e.target === elem) {
                document.addEventListener('mousemove', drag);
                document.addEventListener('mouseup', stopDragging);
            }
        }

        const drag = (e) => {
            if (isDragging) {
                e.preventDefault();

                currentX = e.clientX - initialX;
                currentY = e.clientY - initialY;

                xOffset = currentX;
                yOffset = currentY;

                elem.style.left = currentX + 'px';
                elem.style.top = currentY + 'px';

                // Update connections
                this.updateConnections(elem.id);
            }
        };

        const stopDragging = () => {
            isDragging = false;
            document.removeEventListener('mousemove', drag);
            document.removeEventListener('mouseup', stopDragging);
        };
    }

    handleMouseMove(e) {
        if (this.isConnecting) {
            this.updateTemporaryLine(e);
        }
    }

    handleCanvasClick(e) {
        if (this.isConnecting && !e.target.closest('.canvas-component')) {
            this.endConnectionMode();
        }
    }

    createTemporaryLine() {
        const line = document.createElement('div');
        line.id = 'temp-line';
        line.className = 'connection-line';
        this.canvas.appendChild(line);
    }

    updateTemporaryLine(e) {
        const line = document.getElementById('temp-line');
        if (line && this.sourceComponent) {
            const start = this.getComponentCenter(this.sourceComponent);
            const end = {
                x: e.clientX - this.canvas.offsetLeft,
                y: e.clientY - this.canvas.offsetTop
            };
            this.drawLine(line, start, end);
        }
    }

    removeTemporaryLine() {
        const line = document.getElementById('temp-line');
        if (line) line.remove();
    }

    createConnection(start, end) {
        const connectionId = `connection-${Date.now()}`;
        const line = document.createElement('div');
        line.id = connectionId;
        line.className = 'connection-line';
        line.dataset.source = start.id;
        line.dataset.target = end.id;

        // Add click event to remove connection
        line.addEventListener('click', () => this.removeConnection(connectionId));

        this.canvas.appendChild(line);

        this.components.get(start.id).connections.add(connectionId);
        this.components.get(end.id).connections.add(connectionId);

        this.updateConnection(connectionId, start, end);
        this.connections.add(connectionId);
    }

    removeConnection(connectionId) {
        const line = document.getElementById(connectionId);
        if (!line) return;

        const sourceId = line.dataset.source;
        const targetId = line.dataset.target;

        // Remove connection from components
        this.components.get(sourceId)?.connections.delete(connectionId);
        this.components.get(targetId)?.connections.delete(connectionId);

        // Remove line element
        line.remove();
        this.connections.delete(connectionId);
    }

    updateConnections(componentId) {
        const component = this.components.get(componentId);
        if (component) {
            component.connections.forEach(connectionId => {
                const line = document.getElementById(connectionId);
                if (line) {
                    const sourceId = line.dataset.source;
                    const targetId = line.dataset.target;
                    const sourceElem = document.getElementById(sourceId);
                    const targetElem = document.getElementById(targetId);

                    if (sourceElem && targetElem) {
                        this.updateConnection(connectionId, sourceElem, targetElem);
                    }
                }
            });
        }
    }

    updateConnection(connectionId, start, end) {
        const line = document.getElementById(connectionId);
        if (line) {
            const startPoint = this.getComponentCenter(start);
            const endPoint = this.getComponentCenter(end);
            this.drawLine(line, startPoint, endPoint);
        }
    }

    getComponentCenter(element) {
        const rect = element.getBoundingClientRect();
        const canvasRect = this.canvas.getBoundingClientRect();
        return {
            x: (rect.left + rect.right) / 2 - canvasRect.left,
            y: (rect.top + rect.bottom) / 2 - canvasRect.top
        };
    }

    drawLine(line, start, end) {
        const length = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
        const angle = Math.atan2(end.y - start.y, end.x - start.x) * 180 / Math.PI;

        line.style.width = length + 'px';
        line.style.left = start.x + 'px';
        line.style.top = start.y + 'px';
        line.style.transform = `rotate(${angle}deg)`;
    }
}

const canvas = new Canvas();