document.addEventListener('DOMContentLoaded', () => {
    const components = document.querySelectorAll('.component');
    const canvasElement = document.getElementById('canvas');

    components.forEach(component => {
        component.addEventListener('dragstart', handleDragStart);
    });

    canvasElement.addEventListener('dragover', handleDragOver);
    canvasElement.addEventListener('drop', handleDrop);

    function handleDragStart(e) {
        e.dataTransfer.setData('text/plain', e.target.dataset.type);
    }

    function handleDragOver(e) {
        e.preventDefault();
    }

    function handleDrop(e) {
        e.preventDefault();
        const type = e.dataTransfer.getData('text/plain');
        const rect = canvasElement.getBoundingClientRect();
        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        canvas.addComponent({
            type,
            name: type.charAt(0).toUpperCase() + type.slice(1),
            icon: getComponentIcon(type),
            x,
            y
        });
    }

    function getComponentIcon(type) {
        const icons = {
            usuarios: '👤',
            carrito: '🛒',
            notificaciones: '🔔',
            catalogo: '📚',
            envios: '🚢',
            ordenes: '📋'
        };
        return icons[type] || '📦';
    }
});