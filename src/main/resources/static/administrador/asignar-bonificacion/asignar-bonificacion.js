/**
 * Página: Asignar Bonificaciones
 */

// Configuración de vistaWeb.js
var urlIniciarVista = "/administrador/asignar-bonificacion";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

// Estado interno
let asignacionBonificacionRecienCompletada = false; // Flag para evitar mostrar bonificaciones después de asignar

/**
 * Función que carga los puestos disponibles
 */
function mostrar_puestos(puestos) {
    // Cargar puestos en el select de asignar bonificación
    const selectPuestoBonificacion = document.getElementById('puestoBonificacion');
    if (!selectPuestoBonificacion) return;
    
    puestos.forEach(puesto => {
        const option = document.createElement('option');
        option.value = puesto.id;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        selectPuestoBonificacion.appendChild(option);
    });
    
    console.log(`✅ ${puestos.length} puestos cargados`);
}

/**
 * Función que carga las bonificaciones disponibles en el select
 */
function mostrar_bonificaciones(bonificaciones) {
    const selectBonificacion = document.getElementById('bonificacion');
    if (!selectBonificacion) return;
    
    bonificaciones.forEach(bonif => {
        const option = document.createElement('option');
        option.value = bonif.nombre;
        option.textContent = bonif.nombre;
        selectBonificacion.appendChild(option);
    });
    
    console.log(`✅ ${bonificaciones.length} bonificaciones cargadas`);
}

/**
 * Función que muestra información del propietario buscado
 */
function mostrar_propietario(propietario) {
    console.log('📋 Mostrando información del propietario:', propietario);
    
    document.getElementById('nombrePropietarioBonificacion').textContent = propietario.nombreCompleto;
    document.getElementById('estadoPropietarioBonificacion').textContent = propietario.estado;
    document.getElementById('infoPropietarioBonificacion').style.display = 'block';
    document.getElementById('cedulaBonificacion').value = propietario.cedula;
    const selectBon = document.getElementById('bonificacion');
    const selectPue = document.getElementById('puestoBonificacion');
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (selectBon) selectBon.disabled = false;
    if (selectPue) selectPue.disabled = false;
    if (btnAsignar) btnAsignar.disabled = false;
}

/**
 * Función que muestra las bonificaciones asignadas del propietario
 */
function mostrar_bonificacionesAsignadas(bonificaciones) {
    console.log('🎁 Mostrando bonificaciones asignadas:', bonificaciones);
    
    // Si acabamos de asignar una bonificación, no mostrar la tabla (se limpió en mostrar_mensaje)
    if (asignacionBonificacionRecienCompletada) {
        console.log('↪️ Se omite actualización de bonificaciones (asignación reciente)');
        return;
    }
    
    const contenedor = document.getElementById('tabla-bonificaciones-asignadas');
    const mensaje = document.getElementById('mensaje-sin-bonificaciones-asignadas');
    const tabla = document.getElementById('tabla-bonificaciones-asignadas-container');
    
    // La sección siempre está visible, solo limpiamos o llenamos la tabla
    if (!contenedor) return;
    
    // Limpiar tabla siempre
    contenedor.innerHTML = '';
    
    if (!bonificaciones || bonificaciones.length === 0) {
        // Si no hay bonificaciones, mostrar mensaje y ocultar tabla
        if (mensaje) mensaje.style.display = 'block';
        if (tabla) tabla.style.display = 'none';
        console.log('ℹ️ No hay bonificaciones para mostrar');
        return;
    }
    
    // Hay bonificaciones, mostrar tabla y ocultar mensaje
    if (mensaje) mensaje.style.display = 'none';
    if (tabla) tabla.style.display = 'table';
    
    // Usar utilesVista.js para generar la tabla automáticamente
    // Extraer solo las filas del tbody para evitar duplicar el thead
    const tablaCompleta = crearTablaDesdeJson(bonificaciones);
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = tablaCompleta;
    const tbodyGenerado = tempDiv.querySelector('tbody');
    if (tbodyGenerado) {
        contenedor.innerHTML = tbodyGenerado.innerHTML;
    } else {
        contenedor.innerHTML = tablaCompleta;
    }
    
    console.log(`✅ ${bonificaciones.length} bonificaciones cargadas en la tabla`);
}

function mostrar_mensaje(mensaje) {
    mostrarMensaje(mensaje);
    // Si es mensaje de bonificación asignada, limpiar formulario y ocultar información
    if(mensaje.includes('Bonificación asignada')) {
        // Activar flag para evitar que mostrar_bonificacionesAsignadas muestre la tabla
        asignacionBonificacionRecienCompletada = true;
        
        // Limpiar campo de cédula
        const cedulaInput = document.getElementById('cedulaBonificacion');
        if (cedulaInput) cedulaInput.value = '';
        
        // Limpiar selects
        const selectBonificacion = document.getElementById('bonificacion');
        const selectPuesto = document.getElementById('puestoBonificacion');
        if (selectBonificacion) {
            selectBonificacion.value = '';
            selectBonificacion.disabled = true;
        }
        if (selectPuesto) {
            selectPuesto.value = '';
            selectPuesto.disabled = true;
        }
        
        // Deshabilitar botón de asignar
        const btnAsignar = document.getElementById('btnAsignarBonificacion');
        if (btnAsignar) btnAsignar.disabled = true;
        
        // Limpiar información del propietario
        const nombreBonificacion = document.getElementById('nombrePropietarioBonificacion');
        const estadoBonificacion = document.getElementById('estadoPropietarioBonificacion');
        if (nombreBonificacion) nombreBonificacion.textContent = '-';
        if (estadoBonificacion) estadoBonificacion.textContent = '-';
        
        // Limpiar contenido de la tabla de bonificaciones (pero mantener la sección visible)
        const tablaBonificaciones = document.getElementById('tabla-bonificaciones-asignadas');
        const tablaContainer = document.getElementById('tabla-bonificaciones-asignadas-container');
        const mensajeSinBonificaciones = document.getElementById('mensaje-sin-bonificaciones-asignadas');
        if (tablaBonificaciones) tablaBonificaciones.innerHTML = '';
        if (tablaContainer) tablaContainer.style.display = 'none';
        if (mensajeSinBonificaciones) mensajeSinBonificaciones.style.display = 'block';
        
        // Ocultar sección de información del propietario
        const infoBonificacionBox = document.getElementById('infoPropietarioBonificacion');
        if (infoBonificacionBox) infoBonificacionBox.style.display = 'none';
        
        // Resetear flag después de un breve delay para permitir que se procese el resto de la respuesta
        setTimeout(() => {
            asignacionBonificacionRecienCompletada = false;
        }, 100);
    }
}

function mostrar_redirigir(paginaUrl) {
    window.location.href = paginaUrl;
}

/**
 * Función que maneja las excepciones de aplicación (status 299)
 */
function excepcionDeAplicacion(mensaje) {
    try {
        const respuestas = JSON.parse(mensaje);
        if (Array.isArray(respuestas)) {
            let tieneLogout = false;
            
            // Verificar si hay un logout
            respuestas.forEach(resp => {
                if (resp.id === 'logout') {
                    tieneLogout = true;
                }
            });
            
            if (tieneLogout) {
                // Si tiene logout, usar sessionException (flujo de cierre de sesión)
                sessionException(mensaje);
            } else {
                // Si NO tiene logout, mostrar el mensaje de error directamente
                respuestas.forEach(resp => {
                    if (resp.id === 'mensaje') {
                        console.error('❌ Error:', resp.parametro);
                        mostrarMensaje(resp.parametro);
                    }
                });
            }
        }
    } catch (e) {
        console.error('Error parseando respuesta:', e);
        mostrarMensaje('Error inesperado al procesar la respuesta del servidor');
    }
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

/**
 * Función que busca un propietario por cédula (HU 6)
 */
function buscarPropietario() {
    console.log('🔎 Buscando propietario por cédula');
    const cedula = document.getElementById('cedulaBonificacion')
        ? document.getElementById('cedulaBonificacion').value.trim()
        : '';
    if (!cedula) {
        mostrarMensaje('Por favor, ingrese una cédula');
        return;
    }

    // Reiniciar estado UI
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (btnAsignar) btnAsignar.disabled = true;

    // Enviar al backend: GET request para buscar propietario
    const params = `cedula=${encodeURIComponent(cedula)}`;
    submit('/administrador/buscar-propietario', params, 'GET');
}

/**
 * Función que asigna una bonificación a un propietario (HU 6)
 */
function asignarBonificacion() {
    console.log('🎁 Asignando bonificación');
    
    const cedula = document.getElementById('cedulaBonificacion')
        ? document.getElementById('cedulaBonificacion').value.trim()
        : '';
    const puesto = document.getElementById('puestoBonificacion').value;
    const bonificacion = document.getElementById('bonificacion').value;
    
    // Validaciones básicas
    if(!cedula) {
        mostrarMensaje('Por favor, ingrese una cédula');
        return;
    }
    if(!bonificacion) {
        mostrarMensaje('Por favor, seleccione una bonificación');
        return;
    }
    
    if(!puesto) {
        mostrarMensaje('Por favor, seleccione un puesto');
        return;
    }
    
    // Construir parámetros
    const params = `cedula=${encodeURIComponent(cedula)}&pPuesto=${encodeURIComponent(puesto)}&pBonificacion=${encodeURIComponent(bonificacion)}`;
    
    console.log('📤 Enviando al backend:', params);
    
    // Enviar al backend usando vistaWeb.js
    submit('/administrador/asignar-bonificacion', params, 'POST');
}

document.addEventListener('DOMContentLoaded', function() {
    // Inicialmente deshabilitado hasta encontrar propietario
    const selectBon = document.getElementById('bonificacion');
    const selectPue = document.getElementById('puestoBonificacion');
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (selectBon) selectBon.disabled = true;
    if (selectPue) selectPue.disabled = true;
    if (btnAsignar) btnAsignar.disabled = true;

    // Permitir buscar con Enter en el campo de cédula
    const cedulaInput = document.getElementById('cedulaBonificacion');
    if (cedulaInput) {
        cedulaInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                buscarPropietario();
            }
        });
    }
});

