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
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ort.da.obligatorio339182.model.domain.Vehiculo;
import ort.da.obligatorio339182.model.domain.Puesto;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.dtos.PuestoDTO;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/administrador")
@Scope("session")
public class AdminController extends BaseController {

    public AdminController(Fachada fachada) {
        super(fachada);
    }

    @GetMapping("/dashboard")
    public List<RespuestaDTO> cargarDashboardAdmin(HttpSession session) throws UnauthorizedException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);
        
        // Obtener lista de puestos para el select de emular tránsito
        List<Puesto> puestos = fachada.getTodosPuestos();
        
        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Dashboard cargado correctamente"),
            new RespuestaDTO("puestos", PuestoDTO.list(puestos))
        );
    }

    @PostMapping("/emular-transito")
    public List<RespuestaDTO> emularTransito(
        HttpSession session,
        @RequestParam String pMatricula,
        @RequestParam int pPuesto,
        @RequestParam(required = false) String pFecha,
        @RequestParam(required = false) String pHora) throws UnauthorizedException, AppException {
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if(usuarioId == null) {
            throw new UnauthorizedException("No hay sesión activa");
        }
        fachada.validarPermiso(usuarioId, Permiso.EMULAR_TRANSITO);
        
        //Obtener vehiculo por matricula
        Matricula matricula = new Matricula(pMatricula);
        Vehiculo vehiculo = fachada.getVehiculoPorMatricula(matricula);
        if(vehiculo == null) {
            throw new AppException("No existe el vehículo");
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

        // Parsear fecha y hora si se proporcionan
        LocalDateTime fechaHora = null;
        if(pFecha != null && !pFecha.isEmpty() && pHora != null && !pHora.isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(pFecha);
                LocalTime hora = LocalTime.parse(pHora);
                fechaHora = LocalDateTime.of(fecha, hora);
            } catch (DateTimeParseException e) {
                throw new AppException("Formato de fecha u hora inválido");
            }
        }

        // Agregar tránsito con fecha específica o actual
        if(fechaHora != null) {
            fachada.agregarTransito(propietario, puesto, vehiculo, fechaHora);
        } else {
            fachada.agregarTransito(propietario, puesto, vehiculo);
        }
        
        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Transito emulado correctamente"),
            new RespuestaDTO("nuevoSaldo", propietario.getSaldo())
        );
    }
}
