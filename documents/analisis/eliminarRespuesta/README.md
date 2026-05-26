# eliminarRespuesta > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarRespuesta/eliminarRespuesta.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26

## propósito

Análisis de colaboración del caso de uso `eliminarRespuesta()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de respuestas con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarRespuesta()](/images/analisis/eliminarRespuesta/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarRespuesta/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarRespuestaView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de respuesta
- Presentar información completa de la respuesta a eliminar
- Mostrar contenido y si es correcta o incorrecta
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarRespuesta(respuestaId)` desde `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO`
- **Control**: Se comunica con `RespuestasController`
- **Salida**: **<<include>>** `:Collaboration verRespuestas()` tras eliminación o cancelación

### clases de control

#### RespuestasController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos de la respuesta a eliminar
- Validar que la respuesta existe y puede ser eliminada
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarRespuestaView`
- **Repositorio**: Delega operaciones de datos a `RespuestaRepository`

### clases de entidad (entity)

#### RespuestaRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de respuestas
- Proporcionar método para obtener respuesta por ID
- Implementar eliminación de la respuesta
- Verificar restricciones de integridad antes de eliminar

**Colaboraciones**:
- **Control**: Responde a `RespuestasController`
- **Entidad**: Gestiona instancias de `Respuesta`

#### Respuesta
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información de la respuesta a eliminar
- Encapsular atributos: contenido, esCorrecta
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `RespuestaRepository`

#### Pregunta
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la pregunta propietaria de la respuesta
- Mantener la relación de composición con Respuesta

**Colaboraciones**:
- **Entidad**: Contiene instancias de `Respuesta`

## flujo de colaboración principal

1. **Inicio**: `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO` → `EliminarRespuestaView.eliminarRespuesta(respuestaId)`
2. **Carga**: `EliminarRespuestaView` → `RespuestasController.cargarRespuestaParaEliminacion(respuestaId)`
3. **Obtención**: `RespuestasController` → `RespuestaRepository.obtenerPorId(respuestaId) : Respuesta`
4. **Presentación**: `EliminarRespuestaView` presenta información de la `Respuesta` (contenido, correcta/incorrecta) con advertencia
5. **Confirmación**: Docente confirma o cancela en `EliminarRespuestaView`
6. **Eliminación**: `EliminarRespuestaView` → `RespuestasController.eliminarRespuesta(respuestaId)`
7. **Persistencia**: `RespuestasController` → `RespuestaRepository.eliminar(respuestaId)`
8. **Finalización**: `EliminarRespuestaView` → **<<include>>** `:Collaboration verRespuestas()`

## patrón de eliminación segura para respuestas

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Contenido y si es correcta/incorrecta
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarRespuestaView** maneja confirmación:
- **Presenta datos**: Información completa de la respuesta
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**RespuestasController** valida eliminación:
- **Verifica existencia**: Respuesta existe y es válida
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de respuestas

- **Model**: `Respuesta` + `RespuestaRepository` + `Pregunta` (datos de la respuesta, pregunta propietaria y eliminación)
- **View**: `EliminarRespuestaView` (confirmación e interacción)
- **Controller**: `RespuestasController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarRespuesta() se enfoca en eliminar
- **Reutilización**: **<<include>>** verRespuestas() evita duplicar funcionalidad de listado
- **Doble entrada**: Funciona desde `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada
