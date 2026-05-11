document.addEventListener("DOMContentLoaded", () => {

    // AÑADIR PRODUCTO
    document.querySelectorAll(".btn-add").forEach(btn => {
        btn.addEventListener("click", () => {

            const nombre = btn.dataset.nombre;
            const precio = btn.dataset.precio;

            fetch('/add-carrito', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `nombre=${nombre}&precio=${precio}`
            })
            .then(() => location.reload());
        });
    });

    // SUMAR
    document.querySelectorAll(".btn-sumar").forEach(btn => {
        btn.addEventListener("click", () => {

            const nombre = btn.dataset.nombre;

            fetch('/sumar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `nombre=${nombre}`
            })
            .then(() => location.reload());
        });
    });

    // RESTAR
    document.querySelectorAll(".btn-restar").forEach(btn => {
        btn.addEventListener("click", () => {

            const nombre = btn.dataset.nombre;

            fetch('/restar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: `nombre=${nombre}`
            })
            .then(() => location.reload());
        });
    });

});