package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Entidad de dominio: préstamo de un libro a un estudiante (R2).
 * Las relaciones con Estudiante y Libro son asociaciones dirigidas
 * (muchos préstamos → un estudiante / un libro), no composiciones.
 */
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @Column(nullable = false)
    private LocalDate fechaPrestamo;

    /** Nula mientras el préstamo está activo. */
    private LocalDate fechaDevolucion;

    /** Constructor requerido por JPA. */
    protected Prestamo() {
    }

    public Prestamo(Estudiante estudiante, Libro libro, LocalDate fechaPrestamo) {
        this.estudiante = estudiante;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
    }

    /** Un préstamo está activo mientras no tenga fecha de devolución (R5). */
    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public Long getId() {
        return id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
}
