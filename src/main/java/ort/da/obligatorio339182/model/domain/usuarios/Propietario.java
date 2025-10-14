package ort.da.obligatorio339182.model.domain.usuarios;

import java.util.Set;
import java.util.List;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.model.domain.Notificacion;
import ort.da.obligatorio339182.model.domain.Transito;
import ort.da.obligatorio339182.model.domain.Permiso;
import ort.da.obligatorio339182.model.valueObjects.Contrasenia;
import ort.da.obligatorio339182.model.valueObjects.Cedula;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Propietario extends Usuario {

	private static final Set<Permiso> permisoPropietario = Set.of();

	private int saldo;
	private int saldoMinimo;
	private List<Transito> transitos;
	private List<Vehiculo> vehiculo;
	private Estado estado;
	private List<Notificacion> notificacion;

	public Propietario(int id, String nombreCompleto, Contrasenia contrasenia, Cedula cedula, int saldo,
			int saldoMinimo, List<Transito> transitos, List<Vehiculo> vehiculo,
			List<Notificacion> notificacion) {
		super(id, nombreCompleto, contrasenia, cedula);
		this.saldo = saldo;
		this.saldoMinimo = saldoMinimo;
		this.transitos = transitos;
		this.vehiculo = vehiculo;
		this.estado = Estado.getHabilitado();
		this.notificacion = notificacion;
	}

	@Override
	public void validar() {
	}

	@Override
	public boolean tienePermiso(Permiso permiso) {
		return permisoPropietario.contains(permiso);
	}

}
