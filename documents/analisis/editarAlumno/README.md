# editarAlumno > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarAlumno/editarAlumno.svg)|**Análisis**|Diseño|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `editarAlumno()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar edición completa de alumnos con capacidad de modificación continua.

## diagrama de colaboración

<div align=center>

|![Análisis: editarAlumno()](/images/analisis/editarAlumno/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/editarAlumno/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EditarAlumnoView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edición de alumno
- Interactuar con el controlador para obtener datos del alumno
- Presentar datos completos de edición del alumno (DNI, nombre, apellidos)
- Permitir solicitar modificación de campos específicos
- Mantener sesión de edición activa para modificaciones continuas
- Permitir solicitar guardar cambios, cancelar edición o eliminar alumno

**Colaboraciones**:
- **Entrada**: Recibe `editarAlumno(alumnoId)` desde `:Alumnos Abierto`, `:Alumno Abierto` o desde `:Collaboration CrearAlumno`
- **Control**: Se comunica con `AlumnosController`
- **Salida**: **<<include>>** `:Collaboration verAlumnos` para mostrar lista actualizada, o `:Collaboration eliminarAlumno` para eliminar el alumno

### clases de control

#### AlumnosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos del alumno para edición
- Validar que el alumno existe y es editable
- Manejar la lógica de modificación de campos del alumno
- Procesar guardado de cambios tras modificaciones
- Coordinar navegación entre edición continua y finalización
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarAlumnoView`
- **Repositorio**: Delega operaciones de datos a `AlumnoRepository`

### clases de entidad (entity)

#### AlumnoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de alumnos
- Proporcionar método para obtener alumno por ID con datos completos
- Implementar actualización de campos del alumno
- Validar restricciones de integridad antes de guardar
- Gestionar relaciones con asignaturas y grados durante edición

**Colaboraciones**:
- **Control**: Responde a `AlumnosController`
- **Entidad**: Gestiona instancias de `Alumno`

#### Alumno
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información completa del alumno a editar
- Encapsular atributos: DNI, nombre, apellidos
- Mantener relación con asignaturas matriculadas y grado cursado
- Validar cambios en campos específicos
- Mantener la integridad de los datos durante modificaciones continuas

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AlumnoRepository`

## flujo de colaboración principal

### secuencia: editar alumno

1. **Inicio**: `:Alumnos Abierto` → `EditarAlumnoView.editarAlumno(alumnoId)`
2. **Carga**: `EditarAlumnoView` → `AlumnosController.cargarAlumnoParaEdicion(alumnoId)`
3. **Obtención**: `AlumnosController` → `AlumnoRepository.obtenerPorId(alumnoId) : Alumno`
4. **Presentación**: `EditarAlumnoView` presenta datos completos del `Alumno` para edición (DNI, nombre, apellidos)
5. **Modificación**: Docente modifica campos en `EditarAlumnoView`
6. **Guardado**: `EditarAlumnoView` → `AlumnosController.guardarCambios(alumnoModificado)`
7. **Persistencia**: `AlumnosController` → `AlumnoRepository.actualizar(alumno)`
8. **Continuación**: 
   - **Edición continua**: Permanece en `EditarAlumnoView` para más modificaciones
   - **Guardar y salir**: `EditarAlumnoView` → **<<include>>** `:Collaboration verAlumnos.verAlumnos()`
   - **Cancelar edición**: `EditarAlumnoView` → `:Alumnos Abierto`
   - **Eliminar**: `EditarAlumnoView` → **<<include>>** `:Collaboration eliminarAlumno.eliminarAlumno(alumnoId)`

## patrón de edición completa para alumnos - "el gordo"

### edición continua

Este análisis implementa edición completa que:
- **Presenta datos completos**: Todos los campos del alumno disponibles para edición (DNI, nombre, apellidos)
- **Permite modificación continua**: Múltiples ciclos de edición sin salir del contexto
- **Guarda cambios incrementales**: Cada modificación puede guardarse independientemente
- **Mantiene sesión activa**: El estado `ALUMNO_ABIERTO` se preserva durante la edición

### responsabilidades de edición continua

**EditarAlumnoView** maneja edición continua:
- **Presenta datos**: Información completa del alumno con campos editables
- **Captura modificaciones**: Cambios en cualquier campo del alumno
- **Mantiene contexto**: Sesión de edición activa para múltiples modificaciones
- **Permite guardado**: Guardado incremental sin salir del contexto

**AlumnosController** coordina edición continua:
- **Valida cambios**: Verifica que las modificaciones son válidas
- **Procesa incrementalmente**: Guarda cambios específicos sin afectar otros campos
- **Mantiene coherencia**: Verifica integridad durante modificaciones continuas
- **Gestiona navegación**: Permite continuar editando o finalizar

## patrones arquitectónicos aplicados

### patrón MVC para edición de alumnos

- **Model**: `Alumno` + `AlumnoRepository` (datos del alumno y persistencia)
- **View**: `EditarAlumnoView` (edición continua e interacción)
- **Controller**: `AlumnosController` (coordinación y validación de edición)

### patrón Repository con edición continua

- **Abstracción de persistencia**: `AlumnoRepository` encapsula lógica de actualización
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Transacciones**: Puede implementar guardado transaccional por campos
- **Validaciones continuas**: Verifica restricciones en cada modificación

### edición continua con múltiples ciclos

- **Ciclo 1**: Presentar datos → Modificar → Guardar → Continuar
- **Ciclo 2**: Modificar más campos → Guardar → Continuar o Finalizar
- **Flexibilidad**: Docente controla cuándo finalizar la edición
- **Contexto preservado**: Estado `ALUMNO_ABIERTO` se mantiene durante todos los ciclos

## consideraciones de diseño específicas para alumnos

### reutilización del controlador

El diseño permite que `AlumnosController` sea reutilizado:
- **Compartido**: Con crearAlumno(), eliminarAlumno() y verAlumnos()
- **Método específico**: editarAlumno() con capacidades de edición continua
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para edición continua de alumnos

### patrón include para navegación flexible

- **Separación de responsabilidades**: editarAlumno() se enfoca en editar
- **Reutilización**: **<<include>>** verAlumnos() evita duplicar funcionalidad de listado
- **Múltiples entradas**: Funciona desde `:Alumnos Abierto`, `:Alumno Abierto` o desde creación
- **Navegación controlada**: Permite continuar editando o regresar a lista actualizada

### flexibilidad de edición continua

- **EditarAlumnoView** puede implementar:
  - **Edición por campos**: Modificación campo por campo
  - **Edición en bloque**: Modificación de múltiples campos simultáneamente
  - **Guardado incremental**: Guardado automático o manual por cambios
  - **Validación en tiempo real**: Verificación inmediata de cambios

### experiencia de usuario para "el gordo"

- **Información completa**: Muestra todos los datos editables del alumno
- **Edición flexible**: Permite modificar cualquier combinación de campos
- **Sesión persistente**: Mantiene contexto de edición durante múltiples modificaciones
- **Navegación clara**: Opciones explícitas para continuar o finalizar edición

## validaciones de negocio para edición continua

### restricciones de integridad durante edición

**AlumnosController** debe verificar durante cada modificación:
- **Unicidad de DNI**: DNI del alumno no duplicado con otro alumno existente
- **Coherencia de datos**: Nombre y apellidos no vacíos
- **Formato de DNI**: DNI con formato válido si se modifica
- **Relaciones activas**: Verificar impacto en asignaturas matriculadas y grado cursado si se modifican datos críticos

### manejo de errores en edición continua

- **Validación por campo**: Errores específicos para cada campo modificado
- **Preservación de cambios**: Mantener modificaciones válidas aunque otras fallen
- **Continuidad**: Permitir continuar editando después de corregir errores

## opciones de navegación disponibles

- **<<include>>** `verAlumnos()` → `:Alumnos Abierto`
- **<<include>>** `eliminarAlumno(alumnoId)` → `:Alumnos Abierto`
- **cancelar** → `:Alumnos Abierto`
