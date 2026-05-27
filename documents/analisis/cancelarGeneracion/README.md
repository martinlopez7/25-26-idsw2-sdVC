# Cancelar Generación > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/cancelarGeneracion/cancelarGeneracion.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `cancelarGeneracion()` mediante diagrama de colaboración MVC, identificando clases de análisis y sus interacciones conceptuales para realizar el caso de uso.

### rol metodológico del caso de uso

`cancelarGeneracion()` es el **caso de uso de cancelación** del proceso de generación de exámenes, funcionando como:

- **Cancelador de proceso**: Anula la generación de exámenes no asignados
- **Liberador de estado**: Libera los exámenes generados que aún no han sido asignados
- **Punto de bifurcación**: Presenta dos flujos: confirmación de cancelación o rechazo

**Invocación**: Este caso de uso es invocado desde:
1. **`EXAMENES_GENERADOS`** - Estado desde generarExamenes() global
2. **`EXAMENES_GENERADOS_CONTEXTUALES`** - Estado desde generarExamenes() contextual

**Resultado**: 
- **Confirmado**: Elimina los exámenes generados y retorna al estado anterior
- **No confirmado**: Mantiene los exámenes generados y permanece en el estado actual

## diagrama de colaboración

<div align=center>

|![Análisis cancelarGeneracion()](/images/analisis/cancelarGeneracion/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/cancelarGeneracion/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases model (naranja #F2AC4E)
|Clase|Responsabilidad|Trazabilidad|
|-|-|-|
|**ExamenRepository**|Concepto puro de gestión de exámenes generados|Análisis puro|
|**Examen**|Entidad que representa un examen generado|Modelo del dominio|

### clases view (azul #629EF9)
|Clase|Responsabilidad|Derivación|
|-|-|-|
|**CancelarGeneracionView**|Diálogo de confirmación de cancelación de exámenes|Especificación detallada|

### clases controller (verde #b5bd68)
|Clase|Responsabilidad|Caso de uso|
|-|-|-|
|**ExamenesController**|Control y coordinación completa del caso de uso|cancelarGeneracion()|

### colaboraciones (verde claro #CDEBA5)
|Colaboración|Propósito|Invocación|
|-|-|-|
|**:EXAMENES_GENERADOS**|Origen del caso cuando se cancela desde estado global|Tras rechazo|
|**:EXAMENES_GENERADOS_CONTEXTUALES**|Origen del caso cuando se cancela desde estado contextual|Tras rechazo|
|**:SISTEMA_DISPONIBLE**|Destino cuando docente confirma desde estado global|Tras confirmación|
|**:ASIGNATURA_ABIERTO**|Destino cuando docente confirma desde estado contextual|Tras confirmación|

## mensajes de colaboración

### flujo principal (cancelación confirmada)
|Origen|Destino|Mensaje|Intención|
|-|-|-|-|
|**:EXAMENES_GENERADOS**|**CancelarGeneracionView**|`cancelarGeneracion(examenes)`|Invocación con datos del examen|
|**:EXAMENES_GENERADOS_CONTEXTUALES**|**CancelarGeneracionView**|`cancelarGeneracion(examenes)`|Invocación con datos del examen|
|**CancelarGeneracionView**|**ExamenesController**|`confirmarCancelacion(examenes)`|Delegar proceso de cancelación|
|**ExamenesController**|**ExamenRepository**|`eliminarExamenes(examenes)`|Eliminar exámenes no asignados|
|**ExamenRepository**|**Examen**|`getExamenes()`|Recuperar exámenes generados|
|**CancelarGeneracionView**|**:SISTEMA_DISPONIBLE**|`confirmarCancelacion()`|Retorno tras confirmación global|
|**CancelarGeneracionView**|**:ASIGNATURA_ABIERTO**|`confirmarCancelacion()`|Retorno tras confirmación contextual|

### flujo alternativo (cancelación rechazada)
|Origen|Destino|Mensaje|Intención|
|-|-|-|-|
|**CancelarGeneracionView**|**:EXAMENES_GENERADOS**|`rechazarCancelacion()`|Retornar sin cancelar|
|**CancelarGeneracionView**|**:EXAMENES_GENERADOS_CONTEXTUALES**|`rechazarCancelacion()`|Retornar sin cancelar|

## enlaces de dependencia
- **CancelarGeneracionView** conoce a **ExamenesController** (delegación)
- **CancelarGeneracionView** conoce a **:SISTEMA_DISPONIBLE** (retorno tras confirmación global)
- **CancelarGeneracionView** conoce a **:ASIGNATURA_ABIERTO** (retorno tras confirmación contextual)
- **CancelarGeneracionView** conoce a **:EXAMENES_GENERADOS** (retorno tras rechazo)
- **CancelarGeneracionView** conoce a **:EXAMENES_GENERADOS_CONTEXTUALES** (retorno tras rechazo)
- **ExamenesController** conoce a **ExamenRepository** (gestión de exámenes)
- **ExamenRepository** conoce a **Examen** (gestión de entidad)

## trazabilidad con artefactos previos

### con especificación detallada
- **Estados internos** → **Clases de análisis**
- **RequiringCancelGeneration** → **CancelarGeneracionView.solicitarCancelacion()**
- **ProvidingConfirmation** → **CancelarGeneracionView.mostrarConfirmacion()**
- **Choice point** → **ExamenesController.procesarDecision()**

### con diagrama de contexto
- **EXAMENES_GENERADOS** → Entrada al caso de uso (origen global)
- **EXAMENES_GENERADOS_CONTEXTUALES** → Entrada al caso de uso (origen contextual)
- **SISTEMA_DISPONIBLE** → Destino tras confirmación global
- **ASIGNATURA_ABIERTO** → Destino tras confirmación contextual

### con modelo del dominio
- **Examen** (entidad) → **Examen** (clase de análisis)
- **Relación Examen ↔ Asignatura** → Trazabilidad directa

## características del análisis

### responsabilidades identificadas
- **CancelarGeneracionView**: Capturar decisión del docente y mostrar información de exámenes a cancelar
- **ExamenesController**: Orquestar lógica completa de eliminación de exámenes no asignados
- **ExamenRepository**: Proveer acceso conceptual a gestión de exámenes generados
- **Examen**: Representar exámenes generados pendientes de asignación

### relaciones conceptuales
- **Delegación**: Vista delega lógica de negocio al controlador
- **Gestión**: Controlador gestiona el ciclo de vida de los exámenes a través del repositorio
- **Eliminación**: ExamenRepository coordina la eliminación segura de exámenes
- **Decisión**: Vista maneja la navegación directa a estados

## naturaleza del flujo de control

### bifurcación de flujos
- **Confirmación**: Flujo hacia eliminación de exámenes y cambio de estado (SISTEMA_DISPONIBLE o ASIGNATURA_ABIERTO)
- **Rechazo**: Flujo de retorno al estado actual sin cambios (EXAMENES_GENERADOS o EXAMENES_GENERADOS_CONTEXTUALES)
- **Análisis conceptual**: Se enfoca en responsabilidades de decisión y gestión de estado

### gestión de estado
- **Preservación**: En caso de rechazo, mantiene exámenes generados en el estado actual
- **Eliminación**: En caso de confirmación, elimina exámenes no asignados
- **Transición**: Coordina cambio de estado según origen y decisión del docente

## patrones arquitectónicos aplicados

### patrón MVC para cancelación de exámenes
- **Model**: `Examen` + `ExamenRepository`
- **View**: `CancelarGeneracionView` (confirmación e interacción)
- **Controller**: `ExamenesController` (coordinación y validación)

### patrón de cancelación segura
- **Confirmación obligatoria**: El docente debe confirmar la cancelación
- **Reversibilidad**: Mientras no se confirmen, los exámenes permanecen
- **Retorno al origen**: El flujo de retorno depende del origen de la invocación

**Código fuente:** [colaboracion.puml](/modelosUML/analisis/cancelarGeneracion/colaboracion.puml)