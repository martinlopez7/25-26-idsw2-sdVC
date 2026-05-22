# cerrarSesion > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/cerrarSesion/cerrarSesion.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis
- **Versión**: 1.0
- **Fecha**: 2026-05-22
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `cerrarSesion()` mediante diagrama de colaboración MVC, identificando clases de análisis y sus interacciones conceptuales para realizar el caso de uso.

### rol metodológico del caso de uso

`cerrarSesion()` es el **caso de uso de finalización** del sistema, funcionando como:

- **Terminador de sesión**: Finaliza la sesión activa del usuario autenticado
- **Retorno al estado inicial**: Regresa el sistema al estado no autenticado
- **Transformador de actor**: Convierte Docente/Administrador en UsuarioNoRegistrado
- **Punto de control**: Permite confirmación antes de cerrar para evitar cierre accidental

**Invocación**: Este caso de uso es invocado desde `SISTEMA_DISPONIBLE`

**Resultado**: Termina la sesión activa y regresa al estado `SESION_CERRADA`, requiriendo nueva autenticación para acceder al sistema.

## diagrama de colaboración

<div align=center>

|![Análisis cerrarSesion()](/images/analisis/cerrarSesion/colaboracion.svg)|
|-|
|**Disciplina**: Análisis RUP<br>**Enfoque**: Diagramas de colaboración MVC|

</div>

## clases de análisis identificadas

### clases model (naranja #F2AC4E)
|Clase|Responsabilidad|Trazabilidad|
|-|-|-|
|**Sesion**|Entidad que mantiene estado de autenticación activa|Concepto del caso de uso|
|**Usuario**|Entidad del dominio que representa usuario del sistema|Modelo del dominio|
|**SesionRepository**|Concepto puro de gestión de sesiones activas|Análisis puro|

### clases view (azul #629EF9)
|Clase|Responsabilidad|Derivación|
|-|-|-|
|**CerrarSesionView**|Diálogo de confirmación de cierre de sesión|Especificación detallada|

### clases controller (verde #b5bd68)
|Clase|Responsabilidad|Caso de uso|
|-|-|-|
|**CerrarSesionController**|Control y coordinación completa del caso de uso|cerrarSesion()|

### colaboraciones (verde claro #CDEBA5)
|Colaboración|Propósito|Invocación|
|-|-|-|
|**:Sistema Disponible**|Origen del caso cuando se cancela cierre|Tras cancelación|
|**:Sesión Cerrada**|Destino del caso cuando se confirma el cierre|Tras cancelación|

## mensajes de colaboración

### flujo principal (cierre confirmado)
|Origen|Destino|Mensaje|Intención|
|-|-|-|-|
|**:Sistema Disponible**|**CerrarSesionView**|`cerrarSesion(Usuario)`|Invocación con usuario autenticado|
|**CerrarSesionView**|**CerrarSesionController**|`confirmarCierre(Usuario)`|Delegar proceso de cierre|
|**CerrarSesionController**|**SesionRepository**|`terminarSesion(Usuario)`|Finalizar sesión activa|
|**SesionRepository**|**Sesion**|`destruirSesion(Usuario)`|Destruir objeto de sesión|

### flujo alternativo (cierre cancelado)
|Origen|Destino|Mensaje|Intención|
|-|-|-|-|
|**CerrarSesionView**|**:Sistema Disponible**|`cancelarCierre()`|Retornar sin cerrar sesión|
|**CerrarSesionView**|**:SESION_CERRADA**|`confirmarCierre()`|Transición tras cierre confirmado|

## enlaces de dependencia
- **CerrarSesionView** conoce a **CerrarSesionController** (delegación)
- **CerrarSesionView** conoce a **:Sistema Disponible** (retorno en cancelación)
- **CerrarSesionView** conoce a **:SESION_CERRADA** (transición tras cierre)
- **CerrarSesionController** conoce a **SesionRepository** (gestión sesión)
- **SesionRepository** conoce a **Sesion** (destrucción)
- **Sesion** conoce a **Usuario** (asociación)

## trazabilidad con artefactos previos

### con especificación detallada
- **Estados internos** → **Clases de análisis**
- **SolicitandoCierre** → **CerrarSesionView.solicitarCierre()**
- **ConfirmandoCierre** → **CerrarSesionView.mostrarConfirmacion()**
- **Choice point** → **CerrarSesionController.procesarDecision()**
- **Transformación actor** → **Docente/Admin → UsuarioNoRegistrado**

### con modelo del dominio
- **Usuario** (entidad) → **Usuario** (clase de análisis)
- **Sesión** (concepto) → **Sesion** (clase de análisis)

## características del análisis

### responsabilidades identificadas
- **CerrarSesionView**: Capturar decisión del usuario y coordinar flujo de confirmación
- **CerrarSesionController**: Orquestar lógica completa del caso de uso
- **SesionRepository**: Proveer acceso conceptual a gestión de sesiones
- **Sesion**: Representar estado de autenticación activa
- **Usuario**: Mantener información del usuario autenticado

### relaciones conceptuales
- **Delegación**: Vista delega lógica de negocio al controlador
- **Gestión**: Controlador gestiona el ciclo de vida de la sesión a través del repositorio
- **Destrucción**: SesionRepository coordina la destrucción segura de la sesión
- **Decisión**: Vista maneja la navegación directa a estados

## naturaleza del flujo de control

### bifurcación de flujos
- **Confirmación**: Flujo hacia destrucción de sesión y cambio de estado
- **Cancelación**: Flujo de retorno a estado actual sin cambios
- **Análisis conceptual**: Se enfoca en responsabilidades de decisión y gestión de estado

### gestión de estado
- **Preservación**: En caso de cancelación, mantiene estado actual
- **Destrucción**: En caso de confirmación, elimina estado de sesión
- **Transición**: Coordina cambio de actor y estado del sistema

**Código fuente:** [colaboracion.puml](/modelosUML/analisis/cerrarSesion/colaboracion.puml)