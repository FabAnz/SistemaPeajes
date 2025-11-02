package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.Tarifa;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para tarifas de puestos de peaje
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TarifaDTO {
	private String categoria;
	private int monto;

	public TarifaDTO(Tarifa tarifa) {
		this.categoria = tarifa.getCategoria().getNombre();
		this.monto = tarifa.getMonto();
	}

	public static List<TarifaDTO> list(List<Tarifa> tarifas) {
		return tarifas.stream()
			.map(TarifaDTO::new)
			.collect(Collectors.toList());
	}
}

