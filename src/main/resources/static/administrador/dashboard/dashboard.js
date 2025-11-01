/**
 * Dashboard del Administrador - Menú Principal
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
// Valida la sesión y permisos del administrador
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

/**
 * Funciones para procesar datos iniciales (aunque no los mostremos en el menú,
 * el backend los envía al validar la sesión)
 */
function mostrar_puestos(puestos) {
    // No necesitamos mostrar los puestos en el menú principal
    console.log(`✅ Sesión válida - ${puestos.length} puestos disponibles`);
}

function mostrar_bonificaciones(bonificaciones) {
    // No necesitamos mostrar las bonificaciones en el menú principal
    console.log(`✅ ${bonificaciones.length} bonificaciones disponibles`);
}

function mostrar_estados(estados) {
    // No necesitamos mostrar los estados en el menú principal
    console.log(`✅ ${estados.length} estados disponibles`);
}

function mostrar_mensaje(mensaje) {
    mostrarMensaje(mensaje);
}

function mostrar_redirigir(paginaUrl) {
    window.location.href = paginaUrl;
}

/**
 * Función que maneja las excepciones de aplicación (status 299)
 * Si tiene logout → usa sessionException (que redirige al login)
 * Si NO tiene logout → muestra el mensaje directamente
 */
function excepcionDeAplicacion(mensaje) {
    // Usar sessionException para manejar automáticamente redirecciones al login
    sessionException(mensaje);
}

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