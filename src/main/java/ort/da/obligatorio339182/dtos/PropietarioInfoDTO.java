package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

/**
 * DTO para información personal del propietario
 * Usado en la Historia de Usuario 2.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropietarioInfoDTO {
    private String nombreCompleto;
    private String estado;
    private int saldo;

    /**
     * Constructor que crea el DTO a partir de un Propietario
     * @param propietario El propietario del cual extraer la información
     */
    public PropietarioInfoDTO(Propietario propietario) {
        this.nombreCompleto = propietario.getNombreCompleto();
        this.estado = propietario.getEstado().getNombre();
        this.saldo = propietario.getSaldo();
    }
}

