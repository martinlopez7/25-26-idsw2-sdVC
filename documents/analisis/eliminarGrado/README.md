# eliminarGrado > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarGrado/eliminarGrado.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25

## propósito

Análisis de colaboración del caso de uso `eliminarGrado()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de grados con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarGrado()](/images/analisis/eliminarGrado/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarGrado/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarGradoView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de grado
- Presentar información completa del grado a eliminar
- Mostrar nombre, código y alumnos enlistados en el grado
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarGrado(gradoId)` desde `:GRADOS_ABIERTO` o `:GRADO_ABIERTO`
- **Control**: Se comunica con `GradosController`
- **Salida**: **<<include>>** `:Collaboration verGrados()` tras eliminación o cancelación

### clases de control

#### GradosController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos del grado a eliminar
- Validar que el grado existe y puede ser eliminado
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarGradoView`
- **Repositorio**: Delega operaciones de datos a `GradoRepository`

### clases de entidad (entity)

#### GradoRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de grados
- Proporcionar método para obtener grado por ID
- Implementar eliminación del grado
- Verificar restricciones de integridad antes de eliminar

**Colaboraciones**:
- **Control**: Responde a `GradosController`
- **Entidad**: Gestiona instancias de `Grado`

#### Grado
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información del grado a eliminar
- Encapsular atributos: título, código, lista de alumnos matriculados
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `GradoRepository`

#### Alumno
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar los alumnos matriculados en el grado a eliminar
- Encapsular atributos: nombre, apellidos, DNI
- Mantener la relación de pertenencia al grado durante la verificación de eliminación

**Colaboraciones**:
- **Entidad**: Se relaciona con `Grado` (muchos alumnos se cursan en un grado)

## flujo de colaboración principal

1. **Inicio**: `:GRADOS_ABIERTO` o `:GRADO_ABIERTO` → `EliminarGradoView.eliminarGrado(gradoId)`
2. **Carga**: `EliminarGradoView` → `GradosController.cargarGradoParaEliminacion(gradoId)`
3. **Obtención**: `GradosController` → `GradoRepository.obtenerPorId(gradoId) : Grado`
4. **Presentación**: `EliminarGradoView` presenta información del `Grado` (nombre, código, alumnos enlistados) con advertencia
5. **Confirmación**: Docente confirma o cancela en `EliminarGradoView`
6. **Eliminación**: `EliminarGradoView` → `GradosController.eliminarGrado(gradoId)`
7. **Persistencia**: `GradosController` → `GradoRepository.eliminar(gradoId)`
8. **Finalización**: `EliminarGradoView` → **<<include>>** `:Collaboration verGrados()`

## patrón de eliminación segura para grados

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Nombre, código y alumnos enlistados del grado
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarGradoView** maneja confirmación:
- **Presenta datos**: Información completa del grado con alumnos matriculados
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**GradosController** valida eliminación:
- **Verifica existencia**: Grado existe y es válido
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de grados

- **Model**: `Grado` + `Alumno` + `GradoRepository` (datos del grado, alumnos asociados y eliminación)
- **View**: `EliminarGradoView` (confirmación e interacción)
- **Controller**: `GradosController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarGrado() se enfoca en eliminar
- **Reutilización**: **<<include>>** verGrados() evita duplicar funcionalidad de listado
- **Doble entrada**: Funciona desde `:GRADOS_ABIERTO` o `:GRADO_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada
