package com.elian.market.api.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad request");
        body.put("message", "Errores de validación");
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", "No tienes permisos para realizar esta acción");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationSimple(DataIntegrityViolationException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Conflicto de Datos");
        errorDetails.put("message", "La operación no se pudo completar debido a un conflicto de datos. Asegúrate de que los valores sean únicos y las referencias existan.");
        // O puedes usar el mensaje original de la causa raíz si no te importa exponerlo (no recomendado)
        errorDetails.put("message", ex.getRootCause() != null ? ex.getRootCause().getMessage() : "Error de integridad de datos.");
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        body.put("error", "Method Not Allowed");
        body.put("message", "El método " + ex.getMethod() + " no está soportado en esta ruta. Métodos permitidos: "
                + ex.getSupportedHttpMethods());

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex) {
        Map<String, Object> body = new HashMap<>();

        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", "La ruta " + ex.getRequestURL() + " no existe");

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        System.err.println("Ocurrió un error inesperado: " + ex.getMessage());
        ex.printStackTrace(); // En producción, se loguea, no se imprime a consola

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("error", "Error Interno del Servidor");
        errorDetails.put("message", "Ocurrió un error inesperado. Por favor, inténtalo de nuevo más tarde.");

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}