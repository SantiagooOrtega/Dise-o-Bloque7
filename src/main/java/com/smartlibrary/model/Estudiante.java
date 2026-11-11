package com.smartlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad de dominio: estudiante que puede solicitar préstamos.
 * No expone setters públicos; su estado se fija al construirlo.
 */
@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String correo;

    /** Constructor requerido por JPA. */
    protected Estudiante() {
    }

    public Estudiante(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}
