# SmartLibrary — Taller práctico bloque 7

API REST con **Spring Boot** que transforma el modelo de clases OO de SmartLibrary en un diseño por **tres capas** (Controller → Service → Repository). Implementa el caso de uso **«Registrar préstamo»** y la **consulta de préstamos**, con persistencia en **H2 en memoria**.

## Reglas de negocio

| Regla | Descripción | Dónde se cumple |
|---|---|---|
| R1 | Solo un libro disponible puede prestarse | `Libro.marcarPrestado()` |
| R2 | El préstamo registra estudiante, libro y fecha | `new Prestamo(estudiante, libro, fecha)` |
| R3 | Al prestarlo, el libro deja de estar disponible | `Libro.marcarPrestado()` |
| R4 | Una solicitud inválida no crea préstamo | Validación + excepciones + `@Transactional` |
| R5 | Varios préstamos históricos, solo uno activo | `Libro.marcarPrestado()` + `Prestamo.estaActivo()` |

## Requisitos y ejecución

- Java 21 y Maven 3.9+

```bash
mvn test               # 13 pruebas: unitarias (dominio, servicio) e integración (MockMvc)
mvn spring-boot:run    # API en http://localhost:8080
```

Consola H2: <http://localhost:8080/h2-console> (JDBC URL `jdbc:h2:mem:smartlibrary`, usuario `sa`, sin contraseña).

### Ejemplo

```bash
curl -X POST localhost:8080/api/prestamos \
     -H 'Content-Type: application/json' \
     -d '{"estudianteId":1,"libroId":2}'
```

## Endpoints

| Método | Ruta | Cuerpo | Respuestas |
|---|---|---|---|
| `POST` | `/api/prestamos` | `{"estudianteId": 1, "libroId": 2}` | `201` creado · `400` solicitud inválida · `404` estudiante/libro no existe · `409` libro no disponible |
| `GET` | `/api/prestamos` | — | `200` lista de préstamos |

Los errores se devuelven en formato `ProblemDetail` (RFC 7807).

## Datos iniciales (`data.sql`)

| Libro | id | Estado |
|---|---|---|
| Clean Code | 1 | disponible (tiene un préstamo histórico devuelto) |
| Design Patterns | 2 | disponible |
| Refactoring | 3 | **no disponible** (préstamo activo del estudiante 1) |
| Domain-Driven Design | 4 | disponible |

Estudiantes: 1 Ana Gómez, 2 Luis Pérez, 3 María Rodríguez.

## Estructura de paquetes

```
src/main/java/com/smartlibrary
├── SmartLibraryApplication.java
├── controller/   PrestamoController          ← capa Controller (HTTP)
├── service/      PrestamoService             ← capa Service (caso de uso, transacción)
├── repository/   Estudiante/Libro/PrestamoRepository ← capa Repository (JPA)
├── model/        Estudiante, Libro, Prestamo ← modelo de dominio (entidades)
├── dto/          PrestamoRequest, PrestamoResponse
└── exception/    RecursoNoEncontradoException, LibroNoDisponibleException, GlobalExceptionHandler
```

Dirección de dependencias: `Controller → Service → Repository`. El Controller no usa repositorios ni contiene reglas; el Service no conoce HTTP (ni `ResponseEntity` ni DTO).

## Entregables

| # | Entregable | Ubicación |
|---|---|---|
| 1 | Análisis del modelo inicial (Actividad 1) | [docs/01-analisis-modelo-inicial.md](docs/01-analisis-modelo-inicial.md) |
| 2 | Tabla de responsabilidades | [docs/02-tabla-responsabilidades.md](docs/02-tabla-responsabilidades.md) |
| 3 | Diagramas UML (clases por capas y secuencia) | [docs/03-diagramas-uml.md](docs/03-diagramas-uml.md) |
| 4 | Explicación de decisiones y defensa | [docs/04-explicacion-decisiones.md](docs/04-explicacion-decisiones.md) |
| 5 | Matriz de trazabilidad | [docs/05-trazabilidad.md](docs/05-trazabilidad.md) |
| 6 | Evidencias de pruebas | [docs/06-evidencias-pruebas.md](docs/06-evidencias-pruebas.md) |
| 7 | Código fuente y pruebas | `src/` |
