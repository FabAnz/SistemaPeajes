package ort.da.obligatorio339182.services;

import ort.da.obligatorio339182.model.valueObjects.Cedula;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.Puesto;
import org.springframework.stereotype.Service;

import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.observer.ObservableAbstracto;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.model.domain.Categoria;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Fachada extends ObservableAbstracto {

	private final SistemaUsuarios su;
	private final SistemaPuestos sp;
	private final SistemaVehiculos sv;
	private final SistemaTransitos st;
	private final SistemaBonificaciones sb;
	private final SistemaEstados se;

	public enum Evento {
		BONIFICACION_ASIGNADA
	}

	public Fachada(
			SistemaUsuarios su,
			SistemaPuestos sp,
			SistemaVehiculos sv,
			SistemaTransitos st,
			SistemaBonificaciones sb,
			SistemaEstados se) {
		this.su = su;
		this.sp = sp;
		this.sv = sv;
		this.st = st;
		this.sb = sb;
		this.se = se;
	}

	public Usuario login(String cedula, String contrasenia) throws AppException {
		return su.login(cedula, contrasenia);
	}

	public void agregarUsuario(Usuario usuario) throws AppException {
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

	public Propietario getPropietarioPorCedula(String cedula) throws AppException {
		return su.getPropietarioPorCedula(cedula);
	}

	public void agregarPuesto(Puesto puesto) {
		sp.agregarPuesto(puesto);
	}

	public void agregarBonificacionAsignada(BonificacionAsignada bonificacionAsignada) throws AppException {
		sp.agregarBonificacionAsignada(bonificacionAsignada);
	}

	public List<BonificacionAsignada> getBonificacionesPorPropietario(Propietario propietario) {
		return sp.getBonificacionesPorPropietario(propietario);
	}

	public Usuario getUsuarioPorId(int id) {
		return su.getUsuarioPorId(id);
	}

	public int cantidadTransitosPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.cantidadTransitosPorPropietarioYVehiculo(propietario, vehiculo);
	}

	public int montoTotalPorPropietarioYVehiculo(Propietario propietario, Vehiculo vehiculo) {
		return st.montoTotalPorPropietarioYVehiculo(propietario, vehiculo);
	}

	public BonificacionAsignada getBonificacionEnPuesto(Propietario propietario, Puesto puesto) {
		return sp.getBonificacionAsignadaEnPuesto(propietario, puesto);
	}

	public void agregarVehiculo(Vehiculo vehiculo) throws AppException {
		sv.agregarVehiculo(vehiculo);
	}

	public void agregarVehiculoConPropietario(Vehiculo vehiculo, Propietario propietario) throws AppException {
		sv.agregarVehiculoConPropietario(vehiculo, propietario);
	}

	public Transito agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo) throws AppException {
		return st.agregarTransito(propietario, puesto, vehiculo);
	}

	public Transito agregarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, LocalDateTime fechaHora)
			throws AppException {
		return st.agregarTransito(propietario, puesto, vehiculo, fechaHora);
	}

	public Puesto getPuestoPorId(int id) throws AppException {
		return sp.getPuestoPorId(id);
	}

	public Vehiculo getVehiculoPorMatricula(Matricula matricula) throws AppException {
		return sv.getVehiculoPorMatricula(matricula);
	}

	public List<Puesto> getTodosPuestos() {
		return sp.getTodosPuestos();
	}

	public Bonificacion getBonificacionPorNombre(String nombre) throws AppException {
		return sb.getBonificacionPorNombre(nombre);
	}

	public List<Bonificacion> getTodasBonificaciones() {
		return sb.getTodasBonificaciones();
	}

	public Estado getEstadoPorNombre(String nombre) throws AppException {
		return se.getEstadoPorNombre(nombre);
	}

	public List<Estado> getTodosEstados() {
		return se.getTodosEstados();
	}

	public void registrarSesionAdministrador(Administrador administrador) {
		su.registrarSesionAdministrador(administrador);
	}

	public void tieneSesionActiva(Administrador administrador) throws AppException {
		su.tieneSesionActiva(administrador);
	}

	public void borrarSesionAdministrador(Administrador administrador) {
		su.borrarSesionAdministrador(administrador);
	}

	public void agregarCategoria(Categoria categoria) throws AppException {
		sv.agregarCategoria(categoria);
	}

}
