package ort.da.obligatorio339182.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Puesto {
	private static int nextId = 0;
	
	@Setter(AccessLevel.PRIVATE)
	private int id;
	private String nombre;
	private String direccion;
	private List<Tarifa> tarifas;

	public Puesto(String nombre, String direccion) {
		this.id = ++nextId;
		this.nombre = nombre;
		this.direccion = direccion;
		this.tarifas = new ArrayList<>();
	}

	public Void validar() {
		return null;
	}

	/**
	 * Obtiene la tarifa para una categoría específica en este puesto
	 * Principio de Experto: Puesto es el experto en sus tarifas
	 * @param categoria La categoría del vehículo
	 * @return El monto de la tarifa, o 0 si no existe tarifa para esa categoría
	 */
	public int getTarifaPorCategoria(Categoria categoria) {
		if (tarifas == null || tarifas.isEmpty()) {
			return 0;
		}
		
		return tarifas.stream()
			.filter(t -> t.getCategoria() == categoria)
			.findFirst()
			.map(Tarifa::getMonto)
			.orElse(0);
	}

}
