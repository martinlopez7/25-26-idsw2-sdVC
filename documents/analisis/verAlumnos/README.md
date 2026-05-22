# verAlumnos > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verAlumnos/verAlumnos.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-22
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verAlumnos()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verAlumnos()](/images/analisis/verAlumnos/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verAlumnos/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerAlumnosView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de apertura de alumnos
- Interactuar con el controlador para obtener la lista de alumnos
- Presentar la lista de alumnos al docente (DNI, nombre, apellidos)
- Capturar criterios de filtrado del docente
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verAlumnos()` desde `:Sistema Disponible` o `:Alumno Abierto`
- **Control**: Se comunica con `AlumnosController`
- **Salida**: Navega a `:Sistema Disponible`, `:Alumnos Abierto` o colaboraciones CRUD

### clases de control

#### AlumnosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de alumnos
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerAlumnosView`
- **Repositorio**: Delega operaciones de datos a `AlumnoRepository`

### clases de entidad (entity)

#### AlumnoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de alumnos
- Proporcionar método para obtener todos los alumnos
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de alumnos

**Colaboraciones**:
- **Control**: Responde a `AlumnosController`
- **Entidad**: Gestiona instancias de `Alumno`

#### Alumno
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un alumno individual
- Encapsular atributos: nombre, apellidos, DNI
- Mantener la integridad de los datos del alumno

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AlumnoRepository`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `VerAlumnosView.verAlumnos()`
2. **Listado inicial**: `VerAlumnosView` → `AlumnosController.listarAlumnos()`
3. **Acceso a datos**: `AlumnosController` → `AlumnoRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerAlumnosView` → `AlumnosController.filtrarAlumnos(criterio)`
5. **Búsqueda**: `AlumnosController` → `AlumnoRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerAlumnosView` → Opciones disponibles

### opciones de navegación disponibles

- **completarGestion()** → `:Sistema Disponible`
- **crearAlumno()** → `:Collaboration CrearAlumno`
- **editarAlumno()** → `:Collaboration EditarAlumno`
- **eliminarAlumno()** → `:Collaboration EliminarAlumno`
- **importarAlumnos()** → `:Collaboration ImportarAlumnos`

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Presentar lista de alumnos|`VerAlumnosView`|Coordina con `AlumnosController.listarAlumnos()`|
|Permitir filtrado de lista|`VerAlumnosView`|Invoca `AlumnosController.filtrarAlumnos(criterio)`|
|DNI, nombre, apellidos|`Alumno`|Encapsula atributos del alumno|
|Acceso a datos de alumnos|`AlumnoRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada estándar**: Desde `:Sistema Disponible` o `:Alumno Abierto`
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

- **Origen**: Caso de uso detallado `verAlumnos()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`AlumnoRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerAlumnosView`), lógica de aplicación (`AlumnosController`) y datos (`Alumno`, `AlumnoRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.