# Sistema de Generación de Exámenes > eliminarDocente > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarDocente/eliminarDocente.svg)|[Análisis](/documents/analisis/eliminarDocente/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 2.0 (Spring Boot + React)
- **Fecha**: 2026-06-03
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Services, Repositories) para la eliminación segura de un docente existente, incluyendo la eliminación en cascada de todos sus datos asociados (alumnos, grados, asignaturas, preguntas, respuestas).

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarDocente/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarDocente/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos del docente.
- **DocentesController**: Endpoint REST `DELETE /api/docentes/{id}` protegido.
- **UsuarioService**: Orquesta la eliminación en cascada, delega en servicios específicos.
- **PreguntaService**: Elimina preguntas del docente (respuestas en cascada por JPA).
- **AsignaturaService**: Elimina asignaturas del docente (limpia relaciones con grados y alumnos).
- **AlumnoService**: Elimina alumnos del docente.
- **GradoService**: Elimina grados del docente.
- **Base de Datos (PostgreSQL)**: Eliminación física de entidades con integridad referencial.

## Decisiones de diseño

- Confirmación explícita del usuario mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Eliminación en cascada ordenada para mantener integridad referencial:
  1. **Preguntas** → respuestas se eliminan en cascada (cascade ALL en entidad)
  2. **Asignaturas** → limpia relaciones con grados y alumnos
  3. **Alumnos**
  4. **Grados**
  5. **Usuario** (docente)
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Diseño cohesivo: UsuarioService delega en servicios específicos (PreguntaService, AsignaturaService, AlumnoService, GradoService) siguiendo el principio de responsabilidad única.
- Cada servicio elimina solo sus entidades, evitando accesos directos a repositorios ajenos.