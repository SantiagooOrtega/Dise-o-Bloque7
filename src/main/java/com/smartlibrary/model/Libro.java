package com.smartlibrary.model;

import com.smartlibrary.exception.LibroNoDisponibleException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de dominio: libro del catálogo.
 * Es el experto en información (GRASP) sobre su disponibilidad, por eso
 * las reglas R1 y R3 viven aquí y no existe un setDisponible público.
 */
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private boolean disponible;

    /** Constructor requerido por JPA. */
    protected Libro() {
    }

    public Libro(String titulo, boolean disponible) {
        this.titulo = titulo;
        this.disponible = disponible;
    }

    /**
     * Marca el libro como prestado.
     * R1: solo un libro disponible puede prestarse.
     * R3: al prestarlo, el libro deja de estar disponible.
     *
     * @throws LibroNoDisponibleException si el libro ya está prestado
     */
    public void marcarPrestado() {
        if (!disponible) {
            throw new LibroNoDisponibleException(id, titulo);
        }
        this.disponible = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean estaDisponible() {
        return disponible;
    }
}
