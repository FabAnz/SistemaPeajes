// ========================================
// CONFIGURACIÓN Y VARIABLES
// ========================================

// Variable para prevenir múltiples envíos
let formularioEnviado = false;

// ========================================
// INICIALIZACIÓN
// ========================================

document.addEventListener('DOMContentLoaded', function() {
    // Ocultar loading overlay al iniciar la página
    ocultarLoading();
    
    inicializarEventos();
    configurarValidacionEnTiempoReal();
});

// ========================================
// CONFIGURACIÓN DE EVENTOS
// ========================================

function inicializarEventos() {
    // Toggle para mostrar/ocultar contraseña
    const togglePassword = document.getElementById('togglePassword');
    togglePassword.addEventListener('click', togglearVisibilidadContrasenia);
    
    // Link de ayuda
    const linkAyuda = document.getElementById('linkAyuda');
    linkAyuda.addEventListener('click', mostrarAyuda);
    
    // Limpiar errores al escribir
    document.getElementById('cedula').addEventListener('input', function() {
        limpiarError('cedula');
    });
    
    document.getElementById('contrasenia').addEventListener('input', function() {
        limpiarError('contrasenia');
    });
}

// ========================================
// VALIDACIÓN
// ========================================

function configurarValidacionEnTiempoReal() {
    const inputCedula = document.getElementById('cedula');
    
    inputCedula.addEventListener('blur', function() {
        validarCedula(this.value);
    });
}

function validarCedula(cedula) {
    // Validar que no esté vacío
    if (!cedula.trim()) {
        mostrarError('cedula', 'La cédula es requerida');
        return false;
    }
    
    // Validar formato (solo números, puntos y guiones)
    const formatoValido = /^[0-9.-]+$/.test(cedula);
    if (!formatoValido) {
        mostrarError('cedula', 'La cédula solo puede contener números, puntos y guiones');
        return false;
    }
    
    limpiarError('cedula');
    return true;
}

function validarContrasenia(contrasenia) {
    if (!contrasenia || contrasenia.trim() === '') {
        mostrarError('contrasenia', 'La contraseña es requerida');
        return false;
    }
    
    limpiarError('contrasenia');
    return true;
}

function validarFormulario() {
    const cedula = document.getElementById('cedula').value;
    const contrasenia = document.getElementById('contrasenia').value;
    
    const cedulaValida = validarCedula(cedula);
    const contraseniaValida = validarContrasenia(contrasenia);
    
    return cedulaValida && contraseniaValida;
}

// ========================================
// MANEJO DE ERRORES
// ========================================

function mostrarError(campo, mensaje) {
    const errorElement = document.getElementById(`error${capitalizar(campo)}`);
    const inputElement = document.getElementById(campo);
    
    errorElement.textContent = mensaje;
    inputElement.classList.add('error');
}

function limpiarError(campo) {
    const errorElement = document.getElementById(`error${capitalizar(campo)}`);
    const inputElement = document.getElementById(campo);
    
    errorElement.textContent = '';
    inputElement.classList.remove('error');
}

function limpiarTodosLosErrores() {
    limpiarError('cedula');
    limpiarError('contrasenia');
}

// ========================================
// LOGIN - FUNCIÓN PRINCIPAL
// ========================================

function hacerLogin() {
    // Prevenir múltiples envíos
    if (formularioEnviado) {
        return;
    }
    
    // Limpiar errores previos
    limpiarTodosLosErrores();
    
    // Validar formulario
    if (!validarFormulario()) {
        return;
    }
    
    // Deshabilitar botón y mostrar loading
    formularioEnviado = true;
    deshabilitarBoton();
    mostrarLoading();
    
    // Serializar formulario y enviar usando la librería vistaWeb.js
    const datos = serializarFormulario('formLogin');
    submit('/acceso/login', datos, 'POST');
}

// ========================================
// PROCESAMIENTO DE RESPUESTAS (vistaWeb.js)
// ========================================

// Esta función se ejecuta cuando el backend retorna {"id": "redirigir", "parametro": "/url"}
function mostrar_redirigir(paginaUrl) {
    ocultarLoading();
    console.log("Redirigiendo a:", paginaUrl);
    
    // Mostrar mensaje de éxito y redirigir después de un breve delay
    mostrarMensaje('¡Inicio de sesión exitoso! Redirigiendo...').then(() => {
        setTimeout(() => {
            window.location.href = paginaUrl;
        }, 500);
    });
}

// ========================================
// MANEJO DE EXCEPCIONES (vistaWeb.js)
// ========================================

// Esta función se ejecuta cuando el backend retorna status 299 (error de aplicación)
function excepcionDeAplicacion(mensaje) {
    ocultarLoading();
    habilitarBoton();
    formularioEnviado = false;
    
    // Buscar si el mensaje es un JSON con estructura de RespuestaDTO
    try {
        const respuestas = JSON.parse(mensaje);
        if (Array.isArray(respuestas)) {
            // Es un array de RespuestaDTO
            let mensajeError = 'Error al iniciar sesión';
            respuestas.forEach(resp => {
                if (resp.id === 'error' || resp.id === 'ERROR' || resp.id === 'mensaje') {
                    mensajeError = resp.parametro;
                }
            });
            mostrarMensaje(mensajeError);
            return;
        }
    } catch (e) {
        // No es JSON, es un string directo
    }
    
    // Mostrar el mensaje tal cual
    mostrarMensaje(mensaje);
}

// Esta función se ejecuta cuando hay errores HTTP (4xx, 5xx, etc.)
function procesarErrorSubmit(status, text) {
    ocultarLoading();
    habilitarBoton();
    formularioEnviado = false;
    
    console.error("Error HTTP " + status + ":", text);
    
    let mensaje = 'Error al procesar la solicitud';
    
    if (status === 404) {
        mensaje = 'Servicio no disponible';
    } else if (status === 500) {
        mensaje = 'Error interno del servidor';
    } else if (status === 0) {
        mensaje = 'No se pudo conectar con el servidor. Verifica tu conexión.';
    }
    
    mostrarMensaje(mensaje);
}

// ========================================
// UI - LOADING Y BOTONES
// ========================================

function mostrarLoading() {
    document.getElementById('loadingSpinner').style.display = 'flex';
}

function ocultarLoading() {
    document.getElementById('loadingSpinner').style.display = 'none';
}

function deshabilitarBoton() {
    const boton = document.getElementById('btnLogin');
    boton.disabled = true;
    boton.querySelector('.btn-text').textContent = 'Iniciando sesión...';
}

function habilitarBoton() {
    const boton = document.getElementById('btnLogin');
    boton.disabled = false;
    boton.querySelector('.btn-text').textContent = 'Iniciar Sesión';
}

// ========================================
// UTILIDADES
// ========================================

function togglearVisibilidadContrasenia() {
    const input = document.getElementById('contrasenia');
    
    if (input.type === 'password') {
        input.type = 'text';
        this.setAttribute('aria-label', 'Ocultar contraseña');
    } else {
        input.type = 'password';
        this.setAttribute('aria-label', 'Mostrar contraseña');
    }
}

function mostrarAyuda(event) {
    event.preventDefault();
    mostrarMensaje('Para soporte técnico, contacta al administrador del sistema.');
}

function capitalizar(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}
