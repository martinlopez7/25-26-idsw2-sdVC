# Sistema de Generación de Exámenes > crearDocente > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearDocente/crearDocente.svg)|[Análisis](/documents/analisis/crearDocente/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para la creación de un docente con datos mínimos. El docente creado se almacena en `UsuarioRepository` (compartido con `iniciarSesion`) para que pueda autenticarse y usar el sistema.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/crearDocente/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/crearDocente/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Formulario reactivo `UsuarioFormComponent` con validaciones client-side y datos mínimos.
- **DocentesController**: Endpoint REST `POST /api/docentes` protegido.
- **UsuarioService**: Lógica de creación con tipo=DOCENTE, mapeo DTO-Entidad y transferencia a edición.
- **UsuarioRepository**: Interface Spring Data JPA para persistencia de usuarios (Docente, Administrador Institucional).
- **Base de Datos (PostgreSQL)**: Persistencia unificada de usuarios con campo discriminatorio `tipo`.

## Decisiones de diseño

- Uso de `UsuarioCreateDTO` con validaciones Bean Validation (`@NotBlank`, `@Size`, `@Email`, `@Pattern` para DNI).
- Retorno de código HTTP `201 Created` con el recurso creado.
- Mapeo manual DTO-Entidad en capa de servicio con `tipo='DOCENTE'`.
- Transferencia automática a `editarDocente()` tras creación exitosa.
- Validaciones duplicadas: client-side (UX inmediata) y server-side (seguridad).
- **Importante**: El docente se guarda en `UsuarioRepository` (compartido con iniciarSesion) para que pueda autenticarse posteriormente.
