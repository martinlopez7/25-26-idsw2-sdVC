# Sistema de Generación de Exámenes > completarGestion > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/completarGestion/completarGestion.svg)|[Análisis](/documents/analisis/completarGestion/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para mostrar el menú principal de opciones disponibles según el tipo de actor autenticado (Docente o Administrador Institucional). Este caso de uso funciona como **hub central de navegación** del sistema.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/completarGestion/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/completarGestion/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `MenuPrincipalComponent` que muestra las opciones disponibles según el tipo de actor.
- **AuthController**: Endpoint REST `GET /api/auth/menu` protegido que retorna las opciones disponibles para el usuario.
- **AuthService**: Lógica de negocio para determinar las opciones disponibles según el tipo de actor (DOCENTE o ADMINISTRADOR_INSTITUCIONAL).
- **Sesion**: Mantiene el estado de autenticación activa en el frontend (JWT token).
- **Base de Datos (PostgreSQL)**: No requiere consulta a base de datos, las opciones se determinan por el tipo de actor.

## Decisiones de diseño

- Las opciones del menú se determinan **statelessly** en el servidor según el tipo de actor extraído del JWT.
- No se requiere acceso a base de datos para determinar las opciones, ya que estas son fijas por tipo de actor.
- El frontend recibe una lista de opciones con: `id`, `label`, `icon` y `ruta` de navegación.
- El menú se estructura jerárquicamente por módulos (Gestión, Exámenes, Configuración, Sesión).
- El JWT contiene el `tipo` del actor (DOCENTE o ADMINISTRADOR_INSTITUCIONAL) para determinar permisos en frontend.
- Navegación flexible: el usuario selecciona cualquier opción disponible y se redirige al caso de uso correspondiente.

