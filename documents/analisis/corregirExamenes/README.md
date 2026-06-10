# Corregir Exámenes > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/corregirExamenes/corregirExamenes.svg)|**Análisis**|[Diseño](/documents/diseño/corregirExamenes/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-27
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `corregirExamenes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar el proceso simplificado de corrección de exámenes.

### rol metodológico del caso de uso

`corregirExamenes()` es el **caso de uso de corrección** del proceso de exámenes, funcionando como:

- **Receptor de exámenes**: Recibe los exámenes realizados por alumnos
- **Generador de notas**: Genera una nota aleatoria (1-10) para cada examen
- **Presentador de resultados**: Muestra los resultados de corrección al docente

**Entrada**: Desde `SISTEMA_DISPONIBLE` (estado EXAMENES_CORREGIDOS).

**Resultado**: Transfiere a `EXAMENES_CORREGIDOS` tras corrección exitosa.

## diagrama de colaboración

<div align=center>

|![Análisis: corregirExamenes()](/images/analisis/corregirExamenes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/corregirExamenes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CorregirExamenesView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Presentar formulario de corrección de exámenes
- Capturar los exámenes realizados por los alumnos
- Delegar la corrección al controlador
- Presentar los resultados de la corrección (examen + nota)
- Gestionar navegación a estados de destino

**Colaboraciones**:
- **Entrada**: Recibe `corregirExamenes()` desde `:Sistema Disponible`
- **Control**: Se comunica con `ExamenesController`
- **Salida**: Navega a `:EXAMENES_CORREGIDOS`

### clases de control

#### ExamenesController
**Estereotipo**: Control
**Responsabilidades**:
- Recibir los exámenes a corregir
- Delegar el procesamiento al corrector
- Retornar los resultados al docente

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CorregirExamenesView`
- **Corrector**: Delega la generación de notas

### clases de entidad (entity)

#### Corrector
**Estereotipo**: Entidad
**Responsabilidades**:
- Procesar los exámenes recibidos
- Generar una nota aleatoria (1-10) para cada examen
- Proporcionar lista de resultados (examen, nota)

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`

## flujo de colaboración principal

### secuencia: corregir exámenes

1. **Inicio**: `:Sistema Disponible` → `CorregirExamenesView.corregirExamenes()`
2. **Presentación**: `CorregirExamenesView` presenta formulario de corrección
3. **Delegación**: `CorregirExamenesView` → `ExamenesController.corregirExamenes(examenes)`
4. **Procesamiento**: `ExamenesController` → `Corrector.corregir(examenes)`
5. **Generación**: Para cada examen, `Corrector` genera una nota aleatoria 1-10
6. **Retorno**: `ExamenesController` → `CorregirExamenesView` (resultados: examen, nota)
7. **Navegación**: `CorregirExamenesView` → `:EXAMENES_CORREGIDOS`

## flujo de colaboración

### mensajes de colaboración

|Origen|Destino|Mensaje|Intención|
|-|-|-|-|
|**:Sistema Disponible**|**CorregirExamenesView**|`corregirExamenes()`|Invocación del caso de uso|
|**CorregirExamenesView**|**ExamenesController**|`corregirExamenes(examenes)`|Delegar corrección|
|**ExamenesController**|**Corrector**|`corregir(examenes)`|Procesar exámenes|
|**Corrector**|`:ExamenesController`|resultados (examen, nota)|Retornar resultados|
|**ExamenesController**|**CorregirExamenesView**|resultados|Retornar resultados|
|**CorregirExamenesView**|**:EXAMENES_CORREGIDOS**|`completarGestion()`|Retorno tras corrección|

## enlaces de dependencia

- **CorregirExamenesView** conoce a **ExamenesController** (delegación)
- **CorregirExamenesView** conoce a **:EXAMENES_CORREGIDOS** (retorno)
- **ExamenesController** conoce a **Corrector** (procesamiento)

## coherencia con el diseño

### corrección simplificada

Este análisis es coherente con el diseño:
- **Entrada**: Exámenes realizados por alumnos
- **Proceso**: Generación de notas aleatorias 1-10 por examen
- **Salida**: Lista de resultados (examen, nota)
- **Sin comparación**: No se comparan respuestas con clave de corrección

### sin persistencia

Este caso de uso:
- **Solo retorna resultados**: No persiste en base de datos
- **Visualización**: Los resultados se muestran al docente

## trazabilidad con artefactos previos

### con especificación detallada
- **Estados internos** → **Clases de análisis**
- **RequiringCorrection** → **CorregirExamenesView.solicitarCorreccion()**
- **ProvidingDoneExams** → **CorregirExamenesView.mostrarFormulario()**
- **ProvidingConfirmation** → **CorregirExamenesView.mostrarResultados()**

### con diagrama de contexto
- **SISTEMA_DISPONIBLE** → Entrada al caso de uso (desde menú)
- **EXAMENES_CORREGIDOS** → Estado durante presentación de resultados

## características del análisis

### responsabilidades identificadas
- **CorregirExamenesView**: Formulario de corrección, presentar resultados
- **ExamenesController**: Recibir exámenes, delegar procesamiento, retornar resultados
- **Corrector**: Generar notas aleatorias para cada examen

### relaciones conceptuales
- **Delegación**: Vista delega lógica al controlador
- **Procesamiento**: Controlador usa corrector para generar notas
- **Generación**: Corrector genera notas aleatorias sin intervención humana

## patrones arquitectónicos aplicados

### patrón MVC para corrección de exámenes
- **Model**: `Corrector`
- **View**: `CorregirExamenesView` (formulario y resultados)
- **Controller**: `ExamenesController` (coordinación)

### patrón de corrección simplificada
- **Entrada**: Exámenes a corregir
- **Proceso**: Generación de notas aleatorias 1-10
- **Salida**: Lista de resultados (examen, nota)