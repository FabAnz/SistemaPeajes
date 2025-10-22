package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;

import java.util.List;
import ort.da.obligatorio339182.exceptions.AppException;
import jakarta.servlet.http.HttpSession;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;

@RestController
@RequestMapping("/acceso")
public class LoginController {
    private final Fachada fachada;

    public LoginController(Fachada fachada) {
        this.fachada = fachada;
    }

    @PostMapping("/login")
    public List<RespuestaDTO> login(
            HttpSession session, 
            @RequestParam String cedula, 
            @RequestParam String contrasenia) throws AppException {
        
        Usuario usuario = fachada.login(cedula, contrasenia);
        
        // Seguridad: Solo guardar el ID del usuario en sesión
        session.setAttribute("usuarioId", usuario.getId());
        String paginaRedireccion = "";
        paginaRedireccion = usuario.tienePermiso(Permiso.ADMIN_DASHBOARD) ? "/administrador/dashboard.html" : "";
        paginaRedireccion = usuario.tienePermiso(Permiso.PROPIETARIO_DASHBOARD) ? "/propietario/dashboard/dashboard.html" : "";
        if(paginaRedireccion.isEmpty()) {
            throw new AppException("No tiene permisos para acceder a ningún dashboard");
        }

        // Solo retornar la URL de redirección - Los dashboards ya saben quién son por su propia URL
        return RespuestaDTO.lista(
            new RespuestaDTO("redirigir", paginaRedireccion)
        );
    }

    @GetMapping("/logout")
    public List<RespuestaDTO> logout(HttpSession session) {
        session.removeAttribute("usuarioId");
        session.invalidate();
        return RespuestaDTO.lista(
            new RespuestaDTO("redirigir", "/login/login.html")
        );
    }

}
