# Sistema de Generación de Exámenes > verAsignaturas > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verAsignaturas/verAsignaturas.svg)|[Análisis](/documents/analisis/verAsignaturas/README.md)|**Diseño**|
> |-|-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-01
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de asignaturas del docente autenticado. Cada asignatura guarda referencia al `docenteId` de quien la creó, permitiendo filtrar directamente por el docente autenticado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verAsignaturas/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verAsignaturas/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `AsignaturasListComponent` que muestra la lista filtrada y campo de búsqueda.
- **AsignaturasController**: Endpoints REST `GET /api/asignaturas/mias` y `GET /api/asignaturas/mias?filtro={criterio}` protegido con JWT.
- **AsignaturaService**: Lógica de negocio para obtener asignaturas del docente autenticado mediante `docenteId`.
- **AsignaturaRepository**: Interface Spring Data JPA con `findByDocenteId()` y métodos de búsqueda filtrada.
- **Base de Datos (PostgreSQL)**: Consulta de asignaturas donde `docente_id` coincide con el docente autenticado.

## Decisiones de diseño

- El endpoint `GET /api/asignaturas/mias` filtra automáticamente por el `docenteId` extraído del JWT.
- Se usa `AsignaturaRepository.findByDocenteId(docenteId)` para obtener solo las asignaturas creadas por el docente.
- Filtrado por título, código o curso académico mediante consulta con `Or` conditions (`findByDocenteIdAndTituloContainingOrCodigoContainingOrCursoAcademicoContaining`).
- Retorno de código HTTP `200 OK` con la lista de asignaturas.
- Mapeo de `Asignatura` a `AsignaturaDTO` para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.