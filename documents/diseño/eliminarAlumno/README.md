# eliminarAlumno > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarAlumno/eliminarAlumno.svg)|[Análisis](/documents/analisis/eliminarAlumno/README.md)|**Diseño**|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## propósito

Diseño detallado del caso de uso `eliminarAlumno()` siguiendo el patrón de eliminación segura con confirmación previa, utilizando el stack tecnológico Spring Boot + React + PostgreSQL.

## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarAlumno/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarAlumno/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos del alumno.
- **AlumnosController**: Endpoint REST `DELETE /api/alumnos/{id}` protegido.
- **AlumnoService**: Lógica de eliminación con validación de existencia y pertenencia.
- **AlumnoRepository**: Interface Spring Data JPA para persistencia de alumnos.
- **Base de Datos (PostgreSQL)**: Eliminación física del alumno.

## Decisiones de diseño

- Confirmación explícita del docente mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia del alumno al docente autenticado (extraído del JWT).
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta al frontend sin contenido, actualización optimista de la lista.