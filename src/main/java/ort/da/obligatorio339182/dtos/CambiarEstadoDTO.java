package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitud de cambio de estado de propietario
 * Usado en PUT /administrador/cambiar-estado-propietario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambiarEstadoDTO {
    private String cedula;
    private String estado;
}

