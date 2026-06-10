# eliminarAlumno > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarAlumno/eliminarAlumno.svg)|**Análisis**|[Diseño](/documents/diseño/eliminarAlumno/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25

## propósito

Análisis de colaboración del caso de uso `eliminarAlumno()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de alumnos con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarAlumno()](/images/analisis/eliminarAlumno/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarAlumno/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarAlumnoView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de alumno
- Presentar información completa del alumno a eliminar
- Mostrar nombre, apellidos y DNI del alumno
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarAlumno(alumnoId)` desde `:ALUMNOS_ABIERTO` o `:ALUMNO_ABIERTO`
- **Control**: Se comunica con `AlumnosController`
- **Salida**: **<<include>>** `:Collaboration verAlumnos()` tras eliminación o cancelación

### clases de control

#### AlumnosController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos del alumno a eliminar
- Validar que el alumno existe y puede ser eliminado
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarAlumnoView`
- **Repositorio**: Delega operaciones de datos a `AlumnoRepository`

### clases de entidad (entity)

#### AlumnoRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de alumnos
- Proporcionar método para obtener alumno por ID
- Implementar eliminación del alumno
- Verificar restricciones de integridad antes de eliminar

**Colaboraciones**:
- **Control**: Responde a `AlumnosController`
- **Entidad**: Gestiona instancias de `Alumno`

#### Alumno
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información del alumno a eliminar
- Encapsular atributos: nombre, apellidos, DNI
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AlumnoRepository`

## flujo de colaboración principal

1. **Inicio**: `:ALUMNOS_ABIERTO` o `:ALUMNO_ABIERTO` → `EliminarAlumnoView.eliminarAlumno(alumnoId)`
2. **Carga**: `EliminarAlumnoView` → `AlumnosController.cargarAlumnoParaEliminacion(alumnoId)`
3. **Obtención**: `AlumnosController` → `AlumnoRepository.obtenerPorId(alumnoId) : Alumno`
4. **Presentación**: `EliminarAlumnoView` presenta información del `Alumno` (nombre, apellidos, DNI) con advertencia
5. **Confirmación**: Docente confirma o cancela en `EliminarAlumnoView`
6. **Eliminación**: `EliminarAlumnoView` → `AlumnosController.eliminarAlumno(alumnoId)`
7. **Persistencia**: `AlumnosController` → `AlumnoRepository.eliminar(alumnoId)`
8. **Finalización**: `EliminarAlumnoView` → **<<include>>** `:Collaboration verAlumnos()`

## patrón de eliminación segura para alumnos

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Nombre, apellidos y DNI del alumno
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarAlumnoView** maneja confirmación:
- **Presenta datos**: Información completa del alumno
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**AlumnosController** valida eliminación:
- **Verifica existencia**: Alumno existe y es válido
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de alumnos

- **Model**: `Alumno` + `AlumnoRepository` (datos del alumno y eliminación)
- **View**: `EliminarAlumnoView` (confirmación e interacción)
- **Controller**: `AlumnosController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarAlumno() se enfoca en eliminar
- **Reutilización**: **<<include>>** verAlumnos() evita duplicar funcionalidad de listado
- **Doble entrada**: Funciona desde `:ALUMNOS_ABIERTO` o `:ALUMNO_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada
