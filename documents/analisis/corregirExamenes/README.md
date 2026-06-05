# Corregir Exámenes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/corregirExamenes/corregirExamenes.svg)|**Análisis**|[Diseño](/documents/diseño/corregirExamenes/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `corregirExamenes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar el proceso de corrección de exámenes realizados por alumnos.

### rol metodológico del caso de uso

`corregirExamenes()` es el **caso de uso de corrección** del proceso de exámenes, funcionando como:

- **Receptor de exámenes realizados**: El docente introduce los exámenes ya realizados por los alumnos
- **Comparador con clave**: El sistema verifica las respuestas del alumno contra la clave de corrección
- **Calculador de resultados**: Genera las puntuaciones y resultados de cada examen corregido

**Entrada**: Desde `SISTEMA_DISPONIBLE` (estado EXAMENES_CORREGIDOS).

**Resultado**: Transfiere a `EXAMENES_CORREGIDOS` tras corrección exitosa.

## diagrama de colaboración

<div align=center>

|![Análisis: corregirExamenes()](/images/analisis/corregirExamenes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/corregirExamenes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (naranja #F2AC4E)
| Clase | Responsabilidad | Trazabilidad |
|-|-|-|
| **ExamenRepository** | Concepto puro de gestión de exámenes corregidos y sus resultados | Análisis puro |
| **Examen** | Entidad que representa un examen asignado a un alumno | Modelo del dominio |
| **Pregunta** | Entidad que representa las preguntas del examen | Modelo del dominio |
| **Respuesta** | Entidad que representa las respuestas del alumno | Modelo del dominio |

### clases view (azul #629EF9)
| Clase | Responsabilidad | Derivación |
|-|-|-|
| **CorregirExamenesView** | Formulario de corrección de exámenes realizados | Especificación detallada |

### clases controller (verde #b5bd68)
| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **ExamenesController** | Control y coordinación completa del caso de uso | corregirExamenes() |

### colaboraciones (verde claro #CDEBA5)
| Colaboración | Propósito | Invocación |
|-|-|-|
| **:Sistema Disponible** | Origen del caso de uso | Entrada desde menú |
| **:EXAMENES_CORREGIDOS** | Destino tras corrección exitosa | Retorno tras confirmar |

## mensajes de colaboración

### flujo principal (corrección exitosa)
| Origen | Destino | Mensaje | Intención |
|-|-|-|-|
| **:Sistema Disponible** | **CorregirExamenesView** | `corregirExamenes()` | Invocación del caso de uso |
| **CorregirExamenesView** | **ExamenesController** | `obtenerExamenesPendientes()` | Recuperar exámenes asignados sin corregir |
| **ExamenesController** | **ExamenRepository** | `obtenerExamenesSinCorregir()` | Obtener lista de exámenes pendientes |
| **ExamenRepository** | **Examen** | `getExamenes()` | Acceso a datos del examen |
| **CorregirExamenesView** | **ExamenesController** | `introducirExamenesRealizados(examenes, respuestasAlumno)` | Proporcionar exámenes realizados |
| **ExamenesController** | **ExamenRepository** | `obtenerClaveCorreccion(examenId)` | Recuperar clave de corrección |
| **ExamenRepository** | **Respuesta** | `getClave()` | Obtener respuesta correcta |
| **ExamenesController** | **ExamenesController** | `compararRespuestas(respuestasAlumno, clave)` | Comparar con clave |
| **ExamenesController** | **ExamenRepository** | `guardarResultados(examenId, resultados)` | Persistir resultados corregidos |
| **ExamenRepository** | **Examen** | `setResultados(resultados)` | Asignar resultados al examen |
| **CorregirExamenesView** | **:EXAMENES_CORREGIDOS** | `completarGestion()` | Retorno tras corrección exitosa |

### flujo alternativo (cancelar corrección)
| Origen | Destino | Mensaje | Intención |
|-|-|-|-|
| **CorregirExamenesView** | **ExamenesController** | `cancelarCorreccion()` | Docente cancela la corrección |
| **:Sistema Disponible** | **CorregirExamenesView** | `rechazarCancelacion()` | Permanecer en estado actual |

## enlaces de dependencia
- **CorregirExamenesView** conoce a **ExamenesController** (delegación)
- **CorregirExamenesView** conoce a **:EXAMENES_CORREGIDOS** (retorno tras corrección)
- **ExamenesController** conoce a **ExamenRepository** (gestión de exámenes y corrección)
- **ExamenRepository** conoce a **Examen** (gestión de entidad)
- **ExamenRepository** conoce a **Pregunta** (acceso a preguntas del examen)
- **ExamenRepository** conoce a **Respuesta** (acceso a respuestas y clave de corrección)

## trazabilidad con artefactos previos

### con especificación detallada
- **Estados internos** → **Clases de análisis**
- **RequiringCorrection** → **CorregirExamenesView.solicitarCorreccion()**
- **ProvidingDoneExams** → **CorregirExamenesView.mostrarFormularioExamenes()**
- **ProvidingConfirmation** → **CorregirExamenesView.mostrarConfirmacion()**

### con diagrama de contexto
- **SISTEMA_DISPONIBLE** → Entrada al caso de uso (desde menú)
- **EXAMENES_CORREGIDOS** → Destino tras corrección exitosa

### con modelo del dominio
- **Examen** (entidad) → **Examen** (clase de análisis)
- **Pregunta** (entidad) → **Pregunta** (clase de análisis)
- **Respuesta** (entidad) → **Respuesta** (clase de análisis)
- **Relación Examen → Pregunta** → Preguntas del examen a corregir
- **Relación Pregunta → Respuesta** → Clave de corrección y respuestas del alumno

## características del análisis

### responsabilidades identificadas
- **CorregirExamenesView**: Presentar exámenes pendientes, capturar exámenes realizados y confirmar corrección
- **ExamenesController**: Obtener exámenes pendientes, comparar respuestas con clave, guardar resultados
- **ExamenRepository**: Proveer acceso a exámenes sin corregir, almacenar claves de corrección, persistir resultados
- **Examen**: Representar examen con datos del alumno y resultados de corrección
- **Pregunta**: Representar preguntas del examen con su clave de corrección
- **Respuesta**: Representar respuestas del alumno y la clave correcta

### relaciones conceptuales
- **Delegación**: Vista delega lógica de negocio al controlador
- **Carga de datos**: Controlador obtiene exámenes pendientes desde repositorio
- **Comparación**: Repositorio proporciona clave de corrección para comparar
- **Navegación**: Vista maneja navegación directa a estado de destino

## naturaleza del flujo de control

### flujo único de corrección
- **Introducción**: El docente proporciona los exámenes realizados
- **Comparación**: El sistema compara con la clave de corrección
- **Resultado**: Se calculan y guardan los resultados de cada examen

### gestión de estado
- **Corrección exitosa**: Los exámenes se corrigen y se completan
- **Cancelación**: El docente puede cancelar y no se persisten cambios

## patrones arquitectónicos aplicados

### patrón MVC para corrección de exámenes
- **Model**: `Examen` + `Pregunta` + `Respuesta` + `ExamenRepository`
- **View**: `CorregirExamenesView` (formulario de corrección e interacción)
- **Controller**: `ExamenesController` (coordinación y comparación)

### patrón de corrección con clave
- **Clave de corrección**: Cada examen tiene una clave generada en creación
- **Comparación automática**: El sistema compara respuestas con clave
- **Resultados calculados**: Puntuación y resultado calculado automáticamente

**Código fuente:** [colaboracion.puml](/modelosUML/analisis/corregirExamenes/colaboracion.puml)