# exportarConfiguracionGlobal > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/exportarConfiguracionGlobal/exportarConfiguracionGlobal.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `exportarConfiguracionGlobal()` mediante diagrama de colaboración MVC, identificando clases de análisis y sus interacciones para exportar la configuración global del sistema.

## diagrama de colaboración

<div align=center>

|![Análisis: exportarConfiguracionGlobal()](/images/analisis/exportarConfiguracionGlobal/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/exportarConfiguracionGlobal/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (entidad)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **AlumnoRepository** | Abstrae el acceso a datos de alumnos para exportación | Análisis puro |
| **GradoRepository** | Abstrae el acceso a datos de grados para exportación | Análisis puro |
| **AsignaturaRepository** | Abstrae el acceso a datos de asignaturas para exportación | Análisis puro |
| **PreguntaRepository** | Abstrae el acceso a datos de preguntas para exportación | Análisis puro |

### clases de vista (boundary)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **ExportarConfiguracionGlobalView** | Ventana de diálogo para exportar configuración global | Wireframe del diálogo de exportación |

### clases de control

| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **ExportarConfiguracionGlobalController** | Control y coordinación del proceso de exportación | exportarConfiguracionGlobal() |

### colaboraciones incluidas

| Colaboración | Propósito | Invocación |
|-|-|-|
| **:Collaboration ExportarAlumnos** | Exportar todos los alumnos del sistema | Automática al confirmar exportación |
| **:Collaboration ExportarGrados** | Exportar todos los grados del sistema | Automática al confirmar exportación |
| **:Collaboration ExportarAsignaturas** | Exportar todas las asignaturas del sistema | Automática al confirmar exportación |
| **:Collaboration ExportarPreguntas** | Exportar todas las preguntas del sistema | Automática al confirmar exportación |

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `ExportarConfiguracionGlobalView.mostrarDialogoExportacion()`
2. **Delegación**: `ExportarConfiguracionGlobalView` → `ExportarConfiguracionGlobalController.iniciarExportacion()`
3. **Información**: `ExportarConfiguracionGlobalController` → `AlumnoRepository.obtenerAlumnos()`, `GradoRepository.obtenerGrados()`, `AsignaturaRepository.obtenerAsignaturas()`, `PreguntaRepository.obtenerPreguntas()` para obtener datos a exportar
4. **Presentación**: `ExportarConfiguracionGlobalView` presenta mensaje de confirmación de exportación global (todos los elementos: alumnos, grados, asignaturas, preguntas) y opciones de confirmar/cancelar/salir
5. **Confirmación**: Usuario confirma la exportación
6. **Exportación completa**: `ExportarConfiguracionGlobalController` → `AlumnoRepository`, `GradoRepository`, `AsignaturaRepository`, `PreguntaRepository` exporta todos los datos
7. **Resultado**: Si éxito → retornar al `:Sistema Disponible`; si error → mostrar mensaje de error y volver a opciones

### opciones de navegación disponibles

- **confirmarExportacion()** → `:Collaboration ExportarAlumnos`, `:Collaboration ExportarGrados`, `:Collaboration ExportarAsignaturas`, `:Collaboration ExportarPreguntas` (todas automáticamente)
- **cancelarExportacion()** → `:Sistema Disponible` (retorno sin exportar)
- **salir()** → `:Sistema Disponible` (retorno sin exportar)

## correspondencia con requisitos

### mapeado con especificación detallada

| Requisito del caso de uso | Clase responsable | Método/Colaboración |
|-|-|-|
| Mostrar diálogo de exportación | `ExportarConfiguracionGlobalView` | `mostrarDialogoExportacion()` |
| Obtener todos los datos | `ExportarConfiguracionGlobalController` | `obtenerDatosExportacion()` |
| Exportar todos los elementos | `AlumnoRepository`, `GradoRepository`, `AsignaturaRepository`, `PreguntaRepository` | `exportarTodo()` |
| Gestionar errores | `ExportarConfiguracionGlobalController` | `manejarErrorExportacion()` |

## características del análisis

### separación de responsabilidades MVC

- **Vista**: Presentación del diálogo y captura de selección del usuario
- **Control**: Coordinación del flujo de exportación y manejo de errores
- **Entidad**: Acceso a datos de alumnos, grados, asignaturas y preguntas

### caso de uso de utilidad

A diferencia de los casos de uso de gestión (CRUD), `exportarConfiguracionGlobal()` es un caso de uso de proceso que:
- Permite exportar datos del sistema en un formato unificado
- Incluye múltiples sub-operaciones de exportación
- Mantiene el estado del sistema sin modificaciones

### flujos alternativos identificados

| Flujo alternativo | Condición | Resultado |
|-|-|-|
| Cancelar exportación | Usuario elige cancelar | Retorno al `:Sistema Disponible` sin acción |
| Error en exportación | Fallo durante exportación | Mostrar mensaje de error y volver a opciones |
| Salir sin exportar | Usuario elige salir directamente | Retorno al `:Sistema Disponible` sin acción |

### restricción de exportación global

La exportación global **siempre incluye todos los elementos** del sistema (alumnos, grados, asignaturas, preguntas) debido a las dependencias entre entidades:

- **Preguntas → Asignaturas**: Las preguntas están vinculadas a asignaturas específicas, carecen de sentido sin ellas
- **Asignaturas → Grados**: Las asignaturas pertenecen a grados, perderían contexto sin ellos
- **Alumnos → Asignaturas**: Los alumnos están matriculados en asignaturas, necesitan ese contexto
- **Alumnos → Grado**: Los alumnos están matriculados a un grado concreto, necesitan ese contexto

Esta restricción garantiza la **integridad referencial** de los datos exportados, evitando inconsistencias en la configuración importada en otro sistema.

## estado previo

El caso de uso se ejecuta desde el estado `SISTEMA_DISPONIBLE`, accesible únicamente para el actor **Docente** (según diagrama de contexto).

## enlaces de dependencia

- **ExportarConfiguracionGlobalView** conoce a **ExportarConfiguracionGlobalController** (delegación)
- **ExportarConfiguracionGlobalController** conoce a **AlumnoRepository**, **GradoRepository**, **AsignaturaRepository**, **PreguntaRepository** (operaciones de exportación)
- **ExportarConfiguracionGlobalView** conoce a **AlumnoRepository**, **GradoRepository**, **AsignaturaRepository**, **PreguntaRepository** (para mostrar opciones)

## patrones aplicados

### patrón MVC
- **Un controlador por caso de uso**: ExportarConfiguracionGlobalController
- **Vista derivada de especificación**: ExportarConfiguracionGlobalView desde detallado
- **Modelo del dominio**: AlumnoRepository, GradoRepository, AsignaturaRepository, PreguntaRepository con trazabilidad directa

### repository pattern
Cada repository abstrae el acceso a datos de su entidad, permitiendo diferentes formatos de exportación sin acoplamiento directo.

### navigation pattern
Flechas discontinuas indican las colaboraciones incluidas que pueden ser invocadas según la selección del usuario.