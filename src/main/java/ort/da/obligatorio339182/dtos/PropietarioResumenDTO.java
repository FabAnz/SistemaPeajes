package ort.da.obligatorio339182.dtos;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropietarioResumenDTO {
    
    private String cedula;
    private String nombreCompleto;
    private String estado;

    public PropietarioResumenDTO(Propietario propietario) {
        this.cedula = propietario.getCedula().getValor();
        this.nombreCompleto = propietario.getNombreCompleto();
        this.estado = propietario.getEstado().getNombre();
    }
}


