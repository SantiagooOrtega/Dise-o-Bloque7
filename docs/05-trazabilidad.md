# 05 · Matriz de trazabilidad

**Integrantes:** Santiago Ortega Bolaños y Johan Mosquera Montilla

| Requisito | Clase | Operación | Prueba |
|---|---|---|---|
| **R1** Solo un libro disponible puede prestarse | `Libro` (coordinado por `PrestamoService`) | `marcarPrestado()` lanza `LibroNoDisponibleException` → 409 | `LibroTest.libroNoDisponibleLanzaExcepcion`, `PrestamoServiceTest.libroNoDisponibleNoGuarda`, `PrestamoControllerIT.libroYaPrestado` |
| **R2** El préstamo registra estudiante, libro y fecha | `Prestamo`, `PrestamoService` | `new Prestamo(estudiante, libro, LocalDate.now())`, `registrarPrestamo()` | `PrestamoServiceTest.registrarPrestamoExitoso`, `PrestamoControllerIT.libroDisponible` |
| **R3** Al prestarlo, el libro deja de estar disponible | `Libro`, `LibroRepository` | `marcarPrestado()` + `save(libro)` | `LibroTest.marcarPrestadoCambiaDisponibilidad`, `PrestamoServiceTest.registrarPrestamoExitoso`, `PrestamoControllerIT.libroDisponible` |
| **R4** Una solicitud inválida no crea préstamo | `PrestamoRequest`, `PrestamoService`, `GlobalExceptionHandler` | `@NotNull` + `@Valid`; `orElseThrow(RecursoNoEncontradoException)`; `@Transactional` | `PrestamoServiceTest.estudianteInexistente`, `PrestamoServiceTest.libroInexistente`, `PrestamoControllerIT.estudianteInexistente`, `.libroInexistente`, `.solicitudInvalida` |
| **R5** Varios préstamos históricos, solo uno activo | `Libro`, `Prestamo` | `marcarPrestado()`, `estaActivo()` | `PrestamoControllerIT.soloUnPrestamoActivo` (libro 4: 201 y luego 409); datos iniciales con préstamo histórico del libro 1 |
| **Consulta** de préstamos | `PrestamoController`, `PrestamoService`, `PrestamoRepository` | `GET /api/prestamos` → `listarPrestamos()` → `findAll()` | `PrestamoControllerIT.listarPrestamos` |

Evidencias de ejecución: [06-evidencias-pruebas.md](06-evidencias-pruebas.md).
