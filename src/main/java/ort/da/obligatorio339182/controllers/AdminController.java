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

import java.util.ArrayList;
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
import ort.da.obligatorio339182.dtos.BonificacionAsignadaDTO;
import ort.da.obligatorio339182.dtos.BonificacionDTO;
import ort.da.obligatorio339182.dtos.PropietarioResumenDTO;
import ort.da.obligatorio339182.model.domain.estados.Estado;
import ort.da.obligatorio339182.dtos.CambiarEstadoDTO;
import ort.da.obligatorio339182.dtos.EstadoDTO;
import ort.da.obligatorio339182.dtos.TarifaDTO;
import ort.da.obligatorio339182.dtos.ResultadoEmulacionTransitoDTO;
import ort.da.obligatorio339182.model.domain.Transito;

@RestController
@RequestMapping("/administrador")
@Scope("session")
public class AdminController extends BaseController {

    List<Estado> estados = new ArrayList<>();

    // Permanencia de datos para funcionalidades
    Puesto puesto;
    List<TarifaDTO> tarifasDTO = new ArrayList<>();
    ResultadoEmulacionTransitoDTO resultado;
    Propietario propietario;
    List<BonificacionAsignada> bonificacionesAsignadas;

    public AdminController(Fachada fachada) {
        super(fachada);
    }

    @GetMapping("/dashboard")
    public List<RespuestaDTO> cargarDashboardAdmin(HttpSession session) throws UnauthorizedException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);

        // Obtener lista de estados disponibles para el select
        estados = fachada.getTodosEstados();

        limpiarDatosPermanentes();
        return RespuestaDTO.lista(
        /*
         * new RespuestaDTO("puestos", PuestoDTO.list(puestos)),
         * new RespuestaDTO("bonificaciones", BonificacionDTO.list(bonificaciones)),
         * new RespuestaDTO("estados", EstadoDTO.list(estados))
         */
        );
    }

    private void limpiarDatosPermanentes() {
        puesto = null;
        tarifasDTO = new ArrayList<>();
        resultado = null;
        propietario = null;
        bonificacionesAsignadas = new ArrayList<>();
    }

    @GetMapping("/emular-transito")
    public List<RespuestaDTO> emularTransito(HttpSession session) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.EMULAR_TRANSITO);

        List<Puesto> puestos = fachada.getTodosPuestos();
        List<RespuestaDTO> respuesta = new ArrayList<>();
        respuesta.add(new RespuestaDTO("puestos", PuestoDTO.list(puestos)));

        if (puesto != null)
            respuesta.add(new RespuestaDTO("puesto", new PuestoDTO(puesto)));

        if (tarifasDTO != null && !tarifasDTO.isEmpty())
            respuesta.add(new RespuestaDTO("tarifas", tarifasDTO));

        if (resultado != null)
            respuesta.add(new RespuestaDTO("resultado", resultado));

        return respuesta;
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

        // Obtener vehiculo por matricula
        Matricula matricula = new Matricula(pMatricula);
        Vehiculo vehiculo = fachada.getVehiculoPorMatricula(matricula);

        // Obtener puesto por id
        Puesto puesto = fachada.getPuestoPorId(pPuesto);

        // Obtener propietario
        Propietario propietario = vehiculo.getPropietario();
        if (propietario == null) {
            throw new AppException("Propietario no encontrado");
        }

        // Parsear fecha y hora si se proporcionan
        LocalDateTime fechaHora = null;
        if (pFecha != null && !pFecha.isEmpty() && pHora != null && !pHora.isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(pFecha);
                LocalTime hora = LocalTime.parse(pHora);
                fechaHora = LocalDateTime.of(fecha, hora);
            } catch (DateTimeParseException e) {
                throw new AppException("Formato de fecha u hora inválido");
            }
        }

        // Agregar tránsito con fecha específica o actual
        Transito transito;
        if (fechaHora != null) {
            transito = fachada.agregarTransito(propietario, puesto, vehiculo, fechaHora);
        } else {
            transito = fachada.agregarTransito(propietario, puesto, vehiculo);
        }

        // Crear DTO con el resultado completo
        resultado = new ResultadoEmulacionTransitoDTO(transito, propietario);

        return RespuestaDTO.lista(
                new RespuestaDTO("mensaje", "Transito emulado correctamente"),
                new RespuestaDTO("resultado", resultado));
    }

    @GetMapping("/asignar-bonificacion")
    public List<RespuestaDTO> asignarBonificacion(HttpSession session) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ASIGNAR_BONIFICACION);

        List<RespuestaDTO> respuesta = new ArrayList<>();
        List<Bonificacion> bonificaciones = fachada.getTodasBonificaciones();
        List<Puesto> puestos = fachada.getTodosPuestos();

        respuesta.add(new RespuestaDTO("bonificaciones", BonificacionDTO.list(bonificaciones)));
        respuesta.add(new RespuestaDTO("puestos", PuestoDTO.list(puestos)));

        if (propietario != null)
            respuesta.add(new RespuestaDTO("propietario", new PropietarioResumenDTO(propietario)));

        if (bonificacionesAsignadas != null && !bonificacionesAsignadas.isEmpty())
            respuesta.add(
                    new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas)));

        return respuesta;

    }

    @PostMapping("/asignar-bonificacion")
    public List<RespuestaDTO> asignarBonificacion(
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
                new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas)));
    }

    @GetMapping("/buscar-propietario")
    public List<RespuestaDTO> buscarPropietario(
            HttpSession session,
            @RequestParam String cedula) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);

        // Buscar propietario por cédula
        propietario = fachada.getPropietarioPorCedula(cedula);
        if (propietario == null) {
            throw new AppException("No se encontró un propietario con la cédula ingresada");
        }

        // Obtener bonificaciones asignadas del propietario
        bonificacionesAsignadas = fachada.getBonificacionesPorPropietario(propietario);

        return RespuestaDTO.lista(
                new RespuestaDTO("propietario", new PropietarioResumenDTO(propietario)),
                new RespuestaDTO("bonificacionesAsignadas", BonificacionAsignadaDTO.list(bonificacionesAsignadas)));
    }

    @GetMapping("/cambiar-estado-propietario")
    public List<RespuestaDTO> cambiarEstadoPropietario(HttpSession session) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.CAMBIAR_ESTADO_PROPIETARIO);

        List<RespuestaDTO> respuesta = new ArrayList<>();
        List<Estado> estados = fachada.getTodosEstados();
        respuesta.add(new RespuestaDTO("estados", EstadoDTO.list(estados)));

        if (propietario != null)
            respuesta.add(new RespuestaDTO("propietario", new PropietarioResumenDTO(propietario)));

        return respuesta;
    }

    @PutMapping("/cambiar-estado-propietario")
    public List<RespuestaDTO> cambiarEstadoPropietario(
            HttpSession session,
            @RequestBody CambiarEstadoDTO request) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.CAMBIAR_ESTADO_PROPIETARIO);

        // Buscar propietario por cédula
        propietario = fachada.getPropietarioPorCedula(request.getCedula());
        if (propietario == null) {
            throw new AppException("No se encontró un propietario con la cédula ingresada");
        }

        Estado nuevoEstado = fachada.getEstadoPorNombre(request.getEstado());
        propietario.setEstado(nuevoEstado);

        return RespuestaDTO.lista(
                new RespuestaDTO("mensaje", "Estado cambiado correctamente"),
                new RespuestaDTO("estado", new EstadoDTO(nuevoEstado))
                );
    }

    @GetMapping("/tarifas-puesto")
    public List<RespuestaDTO> obtenerTarifasPuesto(
            HttpSession session,
            @RequestParam int puestoId) throws UnauthorizedException, AppException {
        Integer usuarioId = validarSesion(session);
        fachada.validarPermiso(usuarioId, Permiso.ADMIN_DASHBOARD);

        // Obtener puesto por id
        puesto = fachada.getPuestoPorId(puestoId);

        // Convertir tarifas a DTO
        tarifasDTO = TarifaDTO.list(puesto.getTarifas());

        return RespuestaDTO.lista(
                new RespuestaDTO("tarifas", tarifasDTO));
    }
}
