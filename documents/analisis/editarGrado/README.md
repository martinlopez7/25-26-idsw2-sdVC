# editarGrado > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarGrado/editarGrado.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `editarGrado()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar edición completa de grados con capacidad de modificación continua.

## diagrama de colaboración

<div align=center>

|![Análisis: editarGrado()](/images/analisis/editarGrado/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/editarGrado/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EditarGradoView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edición de grado
- Interactuar con el controlador para obtener datos del grado
- Presentar datos completos de edición del grado (nombre, código, lista de alumnos enlistados)
- Permitir solicitar modificación de campos específicos
- Mantener sesión de edición activa para modificaciones continuas
- Permitir solicitar guardar cambios, cancelar edición o eliminar grado

**Colaboraciones**:
- **Entrada**: Recibe `editarGrado(gradoId)` desde `:Grados Abierto` o `:Grado Abierto`
- **Control**: Se comunica con `GradosController`
- **Salida**: **<<include>>** `:Collaboration verGrados` para mostrar lista actualizada, o `:Collaboration eliminarGrado` para eliminar el grado

### clases de control

#### GradosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos del grado para edición
- Validar que el grado existe y es editable
- Manejar la lógica de modificación de campos del grado
- Procesar guardado de cambios tras modificaciones
- Coordinar navegación entre edición continua y finalización
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarGradoView`
- **Repositorio**: Delega operaciones de datos a `GradoRepository`

### clases de entidad (entity)

#### GradoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de grados
- Proporcionar método para obtener grado por ID con datos completos
- Implementar actualización de campos del grado
- Validar restricciones de integridad antes de guardar
- Gestionar relaciones con asignaturas y alumnos durante edición

**Colaboraciones**:
- **Control**: Responde a `GradosController`
- **Entidad**: Gestiona instancias de `Grado`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información completa del grado a editar
- Encapsular atributos: título, código, lista de alumnos matriculados
- Mantener relación con asignaturas y alumnos
- Validar cambios en campos específicos
- Mantener la integridad de los datos durante modificaciones continuas

**Colaboraciones**:
- **Repositorio**: Es gestionado por `GradoRepository`
- **Entidad**: Se relaciona con `Alumno` (un grado tiene muchos alumnos inscritos)

#### Alumno
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar los alumnos matriculados en el grado
- Encapsular atributos: nombre, apellidos, DNI
- Mantener la relación de pertenencia al grado durante la edición

**Colaboraciones**:
- **Entidad**: Se relaciona con `Grado` (muchos alumnos se cursan en un grado)

## flujo de colaboración principal

### secuencia: editar grado

1. **Inicio**: `:Grados Abierto` → `EditarGradoView.editarGrado(gradoId)`
2. **Carga**: `EditarGradoView` → `GradosController.cargarGradoParaEdicion(gradoId)`
3. **Obtención**: `GradosController` → `GradoRepository.obtenerPorId(gradoId) : Grado`
4. **Presentación**: `EditarGradoView` presenta datos completos del `Grado` para edición (nombre, código, lista de alumnos enlistados)
5. **Modificación**: Docente modifica campos en `EditarGradoView`
6. **Guardado**: `EditarGradoView` → `GradosController.guardarCambios(gradoModificado)`
7. **Persistencia**: `GradosController` → `GradoRepository.actualizar(grado)`
8. **Continuación**: 
   - **Edición continua**: Permanece en `EditarGradoView` para más modificaciones
   - **Guardar y salir**: `EditarGradoView` → **<<include>>** `:Collaboration verGrados.verGrados()`
   - **Cancelar edición**: `EditarGradoView` → `:Grados Abierto`
   - **Eliminar**: `EditarGradoView` → **<<include>>** `:Collaboration eliminarGrado.eliminarGrado(gradoId)`

## patrón de edición completa para grados - "el gordo"

### edición continua

Este análisis implementa edición completa que:
- **Presenta datos completos**: Todos los campos del grado disponibles para edición (nombre, código, lista de alumnos)
- **Permite modificación continua**: Múltiples ciclos de edición sin salir del contexto
- **Guarda cambios incrementales**: Cada modificación puede guardarse independientemente
- **Mantiene sesión activa**: El estado `GRADO_ABIERTO` se preserva durante la edición

### responsabilidades de edición continua

**EditarGradoView** maneja edición continua:
- **Presenta datos**: Información completa del grado con campos editables
- **Captura modificaciones**: Cambios en cualquier campo del grado
- **Mantiene contexto**: Sesión de edición activa para múltiples modificaciones
- **Permite guardado**: Guardado incremental sin salir del contexto

**GradosController** coordina edición continua:
- **Valida cambios**: Verifica que las modificaciones son válidas
- **Procesa incrementalmente**: Guarda cambios específicos sin afectar otros campos
- **Mantiene coherencia**: Verifica integridad durante modificaciones continuas
- **Gestiona navegación**: Permite continuar editando o finalizar

## patrones arquitectónicos aplicados

### patrón MVC para edición de grados

- **Model**: `Grado` + `Alumno` + `GradoRepository` (datos del grado, alumnos asociados y persistencia)
- **View**: `EditarGradoView` (edición continua e interacción)
- **Controller**: `GradosController` (coordinación y validación de edición)

### patrón Repository con edición continua

- **Abstracción de persistencia**: `GradoRepository` encapsula lógica de actualización
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Transacciones**: Puede implementar guardado transaccional por campos
- **Validaciones continuas**: Verifica restricciones en cada modificación

### edición continua con múltiples ciclos

- **Ciclo 1**: Presentar datos → Modificar → Guardar → Continuar
- **Ciclo 2**: Modificar más campos → Guardar → Continuar o Finalizar
- **Flexibilidad**: Docente controla cuándo finalizar la edición
- **Contexto preservado**: Estado `GRADO_ABIERTO` se mantiene durante todos los ciclos

## consideraciones de diseño específicas para grados

### reutilización del controlador

El diseño permite que `GradosController` sea reutilizado:
- **Compartido**: Con crearGrado(), eliminarGrado() y verGrados()
- **Método específico**: editarGrado() con capacidades de edición continua
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para edición continua de grados

### patrón include para navegación flexible

- **Separación de responsabilidades**: editarGrado() se enfoca en editar
- **Reutilización**: **<<include>>** verGrados() evita duplicar funcionalidad de listado
- **Múltiples entradas**: Funciona desde `:Grados Abierto` o `:Grado Abierto`
- **Navegación controlada**: Permite continuar editando o regresar a lista actualizada

### flexibilidad de edición continua

- **EditarGradoView** puede implementar:
  - **Edición por campos**: Modificación campo por campo
  - **Edición en bloque**: Modificación de múltiples campos simultáneamente
  - **Guardado incremental**: Guardado automático o manual por cambios
  - **Validación en tiempo real**: Verificación inmediata de cambios

### experiencia de usuario para "el gordo"

- **Información completa**: Muestra todos los datos editables del grado
- **Edición flexible**: Permite modificar cualquier combinación de campos
- **Sesión persistente**: Mantiene contexto de edición durante múltiples modificaciones
- **Navegación clara**: Opciones explícitas para continuar o finalizar edición

## validaciones de negocio para edición continua

### restricciones de integridad durante edición

**GradosController** debe verificar durante cada modificación:
- **Unicidad de título**: Título del grado no duplicado
- **Unicidad de código**: Código del grado no existente
- **Coherencia de datos**: Título y código no vacíos
- **Relaciones activas**: Verificar impacto en asignaturas asociadas si se modifican datos críticos

### manejo de errores en edición continua

- **Validación por campo**: Errores específicos para cada campo modificado
- **Preservación de cambios**: Mantener modificaciones válidas aunque otras fallen
- **Continuidad**: Permitir continuar editando después de corregir errores

## opciones de navegación disponibles

- **<<include>>** `verGrados()` → `:Grados Abierto`
- **<<include>>** `eliminarGrado(gradoId)` → `:Grados Abierto`
- **cancelar** → `:Grados Abierto`
