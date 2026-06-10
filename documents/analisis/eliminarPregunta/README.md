# eliminarPregunta > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarPregunta/eliminarPregunta.svg)|**Análisis**|[Diseño](/documents/diseño/eliminarPregunta/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26

## propósito

Análisis de colaboración del caso de uso `eliminarPregunta()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de preguntas con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarPregunta()](/images/analisis/eliminarPregunta/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarPregunta/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarPreguntaView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de pregunta
- Presentar información completa de la pregunta a eliminar
- Mostrar enunciado, tema, dificultad y respuestas de la pregunta
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarPregunta(preguntaId)` desde `:PREGUNTAS_ABIERTO`, `:PREGUNTAS_CONTEXTUALES_ABIERTO`, `:PREGUNTA_ABIERTO` o `:PREGUNTA_CONTEXTUAL_ABIERTO`
- **Control**: Se comunica con `PreguntasController`
- **Salida**: **<<include>>** `:Collaboration verPreguntas()` tras eliminación o cancelación

### clases de control

#### PreguntasController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos de la pregunta a eliminar
- Validar que la pregunta existe y puede ser eliminada
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarPreguntaView`
- **Repositorio**: Delega operaciones de datos a `PreguntaRepository`

### clases de entidad (entity)

#### PreguntaRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de preguntas
- Proporcionar método para obtener pregunta por ID
- Implementar eliminación de la pregunta
- Verificar restricciones de integridad antes de eliminar

**Colaboraciones**:
- **Control**: Responde a `PreguntasController`
- **Entidad**: Gestiona instancias de `Pregunta`

#### Pregunta
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información de la pregunta a eliminar
- Encapsular atributos: asignatura, enunciado, tema, dificultad
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `PreguntaRepository`

#### Respuesta
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información de las respuestas que se van a eliminar
- Encapsular atributos: texto de respuesta, esCorrecta

**Colaboraciones**:
- **Repositorio**: Es gestionado por `Pregunta`

## flujo de colaboración principal

1. **Inicio**: `:PREGUNTAS_ABIERTO`, `:PREGUNTAS_CONTEXTUALES_ABIERTO`, `:PREGUNTA_ABIERTO` o `:PREGUNTA_CONTEXTUAL_ABIERTO` → `EliminarPreguntaView.eliminarPregunta(preguntaId)`
2. **Carga**: `EliminarPreguntaView` → `PreguntasController.cargarPreguntaParaEliminacion(preguntaId)`
3. **Obtención**: `PreguntasController` → `PreguntaRepository.obtenerPorId(preguntaId) : Pregunta`
4. **Presentación**: `EliminarPreguntaView` presenta información de la `Pregunta` (enunciado, tema, dificultad y respuestas) con advertencia
5. **Confirmación**: Docente confirma o cancela en `EliminarPreguntaView`
6. **Eliminación**: `EliminarPreguntaView` → `PreguntasController.eliminarPregunta(preguntaId)`
7. **Persistencia**: `PreguntasController` → `PreguntaRepository.eliminar(preguntaId)`
8. **Finalización**: `EliminarPreguntaView` → **<<include>>** `:Collaboration verPreguntas()`

## patrón de eliminación segura para preguntas

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Enunciado, tema, dificultad y respuestas de la pregunta
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarPreguntaView** maneja confirmación:
- **Presenta datos**: Información completa de la pregunta
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**PreguntasController** valida eliminación:
- **Verifica existencia**: Pregunta existe y es válida
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de preguntas

- **Model**: `Pregunta` + `PreguntaRepository` + `Respuesta` (datos de la pregunta, respuesta y eliminación)
- **View**: `EliminarPreguntaView` (confirmación e interacción)
- **Controller**: `PreguntasController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarPregunta() se enfoca en eliminar
- **Reutilización**: **<<include>>** verPreguntas() evita duplicar funcionalidad de listado
- **Entrada múltiple**: Funciona desde `:PREGUNTAS_ABIERTO`, `:PREGUNTAS_CONTEXTUALES_ABIERTO`, `:PREGUNTA_ABIERTO` o `:PREGUNTA_CONTEXTUAL_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada