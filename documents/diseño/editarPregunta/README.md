# editarPregunta > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarPregunta/editarPregunta.svg)|[Análisis](/documents/analisis/editarPregunta/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `editarPregunta()` siguiendo el patrón "el gordo" (edición continua con múltiples ciclos de guardado), utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/editarPregunta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/editarPregunta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario `PreguntaFormComponent` en modo edición con datos precargados (enunciado, tema, dificultad, respuestas, habilitada).
- **PreguntasController**: Endpoints REST `GET /api/preguntas/{id}` y `PUT /api/preguntas/{id}` protegido.
- **PreguntaService**: Lógica de obtención y actualización con validación de existencia, pertenencia y datos únicos.
- **RespuestaService**: Lógica de negocio para gestión de respuestas asociadas a una pregunta (usado por PreguntaService para procesar respuestas).
- **PreguntaRepository**: Interface Spring Data JPA para persistencia de preguntas.
- **RespuestaRepository**: Interface Spring Data JPA para persistencia de respuestas (usado por RespuestaService).
- **Base de Datos (PostgreSQL)**: Persistencia de preguntas con relación a asignatura y respuestas.

## Decisiones de diseño

- Uso de `PreguntaUpdateDTO` con validaciones Bean Validation (`@NotBlank` para enunciado, `@NotNull` para dificultad).
- PUT completo (todos los campos obligatorios en la actualización).
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia de la pregunta al docente autenticado (extraído del JWT).
- Manejo de respuestas como lista dentro del DTO (cascade persist/update).
- Validación de datos únicos (enunciado no duplicado en la misma asignatura).
- JPA `save()` realiza merge automático de entidad existente.
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta 200 OK con entidad actualizada completa.
- El campo `habilitada` permite deshabilitar preguntas sin eliminarlas (soft disable).
- El `docenteId` se extrae del JWT para verificar la pertenencia de la pregunta.
- **Cohesión de servicios**: `PreguntaService` delega en `RespuestaService` el procesamiento de respuestas (agregar/actualizar/eliminar), manteniendo la responsabilidad de cada servicio en su dominio.