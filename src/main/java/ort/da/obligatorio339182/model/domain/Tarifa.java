package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarifa {
	private int id;
	private int monto;
	private Categoria categoria;

	public void validar() {

	}

}
