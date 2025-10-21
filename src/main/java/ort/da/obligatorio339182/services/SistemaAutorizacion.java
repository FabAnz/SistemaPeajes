package ort.da.obligatorio339182.services;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Lazy;
import jakarta.servlet.http.HttpSession;

import ort.da.obligatorio339182.exceptions.UnauthorizedException;
import ort.da.obligatorio339182.model.domain.usuarios.Usuario;
import ort.da.obligatorio339182.model.domain.usuarios.Propietario;
import ort.da.obligatorio339182.model.domain.usuarios.Permiso;

/**
 * Sistema de autorización para validar sesiones y permisos de usuarios
 * Centraliza la lógica de validación para reutilizar en múltiples controladores
 */
@Service
class SistemaAutorizacion {

    private final Fachada fachada;

    // @Lazy rompe la dependencia circular: permite inyectar Fachada sin crear el bean completo en construcción
    SistemaAutorizacion(@Lazy Fachada fachada) {
        this.fachada = fachada;
    }

    /**
     * Valida que exista una sesión activa y que el usuario tenga el permiso requerido
     * Si la validación falla, invalida la sesión antes de lanzar la excepción
     * 
     * @param session La sesión HTTP actual
     * @param permisoRequerido El permiso que debe tener el usuario
     * @return El usuario validado
     * @throws UnauthorizedException Si no hay sesión, el usuario no existe, no tiene permiso, o está deshabilitado
     */
    Usuario validarSesionYPermiso(HttpSession session, Permiso permisoRequerido) throws UnauthorizedException {
        // 1. Validar que existe sesión activa
        Integer usuarioId = (Integer) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            invalidarSesion(session, "No hay sesión activa");
        }

        // 2. Obtener el usuario completo
        Usuario usuario = fachada.getUsuarioPorId(usuarioId);
        if (usuario == null) {
            // ID inválido en sesión - posiblemente sesión corrupta
            invalidarSesion(session, "Usuario no encontrado");
        }

        // 3. Validar que tenga el permiso requerido
        if (!usuario.tienePermiso(permisoRequerido)) {
            invalidarSesion(session, "No tiene permisos para acceder a este recurso");
        }

        // 4. Si es Propietario, validar que pueda entrar al sistema
        if (usuario instanceof Propietario) {
            Propietario propietario = (Propietario) usuario;
            if (!propietario.puedeIngresarAlSistema()) {
                invalidarSesion(session, "Usuario deshabilitado, no puede ingresar al sistema");
            }
        }

        return usuario;
    }

    private void invalidarSesion(HttpSession session, String mensaje) throws UnauthorizedException {
        session.invalidate();
        throw new UnauthorizedException(mensaje);
    }
}

