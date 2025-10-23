package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para bonificaciones asignadas
 * Usado en la Historia de Usuario 2.2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionAsignadaDTO {
    private String nombreBonificacion;
    private String puesto;
    private String fechaAsignacion;

    /**
     * Constructor que crea el DTO a partir de una BonificacionAsignada
     * @param bonificacionAsignada La bonificación asignada del cual extraer la información
     */
    public BonificacionAsignadaDTO(BonificacionAsignada bonificacionAsignada) {
        this.nombreBonificacion = bonificacionAsignada.getBonificacion().getNombre();
        this.puesto = bonificacionAsignada.getPuesto().getNombre();
        
        // Formatear fecha como dd/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fechaAsignacion = bonificacionAsignada.getFechaAsignacion().format(formatter);
    }

    public static List<BonificacionAsignadaDTO> list(List<BonificacionAsignada> bonificaciones) {
        return bonificaciones.stream()
            .map(BonificacionAsignadaDTO::new)
            .collect(Collectors.toList());
    }
}

