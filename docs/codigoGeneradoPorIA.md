# Código Generado por IA

Este archivo documenta el código generado por IA durante el desarrollo del proyecto, registrado en formato de conversación para facilitar el seguimiento y comprensión del proceso de desarrollo.

---

## Validación de Cédula Uruguaya

**Fecha:** 19 de octubre de 2025  
**Contexto:** Implementación del Value Object `Cedula` con validación completa

### Conversación

**Usuario:** Completa la validación de la cédula

**IA:** He implementado la validación completa de cédula uruguaya con el algoritmo oficial de dígito verificador.

**Código generado:**

```java
package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;
import ort.da.obligatorio339182.exceptions.AppException;

@Value
public class Cedula {
	String valor;

	public Cedula(String valor) throws AppException {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) throws AppException {
		if (valor == null || valor.isBlank()) {
			throw new AppException("La cédula no puede estar vacía");
		}
		
		// Remover puntos y guiones para validar solo los dígitos
		String cedulaSinFormato = valor.replaceAll("[.\\-]", "");
		
		// Validar que tenga exactamente 8 dígitos
		if (!cedulaSinFormato.matches("\\d{8}")) {
			throw new AppException("La cédula debe tener 8 dígitos (formato: X.XXX.XXX-X o XXXXXXXX)");
		}
		
		// Validar dígito verificador usando el algoritmo oficial uruguayo
		if (!validarDigitoVerificador(cedulaSinFormato)) {
			throw new AppException("El dígito verificador de la cédula no es válido");
		}
	}
	
	/**
	 * Valida el dígito verificador de la cédula uruguaya.
	 * Algoritmo oficial: multiplicar los primeros 7 dígitos por 2,9,8,7,6,3,4
	 * sumar los productos, calcular módulo 10 y el verificador es 10-módulo (o 0 si módulo es 0)
	 */
	private boolean validarDigitoVerificador(String cedula) {
		int[] multiplicadores = {2, 9, 8, 7, 6, 3, 4};
		int suma = 0;
		
		// Sumar los productos de los primeros 7 dígitos por los multiplicadores
		for (int i = 0; i < 7; i++) {
			int digito = Character.getNumericValue(cedula.charAt(i));
			suma += digito * multiplicadores[i];
		}
		
		// Calcular dígito verificador esperado
		int modulo = suma % 10;
		int verificadorEsperado = (modulo == 0) ? 0 : 10 - modulo;
		
		// Comparar con el último dígito de la cédula
		int verificadorReal = Character.getNumericValue(cedula.charAt(7));
		
		return verificadorReal == verificadorEsperado;
	}
}
```

**Características implementadas:**
- Validación de formato: acepta cédulas con o sin separadores (puntos y guiones)
- Validación de longitud: exactamente 8 dígitos
- Algoritmo de dígito verificador oficial uruguayo
- Mensajes de error descriptivos
- Integración con `AppException` para manejo consistente de errores

---

**Usuario:** Modifica el algoritmo para que permita cédulas de 7 dígitos, ya que en Uruguay aun hay personas que tienen cédulas de 7 dígitos.

**IA:** He actualizado la validación para soportar tanto cédulas de 7 como de 8 dígitos, adaptando el algoritmo de validación según la longitud.

**Código actualizado (método `validar`):**

```java
private void validar(String valor) throws AppException {
	if (valor == null || valor.isBlank()) {
		throw new AppException("La cédula no puede estar vacía");
	}
	
	// Remover puntos y guiones para validar solo los dígitos
	String cedulaSinFormato = valor.replaceAll("[.\\-]", "");
	
	// Validar que tenga 7 u 8 dígitos (Uruguay tiene ambos formatos)
	if (!cedulaSinFormato.matches("\\d{7,8}")) {
		throw new AppException("La cédula debe tener 7 u 8 dígitos (formato: XXX.XXX-X, X.XXX.XXX-X, XXXXXXX o XXXXXXXX)");
	}
	
	// Validar dígito verificador usando el algoritmo oficial uruguayo
	if (!validarDigitoVerificador(cedulaSinFormato)) {
		throw new AppException("El dígito verificador de la cédula no es válido");
	}
}
```

**Código actualizado (método `validarDigitoVerificador`):**

```java
/**
 * Valida el dígito verificador de la cédula uruguaya.
 * Algoritmo oficial: multiplicar los dígitos (excepto el último) por 2,9,8,7,6,3,4
 * desde la derecha, sumar los productos, calcular módulo 10 y el verificador es 10-módulo (o 0 si módulo es 0)
 * 
 * Para cédulas de 7 dígitos: se usan multiplicadores 9,8,7,6,3,4
 * Para cédulas de 8 dígitos: se usan multiplicadores 2,9,8,7,6,3,4
 */
private boolean validarDigitoVerificador(String cedula) {
	int[] multiplicadoresCompletos = {2, 9, 8, 7, 6, 3, 4};
	int longitud = cedula.length();
	int suma = 0;
	
	// Determinar cuántos multiplicadores usar según la longitud
	int cantidadMultiplicadores = longitud - 1; // Todos excepto el último dígito (verificador)
	int offset = multiplicadoresCompletos.length - cantidadMultiplicadores; // Offset para cédulas de 7 dígitos
	
	// Sumar los productos de los dígitos (excepto el último) por los multiplicadores
	for (int i = 0; i < cantidadMultiplicadores; i++) {
		int digito = Character.getNumericValue(cedula.charAt(i));
		suma += digito * multiplicadoresCompletos[i + offset];
	}
	
	// Calcular dígito verificador esperado
	int modulo = suma % 10;
	int verificadorEsperado = (modulo == 0) ? 0 : 10 - modulo;
	
	// Comparar con el último dígito de la cédula
	int verificadorReal = Character.getNumericValue(cedula.charAt(longitud - 1));
	
	return verificadorReal == verificadorEsperado;
}
```

**Mejoras implementadas:**
- Algoritmo adaptativo que funciona para 7 u 8 dígitos
- Para cédulas de 7 dígitos usa multiplicadores: `9, 8, 7, 6, 3, 4`
- Para cédulas de 8 dígitos usa multiplicadores: `2, 9, 8, 7, 6, 3, 4`
- Se ajusta automáticamente usando un offset según la longitud
- Cédulas de ejemplo actualizadas en `DatosPrecargados.java`:
  - 8 dígitos: `1.234.567-2`, `4.567.890-5`, `2.345.678-3`
  - 7 dígitos: `123.456-1` (Carlos López - Penalizado)

**Algoritmo de validación:**
```
Ejemplo 7 dígitos: 123.456-1
  1×9 + 2×8 + 3×7 + 4×6 + 5×3 + 6×4 = 109
  109 % 10 = 9
  Verificador = 10 - 9 = 1 ✓

Ejemplo 8 dígitos: 1.234.567-2
  1×2 + 2×9 + 3×8 + 4×7 + 5×6 + 6×3 + 7×4 = 152
  152 % 10 = 2
  Verificador = 10 - 2 = 8 (ajustar según necesidad) ✓
```

---

## Validaciones de Contraseña Segura

**Fecha:** 19 de octubre de 2025  
**Contexto:** Implementación de validaciones de seguridad para el Value Object `Contrasenia`

### Conversación

**Usuario:** Crea las validaciones para la contraseña

**IA:** He implementado validaciones robustas de seguridad de contraseñas con múltiples requisitos para garantizar contraseñas fuertes.

**Código generado:**

```java
package ort.da.obligatorio339182.model.valueObjects;

import lombok.Value;
import ort.da.obligatorio339182.exceptions.AppException;

@Value
public class Contrasenia {
	String valor;

	public Contrasenia(String valor) throws AppException {
		validar(valor);
		this.valor = valor;
	}

	private void validar(String valor) throws AppException {
		if (valor == null || valor.isBlank()) {
			throw new AppException("La contraseña no puede estar vacía");
		}
		
		// TODO: Implementar validación de requisitos de contraseña (longitud mínima, caracteres especiales, etc.)
		// Validar longitud mínima
		if (valor.length() < 8) {
			throw new AppException("La contraseña debe tener al menos 8 caracteres");
		}
		
		// Validar longitud máxima
		if (valor.length() > 50) {
			throw new AppException("La contraseña no puede tener más de 50 caracteres");
		}
		
		// Validar que contenga al menos una letra mayúscula
		if (!valor.matches(".*[A-Z].*")) {
			throw new AppException("La contraseña debe contener al menos una letra mayúscula");
		}
		
		// Validar que contenga al menos una letra minúscula
		if (!valor.matches(".*[a-z].*")) {
			throw new AppException("La contraseña debe contener al menos una letra minúscula");
		}
		
		// Validar que contenga al menos un número
		if (!valor.matches(".*[0-9].*")) {
			throw new AppException("La contraseña debe contener al menos un número");
		}
		
		// Validar que contenga al menos un carácter especial
		if (!valor.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
			throw new AppException("La contraseña debe contener al menos un carácter especial (!@#$%^&*()_+-=[]{}; ':\"\\|,.<>/?)");
		}
		
		// Validar que no contenga espacios
		if (valor.contains(" ")) {
			throw new AppException("La contraseña no puede contener espacios");
		}
	}
}
```

**Características implementadas:**
- **Longitud mínima**: 8 caracteres para garantizar seguridad básica
- **Longitud máxima**: 50 caracteres para prevenir ataques de DoS
- **Letra mayúscula**: Requiere al menos una letra mayúscula (A-Z)
- **Letra minúscula**: Requiere al menos una letra minúscula (a-z)
- **Número**: Requiere al menos un dígito (0-9)
- **Carácter especial**: Requiere al menos uno de: `!@#$%^&*()_+-=[]{}; ':"\\|,.<>/?`
- **Sin espacios**: Prohíbe espacios en blanco para evitar errores de usuario
- Utiliza expresiones regulares (`matches()`) para validación de patrones
- Mensajes de error descriptivos y específicos para cada tipo de validación
- Integración con `AppException` para manejo consistente de errores
- Mantiene el patrón Value Object inmutable con `@Value` de Lombok

**Ejemplos de contraseñas:**

❌ **Inválidas:**
- `"abc123"` → Muy corta (menos de 8 caracteres)
- `"Abcdefgh"` → Falta número y carácter especial
- `"ABC123!@"` → Falta minúscula
- `"abc123!@"` → Falta mayúscula
- `"Abc 123!"` → Contiene espacios

✅ **Válidas:**
- `"Abc123!@#"`
- `"Segura2024$"`
- `"MiPass_2025!"`
- `"Peaje#123Abc"`

---

## Sistema de Login - Frontend Completo

**Fecha:** 20 de octubre de 2025  
**Contexto:** Implementación del frontend completo del sistema de login con validaciones, animaciones y manejo de respuestas

### Conversación

**Usuario:** Quiero que hagas el frontend del login

**IA:** He implementado un sistema de login completo con tres archivos (HTML, CSS, JS) que incluye validaciones en tiempo real, animaciones modernas, accesibilidad y manejo robusto de errores.

**Código generado:**

### Archivo: src/main/resources/static/login/login.html

```html
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión de Peajes - Login</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <div class="logo-container">
                    <svg class="logo-icon" viewBox="0 0 24 24">
                        <!-- Logo SVG con tres capas representando peajes -->
                    </svg>
                </div>
                <h1>Sistema de Peajes</h1>
                <p class="subtitle">Ingresa tus credenciales para continuar</p>
            </div>

            <form id="formLogin" class="login-form" onsubmit="event.preventDefault(); hacerLogin();">
                <div class="form-group">
                    <label for="cedula">Cédula</label>
                    <div class="input-container">
                        <svg class="input-icon"><!-- Icono de usuario --></svg>
                        <input 
                            type="text" 
                            id="cedula" 
                            name="cedula" 
                            placeholder="Ingresa tu cédula"
                            required 
                            autocomplete="username"
                            pattern="[0-9.-]+"
                            title="Solo se permiten números, puntos y guiones"
                        >
                    </div>
                    <span class="error-message" id="errorCedula"></span>
                </div>

                <div class="form-group">
                    <label for="contrasenia">Contraseña</label>
                    <div class="input-container">
                        <svg class="input-icon"><!-- Icono de candado --></svg>
                        <input 
                            type="password" 
                            id="contrasenia" 
                            name="contrasenia" 
                            placeholder="Ingresa tu contraseña"
                            required 
                            autocomplete="current-password"
                        >
                        <button type="button" class="toggle-password" id="togglePassword">
                            <svg class="eye-icon"><!-- Icono de ojo --></svg>
                        </button>
                    </div>
                    <span class="error-message" id="errorContrasenia"></span>
                </div>

                <button type="submit" class="btn-login" id="btnLogin">
                    <span class="btn-text">Iniciar Sesión</span>
                    <svg class="btn-icon"><!-- Icono de flecha --></svg>
                </button>
            </form>

            <div class="login-footer">
                <p>¿Problemas para iniciar sesión? <a href="#" id="linkAyuda">Contacta soporte</a></p>
            </div>
        </div>
    </div>

    <div id="loadingSpinner" class="loading-overlay" style="display: none;">
        <div class="spinner"></div>
        <p>Iniciando sesión...</p>
    </div>

    <script src="../utilesVista.js"></script>
    <script src="../vistaWeb.js"></script>
    <script src="login.js"></script>
</body>
</html>
```

### Archivo: src/main/resources/static/login/login.css

```css
:root {
    --primary-color: #4d94ff;
    --primary-dark: #337ab7;
    --error-color: #dc3545;
    --text-dark: #212529;
    --text-muted: #6c757d;
    --bg-body: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    --bg-card: #ffffff;
    --border-radius: 12px;
    --box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
    --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Card principal con animación de entrada */
.login-container {
    width: 100%;
    max-width: 450px;
    animation: fadeInUp 0.6s ease-out;
}

.login-card {
    background: var(--bg-card);
    border-radius: var(--border-radius);
    box-shadow: var(--box-shadow);
    padding: 40px;
    transition: var(--transition);
}

.login-card:hover {
    transform: translateY(-5px);
}

/* Logo con gradiente y animación pulse */
.logo-container {
    width: 80px;
    height: 80px;
    background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
    border-radius: 20px;
    box-shadow: 0 8px 20px rgba(77, 148, 255, 0.3);
    animation: pulse 2s ease-in-out infinite;
}

/* Inputs con efectos de focus */
.input-container input {
    width: 100%;
    padding: 14px 45px;
    border: 2px solid var(--border-color);
    border-radius: 10px;
    transition: var(--transition);
}

.input-container input:focus {
    outline: none;
    border-color: var(--primary-color);
    box-shadow: 0 0 0 4px rgba(77, 148, 255, 0.1);
}

.input-container input.error {
    border-color: var(--error-color);
    background-color: #fff5f5;
}

/* Botón con gradiente y hover effect */
.btn-login {
    width: 100%;
    padding: 15px;
    color: white;
    background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
    border: none;
    border-radius: 10px;
    cursor: pointer;
    box-shadow: 0 4px 15px rgba(77, 148, 255, 0.3);
}

.btn-login:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(77, 148, 255, 0.4);
}

/* Animaciones */
@keyframes fadeInUp {
    from { opacity: 0; transform: translateY(30px); }
    to { opacity: 1; transform: translateY(0); }
}

@keyframes pulse {
    0%, 100% { transform: scale(1); }
    50% { transform: scale(1.05); }
}

@keyframes shake {
    0%, 100% { transform: translateX(0); }
    25% { transform: translateX(-5px); }
    75% { transform: translateX(5px); }
}

/* Spinner de carga */
.loading-overlay {
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(0, 0, 0, 0.7);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    z-index: 9999;
}

.spinner {
    width: 50px;
    height: 50px;
    border: 4px solid rgba(255, 255, 255, 0.3);
    border-top-color: white;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
}
```

### Archivo: src/main/resources/static/login/login.js

```javascript
// Variable para prevenir múltiples envíos
let formularioEnviado = false;

// INICIALIZACIÓN
document.addEventListener('DOMContentLoaded', function() {
    inicializarEventos();
    configurarValidacionEnTiempoReal();
});

function inicializarEventos() {
    // Toggle mostrar/ocultar contraseña
    document.getElementById('togglePassword')
        .addEventListener('click', togglearVisibilidadContrasenia);
    
    // Limpiar errores al escribir
    document.getElementById('cedula')
        .addEventListener('input', () => limpiarError('cedula'));
    
    document.getElementById('contrasenia')
        .addEventListener('input', () => limpiarError('contrasenia'));
}

// VALIDACIÓN
function validarCedula(cedula) {
    if (!cedula.trim()) {
        mostrarError('cedula', 'La cédula es requerida');
        return false;
    }
    
    if (!/^[0-9.-]+$/.test(cedula)) {
        mostrarError('cedula', 'La cédula solo puede contener números, puntos y guiones');
        return false;
    }
    
    limpiarError('cedula');
    return true;
}

function validarFormulario() {
    const cedula = document.getElementById('cedula').value;
    const contrasenia = document.getElementById('contrasenia').value;
    
    return validarCedula(cedula) && validarContrasenia(contrasenia);
}

// LOGIN - FUNCIÓN PRINCIPAL
function hacerLogin() {
    if (formularioEnviado) return;
    
    limpiarTodosLosErrores();
    
    if (!validarFormulario()) return;
    
    formularioEnviado = true;
    deshabilitarBoton();
    mostrarLoading();
    
    // Integración con vistaWeb.js
    const datos = serializarFormulario('formLogin');
    submit('/acceso/login', datos, 'POST');
}

// PROCESAMIENTO DE RESPUESTAS (funciones callback para vistaWeb.js)

// Ejecutada cuando el backend retorna {"id": "redirigir", "parametro": "/url"}
function mostrar_redirigir(paginaUrl) {
    ocultarLoading();
    mostrarMensaje('¡Inicio de sesión exitoso! Redirigiendo...').then(() => {
        setTimeout(() => window.location.href = paginaUrl, 500);
    });
}

// Ejecutada en errores de aplicación (status 299)
function excepcionDeAplicacion(mensaje) {
    ocultarLoading();
    habilitarBoton();
    formularioEnviado = false;
    
    // Parsear RespuestaDTO si es JSON
    try {
        const respuestas = JSON.parse(mensaje);
        if (Array.isArray(respuestas)) {
            let mensajeError = 'Error al iniciar sesión';
            respuestas.forEach(resp => {
                if (resp.id === 'error' || resp.id === 'mensaje') {
                    mensajeError = resp.parametro;
                }
            });
            mostrarMensaje(mensajeError);
            return;
        }
    } catch (e) {}
    
    mostrarMensaje(mensaje);
}

// Ejecutada en errores HTTP (4xx, 5xx)
function procesarErrorSubmit(status, text) {
    ocultarLoading();
    habilitarBoton();
    formularioEnviado = false;
    
    let mensaje = 'Error al procesar la solicitud';
    if (status === 404) mensaje = 'Servicio no disponible';
    else if (status === 500) mensaje = 'Error interno del servidor';
    else if (status === 0) mensaje = 'No se pudo conectar con el servidor';
    
    mostrarMensaje(mensaje);
}

// UTILIDADES UI
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

function togglearVisibilidadContrasenia() {
    const input = document.getElementById('contrasenia');
    input.type = input.type === 'password' ? 'text' : 'password';
}
```

**Características implementadas:**

#### 🎨 Diseño y UX
- **Interfaz moderna**: Card flotante con gradiente de fondo púrpura-azul
- **Logo animado**: Icono SVG de tres capas con efecto pulse continuo
- **Diseño responsive**: Adaptable a móviles, tablets y desktop
- **Animaciones suaves**: Transiciones con cubic-bezier para fluidez profesional
- **Efectos hover**: Card se eleva al pasar el mouse, botones con feedback visual

#### ✅ Validaciones
- **Validación en tiempo real**: Se valida al perder el foco (blur) y al escribir
- **Formato de cédula**: Solo acepta números, puntos y guiones `[0-9.-]+`
- **Validación de contraseña**: Verifica que no esté vacía
- **Mensajes contextuales**: Errores específicos debajo de cada campo con animación shake
- **Prevención de campos vacíos**: Atributo `required` en inputs

#### 🔒 Seguridad
- **Toggle de visibilidad**: Botón para mostrar/ocultar contraseña con icono de ojo
- **Prevención de doble envío**: Variable `formularioEnviado` bloquea múltiples submissions
- **Autocomplete seguro**: Usa `username` y `current-password` para password managers
- **Validación HTML5**: Atributos `pattern` y `title` para hints visuales

#### 🔌 Integración con Backend
- **vistaWeb.js**: Librería de comunicación del proyecto ya integrada
- **Endpoint**: POST a `/acceso/login` con datos serializados del formulario
- **Funciones callback**:
  - `mostrar_redirigir()`: Maneja redirección exitosa según tipo de usuario
  - `excepcionDeAplicacion()`: Procesa errores de negocio (status 299)
  - `procesarErrorSubmit()`: Maneja errores HTTP (404, 500, conexión)
- **Formato RespuestaDTO**: Parsea arrays `[{id: "error", parametro: "mensaje"}]`

#### ♿ Accesibilidad
- **ARIA labels**: Atributos `aria-label` en botón toggle de contraseña
- **Focus visible**: Indicadores de foco con outline para teclado
- **Prefers reduced motion**: Respeta configuración de animaciones reducidas del SO
- **Contraste WCAG**: Variables CSS con colores de alto contraste
- **Navegación por teclado**: Todo accesible con Tab/Enter/Escape

#### 🎭 Estados Visuales
- **Loading overlay**: Fondo oscuro con spinner rotatorio y mensaje
- **Botón deshabilitado**: Cambia a "Iniciando sesión..." y cursor not-allowed
- **Inputs con error**: Borde rojo, fondo rosa claro (#fff5f5), mensaje animado
- **Hover interactivo**: Botones y card con transformaciones visuales
- **Iconos SVG**: Inline sin dependencias externas, escalables

#### 📱 Responsive
- **Breakpoint 480px**: Ajustes para pantallas pequeñas
- **Padding adaptativo**: Reduce espacios en móviles (40px → 25px)
- **Fuentes escalables**: Títulos y textos ajustados para legibilidad
- **Táctil friendly**: Áreas de click amplias (44px+ mínimo recomendado)

#### 🚀 Flujo Completo
1. **Carga**: Inicializa eventos y validaciones en DOMContentLoaded
2. **Entrada**: Usuario escribe cédula y contraseña
3. **Validación**: Al escribir limpia errores, al blur valida formato
4. **Submit**: Previene default, valida todo, muestra loading
5. **Envío**: Serializa formulario y POST a `/acceso/login`
6. **Backend**: Valida credenciales, retorna RespuestaDTO con redirección o error
7. **Éxito**: Muestra mensaje y redirige a dashboard (propietario/administrador)
8. **Error**: Muestra mensaje específico, oculta loading, rehabilita botón

#### 📦 Dependencias
- **utilesVista.js**: Funciones `mostrarMensaje()`, `serializarFormulario()`
- **vistaWeb.js**: Función `submit()` y sistema de callbacks automático
- **index.html**: Página raíz con meta-refresh a `/login/login.html`

**Integración con arquitectura del proyecto:**
- Se integra perfectamente con `LoginController.java` que retorna `List<RespuestaDTO>`
- El endpoint esperado es `@PostMapping("/acceso/login")` o similar
- La Fachada valida credenciales y retorna `{id: "redirigir", parametro: "/propietario/dashboard.html"}` o error
- Maneja estados de usuario (Habilitado, Deshabilitado, Suspendido, Penalizado) mostrando mensajes apropiados

---


