package ort.da.obligatorio339182;

import org.springframework.stereotype.Component;

import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.exceptions.AppException;

/**
 * Clase que contiene todos los datos precargados del sistema.
 * 
 * Esta clase se ejecuta automáticamente al iniciar la aplicación y carga
 * datos iniciales necesarios para el funcionamiento del sistema.
 * 
 * IMPORTANTE: Estos datos se cargan tanto en desarrollo como en producción
 * según los requerimientos del proyecto.
 * 
 * ===== USUARIOS PRECARGADOS =====
 * 
 * Propietario 1 - Habilitado:
 *   - Nombre: Juan Pérez
 *   - Cédula: 12345672
 *   - Contraseña: Test1234!
 *   - Estado: Habilitado - Puede hacer todo
 * 
 * Propietario 2 - Suspendido:
 *   - Nombre: María García
 *   - Cédula: 45678905
 *   - Contraseña: Test1234!
 *   - Estado: Suspendido - Solo puede ingresar al sistema
 * 
 * Propietario 3 - Penalizado:
 *   - Nombre: Carlos López
 *   - Cédula: 1234561 (Cédula de 7 dígitos)
 *   - Contraseña: Test1234!
 *   - Estado: Penalizado - No puede recibir bonificaciones
 * 
 * Propietario 4 - Deshabilitado:
 *   - Nombre: Ana Rodríguez
 *   - Cédula: 23456783
 *   - Contraseña: Test1234!
 *   - Estado: Deshabilitado - No puede ingresar al sistema
 */
@Component
public class DatosPrecargados {

    private final Fachada fachada;

    public DatosPrecargados(Fachada fachada) {
        this.fachada = fachada;
    }

    /**
     * Carga todos los datos precargados en el sistema.
     * Este método es llamado automáticamente al iniciar la aplicación.
     */
    public void cargarDatos() throws AppException {
        cargarPropietarios();
        // TODO: Agregar aquí la carga de otros datos precargados:
        // - Administradores
        // - Puestos de peaje
        // - Categorías de vehículos
        // - Tarifas
        // - etc.
    }

    /**
     * Carga los propietarios iniciales del sistema.
     * Estos propietarios permiten probar todas las funcionalidades del sistema.
     */
    private void cargarPropietarios() throws AppException {
        // Propietario 1: Habilitado
        // Puede: Ingresar al sistema, realizar tránsitos, recibir bonificaciones
        // Cédula: 12345672 (cédula válida con dígito verificador correcto)
        Propietario propHabilitado = new Propietario(
            "Juan Pérez",
            new Contrasenia("Test1234!"),
            new Cedula("12345672")
        );
        propHabilitado.setSaldo(5000);
        // Estado por defecto ya es habilitado
        fachada.agregarUsuario(propHabilitado);

        // Propietario 2: Suspendido
        // Puede: Ingresar al sistema
        // No puede: Realizar tránsitos, recibir bonificaciones
        // Cédula: 45678905 (cédula válida con dígito verificador correcto)
        Propietario propSuspendido = new Propietario(
            "María García",
            new Contrasenia("Test1234!"),
            new Cedula("45678905")
        );
        propSuspendido.setSaldo(3000);
        propSuspendido.setEstado(Estado.suspendido());
        fachada.agregarUsuario(propSuspendido);

        // Propietario 3: Penalizado
        // Puede: Ingresar al sistema, realizar tránsitos
        // No puede: Recibir bonificaciones
        // Cédula: 1234561 (cédula de 7 dígitos válida con dígito verificador correcto)
        Propietario propPenalizado = new Propietario(
            "Carlos López",
            new Contrasenia("Test1234!"),
            new Cedula("1234561")
        );
        propPenalizado.setSaldo(2000);
        propPenalizado.setEstado(Estado.penalizado());
        fachada.agregarUsuario(propPenalizado);

        // Propietario 4: Deshabilitado
        // No puede: Ingresar al sistema, realizar tránsitos, recibir bonificaciones
        // Cédula: 23456783 (cédula válida con dígito verificador correcto)
        Propietario propDeshabilitado = new Propietario(
            "Ana Rodríguez",
            new Contrasenia("Test1234!"),
            new Cedula("23456783")
        );
        propDeshabilitado.setSaldo(1000);
        propDeshabilitado.setEstado(Estado.deshabilitado());
        fachada.agregarUsuario(propDeshabilitado);
    }

    // TODO: Agregar métodos para cargar otros datos:
    // private void cargarAdministradores() { ... }
    // private void cargarPuestos() { ... }
    // private void cargarCategorias() { ... }
    // private void cargarTarifas() { ... }
}
