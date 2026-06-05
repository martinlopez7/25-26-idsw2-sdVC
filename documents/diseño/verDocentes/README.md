# Sistema de Generación de Exámenes > verDocentes > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verDocentes/verDocentes.svg)|[Análisis](/documents/analisis/verDocentes/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para visualizar y filtrar la lista de docentes dados de alta en el sistema. Los docentes se almacenan en `UsuarioRepository` (compartido con `iniciarSesion`).

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/verDocentes/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/verDocentes/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `DocentesListComponent` que muestra la lista y campo de filtrado.
- **DocentesController**: Endpoint REST `GET /api/docentes` y `GET /api/docentes?filtro={criterio}` protegido.
- **UsuarioService**: Lógica de negocio para obtener todos los docentes y filtrar por criterios.
- **UsuarioRepository**: Interface Spring Data JPA con `findByTipo("DOCENTE")` y métodos de búsqueda por criterios.
- **Base de Datos (PostgreSQL)**: Consulta de usuarios con tipo='DOCENTE'.

## Decisiones de diseño

- Uso de `UsuarioRepository.findByTipo()` para obtener solo docentes.
- Filtrado por nombre, apellidos o DNI mediante consulta con `Or` conditions.
- Retorno de código HTTP `200 OK` con la lista de docentes.
- Mapeo de `Usuario` a `UsuarioDTO` para no exponer el hash de contraseña.
- El filtrado es opcional y se realiza en el servidor para manejar grandes volúmenes de datos.