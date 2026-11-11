package com.smartlibrary.controller;

import com.smartlibrary.dto.PrestamoRequest;
import com.smartlibrary.dto.PrestamoResponse;
import com.smartlibrary.model.Prestamo;
import com.smartlibrary.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Capa Controller: recibe peticiones HTTP, valida el formato de entrada,
 * delega en el Service y traduce el resultado a DTO + código HTTP.
 * No contiene reglas de negocio ni usa repositorios.
 */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody PrestamoRequest request) {
        Prestamo prestamo = prestamoService.registrarPrestamo(request.estudianteId(), request.libroId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PrestamoResponse.from(prestamo));
    }

    @GetMapping
    public List<PrestamoResponse> listarPrestamos() {
        return prestamoService.listarPrestamos().stream()
                .map(PrestamoResponse::from)
                .toList();
    }
}
