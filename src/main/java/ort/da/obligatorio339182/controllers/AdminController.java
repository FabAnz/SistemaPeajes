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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
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
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.dtos.EstadoDTO;
import ort.da.obligatorio339182.dtos.CambiarEstadoDTO;

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
        
        // Obtener lista de bonificaciones disponibles para el select
        List<Bonificacion> bonificaciones = fachada.getTodasBonificaciones();

        // Obtener lista de estados disponibles para el select
        List<Estado> estados = fachada.getTodosEstados();
        
        return RespuestaDTO.lista(
            new RespuestaDTO("puestos", PuestoDTO.list(puestos)),
            new RespuestaDTO("bonificaciones", BonificacionDTO.list(bonificaciones)),
            new RespuestaDTO("estados", EstadoDTO.list(estados))
        );
    }

    @PostMapping("/emular-transito")
    public List<RespuestaDTO> emularTransito(
        HttpSession session,
        @RequestParam String pMatricula,
        @RequestParam int pPuesto,
        @RequestParam(required = false) String pFecha,
        @RequestParam(required = false) String pHora) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
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

        // Buscar propietario por cédula
        Propietario propietario = fachada.getPropietarioPorCedula(cedula);

        // Obtener datos para construir la bonificacion asignada
        Puesto puesto = fachada.getPuestoPorId(pPuesto);
        Bonificacion bonificacion = fachada.getBonificacionPorNombre(pBonificacion);

        BonificacionAsignada bonificacionAsignada = new BonificacionAsignada(propietario, puesto, bonificacion);
        fachada.agregarBonificacionAsignada(bonificacionAsignada);

        // Obtener bonificaciones actualizadas del propietario
        List<BonificacionAsignada> bonificacionesAsignadas = fachada.getBonificacionesPorPropietario(propietario);

        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Bonificación asignada correctamente"),
            new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas))
        );
    }

    @GetMapping("/buscar-propietario")
    public List<RespuestaDTO> buscarPropietario(
        HttpSession session,
        @RequestParam String cedula
    ) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);

        // Buscar propietario por cédula
        Propietario propietario = fachada.getPropietarioPorCedula(cedula);
        if (propietario == null) {
            throw new AppException("No se encontró un propietario con la cédula ingresada");
        }

        // Obtener bonificaciones asignadas del propietario
        List<BonificacionAsignada> bonificacionesAsignadas = fachada.getBonificacionesPorPropietario(propietario);

        return RespuestaDTO.lista(
            new RespuestaDTO("propietario", PropietarioResumenDTO.from(propietario)),
            new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas))
        );
    }

    @PutMapping("/cambiar-estado-propietario")
    public List<RespuestaDTO> cambiarEstadoPropietario(
        HttpSession session,
        @RequestBody CambiarEstadoDTO request
    ) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.CAMBIAR_ESTADO_PROPIETARIO);

        // Buscar propietario por cédula
        Propietario propietario = fachada.getPropietarioPorCedula(request.getCedula());
        if (propietario == null) {
            throw new AppException("No se encontró un propietario con la cédula ingresada");
        }

        Estado nuevoEstado = fachada.getEstadoPorNombre(request.getEstado());
        propietario.setEstado(nuevoEstado);

        return RespuestaDTO.lista(
            new RespuestaDTO("mensaje", "Estado cambiado correctamente")
        );
    }
}
