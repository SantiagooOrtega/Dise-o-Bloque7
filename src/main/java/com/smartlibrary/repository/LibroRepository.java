package com.smartlibrary.repository;

import com.smartlibrary.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Capa Repository: acceso a datos de {@link Libro}.
 * Spring Data JPA genera la implementación (findById, findAll, save, count...).
 */
public interface LibroRepository extends JpaRepository<Libro, Long> {
}
