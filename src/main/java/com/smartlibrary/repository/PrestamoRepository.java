package com.smartlibrary.repository;

import com.smartlibrary.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Capa Repository: acceso a datos de {@link Prestamo}.
 * Spring Data JPA genera la implementación (findById, findAll, save, count...).
 */
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
