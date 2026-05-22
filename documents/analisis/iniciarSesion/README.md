# iniciarSesion > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/iniciarSesion/iniciarSesion.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Inception (Inicio)
- **Disciplina**: Análisis
- **Versión**: 1.0
- **Fecha**: 2026-05-22
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `iniciarSesion()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: iniciarSesion()](/images/analisis/iniciarSesion/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/iniciarSesion/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### LoginView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir las credenciales de acceso del actor
- Delegar la autenticación al controlador
- Mostrar error en caso de credenciales inválidas
- Gestionar la transición al estado Sistema Disponible tras autenticación exitosa

**Colaboraciones**:
- **Entrada**: Recibe `iniciarSesion()` desde `:SESION_CERRADA`
- **Control**: Se comunica con `IniciarSesionController`
- **Salida**: Navega a `:Sistema Disponible`

### clases de control

#### IniciarSesionController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar el proceso completo de autenticación
- Validar credenciales contra el repositorio de usuarios
- Crear la sesión del usuario tras validación exitosa

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `LoginView`
- **Repositorio**: Delega operaciones de datos a `UsuarioRepository`

### clases de entidad (entity)

#### UsuarioRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de usuarios del sistema
- Proporcionar método para validar credenciales

**Colaboraciones**:
- **Control**: Responde a `IniciarSesionController`
- **Entidad**: Gestiona instancias de `Usuario`

#### Usuario
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un usuario (Docente o Administrador Institucional)
- Encapsular atributos de autenticación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `UsuarioRepository`

#### Sesion
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar el estado de autenticación activa
- Mantener referencia al usuario autenticado

**Colaboraciones**:
- **Control**: Es creada por `IniciarSesionController`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:SESION_CERRADA` → `LoginView.iniciarSesion(usuario, contraseña)`
2. **Autenticación**: `LoginView` → `IniciarSesionController.autenticar(usuario, contraseña)`
3. **Validación**: `IniciarSesionController` → `UsuarioRepository.validarCredenciales(usuario, contraseña)`
4. **Respuesta**: `UsuarioRepository` → `IniciarSesionController` (usuario válido o inválido)
5. **Creación de sesión**: `IniciarSesionController` → `Sesion.crearSesion(usuario)` (si válido)
6. **Transición**: `LoginView` → `:Sistema Disponible`

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Solicitar acceso al sistema|LoginView|Recibe `iniciarSesion(usuario, contraseña)`|
|Mostrar formulario de login|LoginView|Interfaz de captura de credenciales|
|Validar credenciales|IniciarSesionController|`autenticar(usuario, contraseña)`|
|Verificar credenciales|UsuarioRepository|`validarCredenciales(usuario, contraseña)`|
|Crear sesión|Sesion|`crearSesion(usuario)`|
|Credenciales inválidas|LoginView|Muestra error y vuelve a solicitar|
|Credenciales válidas|:Sistema Disponible|Transición de estado|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada estándar**: Desde `:SESION_CERRADA`
- **Análisis MVC completo**: Vista, Control y Entidad claramente separados
- **Salidas múltiples**: Navegación a `:Sistema Disponible` con múltiples opciones

## características del análisis

### separación de responsabilidades MVC

- **Vista**: Solo presentación e interacción con usuario
- **Control**: Solo coordinación y lógica de aplicación  
- **Entidad**: Solo datos y reglas de negocio

### agnóstico tecnológicamente

- No especifica tecnología de interfaz de usuario
- No asume implementación específica de base de datos
- Mantiene independencia de frameworks

### trazabilidad completa

- **Origen**: Caso de uso detallado `iniciarSesion()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`UsuarioRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`LoginView`), lógica de aplicación (`IniciarSesionController`) y datos (`Usuario`, `UsuarioRepository`, `Sesion`).

### session pattern
`Sesion` mantiene el estado de autenticación del usuario activo en el sistema.