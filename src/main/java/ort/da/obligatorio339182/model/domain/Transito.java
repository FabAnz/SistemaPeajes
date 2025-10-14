package ort.da.obligatorio339182.model.domain;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import java.time.LocalDate;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transito {

	private int id;
	private int cobro;
	private LocalDate fecha;
	private Propietario propietario;
	private Puesto puesto;
	private Vehiculo vehiculo;

	public void validar() {

	}

}
