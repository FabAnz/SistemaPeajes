/**
 * Dashboard del Propietario
 * Historia de Usuario 2.1: Visualización de información personal
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/usuarios/dashboard-propietario";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

/**
 * Función que procesa la información del propietario
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Object} dto - PropietarioInfoDTO con {nombreCompleto, estado, saldo}
 */
function mostrar_propietario(dto) {
    // Actualizar nombre
    document.getElementById('nombre').textContent = dto.nombreCompleto;
    
    // Actualizar estado con badge de color según el estado
    const estadoElement = document.getElementById('estado');
    estadoElement.textContent = dto.estado;
    estadoElement.className = 'info-value badge badge-' + obtenerClaseEstado(dto.estado);
    
    // Actualizar saldo con formato de moneda
    document.getElementById('saldo').textContent = formatearSaldo(dto.saldo);
}

/**
 * Retorna la clase CSS según el estado del propietario
 * @param {string} estado - El estado del propietario
 * @returns {string} La clase CSS a aplicar
 */
function obtenerClaseEstado(estado) {
    switch(estado) {
        case 'Habilitado':
            return 'success';
        case 'Suspendido':
            return 'warning';
        case 'Penalizado':
            return 'warning';
        case 'Deshabilitado':
            return 'danger';
        default:
            return 'secondary';
    }
}

/**
 * Formatea el saldo como moneda
 * @param {number} saldo - El saldo a formatear
 * @returns {string} El saldo formateado
 */
function formatearSaldo(saldo) {
    return '$' + saldo.toLocaleString('es-UY');
}

/**
 * Función que maneja las excepciones de aplicación (status 299)
 * vistaWeb.js llama automáticamente a esta función cuando recibe errores del backend
 */
function excepcionDeAplicacion(mensaje) {
    // Parsear el mensaje JSON con estructura de RespuestaDTO
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
 * Función que procesa la redirección
 * Se ejecuta cuando el backend retorna {id: "redirigir", parametro: "/url"}
 * @param {string} paginaUrl - URL a la que redirigir
 */
function mostrar_redirigir(paginaUrl) {
    console.log("Redirigiendo a:", paginaUrl);
    window.location.href = paginaUrl;
}

/**
 * Cierra la sesión del usuario y redirige al login
 */
function cerrarSesion() {
    // Llamar al endpoint de logout usando vistaWeb.js
    submit('/acceso/logout', '', 'GET');
}

