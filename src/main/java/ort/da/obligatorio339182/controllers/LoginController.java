package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.utils.RespuestaDTO;

import java.util.List;
import ort.da.obligatorio339182.exceptions.AppException;
import jakarta.servlet.http.HttpSession;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/acceso")
@Scope("session")
public class LoginController extends BaseController {
    private Usuario usuario;

    public LoginController(Fachada fachada) {
        super(fachada);
        usuario = null;
    }

    @PostMapping("/login")
    public List<RespuestaDTO> login(
            HttpSession session,
            @RequestParam String cedula,
            @RequestParam String contrasenia) throws AppException {

        usuario = fachada.login(cedula, contrasenia);

        // Registra sesion si es administrador
        if (usuario.tienePermiso(Permiso.ADMIN_DASHBOARD)) {
            Administrador administrador = (Administrador) usuario;
            fachada.tieneSesionActiva(administrador);
            fachada.registrarSesionAdministrador(administrador);
        }

        String paginaRedireccion = getPaginaRedireccion(usuario);

        session.setAttribute("usuarioId", usuario.getId());
        return RespuestaDTO.lista(
                new RespuestaDTO("redirigir", paginaRedireccion));
    }

    @GetMapping("/login")
    public List<RespuestaDTO> usarSesionActiva(@SessionAttribute(name = "usuarioId", required=false) Integer usuarioId) throws AppException {
        // Si no hay sesión activa, retornar lista vacía (el frontend mostrará el login)
        if (usuarioId == null) {
            return RespuestaDTO.lista();
        }

        Usuario usuario = fachada.getUsuarioPorId(usuarioId);
        String paginaRedireccion = getPaginaRedireccion(usuario);

        return RespuestaDTO.lista(
                new RespuestaDTO("redirigir", paginaRedireccion));
    }

    private String getPaginaRedireccion(Usuario usuario) throws AppException {
        if (usuario.tienePermiso(Permiso.ADMIN_DASHBOARD)) {
            return "/administrador/dashboard/dashboard.html";
        }
        if (usuario.tienePermiso(Permiso.PROPIETARIO_DASHBOARD)) {
            return "/propietario/dashboard/dashboard.html";
        }
        throw new AppException("No tiene permisos para acceder");
    }

    @GetMapping("/logout")
    public List<RespuestaDTO> logout(HttpSession session) {

        // Borrar sesion del administrador
        if (usuario != null && usuario.tienePermiso(Permiso.ADMIN_DASHBOARD)) {
            Administrador administrador = (Administrador) usuario;
            fachada.borrarSesionAdministrador(administrador);
        }

        session.removeAttribute("usuarioId");
        session.invalidate();
        usuario = null;
        return RespuestaDTO.lista(
                new RespuestaDTO("redirigir", "/login/login.html"));
    }

}
