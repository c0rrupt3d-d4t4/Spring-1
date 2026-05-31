document.addEventListener("DOMContentLoaded", () => {

	// Por cada switch de cada usuario le añade el listener recogiendo el nombre
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