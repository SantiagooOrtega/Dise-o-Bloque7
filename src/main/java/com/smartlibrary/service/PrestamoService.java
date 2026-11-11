package com.smartlibrary.service;

import com.smartlibrary.exception.RecursoNoEncontradoException;
import com.smartlibrary.model.Estudiante;
import com.smartlibrary.model.Libro;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.repository.EstudianteRepository;
import com.smartlibrary.repository.LibroRepository;
import com.smartlibrary.repository.PrestamoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Capa Service: coordina el caso de uso «Registrar préstamo» (controlador GRASP).
 * No conoce HTTP: recibe identificadores y devuelve entidades de dominio.
 */
@Service
public class PrestamoService {

    private final EstudianteRepository estudianteRepository;
    private final LibroRepository libroRepository;
    private final PrestamoRepository prestamoRepository;

    public PrestamoService(EstudianteRepository estudianteRepository,
                           LibroRepository libroRepository,
                           PrestamoRepository prestamoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.libroRepository = libroRepository;
        this.prestamoRepository = prestamoRepository;
    }

    /**
     * Registra un préstamo. Si cualquier paso falla se lanza una excepción y la
     * transacción hace rollback, por lo que no se crea ningún préstamo (R4).
     */
    @Transactional
    public Prestamo registrarPrestamo(Long estudianteId, Long libroId) {
        // 1. Buscar estudiante
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", estudianteId));
        // 2. Buscar libro
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro", libroId));
        // 3. Validar disponibilidad y cambiarla: la regla vive en Libro (R1, R3, R5)
        libro.marcarPrestado();
        // 4. Crear el préstamo con estudiante, libro y fecha (R2)
        Prestamo prestamo = new Prestamo(estudiante, libro, LocalDate.now());
        // 5. y 6. Guardar cambios
        libroRepository.save(libro);
        return prestamoRepository.save(prestamo);
    }

    /** Consulta todos los préstamos (históricos y activos). */
    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamos() {
        return prestamoRepository.findAll();
    }
}
