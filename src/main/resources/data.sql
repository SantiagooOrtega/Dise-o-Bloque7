-- Datos iniciales de SmartLibrary (sin ids explícitos para no chocar con IDENTITY)

-- Estudiantes (ids 1-3)
INSERT INTO estudiantes (nombre, correo) VALUES ('Ana Gómez', 'ana.gomez@universidad.edu');
INSERT INTO estudiantes (nombre, correo) VALUES ('Luis Pérez', 'luis.perez@universidad.edu');
INSERT INTO estudiantes (nombre, correo) VALUES ('María Rodríguez', 'maria.rodriguez@universidad.edu');

-- Libros (ids 1-4); 'Refactoring' ya está prestado
INSERT INTO libros (titulo, disponible) VALUES ('Clean Code', TRUE);
INSERT INTO libros (titulo, disponible) VALUES ('Design Patterns', TRUE);
INSERT INTO libros (titulo, disponible) VALUES ('Refactoring', FALSE);
INSERT INTO libros (titulo, disponible) VALUES ('Domain-Driven Design', TRUE);

-- Préstamos: uno histórico (devuelto) y uno activo
INSERT INTO prestamos (estudiante_id, libro_id, fecha_prestamo, fecha_devolucion) VALUES (2, 1, DATE '2026-10-01', DATE '2026-10-10');
INSERT INTO prestamos (estudiante_id, libro_id, fecha_prestamo, fecha_devolucion) VALUES (1, 3, DATE '2026-11-01', NULL);
