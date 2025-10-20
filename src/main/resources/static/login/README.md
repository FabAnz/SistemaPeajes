# Sistema de Login - Sistema de Gestión de Peajes

## 📋 Descripción

Sistema de autenticación seguro integrado con las librerías frontend del proyecto (`vistaWeb.js` y `utilesVista.js`) para el acceso de usuarios al sistema de gestión de peajes.

## 🎨 Características del Frontend

### Diseño
- **Interfaz moderna** con gradientes y animaciones suaves
- **Diseño responsive** adaptable a dispositivos móviles y desktop
- **Accesibilidad** mejorada con soporte para navegación por teclado
- **Animaciones** fluidas y transiciones CSS

### Funcionalidades
- ✅ Validación en tiempo real de campos
- ✅ Toggle para mostrar/ocultar contraseña
- ✅ Spinner de carga durante el proceso de login
- ✅ Mensajes de error claros y específicos
- ✅ Prevención de múltiples envíos
- ✅ Redirección automática según tipo de usuario
- ✅ Integración completa con `vistaWeb.js` y `utilesVista.js`

## 🔧 Arquitectura de Integración

### Librerías Frontend Utilizadas

Este sistema usa las librerías estándar del proyecto:

#### vistaWeb.js
Gestiona la comunicación HTTP con el backend mediante:
- **`submit(url, datos, method)`**: Envía peticiones POST/GET al backend
- **`serializarFormulario(id)`**: Convierte formularios a formato URL-encoded
- **Procesamiento automático** de respuestas mediante funciones `mostrar_xxx()`
- **Manejo de errores** con status 299 para excepciones de aplicación

#### utilesVista.js
Proporciona utilidades de interfaz:
- **`mostrarMensaje(texto)`**: Diálogo modal con mensaje
- **`mostrarConfirmacion(texto)`**: Diálogo con botones Sí/No
- Generadores dinámicos de tablas y listas

## 🔒 Seguridad Implementada

### Backend
- **Almacenamiento seguro en sesión**: Solo se guarda el ID del usuario (no contraseñas ni datos sensibles)
- **Validación de credenciales** mediante el método `login()` de la Fachada
- **Manejo de excepciones** con `GlobalExceptionHandler` que retorna status 299
- **Parámetros URL-encoded** con `@RequestParam` (no JSON vulnerable)

### Frontend
- Validación de formato de cédula (solo números, puntos y guiones)
- Campos requeridos con validación HTML5 y JavaScript
- Encriptación HTTPS (cuando se implemente en producción)
- Integración con librerías del proyecto para seguridad consistente

## 📁 Archivos

```
static/login/
├── login.html      # Estructura HTML del formulario
├── login.css       # Estilos modernos y responsive
├── login.js        # Lógica de validación integrada con vistaWeb.js
└── README.md       # Este archivo
```

## 🔌 Comunicación Backend-Frontend

### Formato de Petición

**Método:** POST  
**URL:** `/acceso/login`  
**Content-Type:** `application/x-www-form-urlencoded`  
**Datos:** `cedula=12345678&contrasenia=miPassword123`

```javascript
// Frontend (login.js)
function hacerLogin() {
    const datos = serializarFormulario('formLogin'); // "cedula=...&contrasenia=..."
    submit('/acceso/login', datos, 'POST');
}
```

```java
// Backend (LoginController.java)
@PostMapping("/login")
public List<RespuestaDTO> login(
        HttpSession session, 
        @RequestParam String cedula, 
        @RequestParam String contrasenia) throws AppException {
    // ...
}
```

### Formato de Respuesta

**Status 200 (Éxito):**
```json
[
  {
    "id": "mensaje",
    "parametro": "Inicio de sesión exitoso"
  },
  {
    "id": "LOGIN_EXITOSO",
    "parametro": true
  },
  {
    "id": "TIPO_USUARIO",
    "parametro": "Administrador"
  },
  {
    "id": "PAGINA_REDIRECCION",
    "parametro": "/administrador/dashboard.html"
  }
]
```

**Status 299 (Error de aplicación):**
```json
[
  {
    "id": "error",
    "parametro": "Usuario no encontrado o contraseña incorrecta"
  }
]
```

### Procesamiento de Respuestas

El sistema define funciones específicas para cada tipo de respuesta:

```javascript
// Se ejecuta automáticamente cuando llega {"id": "mensaje", ...}
function mostrar_mensaje(texto) {
    console.log("Mensaje:", texto);
}

// Se ejecuta automáticamente cuando llega {"id": "LOGIN_EXITOSO", ...}
function mostrar_LOGIN_EXITOSO(exitoso) {
    if (exitoso) {
        console.log("Login exitoso");
    }
}

// Se ejecuta automáticamente cuando llega {"id": "TIPO_USUARIO", ...}
function mostrar_TIPO_USUARIO(tipoUsuario) {
    sessionStorage.setItem('tipoUsuario', tipoUsuario);
}

// Se ejecuta automáticamente cuando llega {"id": "PAGINA_REDIRECCION", ...}
function mostrar_PAGINA_REDIRECCION(paginaUrl) {
    mostrarMensaje('¡Login exitoso!').then(() => {
        window.location.href = paginaUrl;
    });
}

// Se ejecuta cuando el backend retorna status 299
function excepcionDeAplicacion(mensaje) {
    mostrarMensaje(mensaje); // Muestra diálogo modal con el error
    habilitarBoton();
}
```

## 🚀 Flujo de Autenticación

```
1. Usuario ingresa credenciales
   ↓
2. Click en "Iniciar Sesión" → hacerLogin()
   ↓
3. Validación frontend (formato)
   ↓
4. serializarFormulario('formLogin') → "cedula=...&contrasenia=..."
   ↓
5. submit('/acceso/login', datos, 'POST')
   ↓
6. Backend valida credenciales
   ↓
   ┌─────────────────┬──────────────────┐
   │ ÉXITO (200)     │ ERROR (299)      │
   ├─────────────────┼──────────────────┤
   │ Guarda usuarioId│ Retorna mensaje  │
   │ Retorna RespDTO │ de error         │
   └─────────────────┴──────────────────┘
   ↓                   ↓
7. Frontend procesa   excepcionDeAplicacion()
   respuestas         muestra error
   ↓
8. mostrar_LOGIN_EXITOSO()
   mostrar_TIPO_USUARIO()
   mostrar_PAGINA_REDIRECCION()
   ↓
9. Redirige a dashboard
```

## 🎯 Redirecciones

| Tipo de Usuario | Página destino |
|----------------|----------------|
| Administrador  | `/administrador/dashboard.html` |
| Propietario    | `/propietario/dashboard.html` |

## 🛠️ Personalización

### Colores (CSS Variables)
```css
:root {
    --primary-color: #4d94ff;
    --primary-dark: #337ab7;
    --error-color: #dc3545;
    --success-color: #28a745;
}
```

### Validaciones adicionales
Editar `login.js` → función `validarCedula()` o `validarContrasenia()`

### Agregar nuevas respuestas
1. Backend: Agregar nuevo `RespuestaDTO` con id único
2. Frontend: Crear función `mostrar_<id>(parametro)`

Ejemplo:
```java
// Backend
return RespuestaDTO.lista(
    new RespuestaDTO("nombre_usuario", usuario.getNombreCompleto())
);
```

```javascript
// Frontend
function mostrar_nombre_usuario(nombre) {
    document.getElementById('bienvenida').textContent = `Hola, ${nombre}`;
}
```

## 🧪 Testing

Para probar el login:

1. **Inicia la aplicación:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Accede a:**
   ```
   http://localhost:8080
   ```
   (Redirige automáticamente a `/login/login.html`)

3. **Credenciales de prueba** (ver `DatosPrecargados.java`):
   - Administrador: `cedula` del admin precargado
   - Propietario: `cedula` de un propietario precargado

4. **Verifica en la consola del navegador** (F12):
   - Peticiones HTTP en la pestaña "Network"
   - Logs de JavaScript en la pestaña "Console"
   - Respuestas del servidor con estructura `List<RespuestaDTO>`

## 📝 Convenciones del Proyecto

### Backend

✅ **SIEMPRE retornar `List<RespuestaDTO>`**
```java
@PostMapping("/endpoint")
public List<RespuestaDTO> metodo() {
    return RespuestaDTO.lista(
        new RespuestaDTO("id1", valor1),
        new RespuestaDTO("id2", valor2)
    );
}
```

✅ **Usar status 299 para errores de negocio**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<List<RespuestaDTO>> handleAppException(AppException e) {
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(new RespuestaDTO("error", e.getMessage()))
        );
    }
}
```

✅ **Recibir parámetros URL-encoded con `@RequestParam`**
```java
@PostMapping("/endpoint")
public List<RespuestaDTO> metodo(
    @RequestParam String param1,
    @RequestParam String param2) {
    // ...
}
```

### Frontend

✅ **Definir funciones `mostrar_<id>()` para cada respuesta**
```javascript
function mostrar_usuarios(listaUsuarios) {
    const html = crearTablaDesdeJson(listaUsuarios);
    document.getElementById('tabla').innerHTML = html;
}
```

✅ **Usar `submit()` en lugar de `fetch()` directamente**
```javascript
function enviarDatos() {
    const datos = serializarFormulario('miForm');
    submit('/api/endpoint', datos, 'POST');
}
```

✅ **Personalizar `excepcionDeAplicacion()` para errores**
```javascript
function excepcionDeAplicacion(mensaje) {
    mostrarMensaje(mensaje); // Usar diálogo modal en lugar de alert()
}
```

## 📚 Documentación Relacionada

- **Librerías Frontend**: Ver `context/frontend-libraries.md`
- **Datos Precargados**: Ver `context/datos-precargados.md`
- **Requerimientos**: Ver `context/requerimientos.md`
- **AGENTS.md**: Guía completa del proyecto

## 🐛 Solución de Problemas

### El login no funciona
- ✅ Verifica que el backend esté corriendo (`./mvnw spring-boot:run`)
- ✅ Revisa la consola del navegador (F12) para errores JavaScript
- ✅ Verifica que el endpoint `/acceso/login` responda correctamente
- ✅ Asegúrate de que las credenciales estén en `DatosPrecargados`

### No se procesan las respuestas
- ✅ Verifica que las funciones `mostrar_xxx()` estén definidas en `login.js`
- ✅ Revisa que el backend retorne `List<RespuestaDTO>` correctamente
- ✅ Verifica que los `id` de las respuestas coincidan con los nombres de las funciones

### Errores no se muestran correctamente
- ✅ Verifica que `GlobalExceptionHandler` esté creado y configurado
- ✅ Asegúrate de que retorne status 299 para `AppException`
- ✅ Verifica que `excepcionDeAplicacion()` esté definida en `login.js`

### Errores de CORS
- Si usas diferentes puertos/dominios, configura CORS en Spring Boot
- En desarrollo local (mismo puerto) no deberías tener problemas

## 🔐 Notas de Seguridad

### ✅ Implementado
- Solo se guarda el ID en sesión (no contraseñas)
- Validación de credenciales en backend
- Manejo de errores sin exponer información sensible
- GlobalExceptionHandler centralizado para excepciones
- Parámetros URL-encoded (estándar web)

### 🔜 Recomendaciones futuras
- Implementar rate limiting para prevenir ataques de fuerza bruta
- Agregar CAPTCHA después de múltiples intentos fallidos
- Implementar logout explícito con invalidación de sesión
- Agregar timeout de sesión
- Implementar CSRF tokens
- Agregar autenticación de dos factores (2FA)
- Usar HTTPS en producción

## 📊 Ejemplo Completo de Integración

### HTML
```html
<form id="formLogin" onsubmit="event.preventDefault(); hacerLogin();">
    <input name="cedula" type="text" id="cedula" />
    <input name="contrasenia" type="password" id="contrasenia" />
    <button type="submit">Iniciar Sesión</button>
</form>
```

### JavaScript
```javascript
function hacerLogin() {
    if (!validarFormulario()) return;
    
    const datos = serializarFormulario('formLogin');
    submit('/acceso/login', datos, 'POST');
}

function mostrar_LOGIN_EXITOSO(exitoso) {
    if (exitoso) console.log("Login OK");
}

function mostrar_PAGINA_REDIRECCION(url) {
    window.location.href = url;
}

function excepcionDeAplicacion(mensaje) {
    mostrarMensaje(mensaje);
}
```

### Backend
```java
@PostMapping("/login")
public List<RespuestaDTO> login(
        @RequestParam String cedula,
        @RequestParam String contrasenia) throws AppException {
    
    Usuario usuario = fachada.login(cedula, contrasenia);
    
    return RespuestaDTO.lista(
        new RespuestaDTO("LOGIN_EXITOSO", true),
        new RespuestaDTO("PAGINA_REDIRECCION", "/dashboard.html")
    );
}
```

---

**Última actualización:** 2025-10-19  
**Versión:** 2.0 - Integración completa con librerías del proyecto
