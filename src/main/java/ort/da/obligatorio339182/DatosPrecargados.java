package ort.da.obligatorio339182;

import org.springframework.stereotype.Component;

import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Trabajador;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Frecuente;

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

    // Referencias a puestos para usar en bonificaciones
    private Puesto puesto1;
    private Puesto puesto2;
    private Puesto puesto3;
    
    // Referencias a categorías para usar en puestos y vehículos
    private Categoria auto;
    private Categoria moto;
    private Categoria camioneta;

    /**
     * Carga todos los datos precargados en el sistema.
     * Este método es llamado desde Obligatorio339182Application mediante CommandLineRunner.
     */
    public void cargarDatos() throws AppException {
        cargarPropietarios();
        cargarPuestos();
        cargarBonificaciones();
        cargarVehiculos();
        // TODO: Agregar aquí la carga de otros datos precargados:
        // - Administradores
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

    /**
     * Carga los puestos de peaje iniciales del sistema.
     */
    private void cargarPuestos() {
        // Crear categorías compartidas para puestos y vehículos
        this.auto = new Categoria("Auto");
        this.moto = new Categoria("Moto");
        this.camioneta = new Categoria("Camioneta");
        
        // Puesto 1: Ruta 1 con tarifas
        this.puesto1 = new Puesto("Peaje Ruta 1", "Km 45, Ruta 1");
        this.puesto1.getTarifas().add(new Tarifa(150, this.auto));
        this.puesto1.getTarifas().add(new Tarifa(80, this.moto));
        this.puesto1.getTarifas().add(new Tarifa(300, this.camioneta));
        fachada.agregarPuesto(this.puesto1);
        
        // Puesto 2: Ruta 5 con tarifas
        this.puesto2 = new Puesto("Peaje Ruta 5", "Km 120, Ruta 5");
        this.puesto2.getTarifas().add(new Tarifa(200, this.auto));
        this.puesto2.getTarifas().add(new Tarifa(100, this.moto));
        this.puesto2.getTarifas().add(new Tarifa(400, this.camioneta));
        fachada.agregarPuesto(this.puesto2);
        
        // Puesto 3: Ruta 8 con tarifas
        this.puesto3 = new Puesto("Peaje Ruta 8", "Km 80, Ruta 8");
        this.puesto3.getTarifas().add(new Tarifa(250, this.auto));
        this.puesto3.getTarifas().add(new Tarifa(120, this.moto));
        this.puesto3.getTarifas().add(new Tarifa(350, this.camioneta));
        fachada.agregarPuesto(this.puesto3);
    }
    
    /**
     * Carga las bonificaciones asignadas a los propietarios.
     * Se asignan bonificaciones al propietario "Juan Pérez" para pruebas.
     */
    private void cargarBonificaciones() throws AppException {
        // Obtener el propietario Juan Pérez (ya cargado)
        Propietario juanPerez = (Propietario) fachada.getUsuarioPorCedula("12345672");
        
        // Usar referencias directas a los puestos (no buscar por ID)
        // Asignar bonificación "Trabajador" en Ruta 1
        BonificacionAsignada bonif1 = new BonificacionAsignada(
            juanPerez,
            this.puesto1,
            new Trabajador()
        );
        fachada.agregarBonificacionAsignada(bonif1);
        
        // Asignar bonificación "Frecuente" en Ruta 5
        BonificacionAsignada bonif2 = new BonificacionAsignada(
            juanPerez,
            this.puesto2,
            new Frecuente()
        );
        fachada.agregarBonificacionAsignada(bonif2);
    }

    /**
     * Carga los vehículos y tránsitos iniciales del sistema.
     * Se asignan vehículos a Juan Pérez con tránsitos para pruebas de HU 2.3
     */
    private void cargarVehiculos() throws AppException {
        // Obtener el propietario Juan Pérez (ya cargado)
        Propietario juanPerez = (Propietario) fachada.getUsuarioPorCedula("12345672");
        
        // Usar categorías compartidas (ya creadas en cargarPuestos)
        
        // Vehículo 1: Auto de Juan Pérez
        Vehiculo vehiculo1 = new Vehiculo(
            "Toyota Corolla",
            "Gris",
            this.auto,
            new Matricula("ABC1234")
        );
        juanPerez.agregarVehiculo(vehiculo1);
        
        // Vehículo 2: Moto de Juan Pérez
        Vehiculo vehiculo2 = new Vehiculo(
            "Honda Wave",
            "Rojo",
            this.moto,
            new Matricula("XYZ5678")
        );
        juanPerez.agregarVehiculo(vehiculo2);
        
        // Vehículo 3: Camioneta de Juan Pérez
        Vehiculo vehiculo3 = new Vehiculo(
            "Ford Ranger",
            "Blanco",
            this.camioneta,
            new Matricula("DEF9012")
        );
        juanPerez.agregarVehiculo(vehiculo3);
        
        // Crear tránsitos para el vehículo 1 (ABC1234) - 2 tránsitos
        // Transito 1: Auto en Puesto1 ($150) - Con bonificación Trabajador ($80 si es día laboral)
        // La bonificación se asigna automáticamente al agregar el tránsito
        fachada.agregarTransito(juanPerez, this.puesto1, vehiculo1);
        
        
        // Transito 2: Auto en Puesto2 ($200) - Con bonificación Frecuente ($50 si es segunda pasada)
        fachada.agregarTransito(juanPerez, this.puesto2, vehiculo1);
        
        // Crear tránsitos para el vehículo 2 (XYZ5678) - 1 tránsito
        // Transito 3: Moto en Puesto1 ($80) - Con bonificación Trabajador ($80)
        // Nota: El cobro mínimo debe ser mayor a 0, así que si aplica bonificación completa sería $0
        // Asumimos que no se aplica en este caso o hay un mínimo de $1
        fachada.agregarTransito(juanPerez, this.puesto1, vehiculo2);
        
        // Crear tránsitos para el vehículo 3 (DEF9012) - 3 tránsitos
        // Transito 4: Camioneta en Puesto3 ($350) - SIN bonificación (Juan no tiene bonif en Puesto3)
        fachada.agregarTransito(juanPerez, this.puesto3, vehiculo3);
        
        // Transito 5: Camioneta en Puesto1 ($300) - Con bonificación Trabajador ($80)
        fachada.agregarTransito(juanPerez, this.puesto1, vehiculo3);
        
        // Transito 6: Camioneta en Puesto2 ($400) - Con bonificación Frecuente ($50)
        fachada.agregarTransito(juanPerez, this.puesto2, vehiculo3);
    }

    // TODO: Agregar métodos para cargar otros datos:
    // private void cargarAdministradores() { ... }
    // private void cargarCategorias() { ... }
    // private void cargarTarifas() { ... }
}
