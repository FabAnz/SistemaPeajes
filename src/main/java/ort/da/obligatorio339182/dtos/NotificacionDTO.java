package ort.da.obligatorio339182.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

import ort.da.obligatorio339182.model.domain.Notificacion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private int id;
    private String mensaje;
    private String fecha;  // dd/MM/yyyy
    private String hora;   // HH:mm:ss

    public NotificacionDTO(Notificacion notificacion) {
        this.id = notificacion.getId();
        this.mensaje = notificacion.getMensaje();
        this.fecha = notificacion.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.hora = notificacion.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static List<NotificacionDTO> list(List<Notificacion> notificaciones) {
        return notificaciones.stream()
                .map(NotificacionDTO::new)
                .collect(Collectors.toList());
    }
}
