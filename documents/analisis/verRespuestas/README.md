# 25-26-idsw2-sdVC > verRespuestas > Análisis

>|[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verRespuestas/verRespuestas.svg)|**Análisis**|Diseño|
>|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-23
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verRespuestas()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verRespuestas()](/images/analisis/verRespuestas/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verRespuestas/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerRespuestasView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de visualización de respuestas
- Interactuar con el controlador para obtener la lista de respuestas
- Presentar la lista de respuestas al docente (opción, esCorrecta)
- Capturar criterios de filtrado del docente
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verRespuestas()` desde `:Pregunta Abierto`, `:Respuesta Abierto`, `:Pregunta Contextual Abierto` o `:Respuesta Contextual Abierto`
- **Control**: Se comunica con `RespuestasController`
- **Salida**: Navega a colaboraciones CRUD

### clases de control

#### RespuestasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de respuestas
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerRespuestasView`
- **Repositorio**: Delega operaciones de datos a `RespuestaRepository`

### clases de entidad (entity)

#### RespuestaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de respuestas
- Proporcionar método para obtener todas las respuestas
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de respuestas

**Colaboraciones**:
- **Control**: Responde a `RespuestasController`
- **Entidad**: Gestiona instancias de `Respuesta`

#### Respuesta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar las opciones de respuesta asociadas a una pregunta
- Encapsular atributos: opción, esCorrecta
- Mantener la integridad de los datos de la respuesta

**Colaboraciones**:
- **Repositorio**: Es gestionada por `RespuestaRepository`
- **Pregunta**: Relación de composición con la pregunta padre

#### Pregunta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la pregunta asociada a las respuestas
- Mantener relación de composición con sus respuestas

**Colaboraciones**:
- **Respuesta**: Asociada mediante relación de composición

## diagrama de secuencia

<div align=center>

|![Análisis: verRespuestas()](/images/analisis/verRespuestas/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/analisis/verRespuestas/secuencia.puml)|

</div>

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Pregunta Abierto` / `:Respuesta Abierto` / `:Pregunta Contextual Abierto` / `:Respuesta Contextual Abierto` → `VerRespuestasView.verRespuestas()`
2. **Listado inicial**: `VerRespuestasView` → `RespuestasController.listarRespuestas()`
3. **Acceso a datos**: `RespuestasController` → `RespuestaRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerRespuestasView` → `RespuestasController.filtrarRespuestas(criterio)`
5. **Búsqueda**: `RespuestasController` → `RespuestaRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerRespuestasView` → Opciones disponibles

### opciones de navegación disponibles

- **crearRespuesta()** → `:Collaboration CrearRespuesta`
- **editarRespuesta()** → `:Collaboration EditarRespuesta`
- **eliminarRespuesta()** → `:Collaboration EliminarRespuesta`
- **editarPregunta()** → `:Collaboration EditarPregunta` (Cuando el sistema se encuentra en los estados contextuales)

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Presentar lista de respuestas|`VerRespuestasView`|Coordina con `RespuestasController.listarRespuestas()`|
|Permitir filtrado de lista|`VerRespuestasView`|Invoca `RespuestasController.filtrarRespuestas(criterio)`|
|Mostrar opción y correctness|`Respuesta`|Encapsula atributos de la respuesta|
|Acceso a datos de respuestas|`RespuestaRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada múltiple**: Desde `:Pregunta Abierto`, `:Respuesta Abierto`, `:Pregunta Contextual Abierto` o `:Respuesta Contextual Abierto`
- **Análisis MVC completo**: Vista, Control y Entidad claramente separados
- **Salidas múltiples**: `completarGestion()` + navegación CRUD mediante flechas discontinuas

## características del análisis

### separación de responsabilidades MVC

- **Vista**: Solo presentación e interacción con usuario
- **Control**: Solo coordinación y lógica de aplicación  
- **Entidad**: Solo datos y reglas de negocio

### agnóstico tecnológicamente

- No especifica tecnología de interfaz de usuario
- No asume implementación específica de base de datos
- Mantiene independencia de frameworks

### trazabilidad completa

- **Origen**: Caso de uso detallado `verRespuestas()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`RespuestaRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerRespuestasView`), lógica de aplicación (`RespuestasController`) y datos (`Respuesta`, `RespuestaRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.