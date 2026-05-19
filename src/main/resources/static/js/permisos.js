document.addEventListener("DOMContentLoaded", () => {

    document.querySelectorAll(".toggle-admin").forEach(toggle => {

        toggle.addEventListener("change", () => {

            const nombre = toggle.dataset.nombre;

            fetch('/admin/toggle-admin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `nombre=${nombre}`
            });

        });

    });

});