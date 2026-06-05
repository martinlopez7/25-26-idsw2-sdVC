# Sistema de Generación de Exámenes > crearAlumno > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearAlumno/crearAlumno.svg)|[Análisis](/documents/analisis/crearAlumno/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la creación de un alumno con datos mínimos. El flujo sigue la filosofía C→U (Create→Update), transfiriendo automáticamente a `editarAlumno()` tras la creación.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearAlumno/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearAlumno/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `AlumnoFormComponent` con validaciones client-side y datos mínimos.
- **AlumnosController**: Endpoint REST `POST /api/alumnos` protegido.
- **AlumnoService**: Lógica de creación con mapeo DTO-Entidad y transferencia a edición.
- **AlumnoRepository**: Interface Spring Data JPA para persistencia de alumnos.
- **Base de Datos (PostgreSQL)**: Persistencia de alumnos con validaciones de unicidad.

## Decisiones de diseño

- Uso de `AlumnoCreateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`, `@Pattern` para DNI).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio.
- Transferencia automática a `editarAlumno()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- El alumno creado se asocia al docente extraído del JWT (`docenteId`), manteniendo la trazabilidad de creación.