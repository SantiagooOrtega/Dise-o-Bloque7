package com.smartlibrary.exception;

/**
 * Se lanza cuando un estudiante o libro solicitado no existe (se traduce a 404).
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " no encontrado con id " + id);
    }
}
