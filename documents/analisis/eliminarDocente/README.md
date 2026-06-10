# eliminarDocente > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/eliminarDocente/eliminarDocente.svg)|**Análisis**|[Diseño](/documents/diseño/eliminarDocente/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24

## propósito

Análisis de colaboración del caso de uso `eliminarDocente()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar eliminación segura de docentes con confirmación previa.

## diagrama de colaboración

<div align=center>

|![Análisis: eliminarDocente()](/images/analisis/eliminarDocente/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/eliminarDocente/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EliminarDocenteView
**Estereotipo**: Vista (Boundary)
**Responsabilidades**:
- Recibir la solicitud de eliminación de docente
- Presentar información completa del docente a eliminar
- Mostrar advertencia de eliminación irreversible
- Permitir confirmación o cancelación de eliminación

**Colaboraciones**:
- **Entrada**: Recibe `eliminarDocente(docenteId)` desde `:DOCENTE_ABIERTO` o `:DOCENTES_ABIERTO`
- **Control**: Se comunica con `DocentesController`
- **Salida**: **<<include>>** `:Collaboration verDocentes()` tras eliminación o cancelación

### clases de control

#### DocentesController
**Estereotipo**: Control
**Responsabilidades**:
- Coordinar la carga de datos del docente a eliminar
- Validar que el docente existe y puede ser eliminado
- Procesar la eliminación tras confirmación
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EliminarDocenteView`
- **Repositorio**: Delega operaciones de datos a `DocenteRepository`

### clases de entidad (entity)

#### DocenteRepository
**Estereotipo**: Entidad
**Responsabilidades**:
- Abstraer el acceso a datos de docentes
- Proporcionar método para obtener docente por ID
- Implementar eliminación del docente
- Verificar restricciones de integridad

**Colaboraciones**:
- **Control**: Responde a `DocentesController`
- **Entidad**: Gestiona instancias de `Docente`

#### Docente
**Estereotipo**: Entidad
**Responsabilidades**:
- Representar la información del docente a eliminar
- Encapsular atributos: nombre, apellidos, DNI, nombre de usuario, email, password
- Mantener la integridad de los datos durante eliminación

**Colaboraciones**:
- **Repositorio**: Es gestionado por `DocenteRepository`

## flujo de colaboración principal

1. **Inicio**: `:DOCENTE_ABIERTO` o `:DOCENTES_ABIERTO` → `EliminarDocenteView.eliminarDocente(docenteId)`
2. **Carga**: `EliminarDocenteView` → `DocentesController.cargarDocenteParaEliminacion(docenteId)`
3. **Obtención**: `DocentesController` → `DocenteRepository.obtenerPorId(docenteId) : Docente`
4. **Presentación**: `EliminarDocenteView` presenta información del `Docente` con advertencia
5. **Confirmación**: Administrador confirma o cancela en `EliminarDocenteView`
6. **Eliminación**: `EliminarDocenteView` → `DocentesController.eliminarDocente(docenteId)`
7. **Persistencia**: `DocentesController` → `DocenteRepository.eliminar(docenteId)`
8. **Finalización**: `EliminarDocenteView` → **<<include>>** `:Collaboration verDocentes()`

## patrón de eliminación segura para docentes

### confirmación obligatoria

Este análisis implementa eliminación con confirmación que:
- **Muestra información completa**: Todos los datos del docente
- **Advierte sobre irreversibilidad**: Mensaje claro de advertencia
- **Requiere confirmación explícita**: No permite eliminación accidental

### responsabilidades de seguridad

**EliminarDocenteView** maneja confirmación:
- **Presenta datos**: Información completa del docente
- **Muestra advertencias**: Mensajes de eliminación irreversible
- **Captura decisión**: Confirmación o cancelación explícita

**DocentesController** valida eliminación:
- **Verifica existencia**: Docente existe y es válido
- **Procesa eliminación**: Solo tras confirmación explícita

## patrones arquitectónicos aplicados

### patrón MVC para eliminación de docentes

- **Model**: `Docente` + `DocenteRepository` (datos del docente y eliminación)
- **View**: `EliminarDocenteView` (confirmación e interacción)
- **Controller**: `DocentesController` (coordinación y validación)

### patrón include para navegación

- **Separación de responsabilidades**: eliminarDocente() se enfoca en eliminar
- **Reutilización**: **<<include>>** verDocentes() evita duplicar funcionalidad de listado
- **Doble entrada**: Funciona desde `:DOCENTE_ABIERTO` o `:DOCENTES_ABIERTO`
- **Navegación consistente**: Regresa siempre a lista actualizada