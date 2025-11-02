package ort.da.obligatorio339182.controllers;

import jakarta.servlet.http.HttpSession;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.services.Fachada;

public abstract class BaseController {
    protected final Fachada fachada;

    public BaseController(Fachada fachada) {
        this.fachada = fachada;
    }

    protected Integer validarSesion(HttpSession session) throws UnauthorizedException {
        if(session.getAttribute("usuarioId") == null) {
            throw new UnauthorizedException("No hay sesión activa");
        }
        return (Integer) session.getAttribute("usuarioId");
    }
}
