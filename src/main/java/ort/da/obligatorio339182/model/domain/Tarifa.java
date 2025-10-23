package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;

@Data
@NoArgsConstructor
public class Tarifa {
	private static int nextId = 0;
	
	@Setter(AccessLevel.PRIVATE)
	private int id;
	private int monto;
	private Categoria categoria;

	public Tarifa(int monto, Categoria categoria) {
		this.id = ++nextId;
		this.monto = monto;
		this.categoria = categoria;
	}

	public void validar() {

	}

}
