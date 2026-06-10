# importarConfiguracionGlobal > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/importarConfiguracionGlobal/importarConfiguracionGlobal.svg)|**Análisis**|[Diseño](/documents/diseño/importarConfiguracionGlobal/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `importarConfiguracionGlobal()` mediante diagrama de colaboración MVC, identificando clases de análisis y sus interacciones para importar la configuración global del sistema.

## diagrama de colaboración

<div align=center>

|![Análisis: importarConfiguracionGlobal()](/images/analisis/importarConfiguracionGlobal/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/importarConfiguracionGlobal/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (entidad)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **AlumnoRepository** | Abstrae el acceso a datos de alumnos para importación | Análisis puro |
| **GradoRepository** | Abstrae el acceso a datos de grados para importación | Análisis puro |
| **AsignaturaRepository** | Abstrae el acceso a datos de asignaturas para importación | Análisis puro |
| **PreguntaRepository** | Abstrae el acceso a datos de preguntas para importación | Análisis puro |
| **RespuestaRepository** | Abstrae el acceso a datos de respuestas para importación | Análisis puro |

### clases de vista (boundary)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **ImportarConfiguracionGlobalView** | Ventana de diálogo para importar configuración global | Wireframe del diálogo de importación |

### clases de control

| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **ConfiguracionController** | Control y coordinación del proceso de importación | importarConfiguracionGlobal() |

### colaboraciones incluidas

| Colaboración | Propósito | Invocación |
|-|-|-|
| **:Collaboration ImportarGrados** | Importar todos los grados del sistema | Automática al confirmar importación |
| **:Collaboration ImportarAsignaturas** | Importar todas las asignaturas del sistema | Automática al confirmar importación |
| **:Collaboration ImportarAlumnos** | Importar todos los alumnos del sistema | Automática al confirmar importación |
| **:Collaboration ImportarPreguntas** | Importar todas las preguntas del sistema | Automática al confirmar importación |
| **:Collaboration ImportarRespuestas** | Importar todas las respuestas del sistema | Automática al confirmar importación |

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `ImportarConfiguracionGlobalView.mostrarDialogoImportacion()`
2. **Delegación**: `ImportarConfiguracionGlobalView` → `ConfiguracionController.iniciarImportacion()`
3. **Selección**: Usuario introduce archivo de configuración global
4. **Validación**: `ConfiguracionController` valida el archivo (formato, estructura)
5. **Presentación**: `ImportarConfiguracionGlobalView` presenta mensaje de confirmación de importación global (todos los elementos: grados, asignaturas, alumnos, preguntas y respuestas) y opciones de confirmar/cancelar/salir
6. **Confirmación**: Usuario confirma la importación
7. **Importación secuencial**: `ConfiguracionController` → `AlumnoRepository`, `GradoRepository`, `AsignaturaRepository`,  `PreguntaRepository`, `RespuestaRepository` importa todos los datos en orden (para mantener integridad referencial)
8. **Resultado**: Si éxito → retornar al `:Sistema Disponible`; si error → mostrar mensaje de error y volver a opciones

### opciones de navegación disponibles

- **confirmarImportacion()** → `:Collaboration ImportarGrados`, `:Collaboration ImportarAsignaturas`, `:Collaboration ImportarAlumnos`, `:Collaboration ImportarPreguntas`, `:Collaboration ImportarRespuestas` (todas automáticamente)
- **cancelarImportacion()** → `:Sistema Disponible` (retorno sin importar)
- **salir()** → `:Sistema Disponible` (retorno sin importar)

## correspondencia con requisitos

### mapeado con especificación detallada

| Requisito del caso de uso | Clase responsable | Método/Colaboración |
|-|-|-|
| Mostrar diálogo de importación | `ImportarConfiguracionGlobalView` | `mostrarDialogoImportacion()` |
| Validar archivo de configuración | `ConfiguracionController` | `validarArchivoImportacion()` |
| Importar todos los elementos | `GradoRepository`, `AsignaturaRepository`, `AlumnoRepository`, `PreguntaRepository`, `RespuestaRepository` | `importarTodo()` |
| Gestionar errores | `ImportarConfiguracionGlobalController` | `manejarErrorImportacion()` |

## características del análisis

### separación de responsabilidades MVC

- **Vista**: Presentación del diálogo y captura de selección del usuario
- **Control**: Coordinación del flujo de importación y manejo de errores
- **Entidad**: Acceso a datos de grados, asignaturas, alumnos y preguntas

### caso de uso de utilidad

A diferencia de los casos de uso de gestión (CRUD), `importarConfiguracionGlobal()` es un caso de uso de proceso que:
- Permite importar datos al sistema en un formato unificado
- Incluye múltiples sub-operaciones de importación
- Mantiene el estado del sistema con modificaciones

### flujos alternativos identificados

| Flujo alternativo | Condición | Resultado |
|-|-|-|
| Cancelar importación | Usuario elige cancelar | Retorno al `:Sistema Disponible` sin acción |
| Error en importación | Fallo durante importación | Mostrar mensaje de error y volver a opciones |
| Salir sin importar | Usuario elige salir directamente | Retorno al `:Sistema Disponible` sin acción |

### restricción de importación global

La importación global **siempre incluye todos los elementos** del sistema (grados, asignaturas, alumnos, preguntas y respuestas) debido a las dependencias entre entidades:

- **Grados → Asignaturas**: Los grados son la base para las asignaturas
- **Grados → Alumnos**: Los alumnos están matriculados a un grado concreto
- **Asignaturas → Preguntas**: Las preguntas están vinculadas a asignaturas específicas
- **Preguntas → Respuestas**: Las respuestas están vinculadas a preguntas específicas

Esta restricción garantiza la **integridad referencial** de los datos importados, procesando en el orden correcto: grados → asignaturas → alumnos → preguntas → respuestas.

## estado previo

El caso de uso se ejecuta desde el estado `SISTEMA_DISPONIBLE`, accesible únicamente para el actor **Docente** (según diagrama de contexto).

## enlaces de dependencia

- **ImportarConfiguracionGlobalView** conoce a **ConfiguracionController** (delegación)
- **ConfiguracionController** conoce a **GradoRepository**, **AsignaturaRepository**, **AlumnoRepository**, **PreguntaRepository**, **RespuestaRepository** (operaciones de importación)

## patrones aplicados

### patrón MVC
- **Un controlador por caso de uso**: ConfiguracionController
- **Vista derivada de especificación**: ImportarConfiguracionGlobalView desde detallado
- **Modelo del dominio**: GradoRepository, AsignaturaRepository, AlumnoRepository, PreguntaRepository y RespuestaRepository con trazabilidad directa

### repository pattern
Cada repository abstrae el acceso a datos de su entidad, permitiendo diferentes formatos de importación sin acoplamiento directo.

### navigation pattern
Flechas discontinuas indican las colaboraciones incluidas que pueden ser invocadas según la selección del usuario.