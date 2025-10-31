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

@RestController
@RequestMapping("/acceso")
@Scope("session")
public class LoginController extends BaseController {
    private Usuario usuario;

    public LoginController(Fachada fachada) {
        super(fachada);
    }

    @PostMapping("/login")
    public List<RespuestaDTO> login(
            HttpSession session, 
            @RequestParam String cedula, 
            @RequestParam String contrasenia) throws AppException {
        
        usuario = fachada.login(cedula, contrasenia);

        String paginaRedireccion = "";
        if(usuario.tienePermiso(Permiso.ADMIN_DASHBOARD)) {
            Administrador administrador = (Administrador) usuario;
            fachada.tieneSesionActiva(administrador);
            fachada.registrarSesionAdministrador(administrador);
            paginaRedireccion = "/administrador/dashboard/dashboard.html";
        }
        
        if(usuario.tienePermiso(Permiso.PROPIETARIO_DASHBOARD)) {
            paginaRedireccion = "/propietario/dashboard/dashboard.html";
        } 
        
        if(paginaRedireccion.isEmpty()) {
            throw new AppException("No tiene permisos para acceder");
        }
        
        session.setAttribute("usuarioId", usuario.getId());
        return RespuestaDTO.lista(
            new RespuestaDTO("redirigir", paginaRedireccion)
        );
    }

    @GetMapping("/logout")
    public List<RespuestaDTO> logout(HttpSession session) {


        if(usuario!=null && usuario.tienePermiso(Permiso.ADMIN_DASHBOARD)) {
            Administrador administrador = (Administrador) usuario;
            fachada.borrarSesionAdministrador(administrador);
        }

        session.removeAttribute("usuarioId");
        session.invalidate();
        return RespuestaDTO.lista(
            new RespuestaDTO("redirigir", "/login/login.html")
        );
    }

}
