package ort.da.obligatorio339182.services;

import ort.da.obligatorio339182.model.valueObjects.Cedula;
import java.util.List;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;

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
	public List<BonificacionAsignada> getBonificacionesPorPropietario(int propietarioId) {
		return sp.getBonificacionesPorPropietario(propietarioId);
	}

	/**
	 * Obtiene un usuario por su ID
	 * @param id El ID del usuario
	 * @return El usuario si existe, null en caso contrario
	 */
	public Usuario getUsuarioPorId(int id) {
		return su.getUsuarioPorId(id);
	}

	public int cantidadTransitosPorCedulaYMatricula(Cedula cedula, Matricula matricula) {
		return 0;
	}

	public int gastoEnTransitos(Cedula cedula, Matricula matricula) {
		return 0;
	}

}
