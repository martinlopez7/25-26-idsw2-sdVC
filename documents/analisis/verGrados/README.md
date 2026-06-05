# verGrados > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verGrados/verGrados.svg)|**Análisis**|[Diseño](/documents/diseño/verGrados/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-20
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verGrados()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verGrados()](/images/analisis/verGrados/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verGrados/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerGradosView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de apertura de grados académicos
- Interactuar con el controlador para obtener la lista de grados
- Presentar la lista de grados al docente (nombre, código, alumnos enlistados)
- Capturar criterios de filtrado del docente
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verGrados()` desde `:Sistema Disponible` o `:Grado Abierto`
- **Control**: Se comunica con `GradosController`
- **Salida**: Navega a `:Sistema Disponible`, `:Grados Abierto` o colaboraciones CRUD

### clases de control

#### GradosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de grados académicos
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerGradosView`
- **Repositorio**: Delega operaciones de datos a `GradoRepository`

### clases de entidad (entity)

#### GradoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de grados académicos
- Proporcionar método para obtener todos los grados
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de grados

**Colaboraciones**:
- **Control**: Responde a `GradosController`
- **Entidad**: Gestiona instancias de `Grado`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un grado académico individual
- Encapsular atributos: título, código
- Mantener la integridad de los datos del grado

**Colaboraciones**:
- **Repositorio**: Es gestionado por `GradoRepository`

## diagrama de secuencia

<div align=center>

|![Análisis: verGrados()](/images/analisis/verGrados/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/analisis/verGrados/secuencia.puml)|

</div>

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `VerGradosView.verGrados()`
2. **Listado inicial**: `VerGradosView` → `GradosController.listarGrados()`
3. **Acceso a datos**: `GradosController` → `GradoRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerGradosView` → `GradosController.filtrarGrados(criterio)`
5. **Búsqueda**: `GradosController` → `GradoRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerGradosView` → Opciones disponibles

### opciones de navegación disponibles

- **completarGestion()** → `:Sistema Disponible`
- **crearGrado()** → `:Collaboration CrearGrado`
- **editarGrado()** → `:Collaboration EditarGrado`
- **eliminarGrado()** → `:Collaboration EliminarGrado`
- **importarGrados()** → `:Collaboration ImportarGrados`

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Presentar lista de grados|`VerGradosView`|Coordina con `GradosController.listarGrados()`|
|Permitir filtrado de lista|`VerGradosView`|Invoca `GradosController.filtrarGrados(criterio)`|
|Código, nombre, alumnos enlistados|`Grado`|Encapsula atributos del grado académico|
|Acceso a datos de grados|`GradoRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada estándar**: Desde `:Sistema Disponible` o `:Grado Abierto`
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

- **Origen**: Caso de uso detallado `verGrados()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`GradoRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerGradosView`), lógica de aplicación (`GradosController`) y datos (`Grado`, `GradoRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.
