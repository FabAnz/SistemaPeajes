/**
 * Dashboard del Administrador
 */

// Configuración de vistaWeb.js
// Esta URL se llama automáticamente cuando se carga la página (DOMContentLoaded)
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

// Estado interno
let contextoBusquedaActual = null; // 'bonificacion' | 'estado'
let asignacionBonificacionRecienCompletada = false; // Flag para evitar mostrar bonificaciones después de asignar


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

/**
 * Función que muestra el resultado completo de la emulación de tránsito
 */
function mostrar_resultado(resultado) {
    console.log('📋 Mostrando resultado de emulación:', resultado);
    
    const seccionResultado = document.getElementById('seccion-resultado-transito');
    const contenidoResultado = document.getElementById('contenido-resultado-transito');
    
    if (!resultado) {
        seccionResultado.style.display = 'none';
        return;
    }
    
    // Mostrar la sección
    seccionResultado.style.display = 'block';
    
    // Construir el HTML del resultado
    let html = '<div style="display: grid; gap: 12px;">';
    
    // Propietario
    html += `<div><strong>Propietario:</strong> ${resultado.nombrePropietario} (${resultado.estadoPropietario})</div>`;
    
    // Categoría
    html += `<div><strong>Categoría:</strong> ${resultado.categoriaVehiculo}</div>`;
    
    // Bonificación (si existe)
    if (resultado.bonificacion) {
        html += `<div><strong>Bonificación:</strong> ${resultado.bonificacion}</div>`;
    }
    
    // Costo del tránsito
    html += `<div><strong>Costo del tránsito:</strong> $ ${resultado.costoTransito.toFixed(2)}</div>`;
    
    // Saldo luego del tránsito
    html += `<div><strong>Saldo luego del tránsito:</strong> $ ${resultado.saldoLuegoTransito.toFixed(2)}</div>`;
    
    html += '</div>';
    
    contenidoResultado.innerHTML = html;
    
    // Limpiar el formulario
    document.getElementById('formEmularTransito').reset();
    
    // Ocultar las tarifas si estaban visibles
    const seccionTarifas = document.getElementById('seccion-tarifas-puesto');
    if (seccionTarifas) {
        seccionTarifas.style.display = 'none';
    }
    
    console.log('✅ Resultado mostrado correctamente');
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
        if (estadoBonificacion) {
            estadoBonificacion.textContent = '-';
            estadoBonificacion.className = 'info-value badge';
        }
        
        // Limpiar contenido de la tabla de bonificaciones
        const tablaBonificaciones = document.getElementById('tabla-bonificaciones-asignadas');
        const tablaContainer = document.getElementById('tabla-bonificaciones-asignadas-container');
        const mensajeSinBonificaciones = document.getElementById('mensaje-sin-bonificaciones-asignadas');
        if (tablaBonificaciones) tablaBonificaciones.innerHTML = '';
        if (tablaContainer) tablaContainer.style.display = 'none';
        if (mensajeSinBonificaciones) mensajeSinBonificaciones.style.display = 'none';
        
        // Ocultar secciones
        const infoBonificacionBox = document.getElementById('infoPropietarioBonificacion');
        const formAsignarBonificacion = document.getElementById('formAsignarBonificacion');
        const contenedorBonificaciones = document.getElementById('contenedorBonificacionesAsignadas');
        if (infoBonificacionBox) infoBonificacionBox.style.display = 'none';
        if (formAsignarBonificacion) formAsignarBonificacion.style.display = 'none';
        if (contenedorBonificaciones) contenedorBonificaciones.style.display = 'none';
        
        // Resetear flag después de un breve delay para permitir que se procese el resto de la respuesta
        setTimeout(() => {
            asignacionBonificacionRecienCompletada = false;
        }, 100);
    }
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
    
    // Bloque HU6: Asignar bonificación (solo si el contexto lo indica)
    if (contextoBusquedaActual === 'bonificacion') {
        document.getElementById('nombrePropietarioBonificacion').textContent = propietario.nombreCompleto;
        document.getElementById('estadoPropietarioBonificacion').textContent = propietario.estado;
        const badgeEstado = document.getElementById('estadoPropietarioBonificacion');
        badgeEstado.className = 'info-value badge badge-' + propietario.estado.toLowerCase();
        document.getElementById('infoPropietarioBonificacion').style.display = 'block';
        document.getElementById('formAsignarBonificacion').style.display = 'block';
        const selectBon = document.getElementById('bonificacion');
        const selectPue = document.getElementById('puestoBonificacion');
        const btnAsignar = document.getElementById('btnAsignarBonificacion');
        if (selectBon) selectBon.disabled = false;
        if (selectPue) selectPue.disabled = false;
        if (btnAsignar) btnAsignar.disabled = false;
    }

    // Bloque HU7: Cambiar estado (solo si el contexto lo indica)
    if (contextoBusquedaActual === 'estado') {
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
}

/**
 * Función que muestra las bonificaciones asignadas del propietario
 */
function mostrar_bonificacionesAsignadas(bonificaciones) {
    console.log('🎁 Mostrando bonificaciones asignadas:', bonificaciones);
    // Si la búsqueda fue para HU7 (estado), no actualizar la sección de bonificaciones
    if (contextoBusquedaActual === 'estado') {
        console.log('↪️ Se omite actualización de bonificaciones (contexto HU7)');
        return;
    }
    // Si acabamos de asignar una bonificación, no mostrar la tabla (se limpió en mostrar_mensaje)
    if (asignacionBonificacionRecienCompletada) {
        console.log('↪️ Se omite actualización de bonificaciones (asignación reciente)');
        return;
    }
    
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

    // Event listener para cargar tarifas cuando se selecciona un puesto
    const selectPuestoTransito = document.getElementById('puestoTransito');
    if (selectPuestoTransito) {
        selectPuestoTransito.addEventListener('change', function(e) {
            const puestoId = e.target.value;
            cargarTarifasPuesto(puestoId);
        });
    }

    // Buscar propietario por cédula
    const formBuscarPropietario = document.getElementById('formBuscarPropietario');
    if (formBuscarPropietario) {
        formBuscarPropietario.addEventListener('submit', function(e) {
            e.preventDefault();
            contextoBusquedaActual = 'bonificacion';
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

    // HU 7: Buscar propietario para cambiar estado
    const formBuscarPropietarioEstado = document.getElementById('formBuscarPropietarioEstado');
    if (formBuscarPropietarioEstado) {
        formBuscarPropietarioEstado.addEventListener('submit', function(e) {
            e.preventDefault();
            contextoBusquedaActual = 'estado';
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
    const btnAsignar = document.getElementById('btnAsignarBonificacion');
    if (btnAsignar) btnAsignar.disabled = true;

    // Enviar al backend: GET request para buscar propietario
    const params = `cedula=${encodeURIComponent(cedula)}`;
    submit('/administrador/buscar-propietario', params, 'GET');
}

// HU 7: Búsqueda de propietario desde la sección de estado (reutiliza el mismo endpoint)
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

/**
 * Función que muestra las tarifas de un puesto
 */
function mostrar_tarifas(tarifas) {
    console.log('💰 Mostrando tarifas del puesto:', tarifas);
    
    const seccionTarifas = document.getElementById('seccion-tarifas-puesto');
    const tbodyTarifas = document.getElementById('tbody-tarifas-puesto');
    
    if (!tarifas || tarifas.length === 0) {
        // Si no hay tarifas, ocultar la sección
        seccionTarifas.style.display = 'none';
        return;
    }
    
    // Mostrar la sección
    seccionTarifas.style.display = 'block';
    
    // Limpiar tabla
    tbodyTarifas.innerHTML = '';
    
    // Agregar cada tarifa a la tabla
    tarifas.forEach(tarifa => {
        const row = document.createElement('tr');
        
        // Columna de categoría
        const cellCategoria = document.createElement('td');
        cellCategoria.textContent = tarifa.categoria;
        row.appendChild(cellCategoria);
        
        // Columna de monto (formateado como moneda)
        const cellMonto = document.createElement('td');
        cellMonto.textContent = `$ ${tarifa.monto.toFixed(2)}`;
        row.appendChild(cellMonto);
        
        tbodyTarifas.appendChild(row);
    });
    
    console.log(`✅ ${tarifas.length} tarifas cargadas en la tabla`);
}

/**
 * Función que carga las tarifas de un puesto seleccionado
 */
function cargarTarifasPuesto(puestoId) {
    if (!puestoId || puestoId === '') {
        // Si no hay puesto seleccionado, ocultar la sección de tarifas
        const seccionTarifas = document.getElementById('seccion-tarifas-puesto');
        if (seccionTarifas) {
            seccionTarifas.style.display = 'none';
        }
        return;
    }
    
    console.log('📡 Cargando tarifas para puesto:', puestoId);
    
    // Hacer petición GET al endpoint
    const params = `puestoId=${encodeURIComponent(puestoId)}`;
    submit('/administrador/tarifas-puesto', params, 'GET');
}