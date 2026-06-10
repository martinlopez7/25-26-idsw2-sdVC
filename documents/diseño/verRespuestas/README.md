# verRespuestas > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verRespuestas/verRespuestas.svg)|[Análisis](/documents/analisis/verRespuestas/README.md)|**Diseño**|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `verRespuestas()` para visualizar y filtrar las respuestas asociadas a una pregunta específica, utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verRespuestas/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verRespuestas/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `RespuestasListComponent` para mostrar la lista de respuestas de una pregunta.
- **RespuestasController**: Endpoints REST protegidos con JWT:
  - `GET /api/respuestas/pregunta/{preguntaId}` - Lista todas las respuestas de una pregunta.
  - `GET /api/respuestas/pregunta/{preguntaId}?filtro={criterio}` - Filtra respuestas por contenido.
- **RespuestaService**: Lógica de negocio para obtener y filtrar respuestas por pregunta.
- **PreguntaService**: Lógica de negocio para verificar existencia y pertenencia de la pregunta al docente autenticado.
- **PreguntaRepository**: Interface Spring Data JPA para acceso a datos de preguntas (usado por PreguntaService).
- **RespuestaRepository**: Interface Spring Data JPA con `findByPreguntaId()` y `findByPreguntaIdAndContenidoContainingIgnoreCase()`.
- **Base de Datos (PostgreSQL)**: Consulta de respuestas filtradas por `pregunta_id`.

## Decisiones de diseño

- Las respuestas se filtran siempre por `preguntaId` (pertenecen a una pregunta específica).
- El endpoint verifica que la pregunta existe y pertenece al docente autenticado (`preguntaId` + `docenteId` del JWT).
- Filtrado por contenido mediante consulta con `ContainingIgnoreCase`.
- Retorno de código HTTP `200 OK` con la lista de respuestas.
- Mapeo de `Respuesta` a `RespuestaDTO` para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.
- El `docenteId` se extrae del JWT para verificar la pertenencia de la pregunta.
- **Cohesión de servicios**: `RespuestaService` delega en `PreguntaService` la verificación de existencia y pertenencia de la pregunta, manteniendo la responsabilidad de cada servicio en su dominio.