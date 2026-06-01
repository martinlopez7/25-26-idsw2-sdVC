# editarGrado > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarGrado/editarGrado.svg)|[Análisis](/documents/analisis/editarGrado/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-30
- **Autor**: Equipo de desarrollo

## propósito

Diseño técnico del caso de uso `editarGrado()` siguiendo el stack tecnológico del proyecto (Spring Boot + React + PostgreSQL), aplicando el patrón "el gordo" de edición continua con gestión de alumnos por grado.

## participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Formulario de edición con datos precargados, modal para añadir alumnos, validación client-side |
| **GradosController** | Controller | Recibe peticiones GET/PUT/DELETE, valida existencia, delega a servicio |
| **GradoService** | Service | Lógica de negocio: validación de unicidad título/código, gestión de alumnos por grado (delega a AlumnoService) |
| **AlumnoService** | Service | Lógica de negocio para gestión de alumnos: obtener alumnos sin grado, añadir/quitar alumno de grado |
| **GradoRepository** | Repository | Acceso a datos: findById, save para grados |
| **AlumnoRepository** | Repository | Acceso a datos: findByGradoId, findByGradoIsNull, save (usado por AlumnoService) |
| **Base de Datos (PostgreSQL)** | Entidad | Persistencia de entidades Grado y Alumno (con columna grado_id) |

## decisiones de diseño

### API Endpoints

- **GET `/api/grados/{id}`**: Obtiene un grado por su ID con la lista de alumnos asociados. Retorna `GradoDTO` con título, código y lista de alumnos.
- **PUT `/api/grados/{id}`**: Actualiza un grado existente (título, código). No modifica alumnos.
- **GET `/api/alumnos/sin-grado`**: Obtiene todos los alumnos que no están asignados a ningún grado. Se usa para mostrar opciones al docente al añadir un alumno.
- **PUT `/api/grados/{id}/alumnos/{alumnoId}`**: Asigna un alumno al grado (establece `grado_id` en Alumno).
- **DELETE `/api/grados/{id}/alumnos/{alumnoId}`**: Desasigna un alumno del grado (establece `grado_id = NULL` en Alumno).

### DTOs

- **GradoDTO**: Incluye `id`, `titulo`, `codigo`, `alumnos` (lista con id, nombre, apellidos, dni).
- **GradoUpdateDTO**: Incluye `titulo` y `codigo` para actualización.
- **AlumnoDTO**: Incluye `id`, `nombre`, `apellidos`, `dni`.

### Modelo de datos

- Un **Alumno** pertenece a un único **Grado** (relación many-to-one).
- La información de pertenencia se guarda en la tabla `alumnos` mediante la columna `grado_id`.
- Un alumno con `grado_id = NULL` está sin asignar a ningún grado.

### Flujo de navegación

1. Docente accede desde `GRADOS_ABIERTO` o `GRADO_ABIERTO`
2. Sistema presenta formulario con datos actuales del grado y lista de alumnos
3. Docente puede:
   - Modificar título/código y guardar (edición continua)
   - Añadir un alumno → sistema muestra modal con alumnos sin grado
   - Quitar un alumno del grado
4. Sistema persiste cambios de forma independiente
5. Docente puede seguir editando o navegar a `verGrados()`

### Validaciones

- **Unicidad de título**: No puede existir otro grado con el mismo título.
- **Unicidad de código**: No puede existir otro grado con el mismo código.
- **Campos requeridos**: Título y código no pueden estar vacíos.

## diagrama de secuencia

<div align=center>

|![Diseño: editarGrado()](/images/diseño/editarGrado/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/diseño/editarGrado/secuencia.puml)|

</div>