package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.utils.RespuestaDTO;
import jakarta.servlet.http.HttpSession;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/administrador")
@Scope("session")
public class AdminController {

    private final Fachada fachada;

    public AdminController(Fachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/dashboard")
    public List<RespuestaDTO> cargarDashboardAdmin(HttpSession session) throws UnauthorizedException {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if(usuarioId == null) {
            throw new UnauthorizedException("No hay sesión activa");
        }
        Usuario usuario = fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);
        Administrador administrador = (Administrador) usuario;
        
        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Dashboard cargado correctamente")
        );
    }
}
