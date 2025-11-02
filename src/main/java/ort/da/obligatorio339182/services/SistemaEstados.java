package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.model.domain.estados.Habilitado;
import ort.da.obligatorio339182.model.domain.estados.Deshabilitado;
import ort.da.obligatorio339182.model.domain.estados.Suspendido;
import ort.da.obligatorio339182.model.domain.estados.Penalizado;
import ort.da.obligatorio339182.exceptions.AppException;

@Service
class SistemaEstados {
    private List<Estado> estados;

    SistemaEstados() {
        this.estados = new ArrayList<Estado>();
        this.estados.add(new Habilitado());
        this.estados.add(new Deshabilitado());
        this.estados.add(new Suspendido());
        this.estados.add(new Penalizado());
    }

    Estado getEstadoPorNombre(String nombre) throws AppException {
        Estado estado = estados.stream().filter(e -> e.getNombre().equals(nombre)).findFirst().orElse(null);
        if(estado == null) {
            throw new AppException("Estado no encontrado");
        }
        return estado;
    }

    List<Estado> getTodosEstados() {
        return estados;
    }
}
