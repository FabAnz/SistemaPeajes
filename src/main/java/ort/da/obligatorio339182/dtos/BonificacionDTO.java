package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionDTO {
	private String nombre;

	public BonificacionDTO(Bonificacion bonificacion) {
		this.nombre = bonificacion.getNombre();
	}

	public static List<BonificacionDTO> list(List<Bonificacion> bonificaciones) {
		return bonificaciones.stream()
			.map(BonificacionDTO::new)
			.collect(Collectors.toList());
	}
}

