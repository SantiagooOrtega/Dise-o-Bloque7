package com.smartlibrary.service;

import com.smartlibrary.exception.LibroNoDisponibleException;
import com.smartlibrary.exception.RecursoNoEncontradoException;
import com.smartlibrary.model.Estudiante;
import com.smartlibrary.model.Libro;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.repository.EstudianteRepository;
import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas unitarias del Service con repositorios simulados (Mockito). */
@ExtendWith(MockitoExtension.class)
class PrestamoServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;
    @Mock
    private LibroRepository libroRepository;
    @Mock
    private PrestamoRepository prestamoRepository;

    @InjectMocks
    private PrestamoService prestamoService;

    @Test
    @DisplayName("Camino exitoso: guarda libro y préstamo, y el libro queda no disponible")
    void registrarPrestamoExitoso() {
        Estudiante estudiante = new Estudiante("Ana Gómez", "ana@universidad.edu");
        Libro libro = new Libro("Design Patterns", true);
        when(estudianteRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(libroRepository.findById(2L)).thenReturn(Optional.of(libro));
        when(prestamoRepository.save(any(Prestamo.class))).thenAnswer(inv -> inv.getArgument(0));

        Prestamo prestamo = prestamoService.registrarPrestamo(1L, 2L);

        assertThat(prestamo.getEstudiante()).isSameAs(estudiante);
        assertThat(prestamo.getLibro()).isSameAs(libro);
        assertThat(prestamo.getFechaPrestamo()).isEqualTo(LocalDate.now());
        assertThat(prestamo.estaActivo()).isTrue();
        assertThat(libro.estaDisponible()).isFalse();
        verify(libroRepository).save(libro);
        verify(prestamoRepository).save(prestamo);
    }

    @Test
    @DisplayName("R1/R4: libro no disponible lanza excepción y nunca se guarda nada")
    void libroNoDisponibleNoGuarda() {
        when(estudianteRepository.findById(1L))
                .thenReturn(Optional.of(new Estudiante("Ana Gómez", "ana@universidad.edu")));
        when(libroRepository.findById(3L)).thenReturn(Optional.of(new Libro("Refactoring", false)));

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(1L, 3L))
                .isInstanceOf(LibroNoDisponibleException.class);

        verify(libroRepository, never()).save(any());
        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("R4: estudiante inexistente lanza RecursoNoEncontradoException")
    void estudianteInexistente() {
        when(estudianteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(999L, 2L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessage("Estudiante no encontrado con id 999");

        verify(prestamoRepository, never()).save(any());
    }

    @Test
    @DisplayName("R4: libro inexistente lanza RecursoNoEncontradoException")
    void libroInexistente() {
        when(estudianteRepository.findById(1L))
                .thenReturn(Optional.of(new Estudiante("Ana Gómez", "ana@universidad.edu")));
        when(libroRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> prestamoService.registrarPrestamo(1L, 999L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessage("Libro no encontrado con id 999");

        verify(prestamoRepository, never()).save(any());
    }
}
