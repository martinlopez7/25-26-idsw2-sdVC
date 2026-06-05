# editarDocente > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarDocente/editarDocente.svg)|**Análisis**|[Diseño](/documents/diseño/editarDocente/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `editarDocente()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar edición completa de docentes con capacidad de modificación continua.

## diagrama de colaboración

<div align=center>

|![Análisis: editarDocente()](/images/analisis/editarDocente/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/editarDocente/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EditarDocenteView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edición de docente
- Interactuar con el controlador para obtener datos del docente
- Presentar datos completos de edición del docente
- Permitir solicitar modificación de campos específicos
- Mantener sesión de edición activa para modificaciones continuas
- Permitir solicitar guardar cambios o cancelar edición

**Colaboraciones**:
- **Entrada**: Recibe `editarDocente(docenteId)` desde `:Docentes Abierto` o `:Docente Abierto`
- **Control**: Se comunica con `DocentesController`
- **Salida**: **<<include>>** `:Collaboration verDocentes` para mostrar lista actualizada o mantiene `:Docente Abierto`, o `:Collaboration eliminarDocente` para eliminar el docente.

### clases de control

#### DocentesController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos del docente para edición
- Validar que el docente existe y es editable
- Manejar la lógica de modificación de campos del docente
- Procesar guardado de cambios tras modificaciones
- Coordinar navegación entre edición continua y finalización
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarDocenteView`
- **Repositorio**: Delega operaciones de datos a `DocenteRepository`

### clases de entidad (entity)

#### DocenteRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de docentes
- Proporcionar método para obtener docente por ID con datos completos
- Implementar actualización de campos del docente
- Validar restricciones de integridad antes de guardar
- Gestionar relaciones con asignaturas asignadas durante edición

**Colaboraciones**:
- **Control**: Responde a `DocentesController`
- **Entidad**: Gestiona instancias de `Docente`

#### Docente
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información completa del docente a editar
- Encapsular atributos: nombre, apellidos, DNI, nombre de usuario, email, password
- Mantener relación con asignaturas asignadas
- Validar cambios en campos específicos
- Mantener la integridad de los datos durante modificaciones continuas

**Colaboraciones**:
- **Repositorio**: Es gestionado por `DocenteRepository`

## flujo de colaboración principal

### secuencia: editar docente

1. **Inicio**: `:Docentes Abierto` → `EditarDocenteView.editarDocente(docenteId)`
2. **Carga**: `EditarDocenteView` → `DocentesController.cargarDocenteParaEdicion(docenteId)`
3. **Obtención**: `DocentesController` → `DocenteRepository.obtenerPorId(docenteId) : Docente`
4. **Presentación**: `EditarDocenteView` presenta datos completos del `Docente` para edición
5. **Modificación**: Administrador modifica campos en `EditarDocenteView`
6. **Guardado**: `EditarDocenteView` → `DocentesController.guardarCambios(docenteModificado)`
7. **Persistencia**: `DocentesController` → `DocenteRepository.actualizar(docente)`
8. **Continuación**: 
   - **Edición continua**: Permanece en `EditarDocenteView` para más modificaciones
   - **Finalización**: `EditarDocenteView` → **<<include>>** `:Collaboration verDocentes.verDocentes()` o `EditarDocenteView` → **<<include>>** o`:Collaboration eliminarDocente.eliminarDocente(docenteId)`

## patrón de edición completa para docentes - "el gordo"

### edición continua

Este análisis implementa edición completa que:
- **Presenta datos completos**: Todos los campos del docente disponibles para edición
- **Permite modificación continua**: Múltiples ciclos de edición sin salir del contexto
- **Guarda cambios incrementales**: Cada modificación puede guardarse independientemente
- **Mantiene sesión activa**: El estado `DOCENTE_ABIERTO` se preserva durante la edición

### responsabilidades de edición continua

**EditarDocenteView** maneja edición continua:
- **Presenta datos**: Información completa del docente con campos editables
- **Captura modificaciones**: Cambios en cualquier campo del docente
- **Mantiene contexto**: Sesión de edición activa para múltiples modificaciones
- **Permite guardado**: Guardado incremental sin salir del contexto

**DocentesController** coordina edición continua:
- **Valida cambios**: Verifica que las modificaciones son válidas
- **Procesa incrementalmente**: Guarda cambios específicos sin afectar otros campos
- **Mantiene coherencia**: Verifica integridad durante modificaciones continuas
- **Gestiona navegación**: Permite continuar editando o finalizar

## patrones arquitectónicos aplicados

### patrón MVC para edición de docentes

- **Model**: `Docente` + `DocenteRepository` (datos del docente y persistencia)
- **View**: `EditarDocenteView` (edición continua e interacción)
- **Controller**: `DocentesController` (coordinación y validación de edición)

### patrón Repository con edición continua

- **Abstracción de persistencia**: `DocenteRepository` encapsula lógica de actualización
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Transacciones**: Puede implementar guardado transaccional por campos
- **Validaciones continuas**: Verifica restricciones en cada modificación

### edición continua con múltiples ciclos

- **Ciclo 1**: Presentar datos → Modificar → Guardar → Continuar
- **Ciclo 2**: Modificar más campos → Guardar → Continuar o Finalizar
- **Flexibilidad**: Administrador controla cuándo finalizar la edición
- **Contexto preservado**: Estado `DOCENTE_ABIERTO` se mantiene durante todos los ciclos

## consideraciones de diseño específicas para docentes

### reutilización del controlador

El diseño permite que `DocentesController` sea reutilizado:
- **Compartido**: Con crearDocente() y eliminarDocente() y verDocentes()
- **Método específico**: editarDocente() con capacidades de edición continua
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para edición continua de docentes

### patrón include para navegación flexible

- **Separación de responsabilidades**: editarDocente() se enfoca en editar
- **Reutilización**: **<<include>>** verDocentes() evita duplicar funcionalidad de listado
- **Múltiples entradas**: Funciona desde `:Docentes Abierto` o `:Docente Abierto`
- **Navegación controlada**: Permite continuar editando o regresar a lista actualizada

### flexibilidad de edición continua

- **EditarDocenteView** puede implementar:
  - **Edición por campos**: Modificación campo por campo
  - **Edición en bloque**: Modificación de múltiples campos simultáneamente
  - **Guardado incremental**: Guardado automático o manual por cambios
  - **Validación en tiempo real**: Verificación inmediata de cambios

### experiencia de usuario para "el gordo"

- **Información completa**: Muestra todos los datos editables del docente
- **Edición flexible**: Permite modificar cualquier combinación de campos
- **Sesión persistente**: Mantiene contexto de edición durante múltiples modificaciones
- **Navegación clara**: Opciones explícitas para continuar o finalizar edición

## validaciones de negocio para edición continua

### restricciones de integridad durante edición

**DocentesController** debe verificar durante cada modificación:
- **Unicidad de DNI**: DNI del docente no duplicado
- **Unicidad de nombre de usuario**: Nombre de usuario no existente
- **Unicidad de email**: Email no registrado en el sistema
- **Coherencia de datos**: Nombres y apellidos no vacíos

### manejo de errores en edición continua

- **Validación por campo**: Errores específicos para cada campo modificado
- **Preservación de cambios**: Mantener modificaciones válidas aunque otras fallen
- **Continuidad**: Permitir continuar editando después de corregir errores

## opciones de navegación disponibles

- **<<include>>** `verDocentes()` → `:Docentes Abierto`
- **<<include>>** `eliminarDocente(docenteId)` → `:Docentes Abierto`
- **cancelar** → `:Docentes Abierto`