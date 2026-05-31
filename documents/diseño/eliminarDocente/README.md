# Sistema de Generación de Exámenes > eliminarDocente > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarDocente/eliminarDocente.svg)|[Análisis](/documents/analisis/eliminarDocente/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la eliminación segura de un docente existente, incluyendo confirmación previa y manejo de errores.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/eliminarDocente/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/eliminarDocente/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Modal de confirmación de eliminación con datos del docente.
- **DocentesController**: Endpoint REST `DELETE /api/docentes/{id}` protegido.
- **UsuarioService**: Lógica de eliminación con validación de existencia.
- **UsuarioRepository**: Interface Spring Data JPA para persistencia de usuarios.
- **Base de Datos (PostgreSQL)**: Eliminación física del usuario con campo discriminatorio `tipo='DOCENTE'`.

## Decisiones de diseño

- Confirmación explícita del usuario mediante modal de React.
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Retorno de `204 No Content` al éxito (sin body en respuesta).
- Si el docente tiene entidades asociadas (alumnos, grados, asignaturas, etc...), estas se eliminarán también.
- El docente se elimina de `UsuarioRepository` (compartido con iniciarSesion).
- Respuesta al frontend sin contenido, actualización optimista de la lista.