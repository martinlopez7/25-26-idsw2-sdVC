# Sistema de Generación de Exámenes > crearGrado > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearGrado/crearGrado.svg)|[Análisis](/documents/analisis/crearGrado/README.md)|**Diseño**|
> |-|-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-30
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la creación de un grado con datos mínimos. El flujo sigue la filosofía C→U (Create→Update), transfiriendo automáticamente a `editarGrado()` tras la creación.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearGrado/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearGrado/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `GradoFormComponent` con validaciones client-side y datos mínimos.
- **GradosController**: Endpoint REST `POST /api/grados` protegido.
- **GradoService**: Lógica de creación con mapeo DTO-Entidad y transferencia a edición.
- **GradoRepository**: Interface Spring Data JPA para persistencia de grados.
- **Base de Datos (PostgreSQL)**: Persistencia de grados con validaciones de unicidad.

## Decisiones de diseño

- Uso de `GradoCreateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio.
- Transferencia automática a `editarGrado()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- El grado creado se asocia al docente extraído del JWT (`docenteId`), manteniendo la trazabilidad de creación.