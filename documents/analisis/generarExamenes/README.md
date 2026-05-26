# Generador de Exámenes > generarExamenes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/generarExamenes/generarExamenes.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Generador de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `generarExamenes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar el proceso de generación automática de exámenes con configuración paramétrica por grado.

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
- Transferir automáticamente a asignación de exámenes generados

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
- Obtener los grados asociados a la asignatura seleccionada
- Obtener el número de alumnos matriculados de cada grado en la asignatura
- Validar datos mínimos obligatorios
- Validar que los parámetros por grado están dentro de los límites (1..alumnosDelGrado)
- Verificar que la asignatura tiene preguntas suficientes en la batería
- Delegar la generación al generador especializado
- Gestionar estados de confirmación y cancelación
- Servir como intermediario entre vista, repositorio y generador

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `GenerarExamenesView`
- **Repositorio**: Delega operaciones de datos a `ExamenRepository`
- **Generador**: Delega generación a `ExamenGenerator`
- **Validador**: Delega validaciones a `Validador`

### clases de entidad (entity)

#### ExamenRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de exámenes
- Implementar persistencia de exámenes generados
- Verificar existencia de exámenes previos para la asignatura/evaluación
- Cargar datos de asignatura y batería de preguntas
- Obtener los grados asociados a una asignatura
- Obtener el número de alumnos matriculados de cada grado en una asignatura

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **Generador**: Proporciona datos para `ExamenGenerator`

#### ExamenGenerator
**Estereotipo**: Entidad  
**Responsabilidades**:
- Encapsular la lógica del algoritmo de generación de exámenes
- Seleccionar preguntas de la batería según criterios (tema, dificultad)
- Para cada grado: generar el número de exámenes configurado
- Para cada grado: aplicar la cantidad de tipos de examen configurada
- Para cada grado: aplicar la proporción de dificultad configurada
- Generar exámenes con claves de corrección
- Coordinar persistencia del resultado generado

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **Repositorio**: Solicita datos y persistencia a `ExamenRepository`

#### Validador
**Estereotipo**: Entidad  
**Responsabilidades**:
- Implementar validaciones de datos mínimos del sistema
- Verificar existencia de al menos un grado asociado a la asignatura
- Verificar que la batería tiene preguntas suficientes
- Validar que los temas seleccionados tienen preguntas disponibles
- Validar que los parámetros por grado están dentro de los límites
- Proporcionar información detallada sobre datos faltantes

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **Repositorio**: Consulta datos en `ExamenRepository`

#### Examen
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar un examen generado
- Encapsular atributos: asignatura, evaluación, clave de corrección
- Mantener relación con las preguntas que lo componen
- Permitir asignación a alumnos

**Colaboraciones**:
- **Repositorio**: Es gestionado por `ExamenRepository`

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la asignatura para la que se generan exámenes
- Proporcionar acceso a su batería de preguntas
- Proporcionar acceso a los grados asociados
- Proporcionar acceso a los alumnos matriculados por grado

**Colaboraciones**:
- **Repositorio**: Es consultada por `ExamenRepository`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar un grado asociado a la asignatura
- Conocer los alumnos matriculados en la asignatura de ese grado
- Permitir configuración de parámetros específicos por grado

**Colaboraciones**:
- **Repositorio**: Es consultado por `ExamenRepository`

#### BateriaDePreguntas
**Estereotipo**: Entidad  
**Responsabilidades**:
- Almacenar las preguntas disponibles de la asignatura
- Proporcionar preguntas filtradas por tema y dificultad
- Verificar disponibilidad de preguntas según criterios

**Colaboraciones**:
- **Repositorio**: Es consultada por `ExamenRepository`

## flujo de colaboración principal

### secuencia: generar exámenes

1. **Inicio**: `:Sistema Disponible` o `:Asignatura Abierto` → `GenerarExamenesView.generarExamenes()`
2. **Presentación**: `GenerarExamenesView` presenta formulario de datos mínimos
3. **Datos globales**: Docente proporciona:
   - Asignatura (obligatorio) → el sistema obtiene grados asociados y alumnos por grado
   - Evaluación (obligatorio)
   - Tema/Temas (obligatorio)
   - Número de preguntas (obligatorio)
4. **Configuración por grado**: Para cada grado asociado a la asignatura, el docente configura:
   - Número de exámenes a generar (mín: 1, máx: alumnos matriculados de ese grado)
   - Cantidad de tipos de examen (mín: 1, máx: alumnos matriculados de ese grado)
   - Proporción de dificultad
5. **Validación**: `GenerarExamenesView` → `ExamenesController.validarDatos(datos)`
6. **Verificación**: `ExamenesController` → `Validador.verificarDatosMinimos(datos)` : boolean
7. **Verificación**: `ExamenesController` → `ExamenRepository.verificarPreguntasSuficientes(asignatura, temas, numPreguntas)` : boolean
8. **Confirmación**: `GenerarExamenesView` solicita confirmación de generación
9. **Generación**: `ExamenesController` → `ExamenGenerator.generar(datos)` : List<Examen>
10. **Persistencia**: `ExamenGenerator` → `ExamenRepository.guardarExamenes(examenes)` : boolean
11. **Transferencia**: `GenerarExamenesView` → **<<include>>** `:Collaboration AsignarExamenes.asignarExamenes(examenesGenerados)`

## diagrama de secuencia

<div align=center>

|![Secuencia: generarExamenes()](/images/analisis/generarExamenes/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/analisis/generarExamenes/secuencia.puml)|

</div>

## patrón de proceso de generación

### generación con configuración por grado

Este análisis implementa proceso de generación que:
- **Selecciona asignatura**: El sistema determina grados asociados y alumnos por grado
- **Configura por grado**: Cada grado tiene parámetros independientes
- **Respeta límites**: Número de exámenes y tipos limitados por alumnos matriculados
- **Aplica dificultad**: Proporción configurable de dificultad por grado
- **Genera automáticamente**: Algoritmo de selección de preguntas
- **Transfiere resultado**: Paso automático a asignación de exámenes

### premisas del dominio

- Un grado conoce a los alumnos matriculados
- Una asignatura conoce a qué grados está asociada
- Una asignatura conoce a los alumnos matriculados y a qué grado pertenecen
- Por lo tanto, una asignatura sabe cuántos alumnos tiene de cada grado

### patrón de separación de responsabilidades

La separación implementa:
- **ExamenesController**: Coordinación del proceso y flujo de control
- **Validador**: Verificaciones previas y validaciones de integridad
- **ExamenGenerator**: Encapsulación completa del algoritmo de generación
- **ExamenRepository**: Abstracción de persistencia y acceso a datos
- **GenerarExamenesView**: Interacción con usuario y presentación de estados

### algoritmo como servicio

- **Encapsulación**: ExamenGenerator oculta complejidad de selección de preguntas
- **Abstracción**: Controller trata generación como servicio de alto nivel
- **Separación**: Lógica de generación independiente de validaciones y persistencia
- **Reutilización**: Generador puede ser invocado desde otros contextos futuros

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

### clave de corrección

- **Generación automática**: Se genera automáticamente para cada examen
- **Asociación única**: Cada examen tiene su propia clave
- **Persistencia**: Se almacena junto con el examen para corrección posterior

## validaciones de negocio

### restricciones de integridad

**ExamenesController** debe verificar:
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

- **Model**: `Examen` + `ExamenRepository` + `ExamenGenerator` + `BateriaDePreguntas`
- **View**: `GenerarExamenesView` (formulario de configuración e interacción)
- **Controller**: `ExamenesController` (coordinación y validación)

### patrón Repository con generación compleja

- **Abstracción de generación**: `ExamenGenerator` encapsula lógica de selección
- **Separación de responsabilidades**: Controlador no conoce detalles de algoritmo
- **Flexibilidad**: Puede implementar diferentes estrategias de generación
- **Validaciones**: Específicas para generación de exámenes
