document.addEventListener('DOMContentLoaded', () => {
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
            section.style.display = text.includes(searchTerm) ? 'block' : 'none';
        });
    });
});