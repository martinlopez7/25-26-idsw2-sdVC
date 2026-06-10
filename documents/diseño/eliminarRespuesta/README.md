# eliminarRespuesta > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarRespuesta/eliminarRespuesta.svg)|[Análisis](/documents/analisis/eliminarRespuesta/README.md)|**Diseño**|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `eliminarRespuesta()` siguiendo el patrón de eliminación segura con confirmación previa, utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarRespuesta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarRespuesta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos de la respuesta (contenido, esCorrecta).
- **RespuestasController**: Endpoint REST `DELETE /api/respuestas/{id}` protegido.
- **RespuestaService**: Lógica de eliminación con validación de existencia y pertenencia.
- **RespuestaRepository**: Interface Spring Data JPA para persistencia de respuestas.
- **PreguntaRepository**: Verificación de pertenencia al docente autenticado.
- **Base de Datos (PostgreSQL)**: Eliminación física de la respuesta.

## Decisiones de diseño

- Confirmación explícita del docente mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia de la respuesta al docente autenticado (extraído del JWT).
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta al frontend sin contenido, actualización optimista de la lista.
- El `docenteId` se extrae del JWT para verificar la pertenencia de la respuesta.