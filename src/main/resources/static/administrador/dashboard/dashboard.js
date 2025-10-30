/**
 * Dashboard del Administrador
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP


function mostrar_puestos(puestos) {
    // Cargar puestos en el select de emular tránsito
    const selectPuestoTransito = document.getElementById('puestoTransito');
    puestos.forEach(puesto => {
        const option = document.createElement('option');
        option.value = puesto.id;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        selectPuestoTransito.appendChild(option);
    });
    
    console.log(`✅ ${puestos.length} puestos cargados`);
}

function mostrar_nuevoSaldo(nuevoSaldo) {
    mostrarMensaje(`Tránsito emulado correctamente.\n\nNuevo saldo del propietario: $${nuevoSaldo}`);
    // Limpiar el formulario
    document.getElementById('formEmularTransito').reset();
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
