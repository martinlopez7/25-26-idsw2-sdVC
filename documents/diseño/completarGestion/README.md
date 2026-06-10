# completarGestion > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/completarGestion/completarGestion.svg)|[Análisis](/documents/analisis/completarGestion/README.md)|**Diseño**|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-28
- **Autor**: Equipo de desarrollo

## propósito

Diseño de la navegación directa al menú principal para el caso de uso `completarGestion()`. Este caso de uso no requiere lógica de negocio ni endpoints backend; simplemente muestra el menú correspondiente al actor autenticado.

## naturaleza del diseño

`completarGestion()` es un caso de uso de **navegación pura** que:

- No requiere endpoint backend
- No requiere acceso a base de datos
- El frontend determina el tipo de actor desde el JWT y muestra el menú correspondiente

## participantes

- **Frontend (React + TypeScript)**: Lee el `tipoActor` del JWT y renderiza el menú correspondiente (`AdminMenu` o `DocenteMenu`)
- **JwtTokenProvider**: Utilidad para extraer información del token JWT (tipo de actor)
- **Base de Datos (PostgreSQL)**: No requiere consulta

## decisiones de diseño

- No hay endpoint backend para este caso de uso
- El menú se muestra directamente en el frontend sin llamada HTTP
- El `tipoActor` se extrae del JWT almacenado en LocalStorage
- El flujo de navegación ya está implementado en `iniciarSesion()` que redirige al menú correcto según el tipo de actor

## flujo de navegación

1. El usuario invoca `completarGestion()` desde cualquier estado
2. El frontend extrae el `tipoActor` del JWT
3. Se renderiza el menú correspondiente al actor
4. El usuario selecciona una opción del menú

## relación con otros casos de uso

- `iniciarSesion()` → Ya redirige al menú correcto según tipo de actor
- `completarGestion()` → Mismo comportamiento que la navegación post-login
- `cerrarSesion()` → Vuelve al estado de login

## simplicidad

Este caso de uso no necesita:
- Controller en backend
- Service en backend
- Repository
- DTOs
- Endpoint REST

Es pura navegación frontend basada en el JWT.