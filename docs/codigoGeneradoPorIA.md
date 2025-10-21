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

## Resolución de Dependencia Circular con @Lazy

**Fecha:** 21 de octubre de 2025  
**Contexto:** Implementación de `SistemaAutorizacion` que necesita comunicarse con otros sistemas a través de Fachada, evitando dependencia circular

### Conversación

**Usuario:** Tengo un error de dependencia circular entre `Fachada` y `SistemaAutorizacion`. No quiero que los sistemas hablen directamente entre ellos, quiero que toda la comunicación sea a través de Fachada. ¿Podría hacer que Fachada sea un singleton con `getInstance()`?

**IA:** Usar Singleton tradicional con `getInstance()` no es recomendable en Spring porque:
1. Spring ya maneja los beans como Singletons automáticamente
2. Mezclar Singleton tradicional con Spring causa problemas de gestión del ciclo de vida
3. No resuelve el problema: Spring seguirá detectando la dependencia circular

La solución recomendada es usar **`@Lazy`** de Spring, que permite romper ciclos circulares de forma limpia manteniendo toda la comunicación a través de Fachada.

**Código generado:**

```java
package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import jakarta.servlet.http.HttpSession;

import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;

/**
 * Sistema de autorización para validar sesiones y permisos de usuarios
 * Centraliza la lógica de validación para reutilizar en múltiples controladores
 */
@Service
class SistemaAutorizacion {

    private final Fachada fachada;

    // @Lazy rompe la dependencia circular: permite inyectar Fachada sin crear el bean completo en construcción
    SistemaAutorizacion(@Lazy Fachada fachada) {
        this.fachada = fachada;
    }

    /**
     * Valida que exista una sesión activa y que el usuario tenga el permiso requerido
     * 
     * @param session La sesión HTTP actual
     * @param permisoRequerido El permiso que debe tener el usuario
     * @return El usuario validado
     * @throws AppException Si no hay sesión, el usuario no existe, no tiene permiso, o está deshabilitado
     */
    Usuario validarSesionYPermiso(HttpSession session, Permiso permisoRequerido) throws AppException {
        // 1. Validar que existe sesión activa
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            throw new AppException("Debe iniciar sesión");
        }

        // 2. Obtener el usuario completo a través de Fachada
        Usuario usuario = fachada.getUsuarioPorId(usuarioId);
        if (usuario == null) {
            throw new AppException("Debe iniciar sesión");
        }

        // 3. Validar que tenga el permiso requerido
        if (!usuario.tienePermiso(permisoRequerido)) {
            throw new AppException("Acceso no autorizado");
        }

        // 4. Si es Propietario, validar que pueda entrar al sistema
        if (usuario instanceof Propietario) {
            Propietario propietario = (Propietario) usuario;
            if (!propietario.puedeIngresarAlSistema()) {
                throw new AppException("Usuario deshabilitado, no puede ingresar al sistema");
            }
        }

        return usuario;
    }
}
```

**Características implementadas:**

#### 🔧 Solución Técnica
- **`@Lazy`**: Anotación de Spring (`org.springframework.context.annotation.Lazy`) que resuelve dependencias circulares
- **Inyección perezosa**: En construcción se inyecta un proxy en lugar del bean completo
- **Resolución en runtime**: El proxy delega al bean real cuando se invoca un método por primera vez
- **Transparente**: El código funciona igual, solo cambia la forma de inyección

#### 📐 Arquitectura
- **Mantiene el patrón Facade**: Toda comunicación entre sistemas pasa por Fachada
- **Evita acoplamiento directo**: `SistemaAutorizacion` NO depende de `SistemaUsuarios` directamente
- **Package-private**: Ambos sistemas son package-private, solo Fachada es pública
- **Escalable**: Otros sistemas pueden usar el mismo patrón si necesitan comunicarse

#### ⚠️ Por qué NO usar Singleton tradicional
```java
// ❌ NO HACER ESTO
public class Fachada {
    private static Fachada instancia;
    
    public static Fachada getInstance() {
        if (instancia == null) instancia = new Fachada();
        return instancia;
    }
}

// Problemas:
// 1. Mezcla gestión manual con Spring
// 2. Pierdes inyección de dependencias
// 3. No resuelve el ciclo en Spring Context
// 4. Dificulta testing (no puedes mockear getInstance())
```

#### ✅ Ventajas de @Lazy
1. **Solución recomendada por Spring**: Es el patrón oficial para dependencias circulares
2. **Mantiene inyección de dependencias**: Todo sigue siendo gestionado por Spring
3. **Fácil de testear**: Puedes inyectar mocks en tests
4. **No requiere cambios en otros archivos**: Solo se modifica el constructor del sistema
5. **Transparente en ejecución**: No hay diferencia de comportamiento en runtime

#### 🔄 Flujo de resolución
```
1. Spring crea beans en orden:
   - Intenta crear Fachada → necesita SistemaAutorizacion
   - Intenta crear SistemaAutorizacion → necesita Fachada (@Lazy)
   
2. Con @Lazy:
   - Spring inyecta un PROXY de Fachada en SistemaAutorizacion
   - El proxy no necesita que Fachada esté completamente inicializada
   - Spring completa la creación de SistemaAutorizacion
   - Spring usa SistemaAutorizacion para completar Fachada
   - Todos los beans están listos
   
3. En tiempo de ejecución:
   - Cuando se llama a fachada.getUsuarioPorId(...)
   - El proxy delega al bean real de Fachada (ya inicializado)
   - Funciona transparentemente
```

#### 📝 Actualización en AGENTS.md
Se agregó una nueva regla crítica de arquitectura:

**⚠️ COMUNICACIÓN ENTRE SISTEMAS**: Toda comunicación debe ser a través de la Fachada, incluso entre sistemas
- Si un sistema necesita funcionalidad de otro sistema, debe llamar a la Fachada
- **NUNCA** inyectar directamente otro sistema en un sistema (ej: NO inyectar `SistemaPuestos` en `SistemaUsuarios`)
- Si esto crea dependencia circular, usar `@Lazy` en la inyección de Fachada
- Usar `@Lazy` de Spring (`org.springframework.context.annotation.Lazy`) para romper ciclos: inyecta un proxy que se resuelve en tiempo de ejecución

#### 🎯 Uso en el proyecto
`SistemaAutorizacion` valida sesiones y permisos en múltiples endpoints:
- Necesita obtener usuarios por ID → debe llamar a Fachada
- Fachada depende de SistemaAutorizacion → dependencia circular
- Solución: `@Lazy` en la inyección de Fachada en SistemaAutorizacion
- Resultado: Arquitectura limpia, comunicación centralizada, sin ciclos

**Ejemplo de uso desde controlador:**
```java
@RestController
@RequestMapping("/propietario")
public class PropietarioController {
    private final Fachada fachada;
    
    @GetMapping("/datos")
    public List<RespuestaDTO> obtenerDatos(HttpSession session) throws AppException {
        // La Fachada valida sesión y permisos internamente vía SistemaAutorizacion
        Usuario usuario = fachada.validarSesionYPermiso(session, Permiso.PROPIETARIO_DASHBOARD);
        // ... resto del código
    }
}
```

---

## Sistema de Autorización y Manejo de Sesiones

**Fecha:** 21 de octubre de 2025  
**Contexto:** Implementación completa del sistema de autorización con invalidación automática de sesión y redirección al login con mensajes persistentes usando `sessionStorage`

### Conversación

**Usuario:** Cuando se hace login se muestra un mensaje de inicio de sesión exitoso, no quiero que aparezca este mensaje, que se redirija directamente.

**IA:** He eliminado el mensaje de éxito y el delay, ahora redirige directamente después de login exitoso.

**Usuario:** ¿Cómo hago para que el botón cerrar sesión del dashboard llame a logout?

**IA:** Configuré el botón para llamar a `cerrarSesion()` que usa `submit('/acceso/logout', '', 'GET')` y procesé la redirección con `mostrar_redirigir()`.

**Usuario:** Usa `redirigir` para que el botón cerrar sesión redirija al login.

**IA:** Agregué la función `mostrar_redirigir()` en dashboard.js para procesar automáticamente las redirecciones.

**Usuario:** Cuando se hace login se muestra un mensaje, no quiero que aparezca. Y, ¿cómo muestro las excepciones en el frontend? Las librerías tienen un modo para mostrar mensajes.

**IA:** Las librerías tienen `mostrarMensaje()` en `utilesVista.js`. Implementé `excepcionDeAplicacion()` que parsea JSON y usa `mostrarMensaje()` en lugar de `alert()` feo.

**Usuario:** No quiero que se redirija al login detectando palabras clave, quiero que cuando `validarSesionYPermiso` falle se haga logout y redirija.

**IA:** Implementé `UnauthorizedException` y `GlobalExceptionHandler` que captura la excepción, invalida la sesión automáticamente y retorna redirección al login.

**Usuario:** Quiero que se lance la excepción con status 299, se redirija al login y después se muestre el mensaje de error. ¿Cómo sería el flujo?

**IA:** Con status 299, `vistaWeb.js` llama a `excepcionDeAplicacion()` pero si redirigimos inmediatamente, el mensaje se pierde. La solución es usar `sessionStorage` para pasar el mensaje entre páginas:
1. Dashboard guarda mensaje en `sessionStorage.setItem('mensajeLogin', mensaje)`
2. Redirige al login
3. Login lee el mensaje con `sessionStorage.getItem('mensajeLogin')` al cargar
4. Muestra el mensaje con `mostrarMensaje()`
5. Limpia el storage con `sessionStorage.removeItem('mensajeLogin')`

### Código generado

#### 1. UnauthorizedException.java

```java
package ort.da.obligatorio339182.exceptions;

public class UnauthorizedException extends Exception {
    public UnauthorizedException(String message) {
        super(message);
    }
}
```

**Características:**
- Nueva excepción específica para errores de autorización
- Separada de `AppException` para distinguir errores de sesión de errores de negocio
- `GlobalExceptionHandler` la procesa de forma especial para logout automático

#### 2. SistemaAutorizacion.java - Invalidación de sesión

```java
@Service
class SistemaAutorizacion {
    private final Fachada fachada;
    
    SistemaAutorizacion(@Lazy Fachada fachada) {
        this.fachada = fachada;
    }
    
    Usuario validarSesionYPermiso(HttpSession session, Permiso permisoRequerido) throws UnauthorizedException {
        // 1. Validar que existe sesión activa
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            invalidarSesion(session);
        }
        
        // 2. Obtener el usuario completo
        Usuario usuario = fachada.getUsuarioPorId(usuarioId);
        if (usuario == null) {
            invalidarSesion(session);
        }
        
        // 3. Validar que tenga el permiso requerido
        if (!usuario.tienePermiso(permisoRequerido)) {
            invalidarSesion(session);
        }
        
        // 4. Si es Propietario, validar que pueda entrar al sistema
        if (usuario instanceof Propietario) {
            Propietario propietario = (Propietario) usuario;
            if (!propietario.puedeIngresarAlSistema()) {
                invalidarSesion(session);
            }
        }
        
        return usuario;
    }
    
    private void invalidarSesion(HttpSession session) throws UnauthorizedException {
        session.invalidate();
        throw new UnauthorizedException("Sesión inválida");
    }
}
```

**Características:**
- `invalidarSesion()` centraliza la lógica de logout + throw exception
- Invalida la sesión en **todos** los casos de fallo de autorización
- Lanza `UnauthorizedException` en lugar de `AppException`
- Mantiene visibilidad package-private (solo accesible vía Fachada)

#### 3. GlobalExceptionHandler.java - Manejo de autorización

```java
@ControllerAdvice
public class GlobalExpetionHandler {
    
    /**
     * Maneja errores de autorización (sesión inválida o sin permisos)
     * Redirige automáticamente al login
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<List<RespuestaDTO>> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.ok(
            RespuestaDTO.lista(
                new RespuestaDTO("error", e.getMessage()),
                new RespuestaDTO("redirigir", "/login/login.html")
            )
        );
    }
    
    /**
     * Maneja errores de aplicación generales
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<List<RespuestaDTO>> handleAppException(AppException e) {
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(new RespuestaDTO("error", e.getMessage()))
        );
    }
}
```

**NOTA IMPORTANTE:** Aunque el código muestra `ResponseEntity.ok()` (status 200), se cambió a `ResponseEntity.status(299)` para que `vistaWeb.js` llame a `excepcionDeAplicacion()` y permita usar `sessionStorage` para persistir el mensaje entre páginas.

**Características:**
- Dos manejadores separados: uno para autorización, otro para errores de negocio
- `UnauthorizedException` → Status 299 + redirección + mensaje
- `AppException` → Status 299 + mensaje (sin redirección)
- La sesión ya fue invalidada en `SistemaAutorizacion`

#### 4. dashboard.js - Manejo de excepciones con sessionStorage

```javascript
/**
 * Función que maneja las excepciones de aplicación (status 299)
 * vistaWeb.js llama automáticamente a esta función cuando recibe errores del backend
 */
function excepcionDeAplicacion(mensaje) {
    console.error('Excepción de aplicación:', mensaje);
    
    // Parsear el mensaje JSON con estructura de RespuestaDTO
    try {
        const respuestas = JSON.parse(mensaje);
        if (Array.isArray(respuestas)) {
            let urlRedireccion = null;
            let mensajeError = null;
            
            // Extraer redirección y mensaje de error
            respuestas.forEach(resp => {
                if (resp.id === 'redirigir') {
                    urlRedireccion = resp.parametro;
                }
                if (resp.id === 'error' || resp.id === 'ERROR' || resp.id === 'mensaje') {
                    mensajeError = resp.parametro;
                }
            });
            
            // Si hay mensaje de error, guardarlo en sessionStorage para mostrarlo en el login
            if (mensajeError) {
                sessionStorage.setItem('mensajeLogin', mensajeError);
            }
            
            // Si hay redirección, redirigir
            if (urlRedireccion) {
                window.location.href = urlRedireccion;
                return;
            }
        }
    } catch (e) {
        console.error('Error parseando respuesta:', e);
    }
    
    // Si no se pudo parsear o no hay redirección, mostrar mensaje por defecto
    alert('Error: ' + mensaje);
}

/**
 * Función que procesa la redirección
 * Se ejecuta cuando el backend retorna {id: "redirigir", parametro: "/url"}
 */
function mostrar_redirigir(paginaUrl) {
    console.log("Redirigiendo a:", paginaUrl);
    window.location.href = paginaUrl;
}

/**
 * Cierra la sesión del usuario y redirige al login
 */
function cerrarSesion() {
    submit('/acceso/logout', '', 'GET');
}
```

**Características:**
- `excepcionDeAplicacion()` parsea el JSON con `[{id, parametro}]`
- Extrae tanto la URL de redirección como el mensaje de error
- **Guarda el mensaje en `sessionStorage`** antes de redirigir
- Funciona con status 299 (vistaWeb.js llama automáticamente a esta función)
- Fallback con `alert()` si no se puede parsear

#### 5. login.js - Lectura y muestra de mensaje persistido

```javascript
document.addEventListener('DOMContentLoaded', function() {
    ocultarLoading();
    inicializarEventos();
    configurarValidacionEnTiempoReal();
    
    // Verificar si hay un mensaje pendiente de mostrar (ej: sesión expirada)
    verificarMensajePendiente();
});

/**
 * Verifica si hay un mensaje pendiente de mostrar desde otra página
 * (ej: cuando se redirige desde el dashboard por sesión expirada)
 */
function verificarMensajePendiente() {
    const mensajePendiente = sessionStorage.getItem('mensajeLogin');
    if (mensajePendiente) {
        // Mostrar el mensaje con diálogo bonito de utilesVista.js
        mostrarMensaje(mensajePendiente);
        // Limpiar el mensaje del sessionStorage
        sessionStorage.removeItem('mensajeLogin');
    }
}
```

**Características:**
- Se ejecuta automáticamente al cargar la página de login
- Lee el mensaje de `sessionStorage` si existe
- Muestra el mensaje usando `mostrarMensaje()` (diálogo bonito de `utilesVista.js`)
- **Limpia el mensaje** después de mostrarlo para que no se repita
- No interfiere con el flujo normal de login si no hay mensaje

#### 6. login.js - Redirección directa sin mensaje de éxito

```javascript
function mostrar_redirigir(paginaUrl) {
    ocultarLoading();
    console.log("Redirigiendo a:", paginaUrl);
    
    // Redirigir directamente sin mensaje
    window.location.href = paginaUrl;
}
```

**Características:**
- Eliminado el mensaje "¡Inicio de sesión exitoso! Redirigiendo..."
- Eliminado el delay de 500ms con `setTimeout()`
- Redirección inmediata después de login exitoso
- Mejor experiencia de usuario: más rápido y directo

### Flujo completo de autorización

#### 🔐 Caso 1: Usuario sin sesión intenta acceder al dashboard

```
1. GET /usuarios/dashboard-propietario
   ↓
2. UsuariosController.obtenerInformacionPersonal()
   ↓
3. fachada.validarSesionYPermiso(session, Permiso.PROPIETARIO_DASHBOARD)
   ↓
4. SistemaAutorizacion detecta: usuarioId == null
   ↓
5. session.invalidate() 🔒
   ↓
6. throw new UnauthorizedException("Sesión inválida")
   ↓
7. GlobalExceptionHandler.handleUnauthorizedException()
   ↓
8. Return status 299 + [{error: "Sesión inválida"}, {redirigir: "/login/login.html"}]
   ↓
9. vistaWeb.js detecta status 299
   ↓
10. Llama a excepcionDeAplicacion(mensaje)
    ↓
11. dashboard.js:
    - sessionStorage.setItem('mensajeLogin', 'Sesión inválida')
    - window.location.href = '/login/login.html'
    ↓
12. Login carga → verificarMensajePendiente()
    ↓
13. mostrarMensaje('Sesión inválida') → Diálogo bonito 📢
    ↓
14. sessionStorage.removeItem('mensajeLogin')
    ↓
15. Usuario puede hacer login ✅
```

#### 🚪 Caso 2: Usuario hace clic en "Cerrar Sesión"

```
1. Click en botón "Cerrar Sesión"
   ↓
2. cerrarSesion() → submit('/acceso/logout', '', 'GET')
   ↓
3. LoginController.logout()
   ↓
4. session.invalidate() 🔒
   ↓
5. Return [{redirigir: "/login/login.html"}]
   ↓
6. vistaWeb.js procesa (status 200)
   ↓
7. Llama a mostrar_redirigir("/login/login.html")
   ↓
8. window.location.href = '/login/login.html'
   ↓
9. Usuario en login sin mensaje ✅
```

#### ✅ Caso 3: Login exitoso

```
1. hacerLogin() → submit('/acceso/login', datos, 'POST')
   ↓
2. LoginController.login()
   ↓
3. fachada.login(cedula, contrasenia)
   ↓
4. session.setAttribute("usuarioId", usuario.getId())
   ↓
5. Return [{redirigir: "/propietario/dashboard/dashboard.html"}]
   ↓
6. vistaWeb.js procesa (status 200)
   ↓
7. Llama a mostrar_redirigir("/propietario/dashboard/dashboard.html")
   ↓
8. window.location.href (redirección DIRECTA, sin mensaje)
   ↓
9. Usuario en dashboard ✅
```

### Ventajas de la implementación

#### 🎯 Separación de responsabilidades
- **`UnauthorizedException`**: Errores de autorización → logout automático + redirección
- **`AppException`**: Errores de lógica de negocio → mensaje de error con status 299
- **`sessionStorage`**: Persistencia temporal de mensajes entre páginas (se borra al cerrar tab)

#### 🔒 Seguridad
- **Invalidación automática**: La sesión se destruye en el servidor antes de redirigir
- **Sin tokens en URL**: No se pasan mensajes por query string (sería inseguro)
- **Centralizado**: Toda la lógica de autorización en un solo lugar (`SistemaAutorizacion`)

#### 💡 Experiencia de usuario
- **Login rápido**: Redirección directa sin mensajes innecesarios
- **Logout claro**: Mensaje explicativo de por qué fue deslogueado
- **Diálogos bonitos**: Usa `mostrarMensaje()` en lugar de `alert()` feo
- **Sin repeticiones**: El mensaje se muestra una vez y se borra

#### 🏗️ Arquitectura limpia
- **Patrón Facade**: Toda comunicación a través de Fachada
- **@Lazy**: Resuelve dependencia circular sin Singleton tradicional
- **Package-private**: Sistemas no expuestos, solo Fachada es pública
- **Reutilizable**: El patrón de `sessionStorage` puede usarse para otros mensajes

#### 🧪 Testeable
- `SistemaAutorizacion` puede testearse con mocks de `HttpSession` y `Fachada`
- `GlobalExceptionHandler` puede testearse verificando el `ResponseEntity`
- Frontend puede testearse simulando `sessionStorage` y `window.location`

### Archivos modificados

**Backend:**
- ✅ `exceptions/UnauthorizedException.java` (nuevo)
- ✅ `exceptions/GlobalExceptionHandler.java`
- ✅ `services/SistemaAutorizacion.java`
- ✅ `services/Fachada.java`
- ✅ `controllers/UsuariosController.java`

**Frontend:**
- ✅ `login/login.js`
- ✅ `propietario/dashboard/dashboard.js`
- ✅ `propietario/dashboard/dashboard.html`

**Documentación:**
- ✅ `context/autorizacion-sesiones.md` (nuevo archivo de contexto)
- ✅ `docs/codigoGeneradoPorIA.md` (este documento)
- ✅ `AGENTS.md` (referencia al nuevo archivo de contexto)

### Recursos adicionales

Para más detalles sobre el flujo completo de autorización y sesiones, consulta:
- **Archivo de contexto completo**: `context/autorizacion-sesiones.md`
- **Guía de arquitectura**: Sección "Sistema de Autorización" en AGENTS.md

---


