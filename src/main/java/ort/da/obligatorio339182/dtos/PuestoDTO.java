package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.Puesto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para puestos de peaje
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuestoDTO {
	private int id;
	private String nombre;
	private String direccion;

	public PuestoDTO(Puesto puesto) {
		this.id = puesto.getId();
		this.nombre = puesto.getNombre();
		this.direccion = puesto.getDireccion();
	}

	public static List<PuestoDTO> list(List<Puesto> puestos) {
		return puestos.stream()
			.map(PuestoDTO::new)
			.collect(Collectors.toList());
	}
}

