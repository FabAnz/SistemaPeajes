/**
 * Página: Emular Tránsito
 */

// Configuración de vistaWeb.js
var urlIniciarVista = "/administrador/dashboard";
var parametrosInicioVista = ""; // No necesita parámetros, usa la sesión HTTP

/**
 * Función que carga los puestos disponibles
 */
function mostrar_puestos(puestos) {
    // Cargar puestos en el select de emular tránsito
    const selectPuestoTransito = document.getElementById('puestoTransito');
    if (!selectPuestoTransito) return;
    
    puestos.forEach(puesto => {
        const option = document.createElement('option');
        option.value = puesto.id;
        option.textContent = `${puesto.nombre} - ${puesto.direccion}`;
        selectPuestoTransito.appendChild(option);
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
    
    // Restablecer fecha y hora
    primerSubmitFinalizado();
    
    // Ocultar las tarifas si estaban visibles
    const seccionTarifas = document.getElementById('seccion-tarifas-puesto');
    if (seccionTarifas) {
        seccionTarifas.style.display = 'none';
    }
    
    console.log('✅ Resultado mostrado correctamente');
}

function mostrar_mensaje(mensaje) {
    mostrarMensaje(mensaje);
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

/**
 * Hook que vistaWeb.js llama después del primer submit exitoso
 * Aquí inicializamos los campos de fecha y hora
 */
function primerSubmitFinalizado() {
    console.log('🎉 Página cargada - Inicializando fecha y hora');
    
    // Establecer fecha y hora actual
    const today = new Date();
    const dateString = today.toISOString().split('T')[0];
    const fechaInput = document.getElementById('fechaTransito');
    if (fechaInput) fechaInput.value = dateString;
    
    const hours = String(today.getHours()).padStart(2, '0');
    const minutes = String(today.getMinutes()).padStart(2, '0');
    const horaInput = document.getElementById('horaTransito');
    if (horaInput) horaInput.value = `${hours}:${minutes}`;
    
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
        if (seccionTarifas) seccionTarifas.style.display = 'none';
        return;
    }
    
    // Mostrar la sección
    if (seccionTarifas) seccionTarifas.style.display = 'block';
    
    // Limpiar tabla
    if (tbodyTarifas) tbodyTarifas.innerHTML = '';
    
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
        
        if (tbodyTarifas) tbodyTarifas.appendChild(row);
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

// Event listener para cargar tarifas cuando se selecciona un puesto
document.addEventListener('DOMContentLoaded', function() {
    const selectPuestoTransito = document.getElementById('puestoTransito');
    if (selectPuestoTransito) {
        selectPuestoTransito.addEventListener('change', function(e) {
            const puestoId = e.target.value;
            cargarTarifasPuesto(puestoId);
        });
    }
});

