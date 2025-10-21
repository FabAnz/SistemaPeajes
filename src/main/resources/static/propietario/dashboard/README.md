# Dashboard del Propietario

Panel de control personal para propietarios de vehículos en el Sistema de Gestión de Peajes.

## Archivos

- `dashboard.html` - Interfaz del panel de control
- `dashboard.js` - Lógica JavaScript usando vistaWeb.js
- `dashboard.css` - Estilos específicos del dashboard

## Funcionalidades Implementadas

### ✅ HU 2.1: Información Personal
- Muestra nombre completo del propietario
- Estado actual (Habilitado, Suspendido, Penalizado, Deshabilitado)
- Saldo disponible

## Funcionalidades Pendientes

### 🔄 HU 2.2: Bonificaciones Asignadas
Tabla con bonificaciones del propietario (nombre, puesto, fecha de asignación)

### 🔄 HU 2.3: Vehículos Registrados
Lista de vehículos con matrícula, modelo, color, cantidad de tránsitos y monto gastado

### 🔄 HU 2.4: Historial de Tránsitos
Tabla de tránsitos ordenada por fecha descendente con detalles completos

### 🔄 HU 2.5: Notificaciones
Lista de notificaciones del sistema ordenadas por fecha descendente

## Flujo de Funcionamiento

### 1. Login Exitoso
```
POST /acceso/login
  ↓
LoginController guarda usuarioId en HttpSession
  ↓
Retorna redirección a /propietario/dashboard/dashboard.html
```

### 2. Carga del Dashboard
```
dashboard.html se carga
  ↓
vistaWeb.js ejecuta urlIniciarVista = "/api/usuarios/info"
  ↓
GET /api/usuarios/info (incluye cookie de sesión automáticamente)
  ↓
SistemaAutorizacion valida sesión y permisos
  ↓
Retorna PropietarioInfoDTO
  ↓
dashboard.js ejecuta mostrar_propietario(dto)
  ↓
Se actualiza el DOM con los datos
```

### 3. Manejo de Errores
```
Si no hay sesión o usuario deshabilitado
  ↓
Backend retorna status 299
  ↓
vistaWeb.js detecta el error
  ↓
Redirige automáticamente a /login/login.html
```

## Integración con vistaWeb.js

El dashboard usa la librería `vistaWeb.js` del proyecto con sus convenciones:

### Variables de configuración:
```javascript
var urlIniciarVista = "/api/usuarios/info";  // URL que se llama al cargar
var parametrosInicioVista = "";               // Parámetros (vacío porque usa sesión)
```

### Funciones procesadoras:
```javascript
function mostrar_propietario(dto) {
    // Se ejecuta automáticamente cuando llega la respuesta
    // dto es el PropietarioInfoDTO del backend
}
```

## Endpoints Utilizados

### GET /api/usuarios/info
- **Autenticación**: HttpSession (cookie automática)
- **Permiso requerido**: PROPIETARIO_DASHBOARD
- **Respuesta**: `[{id: "propietario", parametro: PropietarioInfoDTO}]`
- **DTO**: `{nombreCompleto, estado, saldo}`

## Estilos

El dashboard usa:
- **CSS compartido**: `base.css`, `components.css`, `layouts.css`
- **CSS específico**: `dashboard.css` con grid responsive y badges de estado

### Estados y colores:
- 🟢 **Habilitado**: Verde (badge-success)
- 🟡 **Suspendido**: Amarillo (badge-warning)
- 🟡 **Penalizado**: Amarillo (badge-warning)
- 🔴 **Deshabilitado**: Rojo (badge-danger)

## Testing

Para probar el dashboard:

1. **Iniciar la aplicación**:
   ```bash
   mvnw spring-boot:run
   ```

2. **Abrir en navegador**:
   ```
   http://localhost:8080/login/login.html
   ```

3. **Credenciales de prueba** (según `DatosPrecargados.java`):
   
   - **Propietario Habilitado**:
     - Cédula: `12345672`
     - Contraseña: `Test1234!`
     - Saldo: $5,000
   
   - **Propietario Suspendido**:
     - Cédula: `45678905`
     - Contraseña: `Test1234!`
     - Saldo: $3,000
   
   - **Propietario Penalizado**:
     - Cédula: `1234561`
     - Contraseña: `Test1234!`
     - Saldo: $2,000
   
   - **Propietario Deshabilitado** (no puede ingresar):
     - Cédula: `23456783`
     - Contraseña: `Test1234!`

4. **Verificar**:
   - Información personal se carga automáticamente
   - Estado muestra con color correcto
   - Saldo formateado con símbolo $
   - Si no hay sesión, redirige a login

## Notas Técnicas

### Sesión HTTP
- El backend NO recibe parámetros de usuario
- La sesión se mantiene mediante **cookie HttpSession**
- El navegador envía la cookie automáticamente con cada request
- `SistemaAutorizacion` lee el `usuarioId` de la sesión

### Seguridad
- Solo se guarda el **ID del usuario** en sesión (no objeto completo)
- Cada request valida sesión, permisos y estado
- Usuarios deshabilitados son rechazados aunque tengan sesión
- Status 299 indica error de aplicación → frontend redirige a login

### Responsive
- Grid adaptativo para información personal
- Ajuste de tamaños de fuente en móviles
- Layout centrado con container-lg

## Referencias

- **Requerimientos**: `context/requerimientos.md` (HU 2.1 - 2.5)
- **Backend**: `UsuariosController.java`, `SistemaAutorizacion.java`
- **Tests**: `UsuariosControllerTest.java`, `SistemaAutorizacionTest.java`
- **Librerías**: `vistaWeb.js`, `utilesVista.js`

