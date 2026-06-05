# Sistema de Generación de Exámenes > verPreguntas > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verPreguntas/verPreguntas.svg)|[Análisis](/documents/analisis/verPreguntas/README.md)|**Diseño**|
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

- **Frontend (React + TypeScript)**: Componentes `PreguntasListComponent` (lista general) y `AsignaturaFormComponent` (acceso contextual).
- **PreguntasController**: Endpoints REST protegidos con JWT:
  - `GET /api/preguntas/mias` - Lista todas las preguntas del docente autenticado.
  - `GET /api/preguntas/mias?filtro={criterio}` - Filtra preguntas generales.
  - `GET /api/preguntas/asignatura/{asignaturaId}` - Lista preguntas de una asignatura concreta.
  - `GET /api/preguntas/asignatura/{asignaturaId}?filtro={criterio}` - Filtra preguntas de una asignatura.
- **PreguntaService**: Lógica de negocio para obtener preguntas del docente autenticado y por asignatura.
- **PreguntaRepository**: Interface Spring Data JPA con `findByDocenteId()`, `findByDocenteIdAndCriterio()`, `findByAsignaturaId()` y `findByAsignaturaIdAndCriterio()`.
- **Base de Datos (PostgreSQL)**: Consulta de preguntas filtradas por `docente_id` o por `asignatura_id`.

## Decisiones de diseño

- **Doble camino de acceso**:
  - Desde `SISTEMA_DISPONIBLE`: `GET /api/preguntas/mias` → todas las preguntas del docente.
  - Desde `ASIGNATURA_ABIERTO`: `GET /api/preguntas/asignatura/{id}` → preguntas de la asignatura seleccionada.
- El endpoint de asignatura verifica que esta pertenece al docente autenticado (`asignaturaId` + `docenteId` del JWT).
- Filtrado por enunciado, tema o dificultad mediante consulta con `Or` conditions.
- Retorno de código HTTP `200 OK` con la lista de preguntas con sus respuestas asociadas.
- Mapeo de `Pregunta` a `PreguntaDTO` incluyendo las respuestas asociadas para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.