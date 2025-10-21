package ort.da.obligatorio339182.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ort.da.obligatorio339182.utils.RespuestaDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;

@ControllerAdvice
public class GlobalExpetionHandler {
    
    /**
     * Maneja errores de autorización (sesión inválida o sin permisos)
     * Redirige automáticamente al login
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<List<RespuestaDTO>> handleUnauthorizedException(UnauthorizedException e) {
        // La sesión ya fue invalidada en SistemaAutorizacion
        // Retornar redirección al login
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(
                new RespuestaDTO("redirigir", "/login/login.html"),
                new RespuestaDTO("error", e.getMessage())
                )
        );
    }
    
    /**
     * Maneja errores de aplicación generales
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<List<RespuestaDTO>> handleAppException(AppException e) {
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(new RespuestaDTO("error", e.getMessage()))
        );
    }
    
}
