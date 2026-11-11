package com.smartlibrary.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para POST /api/prestamos.
 */
public record PrestamoRequest(
        @NotNull(message = "estudianteId es obligatorio") Long estudianteId,
        @NotNull(message = "libroId es obligatorio") Long libroId) {
}
