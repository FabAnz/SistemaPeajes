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
import ort.da.obligatorio339182.model.domain.bonifiaciones.Bonificacion;
import ort.da.obligatorio339182.model.domain.bonifiaciones.BonificacionAsignada;
import ort.da.obligatorio339182.dtos.BonificacionDTO;
import ort.da.obligatorio339182.dtos.BonificacionAsignadaDTO;
import ort.da.obligatorio339182.dtos.PropietarioResumenDTO;

@RestController
@RequestMapping("/administrador")
@Scope("session")
public class AdminController extends BaseController {

    private Propietario propietarioEncontrado;
    
    public AdminController(Fachada fachada) {
        super(fachada);
    }

    @GetMapping("/dashboard")
    public List<RespuestaDTO> cargarDashboardAdmin(HttpSession session) throws UnauthorizedException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);
        
        // Obtener lista de puestos para el select de emular tránsito
        List<Puesto> puestos = fachada.getTodosPuestos();
        
        // Obtener lista de bonificaciones disponibles para el select
        List<Bonificacion> bonificaciones = fachada.getTodasBonificaciones();
        
        return RespuestaDTO.lista(
            new RespuestaDTO("puestos", PuestoDTO.list(puestos)),
            new RespuestaDTO("bonificaciones", BonificacionDTO.list(bonificaciones))
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

        //Obtener puesto por id
        Puesto puesto = fachada.getPuestoPorId(pPuesto);

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

    @PostMapping("/asignar-bonificacion")
    public List<RespuestaDTO> asignarBonificacion (
        HttpSession session,
        @RequestParam String cedula,
        @RequestParam int pPuesto,
        @RequestParam String pBonificacion) throws UnauthorizedException, AppException {

        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ASIGNAR_BONIFICACION);

        //Obtener datos para construir la bonificacion asignada
        Puesto puesto = fachada.getPuestoPorId(pPuesto);
        Bonificacion bonificacion = fachada.getBonificacionPorNombre(pBonificacion);

        BonificacionAsignada bonificacionAsignada = new BonificacionAsignada(propietarioEncontrado, puesto, bonificacion);
        fachada.agregarBonificacionAsignada(bonificacionAsignada);

        // Obtener bonificaciones actualizadas del propietario
        List<BonificacionAsignada> bonificacionesAsignadas = fachada.getBonificacionesPorPropietario(propietarioEncontrado);

        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Bonificación asignada correctamente"),
            new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas))
        );
    }

    @PostMapping("/buscar-propietario")
    public List<RespuestaDTO> buscarPropietario(
        HttpSession session,
        @RequestParam String cedula
    ) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ASIGNAR_BONIFICACION);

        propietarioEncontrado = fachada.getPropietarioPorCedula(cedula);
        List<BonificacionAsignada> bonificacionesAsignadas = fachada.getBonificacionesPorPropietario(propietarioEncontrado);

        return RespuestaDTO.lista(
            new RespuestaDTO("propietario", PropietarioResumenDTO.from(propietarioEncontrado)),
            new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas))
        );
    }
}
