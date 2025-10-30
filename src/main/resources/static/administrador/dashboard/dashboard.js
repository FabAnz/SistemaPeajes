/**
 * Dashboard del Administrador
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

// Estado interno
let propietarioCargado = false;


function mostrar_puestos(puestos) {
    // Cargar puestos en el select de emular tránsito
    const selectPuestoTransito = document.getElementById('puestoTransito');
    puestos.forEach(puesto => {
        const option = document.createElement('option');
        option.value = puesto.id;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        selectPuestoTransito.appendChild(option);
    });
    
    // Cargar puestos en el select de asignar bonificación
    const selectPuestoBonificacion = document.getElementById('puestoBonificacion');
    puestos.forEach(puesto => {
        const option = document.createElement('option');
        option.value = puesto.id;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        selectPuestoBonificacion.appendChild(option);
    });
    
    console.log(`✅ ${puestos.length} puestos cargados`);
}

function mostrar_nuevoSaldo(nuevoSaldo) {
    mostrarMensaje(`Tránsito emulado correctamente.\n\nNuevo saldo del propietario: $${nuevoSaldo}`);
    // Limpiar el formulario
    document.getElementById('formEmularTransito').reset();
}

function mostrar_mensaje(mensaje) {
    mostrarMensaje(mensaje);
    // Si es mensaje de bonificación asignada, limpiar select
    if(mensaje.includes('Bonificación asignada')) {
        document.getElementById('bonificacion').value = '';
        document.getElementById('puestoBonificacion').value = '';
    }
}

function mostrar_redirigir(paginaUrl) {
    window.location.href = paginaUrl;
}

/**
 * Función que maneja las excepciones de aplicación (status 299)
 * Si tiene logout → usa sessionException
 * Si NO tiene logout → muestra el mensaje directamente
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

// ========== FUNCIONES DE EMULACIÓN DE TRÁNSITO ==========

/**
 * Función que emula un tránsito (HU 5)
 * Se llama desde el botón onclick="emularTransito()"
 */
function emularTransito() {
    console.log('🚀 Emular tránsito iniciado');
    
    const puesto = document.getElementById('puestoTransito').value;
    const matricula = document.getElementById('matriculaTransito').value.trim();
    const fecha = document.getElementById('fechaTransito').value;
    const hora = document.getElementById('horaTransito').value;
    
    console.log('Datos:', { puesto, matricula, fecha, hora });
    
    // Validaciones básicas
    if(!puesto) {
        mostrarMensaje('Por favor, seleccione un puesto de peaje');
        return;
    }
    
    if(!matricula) {
        mostrarMensaje('Por favor, ingrese una matrícula');
        return;
    }
    
    if(!fecha || !hora) {
        mostrarMensaje('Por favor, ingrese fecha y hora del tránsito');
        return;
    }
    
    // Construir parámetros
    const params = `pPuesto=${encodeURIComponent(puesto)}&pMatricula=${encodeURIComponent(matricula)}&pFecha=${encodeURIComponent(fecha)}&pHora=${encodeURIComponent(hora)}`;
    
    console.log('📤 Enviando al backend:', params);
    
    // Enviar al backend usando vistaWeb.js
    submit('/administrador/emular-transito', params, 'POST');
}

// ========== FUNCIONES DE ASIGNACIÓN DE BONIFICACIONES ==========

/**
 * Función que carga las bonificaciones disponibles en el select
 */
function mostrar_bonificaciones(bonificaciones) {
    const selectBonificacion = document.getElementById('bonificacion');
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
    
    // Mostrar información del propietario
    document.getElementById('nombrePropietarioBonificacion').textContent = propietario.nombreCompleto;
    document.getElementById('estadoPropietarioBonificacion').textContent = propietario.estado;
    
    // Aplicar clase de badge según estado
    const badgeEstado = document.getElementById('estadoPropietarioBonificacion');
    badgeEstado.className = 'info-value badge badge-' + propietario.estado.toLowerCase();
    
    // Mostrar contenedores
    document.getElementById('infoPropietarioBonificacion').style.display = 'block';
    document.getElementById('formAsignarBonificacion').style.display = 'block';

    // Habilitar selects y botón de asignación
    const selectBon = document.getElementById('bonificacion');
    const selectPue = document.getElementById('puestoBonificacion');
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (selectBon) selectBon.disabled = false;
    if (selectPue) selectPue.disabled = false;
    if (btnAsignar) btnAsignar.disabled = false;
    propietarioCargado = true;
}

/**
 * Función que muestra las bonificaciones asignadas del propietario
 */
function mostrar_bonificacionesAsignadas(bonificaciones) {
    console.log('🎁 Mostrando bonificaciones asignadas:', bonificaciones);
    
    const contenedor = document.getElementById('tabla-bonificaciones-asignadas');
    const contenedorPrincipal = document.getElementById('contenedorBonificacionesAsignadas');
    const mensaje = document.getElementById('mensaje-sin-bonificaciones-asignadas');
    const tabla = document.getElementById('tabla-bonificaciones-asignadas-container');
    
    // Mostrar el contenedor principal
    contenedorPrincipal.style.display = 'block';
    
    // Verificar si hay bonificaciones
    if (!bonificaciones || bonificaciones.length === 0) {
        // No hay bonificaciones, mostrar mensaje
        mensaje.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    // Hay bonificaciones, mostrar tabla y ocultar mensaje
    mensaje.style.display = 'none';
    tabla.style.display = 'table';
    
    // Usar utilesVista.js para generar la tabla automáticamente
    contenedor.innerHTML = crearTablaDesdeJson(bonificaciones);
}

/**
 * Event listener para buscar propietario
 */
document.addEventListener('DOMContentLoaded', function() {
    // Inicialmente deshabilitado hasta encontrar propietario
    const selectBon = document.getElementById('bonificacion');
    const selectPue = document.getElementById('puestoBonificacion');
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (selectBon) selectBon.disabled = true;
    if (selectPue) selectPue.disabled = true;
    if (btnAsignar) btnAsignar.disabled = true;

    // Buscar propietario por cédula
    const formBuscarPropietario = document.getElementById('formBuscarPropietario');
    if (formBuscarPropietario) {
        formBuscarPropietario.addEventListener('submit', function(e) {
            e.preventDefault();
            buscarPropietario();
        });
    }
    const formAsignarBonificacion = document.getElementById('formAsignarBonificacion');
    if (formAsignarBonificacion) {
        formAsignarBonificacion.addEventListener('submit', function(e) {
            e.preventDefault();
            asignarBonificacion();
        });
    }
});

/**
 * Función que busca un propietario por cédula (HU 6)
 */
// Búsqueda independiente del propietario por cédula
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
    propietarioCargado = false;
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (btnAsignar) btnAsignar.disabled = true;

    // Enviar al backend: requiere endpoint que retorne { propietario, bonificacionesAsignadas }
    const params = `cedula=${encodeURIComponent(cedula)}`;
    submit('/administrador/buscar-propietario', params, 'POST');
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
    if(!propietarioCargado) {
        mostrarMensaje('Primero busque y cargue un propietario por cédula');
        return;
    }
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

// ========== INICIALIZACIÓN ==========

/**
 * Hook que vistaWeb.js llama después del primer submit exitoso
 * Aquí inicializamos los campos de fecha y hora
 */
function primerSubmitFinalizado() {
    console.log('🎉 Dashboard cargado - Inicializando fecha y hora');
    
    // Establecer fecha y hora actual
    const today = new Date();
    const dateString = today.toISOString().split('T')[0];
    document.getElementById('fechaTransito').value = dateString;
    
    const hours = String(today.getHours()).padStart(2, '0');
    const minutes = String(today.getMinutes()).padStart(2, '0');
    document.getElementById('horaTransito').value = `${hours}:${minutes}`;
    
    console.log('✅ Fecha y hora inicializadas');
}
