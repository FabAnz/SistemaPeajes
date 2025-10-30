package ort.da.obligatorio339182;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.Categoria;
import ort.da.obligatorio339182.model.domain.Tarifa;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Trabajador;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Frecuente;
import ort.da.obligatorio339182.model.domain.estados.Suspendido;
import ort.da.obligatorio339182.model.domain.estados.Penalizado;
import ort.da.obligatorio339182.model.domain.estados.Deshabilitado;

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
 * - Nombre: Juan Pérez
 * - Cédula: 12345672
 * - Contraseña: Test1234!
 * - Estado: Habilitado - Puede hacer todo
 * 
 * Propietario 2 - Suspendido:
 * - Nombre: María García
 * - Cédula: 45678905
 * - Contraseña: Test1234!
 * - Estado: Suspendido - Solo puede ingresar al sistema
 * 
 * Propietario 3 - Penalizado:
 * - Nombre: Carlos López
 * - Cédula: 1234561 (Cédula de 7 dígitos)
 * - Contraseña: Test1234!
 * - Estado: Penalizado - No puede recibir bonificaciones
 * 
 * Propietario 4 - Deshabilitado:
 * - Nombre: Ana Rodríguez
 * - Cédula: 23456783
 * - Contraseña: Test1234!
 * - Estado: Deshabilitado - No puede ingresar al sistema
 * 
 * ===== ADMINISTRADORES PRECARGADOS =====
 * 
 * Administrador 1:
 * - Nombre: Admin Principal
 * - Cédula: 11111111
 * - Contraseña: Admin1234!
 * 
 * Administrador 2:
 * - Nombre: Admin Secundario
 * - Cédula: 22222222
 * - Contraseña: Admin1234!
 */
@Component
public class DatosPrecargados {

        private final Fachada fachada;

        public DatosPrecargados(Fachada fachada) {
                this.fachada = fachada;
        }

        // Referencias públicas a usuarios para usar en tests
        public static Propietario juanPerez; // ID 1 - Habilitado
        public static Propietario mariaGarcia; // ID 2 - Suspendido
        public static Propietario carlosLopez; // ID 3 - Penalizado
        public static Propietario anaRodriguez; // ID 4 - Deshabilitado
        public static Administrador adminPrincipal; // ID 5
        public static Administrador adminSecundario; // ID 6

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
         * Este método es llamado desde Obligatorio339182Application mediante
         * CommandLineRunner.
         */
        public void cargarDatos() throws AppException {
                cargarPropietarios(); // Cargar propietarios primero para que tengan IDs 1-4
                cargarAdministradores(); // Luego administradores
                cargarPuestos();
                cargarBonificaciones();
                cargarVehiculos();
                // TODO: Agregar aquí la carga de otros datos precargados si es necesario
        }

        /**
         * Carga los administradores iniciales del sistema.
         * Estos administradores pueden gestionar usuarios, puestos, tránsitos y
         * bonificaciones.
         */
        private void cargarAdministradores() throws AppException {
                // Administrador 1: Admin Principal
                // Tiene acceso completo a todas las funcionalidades de administración
                // Cédula: 11111111 (cédula válida con dígito verificador correcto)
                adminPrincipal = new Administrador(
                                "Admin Principal",
                                new Contrasenia("Admin1234!"),
                                new Cedula("11111111"));
                fachada.agregarUsuario(adminPrincipal);

                // Administrador 2: Admin Secundario
                // Otro usuario administrador para pruebas
                // Cédula: 22222222 (cédula válida con dígito verificador correcto)
                adminSecundario = new Administrador(
                                "Admin Secundario",
                                new Contrasenia("Admin1234!"),
                                new Cedula("22222222"));
                fachada.agregarUsuario(adminSecundario);
        }

        /**
         * Carga los propietarios iniciales del sistema.
         * Estos propietarios permiten probar todas las funcionalidades del sistema.
         */
        private void cargarPropietarios() throws AppException {
                // Propietario 1: Habilitado
                // Puede: Ingresar al sistema, realizar tránsitos, recibir bonificaciones
                // Cédula: 12345672 (cédula válida con dígito verificador correcto)
                juanPerez = new Propietario(
                                "Juan Pérez",
                                new Contrasenia("Test1234!"),
                                new Cedula("12345672"));
                juanPerez.setSaldo(5000);
                // Estado por defecto ya es habilitado
                fachada.agregarUsuario(juanPerez);

                // Propietario 2: Suspendido
                // Puede: Ingresar al sistema
                // No puede: Realizar tránsitos, recibir bonificaciones
                // Cédula: 45678905 (cédula válida con dígito verificador correcto)
                mariaGarcia = new Propietario(
                                "María García",
                                new Contrasenia("Test1234!"),
                                new Cedula("45678905"));
                mariaGarcia.setSaldo(3000);
                mariaGarcia.setEstado(new Suspendido());
                fachada.agregarUsuario(mariaGarcia);

                // Propietario 3: Penalizado
                // Puede: Ingresar al sistema, realizar tránsitos
                // No puede: Recibir bonificaciones
                // Cédula: 1234561 (cédula de 7 dígitos válida con dígito verificador correcto)
                carlosLopez = new Propietario(
                                "Carlos López",
                                new Contrasenia("Test1234!"),
                                new Cedula("1234561"));
                carlosLopez.setSaldo(2000);
                carlosLopez.setEstado(new Penalizado());
                fachada.agregarUsuario(carlosLopez);

                // Propietario 4: Deshabilitado
                // No puede: Ingresar al sistema, realizar tránsitos, recibir bonificaciones
                // Cédula: 23456783 (cédula válida con dígito verificador correcto)
                anaRodriguez = new Propietario(
                                "Ana Rodríguez",
                                new Contrasenia("Test1234!"),
                                new Cedula("23456783"));
                anaRodriguez.setSaldo(1000);
                anaRodriguez.setEstado(new Deshabilitado());
                fachada.agregarUsuario(anaRodriguez);
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
                // Usar la referencia estática a Juan Pérez (ya cargado)
                // Asignar bonificación "Trabajador" en Ruta 1
                BonificacionAsignada bonif1 = new BonificacionAsignada(
                                juanPerez,
                                this.puesto1,
                                new Trabajador());
                fachada.agregarBonificacionAsignada(bonif1);

                // Asignar bonificación "Frecuente" en Ruta 5
                BonificacionAsignada bonif2 = new BonificacionAsignada(
                                juanPerez,
                                this.puesto2,
                                new Frecuente());
                fachada.agregarBonificacionAsignada(bonif2);
        }

        /**
         * Carga los vehículos y tránsitos iniciales del sistema.
         * Se asignan vehículos a Juan Pérez con tránsitos para pruebas de HU 2.3
         */
        private void cargarVehiculos() throws AppException {
                // Usar la referencia estática a Juan Pérez (ya cargado)
                // Usar categorías compartidas (ya creadas en cargarPuestos)

                // Vehículo 1: Auto de Juan Pérez
                Vehiculo vehiculo1 = new Vehiculo(
                                "Toyota Corolla",
                                "Gris",
                                this.auto,
                                new Matricula("ABC1234"));
                fachada.agregarVehiculoConPropietario(vehiculo1, juanPerez);

                // Vehículo 2: Moto de Juan Pérez
                Vehiculo vehiculo2 = new Vehiculo(
                                "Honda Wave",
                                "Rojo",
                                this.moto,
                                new Matricula("XYZ5678"));
                fachada.agregarVehiculoConPropietario(vehiculo2, juanPerez);

                // Vehículo 3: Camioneta de Juan Pérez
                Vehiculo vehiculo3 = new Vehiculo(
                                "Ford Ranger",
                                "Blanco",
                                this.camioneta,
                                new Matricula("DEF9012"));
                fachada.agregarVehiculoConPropietario(vehiculo3, juanPerez);

                // ===== TRÁNSITOS CON FECHAS ESPECÍFICAS PARA PROBAR BONIFICACIONES =====

                // Definir fecha base para las pruebas
                LocalDateTime hoy = LocalDateTime.now();

                LocalDateTime lunesTodayAt10Am = hoy.withDayOfMonth(27).withHour(10).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto1, vehiculo1, lunesTodayAt10Am);

                LocalDateTime sabadoTodayAt10Am = hoy.withHour(10).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto1, vehiculo2, sabadoTodayAt10Am);

                LocalDateTime sabadoTodayAt11Am = hoy.withHour(11).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto3, vehiculo3, sabadoTodayAt11Am);

                LocalDateTime sabadoTodayAt12Pm = hoy.withHour(12).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto2, vehiculo1, sabadoTodayAt12Pm);

                LocalDateTime sabadoTodayAt14Pm = hoy.withHour(14).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto2, vehiculo1, sabadoTodayAt14Pm);

                LocalDateTime sabadoTodayAt16Pm = hoy.withHour(16).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto2, vehiculo1, sabadoTodayAt16Pm);

                LocalDateTime sabadoTodayAt17Pm = hoy.withHour(17).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto2, vehiculo3, sabadoTodayAt17Pm);

                LocalDateTime sabadoTodayAt18Pm = hoy.withHour(18).withMinute(0);
                fachada.agregarTransito(juanPerez, this.puesto2, vehiculo3, sabadoTodayAt18Pm);
        }


        // TODO: Agregar métodos para cargar otros datos:
        // private void cargarAdministradores() { ... }
        // private void cargarCategorias() { ... }
        // private void cargarTarifas() { ... }
}
