document.addEventListener('DOMContentLoaded', () => {
    // Manejo de expandibles en la navegación
    const expandables = document.querySelectorAll('.expandable');

    expandables.forEach(expandable => {
        const span = expandable.querySelector('span');
        span.addEventListener('click', () => {
            expandable.classList.toggle('expanded');
        });
    });

    // Implementar búsqueda
    const searchInput = document.querySelector('.search-bar input');
    searchInput.addEventListener('input', (e) => {
        const searchTerm = e.target.value.toLowerCase();
        const sections = document.querySelectorAll('.doc-article section');

        sections.forEach(section => {
            const text = section.textContent.toLowerCase();
            const shouldShow = text.includes(searchTerm);
            section.style.display = shouldShow ? 'block' : 'none';

            // Si hay término de búsqueda y la sección es visible, expandir las subsecciones
            if (searchTerm && shouldShow) {
                const subsections = section.querySelectorAll('.subsection');
                subsections.forEach(sub => sub.style.display = 'block');
            }
        });
    });
});