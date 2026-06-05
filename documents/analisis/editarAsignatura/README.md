# Generador de Exámenes > editarAsignatura > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarAsignatura/editarAsignatura.svg)|**Análisis**|[Diseño](/documents/diseño/editarAsignatura/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Generador de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `editarAsignatura()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar edición completa de asignaturas con filosofía "el gordo".

## diagrama de colaboración

<div align=center>

|![Análisis: editarAsignatura()](/images/analisis/editarAsignatura/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/editarAsignatura/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EditarAsignaturaView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edición de asignatura
- Interactuar con el controlador para obtener datos completos de la asignatura
- Presentar formulario de edición con todos los campos disponibles
- Capturar modificaciones del docente
- Mantener sesión de edición activa para modificaciones continuas
- Manejar las acciones de ver preguntas, generar examen, eliminar y cancelar

**Colaboraciones**:
- **Entrada**: Recibe `editarAsignatura(asignaturaId)` desde `:Asignaturas Abierto`, `:Asignatura Abierto`, `:Preguntas Contextuales Abierto`, `:Examenes Asignados Contextuales` o desde `:Collaboration CrearAsignatura`
- **Control**: Se comunica con `AsignaturaController`
- **Salida**: **&lt;&lt;include&gt;&gt;** `:Collaboration verAsignaturas()`, `:Collaboration verPreguntas()`, `:Collaboration generarExamenes()`, `:Collaboration eliminarAsignatura()`

### clases de control

#### AsignaturaController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos completos de la asignatura a editar
- Validar que la asignatura existe y puede ser modificada
- Manejar la lógica de modificación de campos
- Procesar guardado de cambios tras confirmación
- Coordinar sesión de edición continua
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarAsignaturaView`
- **Repositorio**: Delega operaciones de datos a `AsignaturaRepository`

### clases de entidad (entity)

#### AsignaturaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de asignaturas
- Proporcionar método para obtener asignatura completa por ID
- Implementar actualización de campos de la asignatura
- Verificar restricciones de integridad durante modificación
- Mantener relación con batería de preguntas

**Colaboraciones**:
- **Control**: Responde a `AsignaturaController`
- **Entidad**: Gestiona instancias de `Asignatura` y `BateriaDePreguntas`

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información completa de la asignatura a editar
- Encapsular atributos: título, código, curso académico, alumnos matriculados, grados asociados
- Mantener relación con batería de preguntas
- Validar cambios en datos de la asignatura
- Mantener la integridad de los datos durante modificación continua

**Colaboraciones**:
- **Repositorio**: Es gestionada por `AsignaturaRepository`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información de un grado académico
- Mantener asociación con asignaturas

**Colaboraciones**:
- **Entidad**: Se muestra en el formulario de edición de asignatura

#### BateriaDePreguntas
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la batería de preguntas asociada a la asignatura
- Contener referencias a las preguntas de la asignatura
- Mantener relación de composición con la asignatura

**Colaboraciones**:
- **Repositorio**: Es gestionada junto con `AsignaturaRepository`

## flujo de colaboración principal

### secuencia: editar asignatura

1. **Inicio**: `:Asignaturas Abierto`, `:Asignatura Abierto`, `:Preguntas Contextuales Abierto`, `:Examenes Asignados Contextuales` o `:CrearAsignatura` → `EditarAsignaturaView.editarAsignatura(asignaturaId)`
2. **Carga**: `EditarAsignaturaView` → `AsignaturaController.cargarAsignaturaParaEdición(asignaturaId)`
3. **Obtención**: `AsignaturaController` → `AsignaturaRepository.obtenerPorId(asignaturaId) : Asignatura`
4. **Presentación**: `EditarAsignaturaView` presenta formulario con datos completos de la `Asignatura`
5. **Modificación**: Docente modifica campos en `EditarAsignaturaView`
6. **Actualización**: `EditarAsignaturaView` → `AsignaturaController.modificarCampos(asignaturaId, cambios)`
7. **Persistencia**: `AsignaturaController` → `AsignaturaRepository.actualizar(asignatura)`
8. **Opciones**:
    - **Ver preguntas**: `EditarAsignaturaView` → **&lt;&lt;include&gt;&gt;** `:Collaboration verPreguntas(asignaturaId)`
    - **Generar examen**: `EditarAsignaturaView` → **&lt;&lt;include&gt;&gt;** `:Collaboration generarExamenes(asignaturaId)`
    - **Eliminar**: `EditarAsignaturaView` → **&lt;&lt;include&gt;&gt;** `:Collaboration eliminarAsignatura(asignaturaId)`
    - **Cancelar**: `EditarAsignaturaView` → **&lt;&lt;include&gt;&gt;** `:Collaboration verAsignaturas()`

## patrón de edición completa para asignaturas

### filosofía "el gordo"

Este análisis implementa edición integral que:
- **Datos completos**: Todos los campos de la asignatura disponibles (título, código, curso académico, grados asociados)
- **Edición continua**: Permite múltiples modificaciones en sesión sin perder datos
- **Persistencia flexible**: Guarda cuando docente solicita
- **Navegación flexible**: Múltiples opciones de salida según contexto de entrada

### responsabilidades de edición completa

**EditarAsignaturaView** maneja edición completa:
- **Presenta datos completos**: Información integral de la asignatura
- **Permite modificaciones**: Campos editables de forma interactiva
- **Mantiene sesión**: Edición continua sin perder cambios
- **Controla salida**: Ver preguntas, generar examen, eliminar o cancelar

**AsignaturaController** coordina modificaciones:
- **Carga completa**: Obtiene asignatura del repositorio
- **Valida cambios**: Verifica integridad de datos
- **Procesa actualizaciones**: Actualiza asignatura según cambios
- **Facilita continuidad**: Mantiene sesión de edición activa

## patrones arquitectónicos aplicados

### patrón MVC para edición de asignaturas

- **Model**: `Asignatura` + `AsignaturaRepository` + `BateriaDePreguntas` (datos completos y modificación)
- **View**: `EditarAsignaturaView` (edición interactiva y presentación completa)
- **Controller**: `AsignaturaController` (coordinación y validación académica integral)

### patrón Repository con modificación académica

- **Abstracción de edición**: `AsignaturaRepository` encapsula lógica de actualización
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Flexibilidad**: Puede implementar diferentes estrategias de actualización
- **Validaciones académicas**: Verifica restricciones de integridad

### edición continua para asignaturas

- **Sesión persistente**: Mantiene estado de edición activo
- **Modificaciones incrementales**: Permite cambios múltiples sin salir
- **Persistencia controlada**: Guarda cuando docente decide
- **Navegación contextual**: Múltiples opciones según el flujo del usuario

## consideraciones de diseño específicas para asignaturas

### múltiples puntos de entrada

El diseño permite entrada desde múltiples contextos:
- **Desde lista**: `:Asignaturas Abierto` → edición directa
- **Desde detalle**: `:Asignatura Abierto` → continuar edición
- **Desde preguntas contextuales**: `:Preguntas Contextuales Abierto` → editar desde contexto de preguntas
- **Desde examenes asignados**: `:Examenes Asignados Contextuales` → editar desde contexto de exámenes
- **Desde creación**: `:CrearAsignatura` → edición automática (C→U)

### patrón include para navegación

- **Separación de responsabilidades**: editarAsignatura() se enfoca en editar
- **Reutilización**: **&lt;&lt;include&gt;&gt;** verAsignaturas(), verPreguntas(), generarExamenes(), eliminarAsignatura() evita duplicar funcionalidad
- **Navegación contextual**: Regresa al contexto apropiado según la acción del usuario

### gestión de grados

- **Asociación múltiple**: Una asignatura puede tener varios grados asociados
- **Presentación**: Los grados asociados se muestran en el formulario de edición
- **Persistencia**: Los cambios en grados se guardan con la asignatura

### experiencia de usuario académica

- **Información completa**: Muestra todos los datos de la asignatura
- **Modificación flexible**: Permite editar cualquier campo disponible
- **Sesión continua**: No pierde trabajo durante modificaciones múltiples
- **Control total**: Usuario decide cuándo guardar y qué acción realizar al salir

## validaciones de negocio académicas

### restricciones de integridad para edición

**AsignaturaController** debe verificar:
- **Existencia de la asignatura**: Asignatura válida y encontrada
- **Unicidad de código**: No duplicar código con otras asignaturas
- **Campos obligatorios**: Título, código y curso académico no vacíos
- **Permisos de docente**: Docente autorizado para modificar asignaturas

### manejo de errores académicos

- **Asignatura no encontrada**: Mensaje informativo
- **Código duplicado**: Explicación de conflicto con asignatura existente
- **Campos vacíos**: Mensaje sobre campos obligatorios requeridos
- **Error de sistema**: Manejo graceful de fallos de persistencia

## diferencias con otros casos CRUD de asignaturas

### editarAsignatura() vs crearAsignatura()

**editarAsignatura():**
- **Objetivo**: Modificación de datos académicos completos
- **Interacción**: Lectura + escritura continua de todos los campos
- **Validaciones**: Restricciones académicas de contenido completo
- **Resultado**: Asignatura actualizada con información completa

**crearAsignatura():**
- **Objetivo**: Creación básica con datos mínimos
- **Interacción**: Solicitud mínima + transferencia automática
- **Validaciones**: Restricciones básicas de creación
- **Resultado**: Asignatura básica creada + edición completa automática

### editarAsignatura() vs eliminarAsignatura()

**editarAsignatura():**
- **Objetivo**: Modificación de información académica
- **Interacción**: Lectura + escritura continua
- **Validaciones**: Restricciones académicas de contenido
- **Resultado**: Asignatura actualizada en sistema

**eliminarAsignatura():**
- **Objetivo**: Confirmación y eliminación
- **Interacción**: Solo lectura + confirmación
- **Validaciones**: Restricciones de integridad (preguntas asociadas)
- **Resultado**: Asignatura removida del sistema

### complementariedad CRUD para asignaturas

- **crearAsignatura()**: "El delgado" - añade asignatura básica al sistema
- **editarAsignatura()**: "El gordo" - completa y modifica información académica completa
- **eliminarAsignatura()**: Remueve asignaturas del sistema con confirmación
- **verAsignaturas()**: Lista y selecciona asignaturas para operaciones