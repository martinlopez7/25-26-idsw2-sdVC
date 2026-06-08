# Cancelar Generación > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/cancelarGeneracion/cancelarGeneracion.svg)|**Análisis**|[Diseño](/documents/diseño/cancelarGeneracion/README.md)|
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
- **Navegador de estados**: Transición simple entre estados tras cancelación

**Invocación**: Este caso de uso es invocado desde:
1. **`EXAMENES_GENERADOS`** - Estado desde generarExamenes() global
2. **`EXAMENES_GENERADOS_CONTEXTUALES`** - Estado desde generarExamenes() contextual

**Resultado**: Retorna al estado anterior según el origen de la invocación.

## diagrama de colaboración

<div align=center>

|![Análisis cancelarGeneracion()](/images/analisis/cancelarGeneracion/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/cancelarGeneracion/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CancelarGeneracionView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de cancelación de generación
- Presentar diálogo de confirmación
- Delegar la cancelación al controlador
- Navegar al estado correspondiente según origen

**Colaboraciones**:
- **Entrada**: Recibe `cancelarGeneracion()` desde `:EXAMENES_GENERADOS` o `:EXAMENES_GENERADOS_CONTEXTUALES`
- **Control**: Se comunica con `ExamenesController`
- **Salida**: Navega a `:SISTEMA_DISPONIBLE` o `:ASIGNATURA_ABIERTO`

### clases de control

#### ExamenesController
**Estereotipo**: Control
**Responsabilidades**:
- Recibir la petición de cancelación con datos de exámenes
- Ejecutar el borrado interno de exámenes generados

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CancelarGeneracionView`
- **Interno**: Ejecuta `borrarExamenesGenerados()` para limpiar plantillas

## flujo de colaboración principal

### secuencia: cancelar generación

1. **Inicio**: `:EXAMENES_GENERADOS` o `:EXAMENES_GENERADOS_CONTEXTUALES` → `CancelarGeneracionView.cancelarGeneracion()`
2. **Delegación**: `CancelarGeneracionView` → `ExamenesController.cancelarGeneracion(examenes)`
3. **Borrado**: `ExamenesController` → `ExamenesController.borrarExamenesGenerados()`
4. **Navegación**: `CancelarGeneracionView` → `:SISTEMA_DISPONIBLE` o `:ASIGNATURA_ABIERTO`

## enlaces de dependencia

- **CancelarGeneracionView** conoce a **ExamenesController** (delegación)
- **CancelarGeneracionView** conoce a **:SISTEMA_DISPONIBLE** (retorno)
- **CancelarGeneracionView** conoce a **:ASIGNATURA_ABIERTO** (retorno)
- **ExamenesController** conoce a **ExamenesController** (llamada interna)

## características del análisis

### responsabilidades identificadas
- **CancelarGeneracionView**: Capturar decisión del docente, delegar cancelación, navegar a estado
- **ExamenesController**: Recibir petición y ejecutar borrado interno de exámenes

### relaciones conceptuales
- **Delegación**: Vista delega lógica de negocio al controlador
- **Navegación**: Vista maneja navegación directa a estados

## patrones arquitectónicos aplicados

### patrón MVC para cancelación de exámenes
- **View**: `CancelarGeneracionView` (confirmación e interacción)
- **Controller**: `ExamenesController` (coordinación y borrado)

### patrón de cancelación simple
- **Sin rechazo visible**: El diagrama muestra flujo único de cancelación