# Sistema de Generación de Exámenes > verGrados > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verGrados/verGrados.svg)|[Análisis](/documents/analisis/verGrados/README.md)|**Diseño**|
> |-|-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-30
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de grados del docente autenticado. Cada grado guarda referencia al `docenteId` de quien lo creó, permitiendo filtrar directamente por el docente autenticado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verGrados/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verGrados/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `GradosListComponent` que muestra la lista filtrada y campo de búsqueda.
- **GradosController**: Endpoints REST `GET /api/grados/mios` y `GET /api/grados/mios?filtro={criterio}` protegido con JWT.
- **GradoService**: Lógica de negocio para obtener grados del docente autenticado mediante `docenteId`.
- **GradoRepository**: Interface Spring Data JPA con `findByDocenteId()` y métodos de búsqueda filtrada.
- **Base de Datos (PostgreSQL)**: Consulta de grados donde `docente_id` coincide con el docente autenticado.

## Decisiones de diseño

- El endpoint `GET /api/grados/mios` filtra automáticamente por el `docenteId` extraído del JWT.
- Se usa `GradoRepository.findByDocenteId(docenteId)` para obtener solo los grados creados por el docente.
- Filtrado por título o código mediante consulta con `Or` conditions (`findByDocenteIdAndTituloContainingOrCodigoContaining`).
- Retorno de código HTTP `200 OK` con la lista de grados.
- Mapeo de `Grado` a `GradoDTO` para no exponer datos sensibles.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.