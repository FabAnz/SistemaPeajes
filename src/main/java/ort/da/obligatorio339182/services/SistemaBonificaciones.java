package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Exonerado;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Trabajador;
import ort.da.obligatorio339182.model.domain.bonifiaciones.Frecuente;
import java.util.List;
import java.util.ArrayList;
import ort.da.obligatorio339182.exceptions.AppException;

@Service
class SistemaBonificaciones {
    private List<Bonificacion> bonificaciones;

    SistemaBonificaciones() {
        this.bonificaciones = new ArrayList<Bonificacion>();
        this.bonificaciones.add(new Exonerado());
        this.bonificaciones.add(new Trabajador());
        this.bonificaciones.add(new Frecuente());
    }
    
    Bonificacion getBonificacionPorNombre(String nombre) throws AppException {
        Bonificacion bonificacion = bonificaciones.stream()
            .filter(b -> b.getNombre().equals(nombre))
            .findFirst()
            .orElse(null);
        if(bonificacion == null) {
            throw new AppException("Bonificación no encontrada");
        }
        return bonificacion;
    }

    List<Bonificacion> getTodasBonificaciones() {
        return bonificaciones;
    }
}
