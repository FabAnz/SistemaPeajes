package ort.da.obligatorio339182.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ort.da.obligatorio339182.utils.RespuestaDTO;

import org.springframework.http.ResponseEntity;

import java.util.List;

@ControllerAdvice
public class GlobalExpetionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<List<RespuestaDTO>> handleAppException(AppException e) {
        return ResponseEntity.status(299).body(
            RespuestaDTO.lista(new RespuestaDTO("error", e.getMessage()))
        );
    }
    
}
