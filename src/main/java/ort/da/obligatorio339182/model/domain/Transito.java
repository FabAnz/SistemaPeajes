package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

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

	public Transito(int cobro, LocalDate fecha, Propietario propietario, Puesto puesto, Vehiculo vehiculo) {
		this.id = ++nextId;
		this.cobro = cobro;
		this.fecha = fecha;
		this.propietario = propietario;
		this.puesto = puesto;
		this.vehiculo = vehiculo;
	}

	public void validar() {

	}

}
