# Asignar Exámenes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/asignarExamenes/asignarExamenes.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `asignarExamenes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar el proceso de asignación de exámenes generados a alumnos destinatarios.

### rol metodológico del caso de uso

`asignarExamenes()` es el **caso de uso de asignación** del proceso de generación de exámenes, funcionando como:

- **Asignador de exámenes**: Asocia exámenes generados a alumnos destinatarios por grado
- **Configurador de destinatarios**: Permite seleccionar qué alumnos reciben qué exámenes
- **Punto de bifurcación**: Presenta tres flujos: confirmar asignación, cancelar asignación, o error en asignación

**Invocación**: Este caso de uso es invocado automáticamente desde `generarExamenes()` mediante <<include>> tras generación exitosa.

**Entrada**: 
1. **`EXAMENES_GENERADOS`** - Estado desde generarExamenes() global
2. **`EXAMENES_GENERADOS_CONTEXTUALES`** - Estado desde generarExamenes() contextual

**Resultado**: 
- **Asignación exitosa**: Transfiere a `EXAMENES_ASIGNADOS` o `EXAMENES_ASIGNADOS_CONTEXTUALES`
- **Cancelación**: Retorna al estado de generación con exámenes sin asignar
- **Error en asignación**: Permite reintentar introducción de destinatarios

## diagrama de colaboración

<div align=center>

|![Análisis: asignarExamenes()](/images/analisis/asignarExamenes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/asignarExamenes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (naranja #F2AC4E)
| Clase | Responsabilidad | Trazabilidad |
|-|-|-|
| **ExamenRepository** | Concepto puro de gestión de exámenes generados y su asignación a alumnos | Análisis puro |
| **Examen** | Entidad que representa un examen generado pendiente de asignación | Modelo del dominio |
| **Alumno** | Entidad que representa al alumno destinatario del examen | Modelo del dominio |
| **Grado** | Entidad que representa el grado para agrupar alumnos destinatarios | Modelo del dominio |

### clases view (azul #629EF9)
| Clase | Responsabilidad | Derivación |
|-|-|-|
| **AsignarExamenesView** | Formulario de asignación de exámenes a alumnos por grado | Especificación detallada |

### clases controller (verde #b5bd68)
| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **ExamenesController** | Control y coordinación completa del caso de uso | asignarExamenes() |

### colaboraciones (verde claro #CDEBA5)
| Colaboración | Propósito | Invocación |
|-|-|-|
| **:EXAMENES_GENERADOS** | Origen del caso cuando se asigna desde estado global | Entrada |
| **:EXAMENES_GENERADOS_CONTEXTUALES** | Origen del caso cuando se asigna desde estado contextual | Entrada |
| **:EXAMENES_ASIGNADOS** | Destino cuando docente confirma asignación global | Tras confirmación global |
| **:EXAMENES_ASIGNADOS_CONTEXTUALES** | Destino cuando docente confirma asignación contextual | Tras confirmación contextual |

## mensajes de colaboración

### flujo principal (asignación confirmada)
| Origen | Destino | Mensaje | Intención |
|-|-|-|-|
| **:EXAMENES_GENERADOS** | **AsignarExamenesView** | `asignarExamenes(examenesGenerados)` | Invocación con exámenes generados |
| **:EXAMENES_GENERADOS_CONTEXTUALES** | **AsignarExamenesView** | `asignarExamenes(examenesGenerados)` | Invocación con exámenes generados |
| **AsignarExamenesView** | **ExamenesController** | `obtenerAlumnosPorGrado(asignaturaId)` | Obtener alumnos matriculados por grado |
| **ExamenesController** | **ExamenRepository** | `obtenerAlumnosMatriculados(asignaturaId)` | Recuperar alumnos por grado |
| **ExamenRepository** | **Alumno** | `getAlumnos()` | Obtener lista de alumnos |
| **AsignarExamenesView** | **ExamenesController** | `asignarExamenes(examenes, asignaciones)` | Delegar proceso de asignación |
| **ExamenesController** | **ExamenRepository** | `asignarAlumnos(examenes, asignaciones)` | Persistir asignaciones |
| **ExamenRepository** | **Examen** | `setAlumno(alumno)` | Asignar alumno a examen |
| **AsignarExamenesView** | **:EXAMENES_ASIGNADOS** | `completarGestion()` | Retorno tras confirmación global |
| **AsignarExamenesView** | **:EXAMENES_ASIGNADOS_CONTEXTUALES** | `completarGestion()` | Retorno tras confirmación contextual |

### flujo alternativo (cancelación)
| Origen | Destino | Mensaje | Intención |
|-|-|-|-|
| **AsignarExamenesView** | **:EXAMENES_GENERADOS** | `cancelarAsignacion()` | Retornar sin asignar |
| **AsignarExamenesView** | **:EXAMENES_GENERADOS_CONTEXTUALES** | `cancelarAsignacion()` | Retornar sin asignar |

### flujo alternativo (error en asignación)
| Origen | Destino | Mensaje | Intención |
|-|-|-|-|
| **AsignarExamenesView** | **AsignarExamenesView** | `reintentarAsignacion()` | Regresar a formulario para reintentar |

## enlaces de dependencia
- **AsignarExamenesView** conoce a **ExamenesController** (delegación)
- **AsignarExamenesView** conoce a **:EXAMENES_ASIGNADOS** (retorno tras confirmación global)
- **AsignarExamenesView** conoce a **:EXAMENES_ASIGNADOS_CONTEXTUALES** (retorno tras confirmación contextual)
- **AsignarExamenesView** conoce a **:EXAMENES_GENERADOS** (retorno tras cancelación)
- **AsignarExamenesView** conoce a **:EXAMENES_GENERADOS_CONTEXTUALES** (retorno tras cancelación)
- **ExamenesController** conoce a **ExamenRepository** (gestión de exámenes y asignaciones)
- **ExamenRepository** conoce a **Examen** (gestión de entidad)
- **ExamenRepository** conoce a **Alumno** (acceso a datos de alumnos)
- **ExamenRepository** conoce a **Grado** (agrupación por grado)

## trazabilidad con artefactos previos

### con especificación detallada
- **Estados internos** → **Clases de análisis**
- **RequiringAssignment** → **AsignarExamenesView.solicitarAsignacion()**
- **ProvidingAssignment** → **AsignarExamenesView.mostrarFormularioAsignacion()**
- **ProvidingConfirmation** → **AsignarExamenesView.mostrarConfirmacion()**
- **Choice point (c)** → **ExamenesController.procesarDecision()**

### con diagrama de contexto
- **EXAMENES_GENERADOS** → Entrada al caso de uso (origen global)
- **EXAMENES_GENERADOS_CONTEXTUALES** → Entrada al caso de uso (origen contextual)
- **EXAMENES_ASIGNADOS** → Destino tras confirmación global
- **EXAMENES_ASIGNADOS_CONTEXTUALES** → Destino tras confirmación contextual

### con modelo del dominio
- **Examen** (entidad) → **Examen** (clase de análisis)
- **Alumno** (entidad) → **Alumno** (clase de análisis)
- **Grado** (entidad) → **Grado** (clase de análisis)
- **Relación Examen → Alumno** → Asignación de alumno a examen
- **Relación Alumno → Grado** → Agrupación por grado para selección de destinatarios

## características del análisis

### responsabilidades identificadas
- **AsignarExamenesView**: Capturar selección de alumnos destinatarios por grado y mostrar confirmación
- **ExamenesController**: Orquestar carga de alumnos por grado y persistencia de asignaciones
- **ExamenRepository**: Proveer acceso a alumnos matriculados y gestionar asignaciones examen-alumno
- **Examen**: Representar exámenes generados pendientes de asignación a alumnos
- **Alumno**: Representar alumnos destinatarios de exámenes
- **Grado**: Representar grado para filtrar y agrupar alumnos destinatarios

### relaciones conceptuales
- **Delegación**: Vista delega lógica de negocio al controlador
- **Carga de datos**: Controlador obtiene alumnos por grado desde repositorio
- **Asignación**: Repositorio coordina la relación examen-alumno
- **Navegación**: Vista maneja la navegación directa a estados de destino

## naturaleza del flujo de control

### bifurcación de flujos
- **Confirmación**: Flujo hacia asignación de alumnos y cambio de estado (EXAMENES_ASIGNADOS o EXAMENES_ASIGNADOS_CONTEXTUALES)
- **Cancelación**: Flujo de retorno al estado de generación sin asignar (EXAMENES_GENERADOS o EXAMENES_GENERADOS_CONTEXTUALES)
- **Reintento**: Flujo de retour al formulario para corregir asignaciones

### gestión de estado
- **Asignación**: En caso de confirmación, asocia exámenes a alumnos destinatarios
- **Preservación**: En caso de cancelación, mantiene exámenes sin asignar
- **Transición**: Coordina cambio de estado según origen y decisión del docente

## patrones arquitectónicos aplicados

### patrón MVC para asignación de exámenes
- **Model**: `Examen` + `Alumno` + `Grado` + `ExamenRepository`
- **View**: `AsignarExamenesView` (formulario de asignación e interacción)
- **Controller**: `ExamenesController` (coordinación y validación)

### patrón de asignación por grado
- **Agrupación por grado**: Los alumnos se muestran y seleccionan por grado
- **Configuración por grado**: Cada grado puede tener diferentes alumnos destinatarios
- **Asignación individual**: Cada examen se asigna a un alumno específico

**Código fuente:** [colaboracion.puml](/modelosUML/analisis/asignarExamenes/colaboracion.puml)