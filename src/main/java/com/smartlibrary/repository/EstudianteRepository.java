package com.smartlibrary.repository;

import com.smartlibrary.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Capa Repository: acceso a datos de {@link Estudiante}.
 * Spring Data JPA genera la implementación (findById, findAll, save, count...).
 */
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
