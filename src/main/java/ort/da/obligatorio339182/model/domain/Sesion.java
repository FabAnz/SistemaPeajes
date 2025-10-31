package ort.da.obligatorio339182.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;

@Data
@AllArgsConstructor
public class Sesion {
    private Administrador administrador;
}
