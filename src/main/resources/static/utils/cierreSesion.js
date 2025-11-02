function sessionException(mensaje) {
    try {
        const respuestas = JSON.parse(mensaje);
        if (Array.isArray(respuestas)) {
            let logout = false;
            let mensajeError = null;

            // Extraer redirección y mensaje de error
            respuestas.forEach(resp => {
                if (resp.id === 'logout') {
                    logout = true;
                    mensajeError = resp.parametro;
                }
                if (resp.id === 'mensaje') {
                    mensajeError = resp.parametro;
                }
            });

            // Si hay mensaje de error, guardarlo en sessionStorage para mostrarlo en el login
            if (mensajeError) {
                sessionStorage.setItem('mensajeLogin', mensajeError);
            }

            // Si hay redirección, redirigir
            if (logout) {
                cerrarSesion();
            }
        }
    } catch (e) {
        console.error('Error parseando respuesta:', e);
    }
}

/**
 * Cierra la sesión del usuario y redirige al login
 */
function cerrarSesion() {
    // Llamar al endpoint de logout usando vistaWeb.js
    submit('/acceso/logout', '', 'GET');
}