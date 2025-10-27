/**
 * Dashboard del Administrador
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

/**
 * Función que procesa el mensaje de carga exitosa del dashboard
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {string} mensaje - Mensaje de confirmación del backend
 */
function mostrar_mensaje(mensaje) {
    console.log('✅ Dashboard cargado:', mensaje);
    // El dashboard se cargó correctamente y la sesión fue validada
}

/**
 * Función que procesa la redirección
 * Se ejecuta cuando el backend retorna {id: "redirigir", parametro: "/url"}
 * 
 * @param {string} paginaUrl - URL a la que redirigir
 */
function mostrar_redirigir(paginaUrl) {
    window.location.href = paginaUrl;
}

/**
 * Función que maneja las excepciones de aplicación (status 299)
 * vistaWeb.js llama automáticamente a esta función cuando recibe errores del backend
 * 
 * @param {string} mensaje - JSON string con el array de respuestas del backend
 */
function excepcionDeAplicacion(mensaje) {
    sessionException(mensaje);
}

/**
 * Función que maneja errores HTTP generales
 * vistaWeb.js llama a esta función cuando hay errores de red o del servidor
 * 
 * @param {number} status - Código de status HTTP (0 = error de red)
 * @param {string} text - Texto del error
 */
function procesarErrorSubmit(status, text) {
    console.error(`Error HTTP ${status}:`, text);
    
    let mensajeUsuario;
    
    if (status === 0) {
        // Error de red: sin conexión, CORS, timeout, servidor caído
        mensajeUsuario = 'No se pudo conectar con el servidor. Por favor, verifica tu conexión a Internet e intenta nuevamente.';
    } else {
        // Cualquier otro error HTTP (no debería ocurrir en esta aplicación)
        mensajeUsuario = `Error inesperado de comunicación (${status}). Por favor, contacta al administrador.`;
    }
    
    // Mostrar el error usando utilesVista.js
    mostrarMensaje(mensajeUsuario);
}
