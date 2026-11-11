# 01 · Análisis del modelo inicial (Actividad 1)

## 1. Datos y operaciones del dominio

| Clase | Datos | Operaciones |
|---|---|---|
| Estudiante | id, nombre, correo | consultar sus datos |
| Libro | id, título, disponible | consultar disponibilidad, marcarse como prestado |
| Préstamo | id, estudiante, libro, fechaPrestamo, fechaDevolucion | saber si está activo |
| (GestorPrestamos) | — | registrar préstamo, listar préstamos |

## 2. ¿Por qué Libro–Préstamo y Estudiante–Préstamo son asociaciones y no composiciones?

Una **composición** implica que la parte no existe sin el todo y que comparten ciclo de vida (si se elimina el todo, se eliminan las partes). Aquí no ocurre:

- Un **libro** existe antes de cualquier préstamo y sigue existiendo cuando el préstamo termina; además, un mismo libro participa en **varios préstamos históricos** (R5). Ni el libro "contiene" préstamos ni el préstamo "contiene" al libro.
- Un **estudiante** existe independientemente de sus préstamos y puede tener muchos.
- El préstamo solo **referencia** a ambos (necesita conocerlos, R2). Eso es una **asociación** dirigida `Prestamo → Libro` y `Prestamo → Estudiante` con multiplicidad `0..* → 1`.

## 3. Operaciones necesarias para consultar, validar y guardar

| Propósito | Operación |
|---|---|
| Consultar | `findById(estudianteId)`, `findById(libroId)`, `findAll()` de préstamos |
| Validar | existencia de estudiante y libro; `Libro.marcarPrestado()` verifica disponibilidad (R1); formato de la solicitud (`@NotNull`) |
| Guardar | `save(libro)` (nuevo estado de disponibilidad) y `save(prestamo)`, dentro de una transacción |

## 4. Responsabilidades que faltan en el modelo inicial

**Para HTTP** (no pertenecen al dominio):
- Recibir la petición `POST /api/prestamos` y convertir JSON ↔ objetos (DTO `PrestamoRequest` / `PrestamoResponse`).
- Validar el formato de entrada (campos obligatorios).
- Traducir el resultado a códigos de estado (201, 400, 404, 409, 200) → `PrestamoController` + `GlobalExceptionHandler`.

**Para datos**:
- Buscar y guardar entidades en una base de datos → repositorios (`EstudianteRepository`, `LibroRepository`, `PrestamoRepository`).
- Mapear clases a tablas (anotaciones JPA) y garantizar atomicidad (`@Transactional`), de modo que una solicitud fallida no deje un préstamo a medias (R4).

## 5. Observaciones al modelo inicial

1. **`GestorPrestamos` es una clase "Dios"**: concentra la búsqueda de datos, la validación, la creación del préstamo y (implícitamente) la persistencia. Se reparte entre `PrestamoController` (HTTP), `PrestamoService` (coordinación del caso de uso) y los repositorios (datos).
2. **`setDisponible(boolean)` público rompe R3**: cualquier clase podría volver a poner un libro disponible o prestar un libro ya prestado sin validar. Se sustituye por `Libro.marcarPrestado()`, que valida y cambia el estado en un solo paso (encapsulamiento).
3. **`Prestamo` duplica estudiante y libro**: aparecen como atributo (p. ej. `idEstudiante`, `idLibro`) **y** como asociación. Es redundante y puede volverse inconsistente; se deja solo la asociación (referencias a objetos, mapeadas con `@ManyToOne`).
4. **Asociaciones sin navegabilidad**: no se indica quién conoce a quién. Se define navegabilidad **Prestamo → Estudiante** y **Prestamo → Libro**; ni Libro ni Estudiante necesitan conocer sus préstamos para este caso de uso.
