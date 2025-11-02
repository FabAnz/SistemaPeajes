/**
 * Dashboard del Propietario
 * Historia de Usuario 2.1: Visualización de información personal
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/propietario/dashboard";
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
 * Función que procesa TODAS las bonificaciones asignadas (HU 2.2)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaBonificaciones - Array de BonificacionAsignadaDTO con {nombreBonificacion, puesto, fechaAsignacion}
 */
function mostrar_bonificaciones(listaBonificaciones) {
    const contenedor = document.getElementById('tabla-bonificaciones');
    const mensaje = document.getElementById('mensaje-sin-bonificaciones');
    const tabla = document.getElementById('tabla-bonificaciones-container');
    
    // Verificar si hay bonificaciones
    if (!listaBonificaciones || listaBonificaciones.length === 0) {
        // No hay bonificaciones, mostrar mensaje
        mensaje.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    // Hay bonificaciones, mostrar tabla y ocultar mensaje
    mensaje.style.display = 'none';
    tabla.style.display = 'table';
    
    // Usar utilesVista.js para generar la tabla automáticamente
    // Extraer solo las filas del tbody para evitar duplicar el thead
    const tablaCompleta = crearTablaDesdeJson(listaBonificaciones);
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = tablaCompleta;
    const tbodyGenerado = tempDiv.querySelector('tbody');
    if (tbodyGenerado) {
        contenedor.innerHTML = tbodyGenerado.innerHTML;
    } else {
        contenedor.innerHTML = tablaCompleta;
    }
}

/**
 * Función que procesa TODOS los vehículos del propietario (HU 2.3)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaVehiculos - Array de VehiculoPropietarioDTO con {matricula, modelo, color, cantidadTransitos, montoTotalGastado}
 */
function mostrar_vehiculos(listaVehiculos) {
    const contenedor = document.getElementById('tabla-vehiculos');
    const mensaje = document.getElementById('mensaje-sin-vehiculos');
    const tabla = document.getElementById('tabla-vehiculos-container');
    
    // Verificar si hay vehículos
    if (!listaVehiculos || listaVehiculos.length === 0) {
        // No hay vehículos, mostrar mensaje
        mensaje.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    // Hay vehículos, mostrar tabla y ocultar mensaje
    mensaje.style.display = 'none';
    tabla.style.display = 'table';
    
    // Formatear el monto antes de mostrar
    const vehiculosFormateados = listaVehiculos.map(v => ({
        ...v,
        montoTotalGastado: formatearSaldo(v.montoTotalGastado)
    }));
    
    // Usar utilesVista.js para generar la tabla automáticamente
    // Extraer solo las filas del tbody para evitar duplicar el thead
    const tablaCompleta = crearTablaDesdeJson(vehiculosFormateados);
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = tablaCompleta;
    const tbodyGenerado = tempDiv.querySelector('tbody');
    if (tbodyGenerado) {
        contenedor.innerHTML = tbodyGenerado.innerHTML;
    } else {
        contenedor.innerHTML = tablaCompleta;
    }
}

/**
 * Función que procesa TODOS los tránsitos del propietario (HU 2.4)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaTransitos - Array de TransitoPropietarioDTO
 */
function mostrar_transitos(listaTransitos) {
    const contenedor = document.getElementById('tabla-transitos');
    const mensaje = document.getElementById('mensaje-sin-transitos');
    const tabla = document.getElementById('tabla-transitos-container');
    
    // Verificar si hay tránsitos
    if (!listaTransitos || listaTransitos.length === 0) {
        // No hay tránsitos, mostrar mensaje
        mensaje.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    // Hay tránsitos, mostrar tabla y ocultar mensaje
    mensaje.style.display = 'none';
    tabla.style.display = 'table';
    
    // Formatear los montos antes de mostrar
    const transitosFormateados = listaTransitos.map(t => ({
        ...t,
        montoTarifa: formatearSaldo(t.montoTarifa),
        montoBonificacion: formatearSaldo(t.montoBonificacion),
        montoPagado: formatearSaldo(t.montoPagado)
    }));
    
    // Usar utilesVista.js para generar la tabla automáticamente
    // Extraer solo las filas del tbody para evitar duplicar el thead
    const tablaCompleta = crearTablaDesdeJson(transitosFormateados);
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = tablaCompleta;
    const tbodyGenerado = tempDiv.querySelector('tbody');
    if (tbodyGenerado) {
        contenedor.innerHTML = tbodyGenerado.innerHTML;
    } else {
        contenedor.innerHTML = tablaCompleta;
    }
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
    sessionException(mensaje);
}

function mostrar_notificaciones(listaNotificaciones) {
    const contenedor = document.getElementById('tabla-notificaciones');
    const mensaje = document.getElementById('mensaje-sin-notificaciones');
    const tabla = document.getElementById('tabla-notificaciones-container');
    
    // Verificar si hay notificaciones
    if (!listaNotificaciones || listaNotificaciones.length === 0) {
        // No hay notificaciones, mostrar mensaje
        mensaje.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    // Hay notificaciones, mostrar tabla y ocultar mensaje
    mensaje.style.display = 'none';
    tabla.style.display = 'table';
    
    // Remover el campo 'id' para no mostrarlo en la tabla
    const notificacionesSinId = listaNotificaciones.map(notif => ({
        fecha: notif.fecha,
        hora: notif.hora,
        mensaje: notif.mensaje
    }));
    
    // Usar utilesVista.js para generar la tabla automáticamente
    // Extraer solo las filas del tbody para evitar duplicar el thead
    const tablaCompleta = crearTablaDesdeJson(notificacionesSinId);
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = tablaCompleta;
    const tbodyGenerado = tempDiv.querySelector('tbody');
    if (tbodyGenerado) {
        contenedor.innerHTML = tbodyGenerado.innerHTML;
    } else {
        contenedor.innerHTML = tablaCompleta;
    }
}

/**
 * Función que procesa la redirección
 * Se ejecuta cuando el backend retorna {id: "redirigir", parametro: "/url"}
 * @param {string} paginaUrl - URL a la que redirigir
 */
function mostrar_redirigir(paginaUrl) {
    window.location.href = paginaUrl;
}

/**
 * Función que borra todas las notificaciones del propietario
 * Muestra un diálogo de confirmación antes de borrar
 */
async function borrarNotificaciones() {
    // Verificar si hay notificaciones antes de mostrar confirmación
    const tabla = document.getElementById('tabla-notificaciones-container');
    if (tabla.style.display === 'none') {
        mostrarMensaje('No hay notificaciones para borrar');
        return;
    }
    
    // Confirmación antes de borrar usando utilesVista.js
    const confirmar = await mostrarConfirmacion(
        '¿Estás seguro que deseas borrar todas las notificaciones? Esta acción no se puede deshacer.'
    );
    
    if (!confirmar) {
        return;
    }
    
    // Deshabilitar el botón para evitar clics múltiples
    const boton = document.getElementById('btn-borrar-notificaciones');
    const textoOriginal = boton.innerHTML;
    boton.disabled = true;
    boton.innerHTML = '⏳ Borrando...';
    
    // Usar vistaWeb.js submit() en lugar de fetch directo
    // Esto maneja automáticamente las respuestas y llama a las funciones mostrar_*
    submit('/propietario/notificaciones', '', 'DELETE');
    
    // Rehabilitar el botón después de un breve delay para que el usuario vea el feedback
    setTimeout(() => {
        boton.disabled = false;
        boton.innerHTML = textoOriginal;
    }, 500);
}

/**
 * Función llamada automáticamente por vistaWeb.js cuando se borran notificaciones exitosamente
 * Convención: mostrar_{id} donde id='mensaje' viene en RespuestaDTO del backend
 */
function mostrar_mensaje(mensaje) {
    // Mostrar mensaje de éxito usando utilesVista.js
    mostrarMensaje(mensaje);
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
