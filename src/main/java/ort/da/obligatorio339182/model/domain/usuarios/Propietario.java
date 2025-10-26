package ort.da.obligatorio339182.model.domain.usuarios;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.model.domain.Notificacion;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import lombok.Getter;
import lombok.Setter;
import ort.da.obligatorio339182.exceptions.AppException;

@Getter
@Setter
public class Propietario extends Usuario {

	private static final Set<Permiso> permisoPropietario = Set.of(
		Permiso.PROPIETARIO_DASHBOARD
	);

	private int saldo;
	private int saldoMinimo;
	private List<Transito> transitos;
	private List<Vehiculo> vehiculos;
	private Estado estado;
	private List<Notificacion> notificaciones;

	public Propietario(String nombreCompleto, Contrasenia contrasenia, Cedula cedula) {
		super(nombreCompleto, contrasenia, cedula);
		this.saldo = 0;
		this.saldoMinimo = 100;
		this.transitos = new ArrayList<>();
		this.vehiculos = new ArrayList<>();
		this.estado = Estado.habilitado();
		this.notificaciones = new ArrayList<>();
	}

	@Override
	public void validar() throws AppException {
		// Validar campos heredados de Usuario
		if (getId() <= 0) {
			throw new AppException("El id debe ser mayor a 0");
		}
		if (getNombreCompleto() == null || getNombreCompleto().isBlank()) {
			throw new AppException("El nombre completo no puede estar vacío");
		}
		if (getContrasenia() == null) {
			throw new AppException("La contraseña no puede ser null");
		}
		if (getCedula() == null) {
			throw new AppException("La cédula no puede ser null");
		}
		
		// Validar campos propios del Propietario
		if (saldoMinimo < 0) {
			throw new AppException("El saldo mínimo no puede ser negativo");
		}
		if (transitos == null) {
			throw new AppException("La lista de tránsitos no puede ser null");
		}
		if (vehiculos == null) {
			throw new AppException("La lista de vehículos no puede ser null");
		}
		if (estado == null) {
			throw new AppException("El estado no puede ser null");
		}
		if (notificaciones == null) {
			throw new AppException("La lista de notificaciones no puede ser null");
		}
	}

	@Override
	public boolean accesoPermitido(String contrasenia) throws AppException {
		if(!this.puedeIngresarAlSistema()) {
			throw new AppException("Usuario deshabilitado, no puede ingresar al sistema");
		}
		return super.accesoPermitido(contrasenia);
	}

	public boolean puedeIngresarAlSistema() {
		return this.estado.puedeIngresarAlSistema();
	}

	public boolean puedeRecibirBonificaciones() {
		return this.estado.puedeAsignarBonificaciones();
	}

	public boolean puedeRealizarTransitos() {
		return this.estado.puedeRealizarTransitos();
	}

	public boolean aplicanBonificaciones() {
		return this.estado.aplicanBonificaciones();
	}

	public boolean recibeNotificaciones() {
		return this.estado.recibeNotificaciones();
	}

	@Override
	public boolean tienePermiso(Permiso permiso) {
		return permisoPropietario.contains(permiso);
	}

	public void agregarVehiculo(Vehiculo vehiculo) throws AppException {
		vehiculo.validar();
		this.vehiculos.add(vehiculo);
	}

	public void agregarTransito(Transito transito) throws AppException {
		transito.validar();
		this.transitos.add(transito);
	}

	public void agregarNotificacion(Notificacion notificacion) throws AppException {
		notificacion.validar();
		this.notificaciones.add(notificacion);
	}

	public void restarSaldo(int monto) throws AppException {
		if(monto > this.saldo) {
			throw new AppException("No tiene saldo suficiente para realizar el pago");
		}
		this.saldo -= monto;
	}

}
