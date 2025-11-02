package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoDTO {
    private String nombre;

    public EstadoDTO(Estado estado) {
        this.nombre = estado.getNombre();
    }

    public static List<EstadoDTO> list(List<Estado> estados) {
        return estados.stream().map(EstadoDTO::new).collect(Collectors.toList());
    }
}
