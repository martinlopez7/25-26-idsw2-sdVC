# verDocentes > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/verDocentes/verDocentes.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-23
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `verDocentes()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para cumplir con los requisitos especificados.

## diagrama de colaboración

<div align=center>

|![Análisis: verDocentes()](/images/analisis/verDocentes/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/verDocentes/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### VerDocentesView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de apertura de docentes
- Interactuar con el controlador para obtener la lista de docentes
- Presentar la lista de docentes al administrador (nombre, apellidos, DNI, nombre de usuario, email, password)
- Capturar criterios de filtrado del administrador
- Manejar la navegación de regreso al sistema y a operaciones CRUD

**Colaboraciones**:
- **Entrada**: Recibe `verDocentes()` desde `:Sistema Disponible` o `:Docente Abierto`
- **Control**: Se comunica con `DocentesController`
- **Salida**: Navega a `:Sistema Disponible`, `:Docentes Abierto` o colaboraciones CRUD

### clases de control

#### DocentesController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la obtención de la lista completa de docentes
- Manejar la lógica de filtrado por criterios de búsqueda
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `VerDocentesView`
- **Repositorio**: Delega operaciones de datos a `DocenteRepository`

### clases de entidad (entity)

#### DocenteRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de docentes
- Proporcionar método para obtener todos los docentes
- Implementar búsqueda por criterios específicos
- Mantener la consistencia de los datos de docentes

**Colaboraciones**:
- **Control**: Responde a `DocentesController`
- **Entidad**: Gestiona instancias de `Docente`

#### Docente
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un docente individual
- Encapsular atributos: nombre, apellidos, DNI, nombre de usuario, email, password
- Mantener la integridad de los datos del docente

**Colaboraciones**:
- **Repositorio**: Es gestionado por `DocenteRepository`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Sistema Disponible` → `VerDocentesView.verDocentes()`
2. **Listado inicial**: `VerDocentesView` → `DocentesController.listarDocentes()`
3. **Acceso a datos**: `DocentesController` → `DocenteRepository.obtenerTodos()`
4. **Filtrado (opcional)**: `VerDocentesView` → `DocentesController.filtrarDocentes(criterio)`
5. **Búsqueda**: `DocentesController` → `DocenteRepository.buscarPorCriterio(criterio)`
6. **Navegación**: `VerDocentesView` → Opciones disponibles

### opciones de navegación disponibles

- **completarGestion()** → `:Sistema Disponible`
- **crearDocente()** → `:Collaboration CrearDocente`
- **editarDocente()** → `:Collaboration EditarDocente`
- **eliminarDocente()** → `:Collaboration EliminarDocente`

## correspondencia con requisitos

### mapeado con especificación detallada

|Requisito del caso de uso|Clase responsable|Método/Colaboración|
|-|-|-|
|Presentar lista de docentes|`VerDocentesView`|Coordina con `DocentesController.listarDocentes()`|
|Permitir filtrado de lista|`VerDocentesView`|Invoca `DocentesController.filtrarDocentes(criterio)`|
|Nom, ape, DNI, user, email, pass|`Docente`|Encapsula atributos del docente|
|Acceso a datos de docentes|`DocenteRepository`|`obtenerTodos()`, `buscarPorCriterio()`|

### patrón de colaboración establecido

Este análisis sigue el **patrón metodológico universal** establecido para el proyecto:
- **Entrada estándar**: Desde `:Sistema Disponible` o `:Docente Abierto`
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

- **Origen**: Caso de uso detallado `verDocentes()`
- **Destino**: Base para diseño arquitectónico
- **Conexión**: Diagrama de contexto → Análisis de colaboración

## patrones aplicados

### repository pattern
`DocenteRepository` abstrae el acceso a datos, permitiendo diferentes implementaciones sin afectar el controlador.

### mvc pattern
Separación clara entre presentación (`VerDocentesView`), lógica de aplicación (`DocentesController`) y datos (`Docente`, `DocenteRepository`).

### navigation pattern
Flechas discontinuas indican opciones de navegación disponibles para el usuario, manteniendo flexibilidad en el flujo.