# eliminarPregunta > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarPregunta/eliminarPregunta.svg)|[Análisis](/documents/analisis/eliminarPregunta/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `eliminarPregunta()` siguiendo el patrón de eliminación segura con confirmación previa, utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarPregunta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarPregunta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos de la pregunta (enunciado, tema, dificultad).
- **PreguntasController**: Endpoint REST `DELETE /api/preguntas/{id}` protegido.
- **PreguntaService**: Lógica de eliminación con validación de existencia y pertenencia.
- **RespuestaService**: Lógica de negocio para eliminación de respuestas asociadas a una pregunta.
- **PreguntaRepository**: Interface Spring Data JPA para persistencia de preguntas.
- **RespuestaRepository**: Interface Spring Data JPA para persistencia de respuestas.
- **Base de Datos (PostgreSQL)**: Eliminación física de la pregunta y sus respuestas.

## Decisiones de diseño

- Confirmación explícita del docente mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia de la pregunta al docente autenticado (extraído del JWT).
- Eliminación en cascada de respuestas asociadas mediante `RespuestaService.eliminarRespuestasPorPregunta()` (relación Composition 1:*).
- **Cohesión de servicios**: `PreguntaService` delega en `RespuestaService` la eliminación de respuestas, manteniendo la responsabilidad de cada servicio en su dominio.
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta al frontend sin contenido, actualización optimista de la lista.
- El `docenteId` se extrae del JWT para verificar la pertenencia de la pregunta.