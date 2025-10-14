package ort.da.obligatorio339182.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaDTO {

	private String id;
	private Object parametro;

	public static List<RespuestaDTO> lista(RespuestaDTO... respuestas) {
		return Arrays.asList(respuestas);
	}

}
