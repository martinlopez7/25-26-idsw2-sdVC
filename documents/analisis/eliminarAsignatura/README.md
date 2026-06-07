# eliminarAsignatura > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarAsignatura/eliminarAsignatura.svg)|**Análisis**|[Diseño](/documents/diseño/eliminarAsignatura/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26

## propósito

Análisis de colaboración del caso de uso `eliminarAsignatura()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de asignaturas con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarAsignatura()](/images/analisis/eliminarAsignatura/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarAsignatura/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarAsignaturaView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de asignatura
- Presentar información completa de la asignatura a eliminar
- Mostrar nombre, código, curso académico y batería de preguntas
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarAsignatura(asignaturaId)` desde `:ASIGNATURAS_ABIERTO` o `:ASIGNATURA_ABIERTO`
- **Control**: Se comunica con `AsignaturasController`
- **Salida**: **<<include>>** `:Collaboration verAsignaturas()` tras eliminación o cancelación

### clases de control

#### AsignaturasController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos de la asignatura a eliminar
- Validar que la asignatura existe y puede ser eliminada
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarAsignaturaView`
- **Repositorio**: Delega operaciones de datos a `AsignaturaRepository`

### clases de entidad (entity)

#### AsignaturaRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de asignaturas
- Proporcionar método para obtener asignatura por ID
- Implementar eliminación de la asignatura
- Verificar restricciones de integridad antes de eliminar

**Colaboraciones**:
- **Control**: Responde a `AsignaturasController`
- **Entidad**: Gestiona instancias de `Asignatura`

#### Asignatura
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información de la asignatura a eliminar
- Encapsular atributos: título, código, curso académico
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AsignaturaRepository`

#### Grado
**Estereotipo**: Entidad
**Responsabilidades**:
- Mostrar grados asociados a las asignaturas

#### Alumno
**Estereotipo**: Entidad
**Responsabilidades**:
- Mostrar alumnos matriculados a las asignaturas

#### Pregunta
**Estereotipo**: Entidad
**Responsabilidades**:
- Mostrar preguntas de la asignatura


## flujo de colaboración principal

1. **Inicio**: `:ASIGNATURAS_ABIERTO` o `:ASIGNATURA_ABIERTO` → `EliminarAsignaturaView.eliminarAsignatura(asignaturaId)`
2. **Carga**: `EliminarAsignaturaView` → `AsignaturasController.cargarAsignaturaParaEliminacion(asignaturaId)`
3. **Obtención**: `AsignaturasController` → `AsignaturaRepository.obtenerPorId(asignaturaId) : Asignatura`
4. **Presentación**: `EliminarAsignaturaView` presenta información de la `Asignatura` (título, código, curso académico, batería de preguntas) con advertencia
5. **Confirmación**: Docente confirma o cancela en `EliminarAsignaturaView`
6. **Eliminación**: `EliminarAsignaturaView` → `AsignaturasController.eliminarAsignatura(asignaturaId)`
7. **Persistencia**: `AsignaturasController` → `AsignaturaRepository.eliminar(asignaturaId)`
8. **Finalización**: `EliminarAsignaturaView` → **<<include>>** `:Collaboration verAsignaturas()`

## patrón de eliminación segura para asignaturas

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Título, código, curso académico, alumnos matriculados, grados asociados y preguntas de la asignatura
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarAsignaturaView** maneja confirmación:
- **Presenta datos**: Información completa de la asignatura
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**AsignaturasController** valida eliminación:
- **Verifica existencia**: Asignatura existe y es válida
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de asignaturas

- **Model**: `Asignatura` + `AsignaturaRepository` (datos de la asignatura y eliminación)
- **View**: `EliminarAsignaturaView` (confirmación e interacción)
- **Controller**: `AsignaturasController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarAsignatura() se enfoca en eliminar
- **Reutilización**: **<<include>>** verAsignaturas() evita duplicar funcionalidad de listado
- **Doble entrada**: Funciona desde `:ASIGNATURAS_ABIERTO` o `:ASIGNATURA_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada