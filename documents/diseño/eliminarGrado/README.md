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
- **GradoService**: Lógica de eliminación con validación de existencia y pertenencia al docente. Colabora con **AlumnoService** para desmatricular alumnos y con **AsignaturaService** para eliminar relaciones grado-asignatura.
- **AsignaturaService**: Servicio que gestiona la eliminación de la relación entre grado y asignatura (método `eliminarRelacionGrado`).
- **AlumnoService**: Servicio que gestiona la desmatriculación de alumnos de asignaturas (método `desmatricularAlumnoDeAsignatura` ya existente).
- **GradoRepository**: Interface Spring Data JPA para persistencia de grados.
- **Base de Datos (PostgreSQL)**: Eliminación física del grado y actualizaciones en cascada de relaciones.

## Decisiones de diseño

- Confirmación explícita del usuario mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia al docente autenticado extraído del JWT.
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- **Eliminación en cascada suave**: Al eliminar un grado:
  1. Se obtienen las asignaturas asociadas al grado
  2. Por cada asignatura, se desmatriculan los alumnos que pertenecen a ese grado (un alumno solo pertenece a un grado) usando **AlumnoService.desmatricularAlumnoDeAsignatura()**
  3. Se elimina la relación entre el grado y la asignatura usando **AsignaturaService.eliminarRelacionGrado()**
  4. Finalmente se elimina el grado
- La eliminación de grado NO elimina las asignaturas ni sus preguntas asociadas (estas siguen existiendo)
- Respuesta al frontend sin contenido, actualización optimista de la lista.