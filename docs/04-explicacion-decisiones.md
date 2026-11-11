# 04 · Explicación de decisiones

## Decisiones principales

1. **Tres capas con dependencias en un solo sentido** (Controller → Service → Repository). Cada capa cambia por una sola razón: HTTP, reglas del caso de uso o tecnología de persistencia.
2. **Las entidades son el modelo de dominio**, no una cuarta capa: las usan Service y Repository, y el Controller solo las convierte a DTO.
3. **Sin setters públicos**: el estado del libro solo cambia con `marcarPrestado()`, que valida antes de cambiar.
4. **DTO en la frontera HTTP** (`PrestamoRequest`, `PrestamoResponse`): no se exponen entidades JPA y el Service no depende de HTTP.
5. **Errores como excepciones de dominio** traducidas en un único `GlobalExceptionHandler` a `ProblemDetail` (404, 409, 400).
6. **`@Transactional` en el Service**: si algo falla no se guarda nada (R4).

## Respuestas de defensa

**¿Qué responsabilidades cambiaron respecto al modelo inicial?**
`GestorPrestamos` se dividió: la recepción HTTP y los códigos de respuesta pasaron a `PrestamoController`/`GlobalExceptionHandler`; la coordinación del caso de uso a `PrestamoService`; la búsqueda y el guardado a los repositorios; y la validación de disponibilidad pasó **al propio `Libro`** (antes era externa vía `setDisponible`).

**¿Qué patrón GRASP Controller se aplicó y por qué un controlador GRASP no equivale a `@RestController`?**
El **controlador GRASP** es `PrestamoService`: es el primer objeto, fuera de la interfaz, que recibe y coordina la operación del sistema «registrar préstamo» (controlador de caso de uso). `@RestController` es un adaptador de la **capa de presentación HTTP**: traduce JSON y códigos de estado. Si mañana la operación se invocara desde consola o mensajería, cambiaría el adaptador, pero el controlador GRASP (`PrestamoService`) seguiría igual.

**¿Dónde está la regla de disponibilidad y por qué?**
En `Libro.marcarPrestado()`. Por **Experto en información**: `Libro` posee el dato `disponible`, así que es quien mejor puede validarlo (R1) y modificarlo (R3). Además, al estar junto al dato, ninguna otra clase puede saltarse la regla. Como un libro solo se marca prestado si está disponible, nunca puede tener dos préstamos activos (R5).

**¿Qué relaciones UML se conservaron y cuáles se modificaron?**
- **Conservadas**: asociaciones `Prestamo–Estudiante` y `Prestamo–Libro` con multiplicidad `0..* → 1`.
- **Modificadas**: ahora son **dirigidas** (`Prestamo →`), se eliminaron los atributos duplicados de estudiante/libro en `Prestamo`, y `GestorPrestamos` se reemplazó por **dependencias** Controller ⇢ Service ⇢ Repository, más **generalizaciones** de los repositorios a `JpaRepository`. No se usa agregación ni composición.
