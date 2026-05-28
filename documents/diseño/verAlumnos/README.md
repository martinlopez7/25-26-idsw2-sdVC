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

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de alumnos dados de alta en el sistema. Los alumnos se persistirán en `AlumnoRepository`.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verAlumnos/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verAlumnos/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `AlumnosListComponent` que muestra la lista y campo de filtrado.
- **AlumnosController**: Endpoint REST `GET /api/alumnos` y `GET /api/alumnos?filtro={criterio}` protegido.
- **AlumnoService**: Lógica de negocio para obtener todos los alumnos y filtrar por criterios.
- **AlumnoRepository**: Interface Spring Data JPA con `findAll()` y métodos de búsqueda por criterios.
- **Base de Datos (PostgreSQL)**: Consulta de alumnos.

## Decisiones de diseño

- Uso de `AlumnoRepository.findAll()` para obtener todos los alumnos.
- Filtrado por nombre, apellidos o DNI mediante consulta con `Or` conditions.
- Retorno de código HTTP `200 OK` con la lista de alumnos.
- Mapeo de `Alumno` a `AlumnoDTO` para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.