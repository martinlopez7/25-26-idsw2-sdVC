# Asignar Exámenes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/asignarExamenes/asignarExamenes.svg)|**Análisis**|[Diseño](/documents/diseño/asignarExamenes/README.md)|
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

- **Asignador de exámenes**: Asocia plantillas de exámenes a alumnos destinatarios por grado
- **Generador de claves**: Genera la clave de corrección definitiva (hash) para cada examen
- **Actualizador de estado**: Marca las plantillas como asignadas con clave definitiva

**Invocación**: Este caso de uso es invocado automáticamente desde `generarExamenes()` mediante <<include>> tras generación exitosa.

**Entrada**:
1. **`EXAMENES_GENERADOS`** - Estado desde generarExamenes() global
2. **`EXAMENES_GENERADOS_CONTEXTUALES`** - Estado desde generarExamenes() contextual

**Resultado**:
- **Asignación exitosa**: Transfiere a `EXAMENES_ASIGNADOS` o `EXAMENES_ASIGNADOS_CONTEXTUALES`

## diagrama de colaboración

<div align=center>

|![Análisis: asignarExamenes()](/images/analisis/asignarExamenes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/asignarExamenes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### AsignarExamenesView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de asignación de exámenes
- Presentar formulario con alumnos destinatarios por grado
- Mostrar configuración de asignación por grado
- Delegar asignación al controlador
- Gestionar navegación a estados de destino

**Colaboraciones**:
- **Entrada**: Recibe `asignarExamenes()` desde `:EXAMENES_GENERADOS` o `:EXAMENES_GENERADOS_CONTEXTUALES`
- **Control**: Se comunica con `ExamenesController`
- **Salida**: Navega a `:EXAMENES_ASIGNADOS` o `:EXAMENES_ASIGNADOS_CONTEXTUALES`

### clases de control

#### ExamenesController
**Estereotipo**: Control
**Responsabilidades**:
- Recibir petición de asignación con datos de exámenes y alumnos
- Obtener exámenes
- Obtener alumnos por grado de la asignatura
- Para cada asignación: actualizar examen con alumno y generar clave de corrección
- Gestionar el flujo de asignación

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `AsignarExamenesView`
- **GestorSesion**: Obtiene y actualiza exámenes
- **GestorAlumnos**: Obtiene alumnos por grado

### clases de entidad (entity)

#### GestorExamen
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a los exámenes
- Actualizar exámenes con alumno asignado y clave de corrección definitiva
- Generar clave de corrección (hash con preguntas + respuestas + alumno)
- Marcar exámenes como asignadas

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **Examen**: Proporciona y actualiza exámenes

#### GestorAlumnos
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a los alumnos matriculados por grado
- Proporcionar mapa de grados con sus alumnos destinatarios

**Colaboraciones**:
- **Control**: Responde a `ExamenesController`
- **Grado**: Agrupa alumnos
- **Alumno**: Proporciona datos de alumno destinatario

#### Examen
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar una plantilla de examen
- Mantener atributos: asignatura, evaluación, preguntas, alumnoAsignado, claveCorreccion
- Mantener estado: clavePendiente (true→false tras asignación), asignada (true/false)

**Colaboraciones**:
- **GestorSesion**: Es obtenida y actualizada por `GestorExamen`

#### Grado
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar un grado asociado a la asignatura
- Agrupar alumnos destinatarios para selección

**Colaboraciones**:
- **GestorAlumnos**: Es consultado para obtener alumnos por grado

#### Alumno
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar un alumno destinatario del examen
- Proporcionar datos para generación de clave de corrección

**Colaboraciones**:
- **GestorAlumnos**: Es proporcionado para asignación

## enlaces de dependencia

- **AsignarExamenesView** conoce a **ExamenesController** (delegación)
- **AsignarExamenesView** conoce a **:EXAMENES_ASIGNADOS** (retorno tras asignación global)
- **AsignarExamenesView** conoce a **:EXAMENES_ASIGNADOS_CONTEXTUALES** (retorno tras asignación contextual)
- **ExamenesController** conoce a **GestorExamen**
- **ExamenesController** conoce a **GestorAlumnos** (obtención de alumnos)
- **GestorAlumnos** conoce a **Grado** y **Alumno** (datos de alumnos)

### con diagrama de contexto
- **EXAMENES_GENERADOS** → Entrada al caso de uso (origen global)
- **EXAMENES_GENERADOS_CONTEXTUALES** → Entrada al caso de uso (origen contextual)
- **EXAMENES_ASIGNADOS** → Destino tras asignación global
- **EXAMENES_ASIGNADOS_CONTEXTUALES** → Destino tras asignación contextual

## características del análisis

### responsabilidades identificadas
- **AsignarExamenesView**: Formulario de asignación, delegar, navegar
- **ExamenesController**: Recibir petición, orquestar flujo, coordinar asignación
- **GestorExamen**: Abstraer examenes y actualizar con alumno y clave
- **GestorAlumnos**: Abstraer acceso a alumnos por grado
- **Examen**: Plantilla con datos de alumno y clave de corrección