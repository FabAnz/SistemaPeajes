# Arquitectura CSS del Sistema de Gestión de Peajes

Este directorio contiene los archivos CSS compartidos del proyecto, organizados según una arquitectura modular y escalable.

## 📁 Estructura

```
css/
├── base.css         → Variables, reset, tipografía, animaciones globales
├── components.css   → Componentes reutilizables (botones, cards, formularios, etc.)
├── layouts.css      → Estructuras de página y sistemas de grid
└── README.md        → Este archivo
```

## 📄 Descripción de archivos

### `base.css` - Fundamentos

**Contiene:**
- ✅ Variables CSS (colores, espaciados, sombras, transiciones)
- ✅ Reset CSS básico
- ✅ Estilos del body base
- ✅ Tipografía global (h1-h6, p, a)
- ✅ Animaciones globales (fadeIn, fadeInUp, spin, pulse, shake)
- ✅ Utilidades (text-center, mb-md, etc.)
- ✅ Estilos de accesibilidad

**Debe ser importado primero en todos los HTML.**

### `components.css` - Componentes Reutilizables

**Contiene:**
- ✅ Cards (`.card`, `.card-header`, `.card-footer`)
- ✅ Botones (`.btn`, `.btn-primary`, `.btn-secondary`, `.btn-danger`)
- ✅ Formularios (`.form-group`, `.form-control`, `.input-container`)
- ✅ Mensajes de error (`.error-message`)
- ✅ Alertas (`.alert`, `.alert-success`, `.alert-error`)
- ✅ Loading spinner (`.spinner`, `.loading-overlay`)
- ✅ Badges (`.badge`)
- ✅ Tablas (`.table`)

**Usa variables de `base.css`.**

### `layouts.css` - Estructuras de Página

**Contiene:**
- ✅ Layout centrado (`.layout-centered`)
- ✅ Containers (`.container`, `.container-sm`, `.container-md`)
- ✅ Sistema de grid (`.row`, `.col`, `.col-2`, `.col-3`)
- ✅ Utilidades flex (`.d-flex`, `.justify-center`, `.align-center`)
- ✅ Responsive breakpoints

**Usa variables de `base.css`.**

## 🎯 Cómo usar

### En todos los HTML, incluir los archivos compartidos:

```html
<head>
    <!-- CSS Compartido -->
    <link rel="stylesheet" href="../css/base.css">
    <link rel="stylesheet" href="../css/components.css">
    <link rel="stylesheet" href="../css/layouts.css">
    
    <!-- CSS Específico de la página (opcional) -->
    <link rel="stylesheet" href="mi-pagina.css">
</head>
```

**Nota:** Ajustar la ruta según la ubicación del HTML (`../css/` desde subcarpetas, `css/` desde la raíz).

### Ejemplo de uso de componentes:

```html
<!-- Botón primario -->
<button class="btn btn-primary">Guardar</button>

<!-- Card con header y footer -->
<div class="card">
    <div class="card-header">
        <h2 class="card-title">Título</h2>
    </div>
    <p>Contenido...</p>
    <div class="card-footer">
        <button class="btn btn-secondary">Cancelar</button>
    </div>
</div>

<!-- Formulario -->
<div class="form-group">
    <label for="nombre">Nombre</label>
    <input type="text" id="nombre" class="form-control" placeholder="Ingresa tu nombre">
    <span class="error-message" id="errorNombre"></span>
</div>

<!-- Layout centrado -->
<body class="layout-centered">
    <div class="container-sm">
        <div class="card text-center">
            <h1>Contenido</h1>
        </div>
    </div>
</body>
```

## 🎨 Variables CSS disponibles

Puedes usar estas variables en cualquier archivo CSS específico:

### Colores
- `var(--primary-color)` - Azul primario (#4d94ff)
- `var(--primary-dark)` - Azul oscuro (#337ab7)
- `var(--success-color)` - Verde (#28a745)
- `var(--error-color)` - Rojo (#dc3545)
- `var(--warning-color)` - Amarillo (#ffc107)
- `var(--text-dark)` - Texto oscuro (#212529)
- `var(--text-muted)` - Texto gris (#6c757d)

### Fondos
- `var(--bg-gradient)` - Gradiente principal
- `var(--bg-card)` - Fondo de cards (#ffffff)
- `var(--bg-input)` - Fondo de inputs (#f8f9fa)

### Espaciados
- `var(--spacing-xs)` - 4px
- `var(--spacing-sm)` - 8px
- `var(--spacing-md)` - 16px
- `var(--spacing-lg)` - 24px
- `var(--spacing-xl)` - 32px

### Bordes y sombras
- `var(--border-radius)` - 12px
- `var(--box-shadow)` - Sombra estándar
- `var(--box-shadow-md)` - Sombra media
- `var(--box-shadow-lg)` - Sombra grande

### Transiciones
- `var(--transition)` - 0.3s ease
- `var(--transition-smooth)` - cubic-bezier suave

## 📝 Convenciones

### CSS Específico de página

Cada página puede tener su propio archivo CSS (ej: `login.css`, `dashboard.css`) que debe:

1. **Importarse después** de los archivos compartidos
2. **Solo contener estilos únicos** de esa página
3. **Usar las variables** definidas en `base.css`
4. **No duplicar** estilos que ya existen en components o layouts

### Ejemplo: `login/login.css`

```css
/* Solo overrides y estilos específicos del login */
body {
    background: var(--bg-gradient);
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-container {
    max-width: 450px;
    animation: fadeInUp 0.6s;
}

.logo-container {
    /* Estilos únicos del logo del login */
}
```

## 🚀 Ventajas de esta arquitectura

✅ **Consistencia**: Todos los componentes tienen el mismo estilo  
✅ **Mantenibilidad**: Cambios globales en un solo lugar  
✅ **Performance**: CSS compartido se cachea una sola vez  
✅ **Escalabilidad**: Fácil agregar nuevas páginas  
✅ **DRY**: No se repite código CSS  
✅ **Claridad**: Cada archivo tiene una responsabilidad única  

## 🔧 Mantenimiento

### Al agregar nuevos componentes:
1. Si es reutilizable → agregar a `components.css`
2. Si es layout → agregar a `layouts.css`
3. Si es específico de una página → crear/usar archivo específico

### Al modificar estilos:
1. ¿Afecta a múltiples páginas? → Modificar en archivos compartidos
2. ¿Solo afecta a una página? → Modificar en archivo específico

### Nuevas variables:
- Agregar a `:root` en `base.css`
- Usar nombres descriptivos con prefijos (ej: `--btn-`, `--card-`)
- Documentar en este README

## 📚 Referencias

- [CSS Variables (MDN)](https://developer.mozilla.org/es/docs/Web/CSS/Using_CSS_custom_properties)
- [BEM Methodology](http://getbem.com/)
- [CSS Architecture](https://philipwalton.com/articles/css-architecture/)

---

**Última actualización:** 20 de octubre de 2025  
**Mantenido por:** Equipo de desarrollo Sistema de Peajes

