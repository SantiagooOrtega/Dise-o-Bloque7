package com.smartlibrary.model;

import com.smartlibrary.exception.LibroNoDisponibleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Pruebas unitarias del modelo de dominio (sin Spring). */
class LibroTest {

    @Test
    @DisplayName("R3: marcarPrestado deja el libro no disponible")
    void marcarPrestadoCambiaDisponibilidad() {
        Libro libro = new Libro("Clean Code", true);

        libro.marcarPrestado();

        assertThat(libro.estaDisponible()).isFalse();
    }

    @Test
    @DisplayName("R1: un libro no disponible no puede prestarse")
    void libroNoDisponibleLanzaExcepcion() {
        Libro libro = new Libro("Refactoring", false);

        assertThatThrownBy(libro::marcarPrestado)
                .isInstanceOf(LibroNoDisponibleException.class)
                .hasMessageContaining("Refactoring");
        assertThat(libro.estaDisponible()).isFalse();
    }
}
