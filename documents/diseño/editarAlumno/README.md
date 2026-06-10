# editarAlumno > Diseño
> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarAlumno/editarAlumno.svg)|[Análisis](/documents/analisis/editarAlumno/README.md)|**Diseño**|
> |-|-|-|-|-|
## información del artefacto
- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React + PostgreSQL)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## propósito
Diseño detallado del caso de uso `editarAlumno()` siguiendo el patrón "el gordo" (edición continua con múltiples ciclos de guardado), utilizando el stack tecnológico Spring Boot + React + PostgreSQL.
## diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/editarAlumno/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/editarAlumno/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `AlumnoFormComponent` en modo edición con datos precargados.
- **AlumnosController**: Endpoints REST `GET /api/alumnos/{id}` y `PUT /api/alumnos/{id}` protegido.
- **AlumnoService**: Lógica de obtención y actualización con validación de existencia y datos únicos.
- **AlumnoRepository**: Interface Spring Data JPA para persistencia de alumnos.
- **DocenteRepository**: Verificación de pertenencia del alumno al docente autenticado.
- **Base de Datos (PostgreSQL)**: Persistencia de alumnos con relación a docente.

## Decisiones de diseño

- Uso de `AlumnoUpdateDTO` con validaciones Bean Validation (`@NotBlank`, `@Pattern` para DNI).
- PUT completo (todos los campos obligatorios en la actualización).
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Verificación de pertenencia del alumno al docente autenticado (extraído del JWT).
- Validación de datos únicos (DNI) antes de actualizar.
- JPA `save()` realiza merge automático de entidad existente.
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta 200 OK con entidad actualizada completa.
