package ort.da.obligatorio339182.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Scope;
import ort.da.obligatorio339182.services.Fachada;
import ort.da.obligatorio339182.exceptions.AppException;
import ort.da.obligatorio339182.model.valueObjects.Matricula;
import ort.da.obligatorio339182.utils.RespuestaDTO;
import jakarta.servlet.http.HttpSession;
import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import ort.da.obligatorio339182.model.domain.usuarios.Administrador;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;

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
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);
        
        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Dashboard cargado correctamente")
        );
    }

    @PostMapping("/emular-transito")
    public List<RespuestaDTO> emularTransito(
        HttpSession session,
        @RequestParam String pMatricula,
        @RequestParam int pPuesto) throws UnauthorizedException, AppException {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if(usuarioId == null) {
            throw new UnauthorizedException("No hay sesión activa");
        }
        fachada.validarPermiso(usuarioId, Permiso.EMULAR_TRANSITO);
        
        //Obtener vehiculo por matricula
        Matricula matricula = new Matricula(pMatricula);
        Vehiculo vehiculo = fachada.getVehiculoPorMatricula(matricula);
        if(vehiculo == null) {
            throw new AppException("Vehículo no encontrado");
        }
        //Obtener puesto por id
        Puesto puesto = fachada.getPuestoPorId(pPuesto);
        if(puesto == null) {
            throw new AppException("Puesto no encontrado");
        }

        //Obtener propietario
        Propietario propietario = vehiculo.getPropietario();
        if(propietario == null) {
            throw new AppException("Propietario no encontrado");
        }

        fachada.agregarTransito(propietario, puesto, vehiculo);
        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Transito emulado correctamente")
        );
    }
}
