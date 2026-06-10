# Sistema de Generación de Exámenes > crearPregunta > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearPregunta/crearPregunta.svg)|[Análisis](/documents/analisis/crearPregunta/README.md)|**Diseño**|
> |-|-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-02
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la creación de una pregunta con datos mínimos. El flujo sigue la filosofía C→U (Create→Update), transfiriendo automáticamente a `editarPregunta()` tras la creación.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearPregunta/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearPregunta/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `PreguntaFormComponent` con validaciones client-side y datos mínimos.
- **PreguntasController**: Endpoint REST `POST /api/preguntas` protegido.
- **PreguntaService**: Lógica de creación con mapeo DTO-Entidad y transferencia a edición.
- **PreguntaRepository**: Interface Spring Data JPA para persistencia de preguntas.
- **AsignaturaRepository**: Para verificar que la asignatura existe.
- **Base de Datos (PostgreSQL)**: Persistencia de preguntas con validaciones de unicidad.

## Decisiones de diseño

- Uso de `PreguntaCreateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`, `@NotNull` para dificultad y tema).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio.
- Transferencia automática a `editarPregunta()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- La pregunta creada se asocia al docente extraído del JWT (`docenteId`) y a la asignatura seleccionada.
- Verificación de unicidad del enunciado dentro de la misma asignatura.
