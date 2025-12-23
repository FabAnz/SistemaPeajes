# Sistema de Gestión de Peajes

Sistema web para la gestión de tránsitos de vehículos por puestos de peaje, desarrollado con Spring Boot. Permite a propietarios de vehículos consultar su información y a administradores gestionar el sistema.

## 📋 Descripción

Este sistema gestiona el registro de tránsitos de vehículos por puestos de peaje, incluyendo:
- **Gestión de usuarios**: Propietarios y administradores con diferentes permisos
- **Registro de tránsitos**: Emulación y registro de pasadas por peajes
- **Bonificaciones**: Asignación y aplicación de descuentos por puesto
- **Estados de usuarios**: Control de habilitación, suspensión y penalización
- **Notificaciones**: Sistema de alertas para propietarios

## ✨ Características Principales

### Para Propietarios
- ✅ Inicio de sesión seguro
- ✅ Visualización de información personal (nombre, estado, saldo)
- ✅ Consulta de bonificaciones asignadas
- ✅ Listado de vehículos registrados con estadísticas
- ✅ Historial de tránsitos realizados
- ✅ Gestión de notificaciones del sistema

### Para Administradores
- ✅ Inicio de sesión con permisos administrativos
- ✅ Emulación de tránsitos de vehículos
- ✅ Asignación de bonificaciones a propietarios
- ✅ Cambio de estados de propietarios
- ✅ Gestión completa del sistema

## 🛠️ Stack Tecnológico

- **Framework**: Spring Boot 3.5.6
- **Lenguaje**: Java 21
- **Build Tool**: Apache Maven (Maven Wrapper incluido)
- **Dependencias principales**:
  - Spring Boot Web Starter
  - Spring Boot DevTools
  - Lombok
  - Spring Boot Test (JUnit 5)
- **Frontend**: HTML5, CSS3, JavaScript (vanilla)
- **Arquitectura**: MVC + Patrón Facade

## 📦 Requisitos Previos

- **Java**: JDK 21 o superior
- **Maven**: 3.6+ (incluido Maven Wrapper)
- **IDE recomendado**: IntelliJ IDEA o Eclipse (con plugin Lombok)
- **Navegador**: Cualquier navegador moderno (Chrome, Firefox, Edge)

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd DA-2025Agosto-N4C-339182
```

### 2. Configurar Lombok en el IDE

#### IntelliJ IDEA
1. Settings → Plugins → Buscar "Lombok" → Instalar
2. Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing

#### Eclipse
1. Descargar `lombok.jar` desde [projectlombok.org](https://projectlombok.org/download)
2. Ejecutar `java -jar lombok.jar` y seguir el asistente

### 3. Compilar el proyecto

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

## ▶️ Ejecución

### Ejecutar la aplicación

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

### Ejecutar tests

```bash
# Todos los tests
mvnw test

# Test específico
mvnw test -Dtest=NombreTest
```

**Nota**: En Windows, si hay problemas con los tests, ejecutarlos desde el IDE.

## 📁 Estructura del Proyecto

```
proyecto/
├── src/main/java/ort/da/obligatorio339182/
│   ├── Obligatorio339182Application.java   # Punto de entrada
│   ├── DatosPrecargados.java               # Datos iniciales del sistema
│   ├── controllers/                         # @RestController (REST APIs)
│   │   ├── LoginController.java
│   │   ├── PropietarioController.java
│   │   └── AdminController.java
│   ├── services/                            # @Service (Lógica de negocio)
│   │   ├── Fachada.java                     # Patrón Facade
│   │   └── [Subsistemas package-private]
│   ├── model/
│   │   ├── domain/                          # Entidades del dominio
│   │   └── valueObjects/                    # Value Objects inmutables
│   ├── dtos/                                # Data Transfer Objects
│   ├── exceptions/                          # Manejo de excepciones
│   ├── observer/                            # Patrón Observer
│   └── utils/                               # Utilidades (RespuestaDTO)
├── src/main/resources/
│   ├── static/                              # Frontend (HTML/CSS/JS)
│   │   ├── login/                           # Página de login
│   │   ├── propietario/dashboard/           # Dashboard de propietario
│   │   ├── administrador/                   # Funcionalidades admin
│   │   ├── css/                             # Estilos compartidos
│   │   ├── vistaWeb.js                      # Librería HTTP
│   │   └── utilesVista.js                   # Utilidades frontend
│   └── application.properties               # Configuración Spring
├── src/test/java/                           # Tests (JUnit 5)
├── context/                                 # 📚 Documentación del proyecto
│   ├── agents/                              # 🤖 Sistema de agentes especializados
│   ├── requerimientos.md                    # Especificación completa
│   ├── autorizacion-sesiones.md             # Sistema de seguridad
│   └── datos-precargados.md                 # Datos iniciales
├── pom.xml                                  # Configuración Maven
└── mvnw / mvnw.cmd                          # Maven Wrapper
```

## 🏗️ Arquitectura

### Patrón Facade
- **Fachada** (`public`): Punto de entrada único a la lógica de negocio
- **Subsistemas** (`package-private`): Solo accesibles vía Fachada
- **Comunicación entre sistemas**: A través de Fachada con `@Lazy` para evitar dependencias circulares

### Patrón MVC
- **Controllers**: Reciben peticiones HTTP → Llaman a Fachada → Retornan `List<RespuestaDTO>`
- **Model**: Lógica de negocio (Services, Domain, Value Objects)
- **View**: Archivos estáticos en `resources/static/`

### Patrón Observer
- Implementado para notificaciones del sistema
- Los propietarios son notificados automáticamente de eventos importantes

## 🔒 Seguridad

- **Sesiones HTTP**: Gestión de sesiones mediante `HttpSession`
- **Autorización**: Validación de permisos por endpoint
- **Múltiples sesiones**: Soporte para sesiones simultáneas por usuario
- **Logout automático**: Invalidación de sesión ante errores de autorización

## 📚 Documentación Adicional

La documentación completa del proyecto se encuentra en la carpeta `context/`:

- **`context/agents/ORCHESTRATOR.md`**: Sistema de agentes especializados
- **`context/requerimientos.md`**: Historias de usuario y especificaciones
- **`context/autorizacion-sesiones.md`**: Detalles del sistema de seguridad
- **`context/datos-precargados.md`**: Información sobre datos iniciales
- **`context/ejecutar-tests.md`**: Guía de testing
- **`AGENTS.md`**: Documentación principal del proyecto

## 🧪 Testing

El proyecto utiliza JUnit 5 con Spring Boot Test:

```java
@SpringBootTest
@AutoConfigureMockMvc
class MiTest {
    // Tests aquí
}
```

**Patrón AAA**: Arrange → Act → Assert

## 🐛 Troubleshooting

### Problemas comunes

| Problema | Solución |
|----------|----------|
| No compila | `mvnw clean install` |
| Tests fallan en Windows | Ejecutar desde IDE |
| Puerto 8080 ocupado | Cambiar en `application.properties` |
| Lombok no funciona | Instalar plugin + Enable annotation processing |
| Sesión se pierde | Revisar `validarSesionYPermiso()` |

## 👥 Usuarios Predefinidos

El sistema incluye datos precargados. Consultar `context/datos-precargados.md` para credenciales de prueba.

## 📝 Licencia

Este proyecto es parte de un trabajo académico de la Universidad ORT Uruguay.

## 🔄 Versión

**Versión actual**: 0.0.1-SNAPSHOT

---

**Desarrollado para**: Universidad ORT Uruguay  
**Curso**: Diseño de Aplicaciones  
**Año**: 2025

