# Sistema de Generación de Exámenes > verPreguntas > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verPreguntas/verPreguntas.svg)|[Análisis](/documents/analisis/verPreguntas/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de preguntas creadas por el docente autenticado. Cada pregunta guarda referencia al `docenteId` de quien la creó, permitiendo filtrar directamente por el docente autenticado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verPreguntas/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verPreguntas/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `PreguntasListComponent` que muestra la lista filtrada y campo de búsqueda.
- **PreguntasController**: Endpoints REST `GET /api/preguntas/mias` y `GET /api/preguntas/mias?filtro={criterio}` protegido con JWT.
- **PreguntaService**: Lógica de negocio para obtener preguntas del docente autenticado mediante `docenteId`.
- **PreguntaRepository**: Interface Spring Data JPA con `findByDocenteId()` y métodos de búsqueda filtrada.
- **Base de Datos (PostgreSQL)**: Consulta de preguntas donde `docente_id` coincide con el docente autenticado.

## Decisiones de diseño

- El endpoint `GET /api/preguntas/mias` filtra automáticamente por el `docenteId` extraído del JWT.
- Se usa `PreguntaRepository.findByDocenteId(profesorId)` para obtener solo las preguntas creadas por el docente.
- Filtrado por enunciado, tema o dificultad mediante consulta con `Or` conditions (`findByDocenteIdAndEnunciadoContainingOr...`).
- Retorno de código HTTP `200 OK` con la lista de preguntas con sus respuestas asociadas.
- Mapeo de `Pregunta` a `PreguntaDTO` incluyendo las respuestas asociadas para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.