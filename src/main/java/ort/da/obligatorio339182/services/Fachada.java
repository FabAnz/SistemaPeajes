package ort.da.obligatorio339182.services;

import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.Puesto;
import org.springframework.stereotype.Service;

import ort.da.obligatorio339182.dtos.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.Transito;

import java.util.List;
import java.time.LocalDateTime;

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

	/**
	 * Obtiene las bonificaciones asignadas a un propietario
	 * @param propietario El propietario
	 * @return Lista de bonificaciones asignadas al propietario
	 */
	public List<BonificacionAsignada> getBonificacionesPorPropietario(Propietario propietario) {
		return sp.getBonificacionesPorPropietario(propietario);
	}

	/**
	 * Obtiene un usuario por su ID
	 * @param id El ID del usuario
	 * @return El usuario si existe, null en caso contrario
	 */
	public Usuario getUsuarioPorId(int id) {
		return su.getUsuarioPorId(id);
	}

	/**
	 * Calcula la cantidad de tránsitos realizados por un propietario en un vehículo específico
	 * @param propietario El propietario
	 * @param vehiculo El vehículo
	 * @return Cantidad de tránsitos
	 */
	public int cantidadTransitosPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.cantidadTransitosPorPropietarioYVehiculo(propietario, vehiculo);
	}

	/**
	 * Calcula el monto total gastado por un propietario en un vehículo específico
	 * @param propietario El propietario
	 * @param vehiculo El vehículo
	 * @return Monto total gastado
	 */
	public int montoTotalPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.montoTotalPorPropietarioYVehiculo(propietario, vehiculo);
	}

	/**
	 * Agrega un tránsito al sistema
	 * @param transito El tránsito a agregar
	 */
	public void agregarTransito(Transito transito) throws AppException {
		st.agregarTransito(transito);
	}

	/**
	 * Obtiene todos los tránsitos de un propietario ordenados por fecha descendente
	 * @param propietario El propietario
	 * @return Lista de tránsitos ordenada por fechaHora descendente
	 */
	public List<Transito> getTransitosPorPropietario(Propietario propietario) {
		return st.getTransitosPorPropietario(propietario);
	}

	/**
	 * Obtiene la bonificación que tenía un propietario en un puesto en una fecha específica
	 * @param propietario El propietario
	 * @param puesto El puesto
	 * @param fechaTransito La fecha/hora del tránsito
	 * @return La bonificación asignada o null si no había
	 */
	public BonificacionAsignada getBonificacionEnPuesto(Propietario propietario, Puesto puesto, LocalDateTime fechaTransito) {
		return sp.getBonificacionAsignadaEnPuesto(propietario, puesto, fechaTransito);
	}

}
