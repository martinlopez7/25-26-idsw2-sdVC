# 25-26-idsw2-sdVC > verPreguntas > Análisis

>|[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verPreguntas/verPreguntas.svg)|**Análisis**|[Diseño](/documents/diseño/verPreguntas/README.md)|
>|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-23
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verPreguntas()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verPreguntas()](/images/analisis/verPreguntas/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verPreguntas/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerPreguntasView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de visualización de preguntas
- Interactuar con el controlador para obtener la lista de preguntas
- Presentar la lista de preguntas al docente (enunciado, asignatura, tema, dificultad, respuestas)
- Capturar criterios de filtrado del docente
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verPreguntas()` desde `:Sistema Disponible`, `:Asignatura Abierto`, `:Pregunta Abierto` o `:Pregunta Contextual Abierto`
- **Control**: Se comunica con `PreguntasController`
- **Salida**: Navega a `:Sistema Disponible` o colaboraciones CRUD

### clases de control

#### PreguntasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de preguntas
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerPreguntasView`
- **Repositorio**: Delega operaciones de datos a `PreguntaRepository`

### clases de entidad (entity)

#### PreguntaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de preguntas
- Proporcionar método para obtener todas las preguntas
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de preguntas

**Colaboraciones**:
- **Control**: Responde a `PreguntasController`
- **Entidad**: Gestiona instancias de `Pregunta`

#### Pregunta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de una pregunta individual
- Encapsular atributos: enunciado, tema, dificultad
- Mantener la integridad de los datos de la pregunta

**Colaboraciones**:
- **Repositorio**: Es gestionada por `PreguntaRepository`
- **Respuesta**: Relación de composición con opciones de respuesta

#### Respuesta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar las opciones de respuesta asociadas a una pregunta
- Encapsular atributos: opción, esCorrecta

**Colaboraciones**:
- **Pregunta**: Pertenece a una pregunta

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la asignatura asociada a la pregunta
- Mantener relación con las preguntas de dicha asignatura

**Colaboraciones**:
- **Pregunta**: Asociada mediante relación de composición

## diagrama de secuencia

<div align=center>

|![Análisis: verPreguntas()](/images/analisis/verPreguntas/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/analisis/verPreguntas/secuencia.puml)|

</div>

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` / `:Asignatura Abierto` / `:Pregunta Abierto` / `:Pregunta Contextual Abierto` → `VerPreguntasView.verPreguntas()`
2. **Listado inicial**: `VerPreguntasView` → `PreguntasController.listarPreguntas()`
3. **Acceso a datos**: `PreguntasController` → `PreguntaRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerPreguntasView` → `PreguntasController.filtrarPreguntas(criterio)`
5. **Búsqueda**: `PreguntasController` → `PreguntaRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerPreguntasView` → Opciones disponibles

### opciones de navegación disponibles

- **completarGestion()** → `:Sistema Disponible`
- **crearPregunta()** → `:Collaboration CrearPregunta`
- **editarPregunta()** → `:Collaboration EditarPregunta`
- **eliminarPregunta()** → `:Collaboration EliminarPregunta`
- **importarPreguntas()** → `:Collaboration ImportarPreguntas`
- **editarAsignatura()** → `:Collaboration EditarAsignatura` (En el caso en el que el sistema se encuentre en el estado PREGUNTAS_CONTEXTUALES_ABIERTO)

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Presentar lista de preguntas|`VerPreguntasView`|Coordina con `PreguntasController.listarPreguntas()`|
|Permitir filtrado de lista|`VerPreguntasView`|Invoca `PreguntasController.filtrarPreguntas(criterio)`|
|Mostrar enunciado, asignatura, tema, dificultad|`Pregunta`|Encapsula atributos de la pregunta|
|Mostrar opciones de respuesta|`Respuesta`|Relación de composición con pregunta|
|Acceso a datos de preguntas|`PreguntaRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada múltiple**: Desde `:Sistema Disponible`, `:Asignatura Abierto`, `:Pregunta Abierto` o `:Pregunta Contextual Abierto`
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

- **Origen**: Caso de uso detallado `verPreguntas()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`PreguntaRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerPreguntasView`), lógica de aplicación (`PreguntasController`) y datos (`Pregunta`, `PreguntaRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.