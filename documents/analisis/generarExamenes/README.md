# Generador de Exámenes > generarExamenes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/generarExamenes/generarExamenes.svg)|**Análisis**|[Diseño](/documents/diseño/generarExamenes/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Generador de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `generarExamenes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar el proceso de generación automática de exámenes con configuración paramétrica por grado. Los exámenes se generan en memoria y se transfieren directamente a `asignarExamenes()` sin persistencia intermedia.

## diagrama de colaboración

<div align=center>

|![Análisis: generarExamenes()](/images/analisis/generarExamenes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/generarExamenes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### GenerarExamenesView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de generación de exámenes
- Presentar selector de asignatura (obligatorio)
- Presentar selector de evaluación (obligatorio)
- Presentar selector de temas (obligatorio)
- Presentar campo de número de preguntas (obligatorio)
- Presentar configuración por grado con los grados asociados a la asignatura seleccionada
- Para cada grado: permitir configurar número de exámenes, tipos de examen y proporción de dificultad
- Permitir confirmar o cancelar generación
- Presentar mensajes de validación de datos insuficientes

**Colaboraciones**:
- **Entrada**: Recibe `generarExamenes()` desde `:Sistema Disponible` o `:Asignatura Abierto`
- **Control**: Se comunica con `ExamenesController`
- **Salida**: **<<include>>** `:Collaboration AsignarExamenes` tras generación exitosa
- **Salida**: **<<include>>** `:Collaboration CancelarGeneracion` tras cancelación

### clases de control

#### ExamenesController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar el proceso completo de generación de exámenes
- Validar datos mínimos obligatorios
- Validar que los parámetros por grado están dentro de los límites (1..alumnosDelGrado)
- Delegar la generación al generador especializado
- Gestionar estados de confirmación y cancelación
- Servir como intermediario entre vista, validador y generador

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `GenerarExamenesView`
- **Validador**: Delega validaciones a `Validador`
- **Generador**: Delega generación a `Generador`

### clases de entidad (entity)

#### Generador
**Estereotipo**: Entidad
**Responsabilidades**:
- Encapsular la lógica del algoritmo de generación de exámenes
- Seleccionar preguntas de la batería según criterios (tema, dificultad)
- Para cada grado: generar el número de exámenes configurado
- Para cada grado: aplicar la cantidad de tipos de examen configurada
- Para cada grado: aplicar la proporción de dificultad configurada
- Generar exámenes con clavePendiente=true

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **PreguntaRepository**: Solicita preguntas filtradas

#### Validador
**Estereotipo**: Entidad
**Responsabilidades**:
- Implementar validaciones de datos mínimos del sistema
- Verificar que existen preguntas suficientes
- Validar que los temas seleccionados tienen preguntas disponibles
- Validar que los parámetros por grado están dentro de los límites

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`

#### Examen
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar un examen generado
- Encapsular atributos: asignatura, evaluación, preguntas, clavePendiente=true
- Mantener relación con las preguntas que lo componen
- Permitir asignación a alumnos en `asignarExamenes()`

**Colaboraciones**:
- **Generador**: Es creado por `Generador`

#### Asignatura
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la asignatura para la que se generan exámenes
- Proporcionar acceso a su batería de preguntas
- Proporcionar acceso a los grados asociados
- Proporcionar acceso a los alumnos matriculados por grado

**Colaboraciones**:
- **Validador**: Es consultada para validación
- **Generador**: Proporciona datos para generación

#### Grado
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar un grado asociado a la asignatura
- Conocer los alumnos matriculados en la asignatura de ese grado

**Colaboraciones**:
- **Validador**: Es consultado para validación de límites
- **Generador**: Proporciona datos para configuración por grado

#### PreguntaRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Almacenar las preguntas disponibles de la asignatura
- Proporcionar preguntas filtradas por tema y dificultad
- Verificar disponibilidad de preguntas según criterios

**Colaboraciones**:
- **Validador**: Es consultada para verificación
- **Generador**: Proporciona preguntas para generación

## patrón de proceso de generación

### generación con transferencia directa

Este análisis implementa proceso de generación que:
- **Sin persistencia intermedia**: Los exámenes se generan en memoria y se transfieren directamente
- **Selecciona asignatura**: El sistema determina grados asociados y alumnos por grado
- **Configura por grado**: Cada grado tiene parámetros independientes
- **Respeta límites**: Número de exámenes y tipos limitados por alumnos matriculados
- **Aplica dificultad**: Proporción configurable de dificultad por grado
- **Genera automáticamente**: Algoritmo de selección de preguntas
- **Transfiere directamente**: Paso inmediato a asignación de exámenes sin almacenamiento

### premisas del dominio

- Un grado conoce a los alumnos matriculados
- Una asignatura conoce a qué grados está asociada
- Una asignatura conoce a los alumnos matriculados y a qué grado pertenecen
- Por lo tanto, una asignatura sabe cuántos alumnos tiene de cada grado

### patrón de separación de responsabilidades

La separación implementa:
- **ExamenesController**: Coordinación del proceso y flujo de control
- **Validador**: Verificaciones previas y validaciones de integridad
- **Generador**: Encapsulación completa del algoritmo de generación
- **PreguntaRepository**: Abstracción de acceso a preguntas
- **GenerarExamenesView**: Interacción con usuario y presentación de estados

### algoritmo como servicio

- **Encapsulación**: Generador oculta complejidad de selección de preguntas
- **Abstracción**: Controller trata generación como servicio de alto nivel
- **Separación**: Lógica de generación independiente de validaciones
- **Transferencia directa**: Sin almacenamiento intermedio entre generación y asignación

## consideraciones de diseño específicas para exámenes

### configuración por grado

- **Flexibilidad**: Cada grado tiene configuración independiente
- **Límites dinámicos**: Los límites de exámenes y tipos dependen de los alumnos matriculados de ese grado en la asignatura
- **Validación específica**: Verificar que los parámetros están dentro de los límites

### selección de preguntas

- **Por tema**: Filtrar preguntas por los temas seleccionados
- **Por dificultad**: Aplicar proporción de dificultad configurada por grado
- **Sin repetición**: Evitar repetir preguntas en el mismo examen
- **Balanceo**: Distribuir equitativamente entre temas seleccionados

### tipos de examen

- **Mínimo 1**: Mismo tipo de examen para todos los alumnos del grado (con distinto orden de preguntas)
- **Máximo alumnosDelGrado**: Un tipo de examen distinto para cada alumno del grado
- **Intermedio**: Cantidad configurable de tipos, distribuyendo alumnos entre tipos

### clave de corrección pendiente

- **Generación automática**: Se genera con clavePendiente=true durante generación
- **Pendiente**: La clave definitiva se genera en `asignarExamenes()` con datos del alumno
- **Temporal**: No se persiste, solo existe durante la transferencia

## validaciones de negocio

### restricciones de integridad

**ExamenesController** debe verificar (vía Validador):
- **Asignatura obligatoria**: Debe seleccionarse una asignatura
- **Evaluación obligatoria**: Debe seleccionarse un tipo de evaluación
- **Temas obligatorios**: Debe seleccionarse al menos un tema
- **Número de preguntas**: Debe ser mayor que 0
- **Preguntas suficientes**: La batería debe tener preguntas suficientes
- **Exámenes por grado**: Mínimo 1, máximo alumnos matriculados de ese grado
- **Tipos por grado**: Mínimo 1, máximo alumnos matriculados de ese grado
- **Dificultad por grado**: Proporción válida

### manejo de errores

- **Datos insuficientes**: Mensaje informativo sobre campos obligatorios
- **Preguntas insuficientes**: Explicación sobre disponibilidad en batería
- **Límites excedidos**: Mensaje sobre límites permitidos por grado
- **Error de sistema**: Manejo graceful de fallos de generación

## patrones arquitectónicos aplicados

### patrón MVC para generación de exámenes

- **Model**: `Generador` + `Validador` + `Examen` + `Asignatura` + `Grado` + `PreguntaRepository`
- **View**: `GenerarExamenesView` (formulario de configuración e interacción)
- **Controller**: `ExamenesController` (coordinación y validación)

### patrón de generación con transferencia directa

- **Sin persistencia**: Exámenes en memoria durante generación
- **Transferencia inmediata**: Pasa directamente a `asignarExamenes()` o `cancelarGeneracion()`
- **Abstracción**: Generador encapsula lógica sin conocer destino final
- **Validación desacoplada**: Validador separado del generador