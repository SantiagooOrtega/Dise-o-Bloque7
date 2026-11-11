package com.smartlibrary.exception;

/**
 * Se lanza cuando se intenta prestar un libro que no está disponible (R1, se traduce a 409).
 */
public class LibroNoDisponibleException extends RuntimeException {

    public LibroNoDisponibleException(Long id, String titulo) {
        super("El libro '" + titulo + "' (id " + id + ") no está disponible");
    }
}
