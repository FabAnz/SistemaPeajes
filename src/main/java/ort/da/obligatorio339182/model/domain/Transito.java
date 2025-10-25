package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import ort.da.obligatorio339182.exceptions.AppException;

@Data
@NoArgsConstructor
public class Transito {
	private static int nextId = 0;

	@Setter(AccessLevel.PRIVATE)
	private int id;
	private int cobro;
	private LocalDate fecha;
	private Propietario propietario;
	private Puesto puesto;
	private Vehiculo vehiculo;

	public Transito(int cobro, Propietario propietario, Puesto puesto, Vehiculo vehiculo) {
		this.id = ++nextId;
		this.cobro = cobro;
		this.fecha = LocalDate.now();
		this.propietario = propietario;
		this.puesto = puesto;
		this.vehiculo = vehiculo;
	}

	public void validar() throws AppException {
		if(cobro <= 0) {
			throw new AppException("El cobro debe ser mayor a 0");
		}
		if(fecha == null) {
			throw new AppException("La fecha no puede ser null");
		}
		if(propietario == null) {
			throw new AppException("El propietario no puede ser null");
		}
		if(puesto == null) {
			throw new AppException("El puesto no puede ser null");
		}
		if(vehiculo == null) {
			throw new AppException("El vehiculo no puede ser null");
		}
	}

}
