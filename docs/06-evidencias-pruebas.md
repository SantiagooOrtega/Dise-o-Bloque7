# 06 · Evidencias de pruebas

Todas las salidas de este documento son **reales**, obtenidas el 2026-11-11 ejecutando el proyecto en local (Java 21.0.10, Maven 3.9.15, Spring Boot 3.5.7).

## 1. Pruebas automáticas (`mvn -B test`)

Resumen extraído de la salida de Maven:

```
[INFO] Running com.smartlibrary.controller.PrestamoControllerIT
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.087 s -- in com.smartlibrary.controller.PrestamoControllerIT
[INFO] Running com.smartlibrary.model.LibroTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.001 s -- in com.smartlibrary.model.LibroTest
[INFO] Running com.smartlibrary.service.PrestamoServiceTest
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.147 s -- in com.smartlibrary.service.PrestamoServiceTest
[INFO] Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

| Clase de prueba | Tipo | Pruebas | Resultado |
|---|---|---|---|
| `LibroTest` | Unitaria de dominio (sin Spring) | 2 | ✅ |
| `PrestamoServiceTest` | Unitaria con Mockito | 4 | ✅ |
| `PrestamoControllerIT` | Integración (MockMvc + H2) | 7 | ✅ |
| **Total** | | **13** | **BUILD SUCCESS** |

## 2. Pruebas manuales con `curl` (aplicación en ejecución)

La aplicación se arrancó con `mvn spring-boot:run` (puerto 8080) y los escenarios se ejecutaron **en este orden** sobre la misma base H2 (por eso el préstamo creado en el escenario 1 aparece en la consulta del escenario 5).

| # | Escenario | Código esperado | Código obtenido |
|---|---|---|---|
| 1 | Libro disponible | 201 | 201 ✅ |
| 2 | Libro ya prestado | 409 | 409 ✅ |
| 3 | Estudiante inexistente | 404 | 404 ✅ |
| 4 | Libro inexistente | 404 | 404 ✅ |
| 5 | Consultar préstamos | 200 | 200 ✅ |
| 6 | Solicitud inválida `{}` | 400 | 400 ✅ |

### 1. Libro disponible (estudiante 1, libro 2)
```
$ curl -s -w '\nHTTP %{http_code}' -X POST localhost:8080/api/prestamos -H 'Content-Type: application/json' -d '{"estudianteId":1,"libroId":2}'
{"id":3,"estudianteId":1,"estudianteNombre":"Ana Gómez","libroId":2,"libroTitulo":"Design Patterns","fechaPrestamo":"2026-11-11","fechaDevolucion":null}
HTTP 201
```

### 2. Libro ya prestado (libro 3)
```
$ curl -s -w '\nHTTP %{http_code}' -X POST localhost:8080/api/prestamos -H 'Content-Type: application/json' -d '{"estudianteId":2,"libroId":3}'
{"type":"about:blank","title":"Libro no disponible","status":409,"detail":"El libro 'Refactoring' (id 3) no está disponible","instance":"/api/prestamos"}
HTTP 409
```

### 3. Estudiante inexistente (999)
```
$ curl -s -w '\nHTTP %{http_code}' -X POST localhost:8080/api/prestamos -H 'Content-Type: application/json' -d '{"estudianteId":999,"libroId":4}'
{"type":"about:blank","title":"Recurso no encontrado","status":404,"detail":"Estudiante no encontrado con id 999","instance":"/api/prestamos"}
HTTP 404
```

### 4. Libro inexistente (999)
```
$ curl -s -w '\nHTTP %{http_code}' -X POST localhost:8080/api/prestamos -H 'Content-Type: application/json' -d '{"estudianteId":1,"libroId":999}'
{"type":"about:blank","title":"Recurso no encontrado","status":404,"detail":"Libro no encontrado con id 999","instance":"/api/prestamos"}
HTTP 404
```

### 5. Consultar préstamos
```
$ curl -s -w '\nHTTP %{http_code}' localhost:8080/api/prestamos
[{"id":1,"estudianteId":2,"estudianteNombre":"Luis Pérez","libroId":1,"libroTitulo":"Clean Code","fechaPrestamo":"2026-10-01","fechaDevolucion":"2026-10-10"},{"id":2,"estudianteId":1,"estudianteNombre":"Ana Gómez","libroId":3,"libroTitulo":"Refactoring","fechaPrestamo":"2026-11-01","fechaDevolucion":null},{"id":3,"estudianteId":1,"estudianteNombre":"Ana Gómez","libroId":2,"libroTitulo":"Design Patterns","fechaPrestamo":"2026-11-11","fechaDevolucion":null}]
HTTP 200
```

### 6. Extra: solicitud inválida (cuerpo {})
```
$ curl -s -w '\nHTTP %{http_code}' -X POST localhost:8080/api/prestamos -H 'Content-Type: application/json' -d '{}'
{"type":"about:blank","title":"Solicitud inválida","status":400,"detail":"estudianteId es obligatorio; libroId es obligatorio","instance":"/api/prestamos"}
HTTP 400
```

