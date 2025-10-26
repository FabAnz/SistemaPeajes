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
 * Función que procesa TODAS las bonificaciones asignadas (HU 2.2)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaBonificaciones - Array de BonificacionAsignadaDTO con {nombreBonificacion, puesto, fechaAsignacion}
 */
function mostrar_bonificaciones(listaBonificaciones) {
    const tbody = document.getElementById('tabla-bonificaciones');
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
    
    // Limpiar el tbody por si acaso
    tbody.innerHTML = '';
    
    // Agregar todas las bonificaciones a la tabla
    listaBonificaciones.forEach(bonif => {
        // Crear una nueva fila
        const fila = document.createElement('tr');
        
        // Crear celdas para cada dato
        const celdaNombre = document.createElement('td');
        celdaNombre.textContent = bonif.nombreBonificacion;
        
        const celdaPuesto = document.createElement('td');
        celdaPuesto.textContent = bonif.puesto;
        
        const celdaFecha = document.createElement('td');
        celdaFecha.textContent = bonif.fechaAsignacion;
        
        // Agregar las celdas a la fila
        fila.appendChild(celdaNombre);
        fila.appendChild(celdaPuesto);
        fila.appendChild(celdaFecha);
        
        // Agregar la fila al tbody
        tbody.appendChild(fila);
    });
}

/**
 * Función que procesa TODOS los vehículos del propietario (HU 2.3)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaVehiculos - Array de VehiculoPropietarioDTO con {matricula, modelo, color, cantidadTransitos, montoTotalGastado}
 */
function mostrar_vehiculos(listaVehiculos) {
    const tbody = document.getElementById('tabla-vehiculos');
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
    
    // Limpiar el tbody por si acaso
    tbody.innerHTML = '';
    
    // Agregar todos los vehículos a la tabla
    listaVehiculos.forEach(vehiculo => {
        // Crear una nueva fila
        const fila = document.createElement('tr');
        
        // Crear celdas para cada dato
        const celdaMatricula = document.createElement('td');
        celdaMatricula.textContent = vehiculo.matricula;
        celdaMatricula.style.fontWeight = 'bold';
        
        const celdaModelo = document.createElement('td');
        celdaModelo.textContent = vehiculo.modelo;
        
        const celdaColor = document.createElement('td');
        celdaColor.textContent = vehiculo.color;
        
        const celdaCantidad = document.createElement('td');
        celdaCantidad.textContent = vehiculo.cantidadTransitos;
        celdaCantidad.style.textAlign = 'center';
        
        const celdaMonto = document.createElement('td');
        celdaMonto.textContent = formatearSaldo(vehiculo.montoTotalGastado);
        celdaMonto.style.textAlign = 'right';
        celdaMonto.style.fontWeight = 'bold';
        
        // Agregar las celdas a la fila
        fila.appendChild(celdaMatricula);
        fila.appendChild(celdaModelo);
        fila.appendChild(celdaColor);
        fila.appendChild(celdaCantidad);
        fila.appendChild(celdaMonto);
        
        // Agregar la fila al tbody
        tbody.appendChild(fila);
    });
}

/**
 * Función que procesa TODOS los tránsitos del propietario (HU 2.4)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaTransitos - Array de TransitoPropietarioDTO
 */
function mostrar_transitos(listaTransitos) {
    const tbody = document.getElementById('tabla-transitos');
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
    
    // Limpiar el tbody por si acaso
    tbody.innerHTML = '';
    
    // Agregar todos los tránsitos a la tabla
    listaTransitos.forEach(transito => {
        // Crear una nueva fila
        const fila = document.createElement('tr');
        
        // Crear celdas para cada dato
        const celdaPuesto = document.createElement('td');
        celdaPuesto.textContent = transito.puesto;
        
        const celdaMatricula = document.createElement('td');
        celdaMatricula.textContent = transito.matricula;
        celdaMatricula.style.fontWeight = 'bold';
        
        const celdaCategoria = document.createElement('td');
        celdaCategoria.textContent = transito.categoria;
        
        const celdaTarifa = document.createElement('td');
        celdaTarifa.textContent = formatearSaldo(transito.montoTarifa);
        celdaTarifa.style.textAlign = 'right';
        
        const celdaBonificacion = document.createElement('td');
        celdaBonificacion.textContent = transito.bonificacionAplicada;
        // Resaltar si hay bonificación
        if (transito.bonificacionAplicada !== 'Sin bonificación') {
            celdaBonificacion.style.color = '#28a745';
            celdaBonificacion.style.fontWeight = 'bold';
        } else {
            celdaBonificacion.style.color = '#6c757d';
        }
        
        const celdaDescuento = document.createElement('td');
        celdaDescuento.textContent = formatearSaldo(transito.montoBonificacion);
        celdaDescuento.style.textAlign = 'right';
        if (transito.montoBonificacion > 0) {
            celdaDescuento.style.color = '#28a745';
            celdaDescuento.style.fontWeight = 'bold';
        }
        
        const celdaPagado = document.createElement('td');
        celdaPagado.textContent = formatearSaldo(transito.montoPagado);
        celdaPagado.style.textAlign = 'right';
        celdaPagado.style.fontWeight = 'bold';
        
        const celdaFecha = document.createElement('td');
        celdaFecha.textContent = transito.fecha;
        
        const celdaHora = document.createElement('td');
        celdaHora.textContent = transito.hora;
        
        // Agregar las celdas a la fila
        fila.appendChild(celdaPuesto);
        fila.appendChild(celdaMatricula);
        fila.appendChild(celdaCategoria);
        fila.appendChild(celdaTarifa);
        fila.appendChild(celdaBonificacion);
        fila.appendChild(celdaDescuento);
        fila.appendChild(celdaPagado);
        fila.appendChild(celdaFecha);
        fila.appendChild(celdaHora);
        
        // Agregar la fila al tbody
        tbody.appendChild(fila);
    });
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

/**
 * Función que procesa TODAS las notificaciones del propietario (HU 2.5)
 * Convención de vistaWeb.js: mostrar_{id} donde id es el que viene en RespuestaDTO
 * 
 * @param {Array} listaNotificaciones - Array de NotificacionDTO con {id, mensaje, fecha, hora}
 */
function mostrar_notificaciones(listaNotificaciones) {
    const tbody = document.getElementById('tabla-notificaciones');
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
    
    // Limpiar el tbody por si acaso
    tbody.innerHTML = '';
    
    // Agregar todas las notificaciones a la tabla
    listaNotificaciones.forEach(notif => {
        const fila = document.createElement('tr');
        
        const celdaFecha = document.createElement('td');
        celdaFecha.textContent = notif.fecha;
        celdaFecha.style.whiteSpace = 'nowrap';
        
        const celdaHora = document.createElement('td');
        celdaHora.textContent = notif.hora;
        celdaHora.style.whiteSpace = 'nowrap';
        
        const celdaMensaje = document.createElement('td');
        celdaMensaje.textContent = notif.mensaje;
        
        fila.appendChild(celdaFecha);
        fila.appendChild(celdaHora);
        fila.appendChild(celdaMensaje);
        tbody.appendChild(fila);
    });
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