package ort.da.obligatorio339182.dtos;

import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

public class PropietarioResumenDTO {

    private String nombreCompleto;
    private String estado;

    public PropietarioResumenDTO(String nombreCompleto, String estado) {
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEstado() {
        return estado;
    }

    public static PropietarioResumenDTO from(Propietario propietario) {
        String nombre = propietario.getNombreCompleto();
        String estadoActual = propietario.getEstado().getNombre();
        return new PropietarioResumenDTO(nombre, estadoActual);
    }
}


