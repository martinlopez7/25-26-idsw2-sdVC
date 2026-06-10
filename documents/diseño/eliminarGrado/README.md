# Sistema de Generación de Exámenes > eliminarGrado > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarGrado/eliminarGrado.svg)|[Análisis](/documents/analisis/eliminarGrado/README.md)|**Diseño**|
> |-|-|-|-|-|

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
- **GradoService**: Lógica de eliminación con validación de existencia y pertenencia al docente. Colabora con **AsignaturaService** para procesar la eliminación en cascada.
- **AsignaturaService**: Servicio que gestiona el procesamiento de eliminación de un grado (método `procesarEliminacionGrado`). Desmatricula alumnos y elimina relaciones grado-asignatura.
- **AlumnoService**: Servicio que gestiona la desmatriculación de alumnos de asignaturas (método `desmatricularAlumnoDeAsignatura`) y la desvinculación de alumnos de un grado (método `quitarAlumnoDeGrado`).
- **GradoRepository**: Interface Spring Data JPA para persistencia de grados.
- **Base de Datos (PostgreSQL)**: Eliminación física del grado y actualizaciones en cascada de relaciones.

## Decisiones de diseño

- Confirmación explícita del usuario mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia al docente autenticado extraído del JWT.
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- **Eliminación en cascada suave**: Al eliminar un grado:
  1. Se verifican existencia y pertenencia del grado
  2. Se llama a **AsignaturaService.procesarEliminacionGrado(gradoId)** que:
     - Obtiene las asignaturas asociadas a este grado
     - Por cada asignatura, por cada alumno que pertenece al grado:
       - **AlumnoService.desmatricularAlumnoDeAsignatura()** → elimina relación con la asignatura
       - **AlumnoService.quitarAlumnoDeGrado()** → limpia la referencia al grado
     - **AsignaturaService.eliminarRelacionGrado()** → elimina la relación entre el grado y la asignatura
  3. Finalmente se elimina el grado
- La eliminación de grado NO elimina las asignaturas ni sus preguntas asociadas (estas siguen existiendo)
- Respuesta al frontend sin contenido, actualización optimista de la lista.