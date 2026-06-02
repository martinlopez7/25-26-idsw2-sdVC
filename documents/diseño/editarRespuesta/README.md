# editarRespuesta > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarRespuesta/editarRespuesta.svg)|[Análisis](/documents/analisis/editarRespuesta/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `editarRespuesta()` siguiendo el patrón "el gordo" (edición continua con múltiples ciclos de guardado), utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/editarRespuesta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/editarRespuesta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario `RespuestaFormComponent` en modo edición con datos precargados (contenido, esCorrecta).
- **RespuestasController**: Endpoints REST `GET /api/respuestas/{id}` y `PUT /api/respuestas/{id}` protegido.
- **RespuestaService**: Lógica de obtención y actualización con validación de existencia y pertenencia.
- **PreguntaService**: Lógica de negocio para verificar existencia y pertenencia de la pregunta al docente autenticado.
- **RespuestaRepository**: Interface Spring Data JPA para persistencia de respuestas.
- **PreguntaRepository**: Interface Spring Data JPA para acceso a datos de preguntas (usado por PreguntaService).
- **Base de Datos (PostgreSQL)**: Persistencia de respuestas con relación a pregunta.

## Decisiones de diseño

- Uso de `RespuestaUpdateDTO` con validaciones Bean Validation (`@NotBlank` para contenido, `@NotNull` para esCorrecta).
- PUT completo (todos los campos obligatorios en la actualización).
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia de la respuesta al docente autenticado (extraído del JWT).
- Validación de contenido no vacío.
- JPA `save()` realiza merge automático de entidad existente.
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta 200 OK con entidad actualizada completa.
- El `docenteId` se extrae del JWT para verificar la pertenencia de la respuesta.
- **Cohesión de servicios**: `RespuestaService` delega en `PreguntaService` la verificación de existencia y pertenencia de la pregunta, manteniendo la responsabilidad de cada servicio en su dominio.