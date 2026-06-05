# Sistema de Generación de Exámenes > editarDocente > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarDocente/editarDocente.svg)|[Análisis](/documents/analisis/editarDocente/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la edición completa de un docente existente. Implementa el patrón "el gordo" con edición continua y múltiples ciclos de guardado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/editarDocente/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/editarDocente/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `UsuarioFormComponent` en modo edición con datos precargados.
- **DocentesController**: Endpoint REST `GET /api/docentes/{id}` y `PUT /api/docentes/{id}` protegido.
- **UsuarioService**: Lógica de obtención y actualización con validación de existencia y datos únicos.
- **UsuarioRepository**: Interface Spring Data JPA para persistencia de usuarios.
- **Base de Datos (PostgreSQL)**: Persistencia de usuarios con campo discriminatorio `tipo`.

## Decisiones de diseño

- Uso de `UsuarioUpdateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`, `@Email`, `@Pattern` para DNI).
- PUT completo (todos los campos obligatorios en la actualización).
- Verificación previa de existencia del ID (lanza `EntityNotFoundException` → 404).
- Validación de datos únicos (DNI, username, email) antes de actualizar.
- JPA `save()` realiza merge automático de entidad existente.
- Manejo de excepciones con `@ControllerAdvice` para respuestas consistentes.
- Respuesta 200 OK con entidad actualizada completa.
- El docente se guarda en `UsuarioRepository` (compartido con iniciarSesion) para que pueda autenticarse posteriormente.