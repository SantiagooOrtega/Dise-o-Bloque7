package com.smartlibrary.controller;

import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración de los escenarios del taller (Controller + Service + Repository + H2).
 * Cada prueba es @Transactional, por lo que se revierte al terminar y no afecta a las demás.
 * Los repositorios se usan aquí solo para verificar el estado; el Controller nunca los usa.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PrestamoControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PrestamoRepository prestamoRepository;
    @Autowired
    private LibroRepository libroRepository;

    private static String cuerpo(long estudianteId, long libroId) {
        return "{\"estudianteId\":" + estudianteId + ",\"libroId\":" + libroId + "}";
    }

    @Test
    @DisplayName("Escenario 1: libro disponible → 201 y el libro deja de estar disponible")
    void libroDisponible() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 2)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.estudianteId").value(1))
                .andExpect(jsonPath("$.libroId").value(2))
                .andExpect(jsonPath("$.libroTitulo").value("Design Patterns"))
                .andExpect(jsonPath("$.fechaPrestamo").isNotEmpty())
                .andExpect(jsonPath("$.fechaDevolucion").doesNotExist());

        assertThat(prestamoRepository.count()).isEqualTo(antes + 1);
        assertThat(libroRepository.findById(2L).orElseThrow().estaDisponible()).isFalse();
    }

    @Test
    @DisplayName("Escenario 2: libro ya prestado → 409 y no se crea préstamo")
    void libroYaPrestado() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(2, 3)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("Escenario 3: estudiante inexistente → 404 y no se crea préstamo")
    void estudianteInexistente() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(999, 2)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Estudiante no encontrado con id 999"));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("Escenario 4: libro inexistente → 404 y no se crea préstamo")
    void libroInexistente() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 999)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Libro no encontrado con id 999"));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("Escenario 5: GET /api/prestamos → 200 con al menos 2 registros")
    void listarPrestamos() throws Exception {
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)));
    }

    @Test
    @DisplayName("Extra: cuerpo vacío {} → 400 y no se crea préstamo")
    void solicitudInvalida() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("estudianteId es obligatorio; libroId es obligatorio"));

        assertThat(prestamoRepository.count()).isEqualTo(antes);
    }

    @Test
    @DisplayName("Extra R5: un libro solo puede tener un préstamo activo → 201 y luego 409")
    void soloUnPrestamoActivo() throws Exception {
        long antes = prestamoRepository.count();

        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(1, 4)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/prestamos").contentType(MediaType.APPLICATION_JSON).content(cuerpo(2, 4)))
                .andExpect(status().isConflict());

        assertThat(prestamoRepository.count()).isEqualTo(antes + 1);
    }
}
