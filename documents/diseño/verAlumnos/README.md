# Sistema de Generación de Exámenes > verAlumnos > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verAlumnos/verAlumnos.svg)|[Análisis](/documents/analisis/verAlumnos/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de alumnos creados por el docente autenticado. Cada alumno guarda referencia al `docenteId` de quien lo creó, permitiendo filtrar directamente por el docente autenticado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verAlumnos/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verAlumnos/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `AlumnosListComponent` que muestra la lista filtrada y campo de búsqueda.
- **AlumnosController**: Endpoints REST `GET /api/alumnos/mios` y `GET /api/alumnos/mios?filtro={criterio}` protegido con JWT.
- **AlumnoService**: Lógica de negocio para obtener alumnos del docente autenticado mediante `docenteId`.
- **AlumnoRepository**: Interface Spring Data JPA con `findByDocenteId()` y métodos de búsqueda filtrada.
- **Base de Datos (PostgreSQL)**: Consulta de alumnos donde `docente_id` coincide con el docente autenticado.

## Decisiones de diseño

- El endpoint `GET /api/alumnos/mios` filtra automáticamente por el `docenteId` extraído del JWT.
- Se usa `AlumnoRepository.findByDocenteId(profesorId)` para obtener solo los alumnos creados por el docente.
- Filtrado por nombre, apellidos o DNI mediante consulta con `Or` conditions (`findByDocenteIdAndNombreContainingOr...`).
- Retorno de código HTTP `200 OK` con la lista de alumnos.
- Mapeo de `Alumno` a `AlumnoDTO` para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.