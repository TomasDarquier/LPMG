class Canvas {
    constructor() {
        this.canvas = document.getElementById('canvas');
        this.components = new Map();
        this.connections = new Set();
        this.isConnecting = false;
        this.sourceComponent = null;
        this.componentConfigs = new Map();
        this.globalConfig = {
            groupId: 'com.example',
            projectName: `Project-${new Date().toISOString()}`,
            version: '1.0.0',
            project: 'MAVEN',
            description: 'Generated Project'
        };

        this.templateMap = {
            usuarios: 'USER_SERVICE_V1',
            carrito: 'CART_SERVICE_V1',
            notificaciones: 'NOTIFICATION_SERVICE_V1',
            productos: 'PRODUCT_SERVICE_V1',
            envios: 'SHIPPING_SERVICE_V1',
            ordenes: 'ORDER_SERVICE_V1'
        };
     this.compatibilityMap = {
        usuarios: ['carrito', 'notificaciones', 'ordenes', 'envios'],
        carrito: ['usuarios', 'productos', 'notificaciones', 'ordenes'],
        ordenes: ['usuarios', 'notificaciones', 'envios', 'carrito'],
        productos: ['notificaciones', 'envios', 'ordenes'],
        envios: ['usuarios', 'notificaciones', 'ordenes'],
        notificaciones: ['usuarios', 'carrito', 'ordenes', 'productos', 'envios']
    };

        this.initializeEventListeners();
        this.addGlobalConfigButton();

    }

    addGlobalConfigButton() {
        const configBtn = document.createElement('button');
        configBtn.className = 'global-config-btn';
        configBtn.innerHTML = '⚙️ Global Config';
        configBtn.addEventListener('click', () => this.openGlobalConfigModal());
        this.canvas.appendChild(configBtn);
    }
    openGlobalConfigModal() {
        const modal = document.createElement('div');
        modal.className = 'config-modal';
        modal.innerHTML = `
        <h2>Global Configuration</h2>
        <div class="form-group">
            <label for="groupId">Group ID</label>
            <input type="text" id="groupId" value="${this.globalConfig.groupId}">
            <small id="error-message" class="error-message" style="color: red; display: none;">Formato inválido. Usa palabras entre puntos, sin espacios</small>
        </div>
        <div class="buttons">
            <button class="cancel-btn">Cancelar</button>
            <button class="accept-btn">Aceptar</button>
        </div>
    `;

        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        document.body.appendChild(overlay);
        document.body.appendChild(modal);

        const groupIdInput = modal.querySelector('#groupId');
        const errorMessage = modal.querySelector('#error-message');
        const cancelButton = modal.querySelector('.cancel-btn');
        const acceptButton = modal.querySelector('.accept-btn');

        const groupIdRegex = /^([a-zA-Z0-9]+(\.[a-zA-Z0-9]+)+)$/;

        cancelButton.addEventListener('click', () => {
            modal.remove();
            overlay.remove();
        });

        acceptButton.addEventListener('click', () => {
            const groupIdValue = groupIdInput.value.trim();

            if (groupIdRegex.test(groupIdValue)) {
                this.globalConfig.groupId = groupIdValue;
                modal.remove();
                overlay.remove();
            } else {
                errorMessage.style.display = 'block';
            }
        });

        groupIdInput.addEventListener('input', () => {
            errorMessage.style.display = 'none';
        });
    }

    initializeEventListeners() {
        this.canvas.addEventListener('mousemove', this.handleMouseMove.bind(this));
        this.canvas.addEventListener('click', this.handleCanvasClick.bind(this));

        const generateBtn = document.getElementById('generateCode');
        generateBtn.addEventListener('click', () => this.generateJson());
    }

    generateJson() {
        const services = [];
        let portCounter = 8080;

        this.components.forEach((component, id) => {
            const config = this.componentConfigs.get(id);
            const type = component.elem.getAttribute('data-type');
            const name = component.elem.querySelector('span').textContent;

            services.push({
                template: this.templateMap[type],
                sbVersion: '3.3.4',
                name: config.name || name.toLowerCase(),
                description: `Servicio para administrar ${name.toLowerCase()}`,
                groupId: this.globalConfig.groupId,
                artifactId: config.artifactId || `${type}-service`,
                baseDir: `${type}-service`,
                pathPrefix: config.endpointPrefix,
                persistence: config.persistence.toUpperCase(),
                port: config.port || (portCounter++).toString()
            });
        });

        const connections = [];
        this.connections.forEach(connectionId => {
            const connection = document.getElementById(connectionId);
            const sourceId = connection.dataset.source;
            const targetId = connection.dataset.target;
            const sourceConfig = this.componentConfigs.get(sourceId);
            const targetConfig = this.componentConfigs.get(targetId);

            connections.push({
                serviceOne: sourceConfig.name,
                serviceTwo: targetConfig.name,
                protocol: 'HTTP',
                type: sourceConfig.communication
            });
        });

        const toggleStates = window.togglesInstance.getTogglesState();
        const json = {
            projectName: this.globalConfig.projectName,
            version: this.globalConfig.version,
            project: this.globalConfig.project,
            description: this.globalConfig.description,
            services,
            connections,
            infrastructure: {
                configServer: toggleStates.configServer,
                'discovery-server': toggleStates.discoveryServer,
                gateway: toggleStates.gateway
            },
            security: {
                type: toggleStates.jwtSecurity ? 'JWT' : null
            }
        };

        const modelId = document.body.getAttribute('data-model-id');
        this.sendJsonToServer(json, modelId);
    }

    async sendJsonToServer(json, modelId) {
        const csrfToken = document.body.getAttribute('_csrftoken');
        console.log(json);
        try {
            const response = await fetch(`/submit/${modelId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: JSON.stringify(json)
            });

            const messageElem = document.createElement('div');
            messageElem.className = 'notification';

            if (response.ok) {
                messageElem.textContent = 'Código generado exitosamente. Descargalo desde tu Perfil.';
                messageElem.classList.add('success');
            } else {
                throw new Error('Error al enviar la configuración');
            }

            document.body.appendChild(messageElem);
        } catch (error) {
            console.error('Error:', error);

            const messageElem = document.createElement('div');
            messageElem.className = 'notification error';
            messageElem.textContent = 'Error al generar el código.';
            document.body.appendChild(messageElem);
        }

        setTimeout(() => {
            const notifications = document.querySelectorAll('.notification');
            notifications.forEach((notification) => {
                notification.remove();
            });
        }, 3000);
    }
    addComponent(component) {
        const existingComponent = Array.from(this.components.values())
            .find(comp => comp.elem.getAttribute('data-type') === component.type);

        if (existingComponent) {
            this.showNotification(`Ya existe un componente de tipo ${component.type}`, 'error');
            return;
        }

        const id = 'component-' + Date.now();
        const elem = document.createElement('div');
        elem.className = 'canvas-component';
        elem.id = id;
        elem.setAttribute('data-type', component.type);
        elem.innerHTML = `
            <div class="icon">${component.icon}</div>
            <span>${component.name}</span>
            <div class="actions">
                <button class="config-btn" title="Configurar">⚙️</button>
                <button class="connect-btn" title="Conectar">⚡</button>
                <button class="delete-btn" title="Eliminar">×</button>
            </div>
        `;

        elem.style.left = component.x + 'px';
        elem.style.top = component.y + 'px';

        this.canvas.appendChild(elem);
        this.components.set(id, { elem, connections: new Set() });

        this.componentConfigs.set(id, {
            name: component.name.toLowerCase(),
            artifactId: `${component.type}-service`,
            port: (8080 + this.components.size - 1).toString(),
            persistence: 'PostgreSQL',
            communication: 'REST',
            endpointPrefix: '/api/v1'
        });

        this.makeComponentDraggable(elem);

        const deleteBtn = elem.querySelector('.delete-btn');
        const connectBtn = elem.querySelector('.connect-btn');
        const configBtn = elem.querySelector('.config-btn');

        deleteBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.removeComponent(id);
        });

        connectBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.handleConnectClick(elem);
        });

        configBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            this.openConfigModal(id);
        });
    }
    areComponentsCompatible(source, target) {
        const sourceType = source.getAttribute('data-type');
        const targetType = target.getAttribute('data-type');
        return this.compatibilityMap[sourceType]?.includes(targetType);
    }

    openConfigModal(componentId) {
        const config = this.componentConfigs.get(componentId);
        const component = this.components.get(componentId);
        const componentName = component.elem.querySelector('span').textContent;

        const overlay = document.createElement('div');
        overlay.className = 'modal-overlay';
        document.body.appendChild(overlay);

        const showCache = config.name === 'carrito';
        const showRelationalDb = config.name === 'usuarios' || config.name === 'productos' || config.name === 'ordenes';
        const showNoRelationalDb = false;

        const modal = document.createElement('div');
        modal.className = 'config-modal';
        modal.innerHTML = `
        <h2>${componentName}</h2>
        <div class="form-group">
            <label for="name">Nombre del Servicio</label>
            <input type="text" id="name" value="${config.name}">
            <small class="error-message" id="name-error" style="color: red; display: none;">Solo caracteres alfanuméricos y guiones.</small>
        </div>
        <div class="form-group">
            <label for="artifactId">Artifact ID</label>
            <input type="text" id="artifactId" value="${config.artifactId}">
            <small class="error-message" id="artifactId-error" style="color: red; display: none;">Solo caracteres alfanuméricos y guiones.</small>
        </div>
        <div class="form-group">
            <label for="port">Puerto</label>
            <input type="text" id="port" value="${config.port}">
            <small class="error-message" id="port-error" style="color: red; display: none;">Debe ser un número entre 1 y 60000.</small>
        </div>
        <div class="form-group">
            <label for="persistence">Persistencia</label>
            <select id="persistence">
                ${showRelationalDb ? `
                    <option value="PostgreSQL" ${config.persistence === 'PostgreSQL' ? 'selected' : ''}>PostgreSQL</option>
                    <option value="MySQL" ${config.persistence === 'MySQL' ? 'selected' : ''}>MySQL</option>
                ` : ''}
                ${showNoRelationalDb ? `
                    <option value="MongoDB" ${config.persistence === 'MongoDB' ? 'selected' : ''}>MongoDB</option>
                ` : ''}
                ${showCache ? `
                    <option value="Redis" ${config.persistence === 'Redis' ? 'selected' : ''}>Redis</option>
                ` : ''}
            </select>
        </div>
        <div class="form-group">
            <label for="communication">Comunicación</label>
            <select id="communication">
                <option value="REST" ${config.communication === 'REST' ? 'selected' : ''}>REST</option>
                <option value="GraphQL" ${config.communication === 'GraphQL' ? 'selected' : ''}>GraphQL</option>
            </select>
        </div>
        <div class="form-group">
            <label for="endpointPrefix">Endpoints Prefix</label>
            <input type="text" id="endpointPrefix" value="${config.endpointPrefix}">
            <small class="error-message" id="endpointPrefix-error" style="color: red; display: none;">Debe iniciar con / y solo contener letras,<br>números y /. No terminar con /.</small>
        </div>
        <div class="buttons">
            <button class="cancel-btn">Cancelar</button>
            <button class="accept-btn">Aceptar</button>
        </div>
    `;

        document.body.appendChild(modal);

        const cancelBtn = modal.querySelector('.cancel-btn');
        const acceptBtn = modal.querySelector('.accept-btn');

        // Validación
        const validateInput = (input, regex, errorElement) => {
            if (!regex.test(input.value.trim())) {
                errorElement.style.display = 'block';
                return false;
            }
            errorElement.style.display = 'none';
            return true;
        };

        const alphanumericRegex = /^[a-zA-Z0-9-_]+$/;
        const portRegex = /^([1-9][0-9]{0,4})$/;
        const endpointRegex = /^\/[a-zA-Z0-9]+(\/[a-zA-Z0-9]+)*$/;

        cancelBtn.addEventListener('click', () => {
            modal.remove();
            overlay.remove();
        });

        acceptBtn.addEventListener('click', () => {
            const nameInput = modal.querySelector('#name');
            const artifactIdInput = modal.querySelector('#artifactId');
            const portInput = modal.querySelector('#port');
            const endpointPrefixInput = modal.querySelector('#endpointPrefix');

            const nameError = modal.querySelector('#name-error');
            const artifactIdError = modal.querySelector('#artifactId-error');
            const portError = modal.querySelector('#port-error');
            const endpointPrefixError = modal.querySelector('#endpointPrefix-error');

            const isNameValid = validateInput(nameInput, alphanumericRegex, nameError);
            const isArtifactIdValid = validateInput(artifactIdInput, alphanumericRegex, artifactIdError);
            const isPortValid = validateInput(portInput, portRegex, portError) && (parseInt(portInput.value) <= 65535);
            const isEndpointPrefixValid = validateInput(endpointPrefixInput, endpointRegex, endpointPrefixError);

            if (isNameValid && isArtifactIdValid && isPortValid && isEndpointPrefixValid) {
                config.name = nameInput.value.trim();
                config.artifactId = artifactIdInput.value.trim();
                config.port = parseInt(portInput.value.trim());
                config.endpointPrefix = endpointPrefixInput.value.trim();

                modal.remove();
                overlay.remove();
            }
        });

        modal.querySelectorAll('input').forEach(input => {
            input.addEventListener('input', () => {
                const errorElement = modal.querySelector(`#${input.id}-error`);
                errorElement.style.display = 'none';
            });
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
            component.connections.forEach(connectionId => {
                const line = document.getElementById(connectionId);
                if (line) line.remove();
                this.connections.delete(connectionId);
            });

            this.components.forEach(comp => {
                comp.connections.forEach(connectionId => {
                    if (document.getElementById(connectionId)?.dataset.source === id ||
                        document.getElementById(connectionId)?.dataset.target === id) {
                        comp.connections.delete(connectionId);
                    }
                });
            });

            this.componentConfigs.delete(id);

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
            if (e.target.closest('.connect-btn, .delete-btn, .config-btn')) {
                return;
            }

            const rect = elem.getBoundingClientRect();

            xOffset = rect.left - elem.parentElement.offsetLeft;
            yOffset = rect.top - elem.parentElement.offsetTop;

            initialX = e.clientX - xOffset;
            initialY = e.clientY - yOffset;

            isDragging = true;

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
        if (!this.areComponentsCompatible(start, end)) {
            this.showNotification(`${start.getAttribute('data-type')} no es compatible con ${end.getAttribute('data-type')}`, 'error');
            return;
        }

        const connectionId = `connection-${Date.now()}`;
        const line = document.createElement('div');
        line.id = connectionId;
        line.className = 'connection-line';
        line.dataset.source = start.id;
        line.dataset.target = end.id;

        line.addEventListener('click', () => this.removeConnection(connectionId));

        this.canvas.appendChild(line);

        this.components.get(start.id).connections.add(connectionId);
        this.components.get(end.id).connections.add(connectionId);

        this.updateConnection(connectionId, start, end);
        this.connections.add(connectionId);
    }

    showNotification(message, type = 'info') {
        const notification = document.createElement('div');
        notification.className = `notification ${type}`;
        notification.textContent = message;
        document.body.appendChild(notification);

        setTimeout(() => {
            notification.remove();
        }, 3000);
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
