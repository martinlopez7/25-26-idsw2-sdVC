# cerrarSesion > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/cerrarSesion/cerrarSesion.svg)|[Análisis](/documents/analisis/cerrarSesion/README.md)|**Diseño**|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## propósito

Detallar la interacción entre los componentes del sistema (Frontend React, AuthController, AuthService, JwtTokenProvider) para cerrar la sesión del usuario autenticado e invalidar el token JWT.

## Diagrama de secuencia de diseño

![Diagrama de Secuencia](/images/diseño/cerrarSesion/secuencia.svg)

[Código PlantUML](/modelosUML/diseño/cerrarSesion/secuencia.puml)

## Participantes
*   **Frontend (React)**: Gestiona el botón de logout, solicita confirmación, elimina el token del almacenamiento y redirige a la pantalla de login.
*   **AuthController**: Endpoint REST `/api/auth/logout` que recibe la petición de cierre.
*   **AuthService**: Lógica de negocio para invalidar la sesión del usuario.
*   **JwtTokenProvider**: Componente de utilidad para invalidar yblacklistear el token JWT.
*   **SecurityConfig**: Configuración de Spring Security para cerrar la sesión y limpiar el contexto de seguridad.

## Decisiones de diseño
*   Uso de **blacklist de tokens JWT** para invalidar sesiones antes de su expiración natural.
*   **Spring Security** con `SecurityContextLogoutHandler` para limpiar el contexto de seguridad.
*   Separación de responsabilidades: `AuthService` para lógica de negocio, `JwtTokenProvider` para manejo de tokens.
*   Token eliminado del `LocalStorage` del navegador (React Auth Service).