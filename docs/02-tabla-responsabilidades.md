# 02 · Tabla de responsabilidades

**Integrantes:** Santiago Ortega Bolaños y Johan Mosquera Montilla

| Operación | Clase responsable | Justificación |
|---|---|---|
| Recibir `POST /api/prestamos` | `PrestamoController` | Es el punto de entrada HTTP: deserializa el JSON a `PrestamoRequest`, valida el formato con `@Valid` y delega. No contiene reglas de negocio. |
| Buscar estudiante | `EstudianteRepository` (invocado por `PrestamoService`) | El acceso a datos es responsabilidad del repositorio; el servicio decide **cuándo** buscar y qué hacer si no existe (`RecursoNoEncontradoException`). |
| Buscar libro | `LibroRepository` (invocado por `PrestamoService`) | Igual que el anterior: el repositorio sabe consultar la tabla `libros`; el servicio coordina. |
| Validar disponibilidad | `Libro.marcarPrestado()`, coordinado por `PrestamoService` | GRASP **Experto en información**: `Libro` tiene el dato `disponible`, por lo tanto es quien valida R1. El servicio solo invoca la operación. |
| Crear el préstamo | `PrestamoService` | GRASP **Creador** y **Controlador**: el servicio tiene los datos necesarios (estudiante, libro, fecha) y coordina el caso de uso «Registrar préstamo» (R2). |
| Cambiar disponibilidad | `Libro` | El propio libro cambia su estado dentro de `marcarPrestado()` (R3); no existe `setDisponible` público. |
| Guardar cambios | `LibroRepository` y `PrestamoRepository`, dentro de la transacción `@Transactional` de `PrestamoService` | La persistencia es de los repositorios; la transacción del servicio garantiza que se guarde todo o nada (R4). |
| Devolver respuesta HTTP | `PrestamoController` + `GlobalExceptionHandler` | El controller devuelve `201` con `PrestamoResponse`; el manejador global traduce excepciones a `400/404/409` con `ProblemDetail`. El servicio no conoce HTTP. |
