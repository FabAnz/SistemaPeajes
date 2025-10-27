# Dashboard del Administrador

## Descripción
Dashboard principal para usuarios administradores del Sistema de Gestión de Peajes.

## Estado Actual
✅ **Estructura básica implementada**  
⏳ **Funcionalidades pendientes (HU 3)**

## Archivos
- `dashboard.html` - Estructura de la página
- `dashboard.css` - Estilos específicos
- `dashboard.js` - Lógica del dashboard

## Funcionalidades Pendientes

### HU 3 - Gestión por Administrador
El dashboard está preparado para implementar:

1. **Gestión de usuarios**
   - Ver lista de propietarios
   - Cambiar estados de usuarios
   - Gestionar saldos

2. **Gestión de puestos**
   - Crear nuevos puestos
   - Configurar tarifas
   - Activar/desactivar puestos

3. **Emulación de tránsitos**
   - Registrar tránsitos de prueba
   - Ver histórico de tránsitos

4. **Asignación de bonificaciones**
   - Asignar bonificaciones a propietarios
   - Gestionar bonificaciones por puesto

## Validación de Sesión
⚠️ **NOTA IMPORTANTE**: La validación de sesión está temporalmente deshabilitada en el backend para permitir el desarrollo de las funcionalidades de la HU 3.

**Archivo afectado**: `AdminController.java`
```java
// TODO: Descomentar validación de sesión cuando se implemente
```

**Acción requerida**: Descomentar la validación de sesión en `AdminController.cargarDashboardAdmin()` antes de producción.

## Acceso
- **URL**: `/administrador/dashboard/dashboard.html`
- **Ruta de API**: `/administrador/dashboard` (GET)
- **Permiso requerido**: `ADMIN_DASHBOARD` (cuando se habilite validación)

## Dependencias
- `vistaWeb.js` - Comunicación con backend
- `utilesVista.js` - Utilidades de interfaz
- `cierreSesion.js` - Funcionalidad de logout
- Estilos compartidos: `base.css`, `components.css`, `layouts.css`

## Próximos Pasos
1. Implementar las funcionalidades de la HU 3
2. Habilitar validación de sesión
3. Agregar tests de integración
4. Documentar endpoints específicos

---

**Última actualización**: 2025-10-26  
**Estado**: En desarrollo

