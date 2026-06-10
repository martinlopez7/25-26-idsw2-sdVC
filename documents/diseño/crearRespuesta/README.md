# crearRespuesta > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearRespuesta/crearRespuesta.svg)|[Análisis](/documents/analisis/crearRespuesta/README.md)|**Diseño**|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `crearRespuesta()` siguiendo la filosofía C→U (Create→Update) como "el delgado", transfiriendo automáticamente a `editarRespuesta()` tras la creación.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearRespuesta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearRespuesta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario `RespuestaFormComponent` con validaciones client-side y datos mínimos (contenido, esCorrecta).
- **RespuestasController**: Endpoint REST `POST /api/respuestas` protegido.
- **RespuestaService**: Lógica de creación con mapeo DTO-Entidad y transferencia a edición.
- **PreguntaService**: Lógica de negocio para verificar existencia y pertenencia de la pregunta al docente autenticado.
- **PreguntaRepository**: Interface Spring Data JPA para acceso a datos de preguntas.
- **RespuestaRepository**: Interface Spring Data JPA para persistencia de respuestas.
- **Base de Datos (PostgreSQL)**: Persistencia de respuestas con validación de pregunta asociada.

## Decisiones de diseño

- Uso de `RespuestaCreateDTO` con validaciones Bean Validation (`@NotBlank` para contenido, `@NotNull` para esCorrecta).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio.
- Transferencia automática a `editarRespuesta()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- La respuesta creada se asocia a la pregunta extraída del `preguntaId` y al docente del JWT.
- **Cohesión de servicios**: `RespuestaService` delega en `PreguntaService` la verificación de existencia y pertenencia de la pregunta, manteniendo la responsabilidad de cada servicio en su dominio.
- El `docenteId` se extrae del JWT para verificar la pertenencia de la pregunta.