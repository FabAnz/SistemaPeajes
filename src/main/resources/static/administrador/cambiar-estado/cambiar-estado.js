/**
 * Página: Cambiar Estado de Propietario
 */

// Configuración de vistaWeb.js
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

/**
 * Función que carga los estados disponibles en el select (HU 7)
 */
function mostrar_estados(estados) {
    const selectEstado = document.getElementById('nuevoEstado');
    if (!selectEstado) return;

    // Limpiar dejando el placeholder si existe
    const placeholder = selectEstado.querySelector('option[value=""]');
    selectEstado.innerHTML = '';
    if (placeholder) {
        selectEstado.appendChild(placeholder);
    } else {
        const opt = document.createElement('option');
        opt.value = '';
        opt.textContent = 'Seleccione un estado';
        selectEstado.appendChild(opt);
    }

    estados.forEach(est => {
        const option = document.createElement('option');
        option.value = est.nombre;
        option.textContent = est.nombre;
        selectEstado.appendChild(option);
    });

    console.log(`✅ ${estados.length} estados cargados`);
}

/**
 * Función que muestra información del propietario buscado
 */
function mostrar_propietario(propietario) {
    console.log('📋 Mostrando información del propietario:', propietario);
    
    const nombreEstado = document.getElementById('nombrePropietarioEstado');
    const estadoActual = document.getElementById('estadoActualPropietario');
    const infoEstadoBox = document.getElementById('infoPropietarioEstado');
    const formCambiarEstado = document.getElementById('formCambiarEstado');
    
    if (nombreEstado) nombreEstado.textContent = propietario.nombreCompleto;
    if (estadoActual) {
        estadoActual.textContent = propietario.estado;
        estadoActual.className = 'info-value badge badge-' + propietario.estado.toLowerCase();
    }
    if (infoEstadoBox) infoEstadoBox.style.display = 'block';
    if (formCambiarEstado) formCambiarEstado.style.display = 'block';
}

function mostrar_mensaje(mensaje) {
    mostrarMensaje(mensaje);
    // Si es mensaje de cambio de estado exitoso, limpiar formulario y ocultar información
    if(mensaje.includes('Estado cambiado correctamente')) {
        // Limpiar campo de cédula
        const cedulaInput = document.getElementById('cedulaEstado');
        if (cedulaInput) cedulaInput.value = '';
        
        // Limpiar select de nuevo estado
        const selectEstado = document.getElementById('nuevoEstado');
        if (selectEstado) selectEstado.value = '';
        
        // Limpiar información del propietario
        const nombreEstado = document.getElementById('nombrePropietarioEstado');
        const estadoActual = document.getElementById('estadoActualPropietario');
        if (nombreEstado) nombreEstado.textContent = '-';
        if (estadoActual) {
            estadoActual.textContent = '-';
            estadoActual.className = 'info-value badge';
        }
        
        // Ocultar secciones
        const infoEstadoBox = document.getElementById('infoPropietarioEstado');
        const formCambiarEstado = document.getElementById('formCambiarEstado');
        if (infoEstadoBox) infoEstadoBox.style.display = 'none';
        if (formCambiarEstado) formCambiarEstado.style.display = 'none';
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

// ========== HU 7: Cambiar Estado ==========
function cambiarEstadoPropietario() {
    const cedula = document.getElementById('cedulaEstado')
        ? document.getElementById('cedulaEstado').value.trim()
        : '';
    const selectEstado = document.getElementById('nuevoEstado');
    const estado = selectEstado ? selectEstado.value : '';

    if (!cedula) {
        mostrarMensaje('Por favor, busque un propietario primero');
        return;
    }

    if (!estado) {
        mostrarMensaje('Por favor, seleccione un estado');
        return;
    }

    // Enviar como JSON para usar PUT con @RequestBody
    const requestBody = JSON.stringify({
        cedula: cedula,
        estado: estado
    });

    fetch('/administrador/cambiar-estado-propietario', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include', // Incluir cookies de sesión
        body: requestBody
    })
    .then(async response => {
        const status = response.status;
        const text = await response.text();
        
        // Manejar errores HTTP
        if (status < 200 || status > 299) {
            if (status === 299) {
                // Excepción de aplicación
                try {
                    excepcionDeAplicacion(text);
                } catch (e) {
                    mostrarMensaje(text);
                }
            } else {
                procesarErrorSubmit(status, text);
            }
            return;
        }

        // Procesar respuesta exitosa
        try {
            const json = JSON.parse(text);
            if (Array.isArray(json)) {
                procesarResultadosSubmit(json);
            }
        } catch (e) {
            console.error('Error procesando la respuesta:', e);
        }
    })
    .catch(error => {
        procesarErrorSubmit(0, error.message);
    });
}

/**
 * Búsqueda de propietario desde la sección de estado
 */
function buscarPropietarioEstado() {
    console.log('🔎 [HU7] Buscando propietario por cédula (cambiar estado)');
    const cedula = document.getElementById('cedulaEstado')
        ? document.getElementById('cedulaEstado').value.trim()
        : '';
    if (!cedula) {
        mostrarMensaje('Por favor, ingrese una cédula');
        return;
    }

    // Limpia visualmente la sección HU7 antes de cargar
    const infoEstadoBox = document.getElementById('infoPropietarioEstado');
    const formCambiarEstado = document.getElementById('formCambiarEstado');
    if (infoEstadoBox) infoEstadoBox.style.display = 'none';
    if (formCambiarEstado) formCambiarEstado.style.display = 'none';

    const params = `cedula=${encodeURIComponent(cedula)}`;
    submit('/administrador/buscar-propietario', params, 'GET');
}

document.addEventListener('DOMContentLoaded', function() {
    // HU 7: Buscar propietario para cambiar estado
    const formBuscarPropietarioEstado = document.getElementById('formBuscarPropietarioEstado');
    if (formBuscarPropietarioEstado) {
        formBuscarPropietarioEstado.addEventListener('submit', function(e) {
            e.preventDefault();
            buscarPropietarioEstado();
        });
    }

    // HU 7: Cambiar estado submit
    const formCambiarEstado = document.getElementById('formCambiarEstado');
    if (formCambiarEstado) {
        formCambiarEstado.addEventListener('submit', function(e) {
            e.preventDefault();
            cambiarEstadoPropietario();
        });
    }
});

