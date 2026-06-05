# Sistema de Generación de Exámenes > crearAsignatura > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearAsignatura/crearAsignatura.svg)|[Análisis](/documents/analisis/crearAsignatura/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-01
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la creación de una asignatura con datos mínimos. El flujo sigue la filosofía C→U (Create→Update), transfiriendo automáticamente a `editarAsignatura()` tras la creación.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearAsignatura/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearAsignatura/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `AsignaturaFormComponent` con validaciones client-side y datos mínimos.
- **AsignaturasController**: Endpoint REST `POST /api/asignaturas` protegido.
- **AsignaturaService**: Lógica de creación con mapeo DTO-Entidad y transferencia a edición.
- **AsignaturaRepository**: Interface Spring Data JPA para persistencia de asignaturas.
- **Base de Datos (PostgreSQL)**: Persistencia de asignaturas con validaciones de unicidad.

## Decisiones de diseño

- Uso de `AsignaturaCreateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio.
- Transferencia automática a `editarAsignatura()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- La asignatura creada se asocia al docente extraído del JWT (`docenteId`), manteniendo la trazabilidad de creación.