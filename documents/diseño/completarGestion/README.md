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

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service) para mostrar el menú principal de opciones disponibles según el tipo de actor autenticado (Docente o Administrador Institucional). El tipo de actor se extrae del JWT almacenado en LocalStorage.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/completarGestion/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/completarGestion/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Componente `MenuPrincipalComponent` que muestra las opciones disponibles según el tipo de actor. Extrae el JWT de LocalStorage y lo envía en la cabecera Authorization.
- **AuthController**: Endpoint REST `GET /api/auth/menu` protegido con JWT. Extrae el tipo de actor del token para determinar las opciones disponibles.
- **AuthService**: Lógica de negocio para determinar las opciones disponibles según el tipo de actor (DOCENTE o ADMINISTRADOR_INSTITUCIONAL). No requiere acceso a base de datos.
- **JwtTokenProvider**: Componente de utilidad para extraer información del token JWT (tipo de actor).
- **Base de Datos (PostgreSQL)**: No requiere consulta a base de datos, las opciones se determinan por el tipo de actor extraído del JWT.

## Decisiones de diseño

- Las opciones del menú se determinan en el servidor según el **tipo de actor extraído del JWT**, no hay consulta a base de datos.
- El endpoint está protegido con JWT (`@PreAuthorize`) igual que el resto de endpoints autenticados.
- El JWT se envía en la cabecera `Authorization: Bearer <token>` desde el frontend.
- No se requiere `Sesion` como entidad, el estado de autenticación es el JWT mismo (coherente con iniciarSesion/cerrarSesion).
- Retorno de `MenuDTO` con la lista de opciones disponibles para el tipo de actor.

## Flujo de navegación

1. El usuario selecciona "Completar Gestión" desde cualquier estado del sistema
2. El frontend envía GET /api/auth/menu con el JWT en la cabecera
3. El backend extrae el tipo de actor del JWT
4. El backend retorna las opciones disponibles para ese tipo
5. El frontend renderiza el menú y el usuario selecciona una opción
6. El usuario es redirigido al caso de uso correspondiente
