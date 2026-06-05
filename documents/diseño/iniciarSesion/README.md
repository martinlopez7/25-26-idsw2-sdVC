# Sistema de Generación de Exámenes > iniciarSesion > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/iniciarSesion/iniciarSesion.svg)|[Análisis](/documents/analisis/iniciarSesion/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para autenticar a un usuario y generar un token JWT.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/iniciarSesion/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/iniciarSesion/secuencia.puml)|

</div>

## Participantes

- **Frontend (React + TypeScript)**: Captura credenciales mediante formulario reactivo y gestiona el almacenamiento del token JWT en LocalStorage.
- **AuthController**: Endpoint REST `/api/auth/login` que recibe `LoginRequest`.
- **AuthService**: Lógica de negocio para verificar contraseñas con `PasswordEncoder` y delegar generación de tokens JWT.
- **UsuarioRepository**: Interface Spring Data JPA para acceso a datos de usuarios.
- **JwtTokenProvider**: Componente de utilidad para generar y validar tokens JWT.
- **Base de Datos (PostgreSQL)**: Persistencia de usuarios (Docente, Administrador Institucional) y hashes de contraseñas.

## Decisiones de diseño

- Uso de **JWT (JSON Web Tokens)** para autenticación stateless.
- **Spring Security** con `PasswordEncoder` (BCrypt) para hashing seguro de contraseñas.
- Separación de responsabilidades: `AuthService` para lógica de negocio, `JwtTokenProvider` para manejo de tokens.
- Manejo de excepciones con `@ControllerAdvice` para respuestas 401 consistentes.
- Token almacenado en `LocalStorage` del navegador (React Hook useAuth).
- El token incluye el tipo de actor (Docente o Administrador Institucional) para gestión de permisos en frontend.