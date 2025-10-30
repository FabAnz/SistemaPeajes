package ort.da.obligatorio339182.services;

import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.Puesto;
import org.springframework.stereotype.Service;

import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.valueObjects.Matricula;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Fachada {

	private final SistemaUsuarios su;
	private final SistemaPuestos sp;
	private final SistemaVehiculos sv;
	private final SistemaTransitos st;

	public Fachada(SistemaUsuarios su, SistemaPuestos sp, SistemaVehiculos sv, SistemaTransitos st) {
		this.su = su;
		this.sp = sp;
		this.sv = sv;
		this.st = st;
	}


	public Usuario login(String cedula, String contrasenia) throws AppException {
		return su.login(cedula, contrasenia);
	}

	public void agregarUsuario(Usuario usuario) throws AppException{
		su.agregarUsuario(usuario);
	}

	public Usuario validarPermiso(Integer usuarioId, Permiso permisoRequerido) throws UnauthorizedException {
		return su.validarPermiso(usuarioId, permisoRequerido);
	}

	public List<Vehiculo> getVehiculosDelUsuario(Cedula cedula) {
		return null;
	}

	public Usuario getUsuarioPorCedula(String cedula) {
		return su.getUsuarioPorCedula(cedula);
	}

	/**
	 * Agrega un puesto al sistema
	 * @param puesto El puesto a agregar
	 */
	public void agregarPuesto(Puesto puesto) {
		sp.agregarPuesto(puesto);
	}

	/**
	 * Agrega una bonificación asignada al sistema
	 * @param bonificacionAsignada La bonificación asignada a agregar
	 */
	public void agregarBonificacionAsignada(BonificacionAsignada bonificacionAsignada) throws AppException {
		sp.agregarBonificacionAsignada(bonificacionAsignada);
	}

	// Obtiene las bonificaciones asignadas a un propietario
	public List<BonificacionAsignada> getBonificacionesPorPropietario(Propietario propietario) {
		return sp.getBonificacionesPorPropietario(propietario);
	}

	// Obtiene un usuario por su ID
	public Usuario getUsuarioPorId(int id) {
		return su.getUsuarioPorId(id);
	}

	// Calcula la cantidad de tránsitos realizados por un propietario en un vehículo específico
	public int cantidadTransitosPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.cantidadTransitosPorPropietarioYVehiculo(propietario, vehiculo);
	}

	// Calcula el monto total gastado por un propietario en un vehículo específico
	public int montoTotalPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.montoTotalPorPropietarioYVehiculo(propietario, vehiculo);
	}

	// Obtiene la bonificación que tenía un propietario en un puesto en una fecha específica
	public BonificacionAsignada getBonificacionEnPuesto(Propietario propietario, Puesto puesto) {
		return sp.getBonificacionAsignadaEnPuesto(propietario, puesto);
	}

	public void agregarVehiculo(Vehiculo vehiculo) throws AppException {
		sv.agregarVehiculo(vehiculo);
	}

	public void agregarVehiculoConPropietario(Vehiculo vehiculo, Propietario propietario) throws AppException {
		sv.agregarVehiculoConPropietario(vehiculo, propietario);
	}

	// Agrega un tránsito con la fecha actual
	public void agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo) throws AppException {
		st.agregarTransito(propietario, puesto, vehiculo);
	}

	// Agrega un tránsito con una fecha específica
	// Útil para cargar datos de prueba
	public void agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, LocalDateTime fechaHora) throws AppException {
		st.agregarTransito(propietario, puesto, vehiculo, fechaHora);
	}

	public Puesto getPuestoPorId(int id) {
		return sp.getPuestoPorId(id);
	}

	public Vehiculo getVehiculoPorMatricula(Matricula matricula) {
		return sv.getVehiculoPorMatricula(matricula);
	}

	public List<Puesto> getTodosPuestos() {
		return sp.getTodosPuestos();
	}

}
