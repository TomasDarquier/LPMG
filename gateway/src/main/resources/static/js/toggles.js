class Toggles {
    constructor() {
        this.toggles = {
            gateway: false,
            discoveryServer: false,
            configServer: false,
            jwtSecurity: false
        };

        this.init();
    }

    init() {
        const togglesContainer = document.createElement('div');
        togglesContainer.className = 'toggles-container';

        const toggles = [
            { id: 'gateway', label: 'API Gateway' },
            { id: 'discoveryServer', label: 'Discovery Server' },
            { id: 'configServer', label: 'Config Server' },
            { id: 'jwtSecurity', label: 'JWT Security' }
        ];

        toggles.forEach(toggle => {
            const toggleWrapper = document.createElement('div');
            toggleWrapper.className = 'toggle-wrapper';

            const label = document.createElement('span');
            label.textContent = toggle.label;

            const toggleButton = document.createElement('button');
            toggleButton.className = 'toggle-button';
            toggleButton.dataset.toggle = toggle.id;
            toggleButton.addEventListener('click', () => this.handleToggle(toggle.id));

            toggleWrapper.appendChild(label);
            toggleWrapper.appendChild(toggleButton);
            togglesContainer.appendChild(toggleWrapper);
        });

        document.getElementById('canvas').appendChild(togglesContainer);
        this.updateToggleStates();
    }

    handleToggle(toggleId) {
        const newState = !this.toggles[toggleId];

        if (toggleId === 'discoveryServer' && !newState) {
            this.toggles.configServer = false;
        } else if (toggleId === 'configServer' && newState && !this.toggles.discoveryServer) {
            return;
        }

        this.toggles[toggleId] = newState;
        this.updateToggleStates();
    }

    updateToggleStates() {
        Object.entries(this.toggles).forEach(([id, state]) => {
            const button = document.querySelector(`[data-toggle="${id}"]`);
            if (button) {
                button.classList.toggle('active', state);

                // Special handling for Config Server button
                if (id === 'configServer') {
                    button.classList.toggle('disabled', !this.toggles.discoveryServer);
                }
            }
        });
    }

    getTogglesState() {
        return this.toggles;
    }
}

window.togglesInstance = new Toggles();