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
     * Retorna ID "logout" para que el frontend ejecute el proceso de logout
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<List<RespuestaDTO>> handleUnauthorizedException(UnauthorizedException e) {
        // El frontend ejecutará mostrar_logout() que hará fetch a /acceso/logout
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(
                new RespuestaDTO("logout", e.getMessage())
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
