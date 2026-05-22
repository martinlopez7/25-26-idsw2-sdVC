# 25-26-idsw2-sdVC > verAsignaturas > Análisis

>|[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verGrados/verGrados.svg)|Analisis|Diseño|
>|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-21
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verAsignaturas()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verAsignaturas()](/images/analisis/verAsignaturas/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verAsignaturas/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerAsignaturasView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de visualización de asignaturas
- Interactuar con el controlador para obtener la lista de asignaturas
- Presentar la lista de asignaturas al docente
- Capturar criterios de filtrado del docente
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verAsignaturas()` desde `:Sistema Disponible` o desde `:Asignatura Abierto`
- **Control**: Se comunica con `AsignaturasController`
- **Salida**: Navega a `:Sistema Disponible` o colaboraciones CRUD

### clases de control

#### AsignaturasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de asignaturas
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerAsignaturasView`
- **Repositorio**: Delega operaciones de datos a `AsignaturaRepository`

### clases de entidad (entity)

#### AsignaturaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de asignaturas
- Proporcionar método para obtener todas las asignaturas
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de asignaturas

**Colaboraciones**:
- **Control**: Responde a `AsignaturasController`
- **Entidad**: Gestiona instancias de `Asignatura`

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de una asignatura individual
- Encapsular atributos: título, código, curso académico
- Mantener la integridad de los datos de la asignatura

**Colaboraciones**:
- **Repositorio**: Es gestionada por `AsignaturaRepository`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un grado académico
- Mantener relación con las asignaturas asociadas

**Colaboraciones**:
- **Asignatura**: Asociada mediante relación de pertenencia

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `VerAsignaturasView.verAsignaturas()`
2. **Listado inicial**: `VerAsignaturasView` → `AsignaturasController.listarAsignaturas()`
3. **Acceso a datos**: `AsignaturasController` → `AsignaturaRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerAsignaturasView` → `AsignaturasController.filtrarAsignaturas(criterio)`
5. **Búsqueda**: `AsignaturasController` → `AsignaturaRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerAsignaturasView` → Opciones disponibles

### opciones de navegación disponibles

- **completarGestion()** → `:Sistema Disponible`
- **imortarAsigntura()** → `:Collaboration ImportarAsignatura`
- **crearAsignatura()** → `:Collaboration CrearAsignatura`
- **editarAsignatura()** → `:Collaboration EditarAsignatura`
- **eliminarAsignatura()** → `:Collaboration EliminarAsignatura`

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|
|Presentar lista de asignaturas|`VerAsignaturasView`|Coordina con `AsignaturasController.listarAsignaturas()`|
|Permitir filtrado de lista|`VerAsignaturasView`|Invoca `AsignaturasController.filtrarAsignaturas(criterio)`|
|Mostrar nombre, código, curso académico|`Asignatura`|Encapsula atributos de la asignatura|
|Mostrar grados asociados|`Grado`|Relación con asignaturas|
|Acceso a datos de asignaturas|`AsignaturaRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada estándar**: Desde `:Sistema Disponible`
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

- **Origen**: Caso de uso detallado `verAsignaturas()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`AsignaturaRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerAsignaturasView`), lógica de aplicación (`AsignaturasController`) y datos (`Asignatura`, `AsignaturaRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.