# Sistema de Generación de Exámenes > eliminarGrado > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarGrado/eliminarGrado.svg)|[Análisis](/documents/analisis/eliminarGrado/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-30
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la eliminación segura de un grado existente, incluyendo confirmación previa y manejo de errores.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarGrado/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarGrado/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos del grado (nombre, código, alumnos enlistados).
- **GradosController**: Endpoint REST `DELETE /api/grados/{id}` protegido.
- **GradoService**: Lógica de eliminación con validación de existencia.
- **GradoRepository**: Interface Spring Data JPA para persistencia de grados.
- **Base de Datos (PostgreSQL)**: Eliminación física del grado.

## Decisiones de diseño

- Confirmación explícita del usuario mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Si el grado tiene asignaturas o alumnos asociados, al eliminarlo los alumnos y/o asignaturas dejarán de tener referencia a ese grado (es decir, grado_id = null).
- Respuesta al frontend sin contenido, actualización optimista de la lista.