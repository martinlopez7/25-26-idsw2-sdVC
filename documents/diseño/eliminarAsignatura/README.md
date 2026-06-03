# exam-generator > eliminarAsignatura > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarAsignatura/eliminarAsignatura.svg)|[Análisis](/documents/analisis/eliminarAsignatura/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-01

## Propósito

Especificar el flujo para la eliminación física de un registro de asignatura, incluyendo confirmación previa, validación de pertenencia al docente autenticado y manejo de errores.

## Diagrama de secuencia de diseño

![Diagrama de Secuencia](/images/diseño/eliminarAsignatura/secuencia.svg)

[Código PlantUML](/modelosUML/diseño/eliminarAsignatura/secuencia.puml)

## Participantes
- **Frontend (React)**: Botón de eliminación con modal de confirmación.
- **AsignaturasController**: Endpoint REST `DELETE /api/asignaturas/{id}` protegido.
- **AsignaturaService**: Lógica de eliminación con validación de existencia y pertenencia. Colabora con **PreguntaService** para eliminar las preguntas asociadas.
- **PreguntaService**: Servicio que gestiona la eliminación de preguntas y sus respuestas asociadas a la batería de preguntas.
- **AsignaturaRepository**: Interface Spring Data JPA para eliminación de asignaturas.
- **PreguntaRepository**: Interface Spring Data JPA para eliminación de preguntas (y en cascada sus respuestas).

## Decisiones de diseño
- Confirmación explícita del usuario mediante modal de Bootstrap.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia al docente autenticado extraído del JWT.
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- **Eliminación en cascada**: Al eliminar una asignatura, si esta tiene preguntas se eliminan (y sus respuestas asociadas), y finalmente la asignatura. La eliminación de preguntas se realiza a través de `PreguntaService` para mantener la separación de responsabilidades entre servicios.
- Si asignatura tiene relaciones (tiene alumnos y grados asociados) y se elimina, estas entidades relacionadas dejarán de tener relación con la asignatura.
- Respuesta al frontend sin contenido, actualización optimista de la lista.