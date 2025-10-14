package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Puesto {
	private int id;
	private String nombre;
	private String direccion;
	private List<Tarifa> tarifa;

	public Void validar() {
		return null;
	}

}
