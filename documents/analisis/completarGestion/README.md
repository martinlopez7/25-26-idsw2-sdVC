# completarGestion > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/completarGestion/completarGestion.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-23
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `completarGestion()` mediante diagrama de colaboración MVC, identificando clases de análisis y sus interacciones. Este caso de uso funciona como **hub central de navegación** para ambos actores del sistema.

### rol metodológico del caso de uso

`completarGestion()` es el **caso de uso de convergencia** del sistema, funcionando como:

- **Hub central de navegación**: Punto de retorno común desde todas las funcionalidades específicas
- **Coordinador de capacidades**: Presenta al usuario todas las opciones disponibles según sus permisos
- **Mantenedor de contexto**: Preserva la sesión activa mientras permite acceso a cualquier funcionalidad

**Invocación**: Este caso de uso es invocado por:
1. **`iniciarSesion()`** - Al establecer sesión exitosa inicialmente
2. **Todos los casos de uso de gestión** - Al completar operaciones CRUD (`verGrados()`, `verAsignaturas()`, `verAlumnos()`, etc.)
3. **Casos de uso de examen** - Al finalizar procesos específicos (`generarExamenes()`, `asignarExamenes()`, `corregirExamenes()`)

**Resultado**: Mantiene al sistema en estado `SISTEMA_DISPONIBLE`, permitiendo solicitar cualquier funcionalidad del diagrama de contexto.

## diagrama de colaboración

<div align=center>

|![Análisis: completarGestion()](/images/analisis/completarGestion/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/completarGestion/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (entidad)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **Sesion** | Entidad que mantiene estado de autenticación activa | Concepto del caso de uso |
| **OpcionesMenu** | Entidad que representa opciones disponibles para el usuario | Análisis puro |
| **PermisosRepository** | Concepto puro de acceso a permisos y opciones de usuario | Análisis puro |

### clases de vista (boundary)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **CompletarGestionView** | Ventana principal de navegación del sistema | Wireframe del menú principal |

### clases de control

| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **CompletarGestionController** | Control y coordinación completa del caso de uso | completarGestion() |

### colaboraciones disponibles

#### Para Actor Docente

| Colaboración | Propósito | Invocación |
|-|-|-|
| **:Collaboration VerGrados** | Permitir solicitar gestión de grados | Según selección |
| **:Collaboration VerAsignaturas** | Permitir solicitar gestión de asignaturas | Según selección |
| **:Collaboration VerAlumnos** | Permitir solicitar gestión de alumnos | Según selección |
| **:Collaboration VerPreguntas** | Permitir solicitar gestión de preguntas | Según selección |
| **:Collaboration ImportarConfiguracionGlobal** | Permitir importar configuración global | Según selección |
| **:Collaboration ExportarConfiguracionGlobal** | Permitir exportar configuración global | Según selección |
| **:Collaboration GenerarExamenes** | Permitir generar exámenes | Según selección |
| **:Collaboration CorregirExamenes** | Permitir corregir exámenes | Según selección |
| **:Collaboration CerrarSesion** | Permitir solicitar cierre de sesión | Según selección |

#### Para Actor Administrador Institucional

| Colaboración | Propósito | Invocación |
|-|-|-|
| **:Collaboration VerDocentes** | Permitir solicitar gestión de docentes | Según selección |
| **:Collaboration CerrarSesion** | Permitir solicitar cierre de sesión | Según selección |

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `CompletarGestionView.mostrarMenu()`
2. **Delegación**: `CompletarGestionView` → `CompletarGestionController.habilitarOpciones(usuario)`
3. **Obtención de sesión**: `CompletarGestionController` → `Sesion.getUsuario()`
4. **Obtención de opciones**: `CompletarGestionController` → `PermisosRepository.obtenerOpciones(usuario)`
5. **Creación de estructura**: `CompletarGestionController` → `OpcionesMenu.crearOpciones(opciones)`
6. **Acceso a opciones**: `CompletarGestionView` → `OpcionesMenu.getOpciones()`
7. **Navegación**: `CompletarGestionView` → `:Collaboration [Según selección del actor]`

### opciones de navegación disponibles

#### Para Actor Docente

- **verGrados()** → `:Collaboration VerGrados`
- **verAsignaturas()** → `:Collaboration VerAsignaturas`
- **verAlumnos()** → `:Collaboration VerAlumnos`
- **verPreguntas()** → `:Collaboration VerPreguntas`
- **generarExamenes()** → `:Collaboration GenerarExamenes`
- **corregirExamenes()** → `:Collaboration CorregirExamenes`
- **importarConfiguracionGlobal()** → `:Collaboration ImportarConfiguracionGlobal`
- **exportarConfiguracionGlobal()** → `:Collaboration ExportarConfiguracionGlobal`
- **cerrarSesion()** → `:Collaboration CerrarSesion`

#### Para Actor Administrador Institucional

- **verDocentes()** → `:Collaboration VerDocentes`
- **cerrarSesion()** → `:Collaboration CerrarSesion`

## correspondencia con requisitos

### mapeado con especificación detallada

| Requisito del caso de uso | Clase responsable | Método/Colaboración |
|-|-|-|
| Presentar menú de opciones | `CompletarGestionView` | Coordina con `CompletarGestionController` |
| Mantener sesión activa | `Sesion` | `getUsuario()` |
| Obtener opciones según permisos | `PermisosRepository` | `obtenerOpciones(usuario)` |
| Estructurar opciones del menú | `OpcionesMenu` | `crearOpciones()`, `getOpciones()` |
| Retornar al sistema disponible | `CompletarGestionController` | Gestión de navegación |

## características del análisis

### separación de responsabilidades MVC

- **Vista**: Solo presentación e interacción con usuario (menú de opciones)
- **Control**: Solo coordinación y lógica de aplicación
- **Entidad**: Solo datos (sesión, permisos, opciones)

### multi-actor

A diferencia de otros casos de uso, `completarGestion()` es compartido por ambos actores del sistema:
- **Docente**: Acceso a funcionalidades de gestión académica y exámenes
- **Administrador Institucional**: Acceso a gestión de docentes

### agnóstico tecnológicamente

- No especifica tecnología de interfaz de usuario
- No asume implementación específica de base de datos
- Mantiene independencia de frameworks

### centralidad del caso de uso

- **Punto de convergencia**: Todos los casos de uso regresan al estado `SISTEMA_DISPONIBLE` vía `completarGestion()`
- **Punto de divergencia**: Desde aquí se accede a todas las funcionalidades del sistema
- **Multiplicidad de colaboraciones**: Conecta con todos los casos de uso según el actor autenticado

## enlaces de dependencia

- **CompletarGestionView** conoce a **CompletarGestionController** (delegación)
- **CompletarGestionView** conoce a **OpcionesMenu** (acceso a opciones)
- **CompletarGestionView** conoce a **Sesion** (acceso a estado)
- **CompletarGestionView** conoce a **todas las colaboraciones** (invocación según selección)
- **CompletarGestionController** conoce a **PermisosRepository** (obtención opciones)
- **CompletarGestionController** conoce a **OpcionesMenu** (creación estructura)
- **CompletarGestionController** conoce a **Sesion** (acceso a usuario)
- **PermisosRepository** conoce a **OpcionesMenu** (gestión opciones)

## patrones aplicados

### patrón mvc
- **Un controlador por caso de uso**: CompletarGestionController
- **Vista derivada de prototipo**: CompletarGestionView desde wireframe del menú
- **Modelo del dominio**: OpcionesMenu con trazabilidad directa

### repository pattern
`PermisosRepository` abstrae el acceso a permisos, permitiendo diferentes implementaciones sin afectar el controlador.

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo según el actor autenticado.