# Generador de Exámenes > editarAsignatura > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarAsignatura/editarAsignatura.svg)|[Análisis](/documents/analisis/editarAsignatura/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Diseño
- **Versión**: 1.0
- **Fecha**: 2026-06-01
- **Autor**: Equipo de desarrollo

## propósito

Diseño técnico del caso de uso `editarAsignatura()` siguiendo el stack tecnológico del proyecto (Spring Boot + React + PostgreSQL), aplicando el patrón "el gordo" de edición continua con gestión de grados y matriculación de alumnos.

## participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Formulario de edición con datos precargados, modal para gestionar grados y alumnos, validación client-side |
| **AsignaturasController** | Controller | Recibe peticiones GET/PUT, valida existencia, delega a servicio |
| **AsignaturaService** | Service | Lógica de negocio: validación de unicidad título/código, gestión de grados y alumnos matriculados (delega a GradoService y AlumnoService) |
| **GradoService** | Service | Lógica de negocio para gestión de grados: obtener grados por asignatura, añadir/quitar grado de asignatura |
| **AlumnoService** | Service | Lógica de negocio para matriculación: obtener alumnos matriculables (que pertenezcan a algún grado de la asignatura), matricular/desmatricular alumno |
| **AsignaturaRepository** | Repository | Acceso a datos: findById, save para asignaturas |
| **GradoRepository** | Repository | Acceso a datos: findByAsignaturaId |
| **AlumnoRepository** | Repository | Acceso a datos: findByAsignaturaId, findByGradoId, save (usado por AlumnoService) |
| **Base de Datos (PostgreSQL)** | Entidad | Persistencia de entidades Asignatura, Grado, Alumno (con relaciones a través de asignaturas.grado_id y alumnos_asignaturas) |

## decisiones de diseño

### API Endpoints

- **GET `/api/asignaturas/{id}`**: Obtiene una asignatura por su ID con la lista de grados asociados y alumnos matriculados. Retorna `AsignaturaDTO` con título, código, curso académico, lista de grados y lista de alumnos.
- **PUT `/api/asignaturas/{id}`**: Actualiza una asignatura existente (título, código, curso académico). No modifica grados ni alumnos directamente.
- **GET `/api/asignaturas/{id}/alumnos-disponibles`**: Obtiene todos los alumnos que pueden ser matriculados en la asignatura. Un alumno es matriculable si pertenece a alguno de los grados asociados a la asignatura y aún no está matriculado en ella.
- **POST `/api/asignaturas/{id}/alumnos/{alumnoId}`**: Matricula a un alumno en la asignatura (inserta en alumnos_asignaturas).
- **DELETE `/api/asignaturas/{id}/alumnos/{alumnoId}`**: Desmatricula a un alumno de la asignatura (elimina de alumnos_asignaturas).

### DTOs

- **AsignaturaDTO**: Incluye `id`, `titulo`, `codigo`, `cursoAcademico`, `bateriaPreguntasId`, `grados` (lista con id, titulo, codigo), `alumnos` (lista con id, nombre, apellidos, dni).
- **AsignaturaUpdateDTO**: Incluye `titulo`, `codigo` y `cursoAcademico` para actualización.
- **GradoDTO**: Incluye `id`, `titulo`, `codigo`.
- **AlumnoDTO**: Incluye `id`, `nombre`, `apellidos`, `dni`.

### Modelo de datos

- Una **Asignatura** pertenece a un único **Grado** (relación many-to-one a través de `asignaturas.grado_id`).
- Un **Grado** puede tener varias **Asignaturas** (1:N).
- La relación entre **Alumno** y **Asignatura** es many-to-many a través de la tabla intermedia `alumnos_asignaturas`.
- **Restricción**: Un alumno solo puede matricularse en una asignatura si pertenece a alguno de los grados asociados a dicha asignatura.

### Flujo de navegación

1. Docente accede desde `ASIGNATURAS_ABIERTO`, `ASIGNATURA_ABIERTO`, `PREGUNTAS_CONTEXTUALES_ABIERTO`, `EXAMENES_ASIGNADOS_CONTEXTUALES` o desde `crearAsignatura()` (C→U)
2. Sistema presenta formulario con datos actuales de la asignatura, lista de grados asociados y lista de alumnos matriculados
3. Docente puede:
   - Modificar título, código, curso académico y guardar (edición continua)
   - Añadir un alumno → sistema muestra modal con alumnos disponibles (que pertenezcan a algún grado de la asignatura y no estén ya matriculados)
   - Quitar un alumno de la asignatura
4. Sistema persiste cambios de forma independiente
5. Docente puede seguir editando o navegar a `verAsignaturas()`, `verPreguntas()`, `generarExamenes()` o `eliminarAsignatura()`

### Validaciones

- **Unicidad de título**: No puede existir otra asignatura con el mismo título para el mismo docente.
- **Unicidad de código**: No puede existir otra asignatura con el mismo código para el mismo docente.
- **Campos requeridos**: Título, código y curso académico no pueden estar vacíos.
- **Pertinencia del alumno**: Solo se pueden matricular alumnos que pertenezcan a algún grado asociado a la asignatura.

### Restricción de negocio: alumnos matriculables

Un alumno solo puede ser matriculado en una asignatura si cumple la siguiente condición:

```
alumno.grado_id ∈ asignatura.grados[]
```

Es decir, el grado del alumno debe ser uno de los grados asociados a la asignatura. Esta validación se realiza en el backend mediante el endpoint `GET /api/asignaturas/{id}/alumnos-disponibles` que filtra:
1. Alumnos que pertenecen al docente autenticado
2. Alumnos cuyo `grado_id` esté en la lista de grados de la asignatura
3. Alumnos que no estén ya matriculados en la asignatura (no existan en `alumnos_asignaturas`)

## diagrama de secuencia

<div align=center>

|![Diseño: editarAsignatura()](/images/diseño/editarAsignatura/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/diseño/editarAsignatura/secuencia.puml)|

</div>