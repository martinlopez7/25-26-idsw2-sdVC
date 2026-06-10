# editarPregunta > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarPregunta/editarPregunta.svg)|**Análisis**|[Diseño](/documents/diseño/editarPregunta/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `editarPregunta()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar edición completa de preguntas con capacidad de modificación continua de todos sus campos incluyendo respuestas asociadas.

## diagrama de colaboración

<div align=center>

|![Análisis: editarPregunta()](/images/analisis/editarPregunta/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/editarPregunta/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### EditarPreguntaView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edición de pregunta
- Interactuar con el controlador para obtener datos completos de la pregunta
- Presentar datos de edición (asignatura, enunciado, tema, dificultad, respuestas, habilitada)
- Permitir solicitar modificación de campos específicos
- Mantener sesión de edición activa para modificaciones continuas
- Permitir solicitar guardar cambios, cancelar edición, eliminar pregunta o ver respuestas

**Colaboraciones**:
- **Entrada**: Recibe `editarPregunta(preguntaId)` desde múltiples estados (`:Preguntas Abierto`, `:Preguntas Contextuales Abierto`, `:Pregunta Abierto`, `:Pregunta Contextual Abierto`, `:Respuestas Abierto`, `:Respuestas Contextual Abierto`) o desde `:Collaboration crearPregunta`
- **Control**: Se comunica con `PreguntasController`
- **Salida**: **<<include>>** `:Collaboration verRespuestas`, `:Collaboration eliminarPregunta`, `:Collaboration verPreguntas`

### clases de control

#### PreguntasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos de la pregunta para edición
- Validar que la pregunta existe y es editable
- Manejar la lógica de modificación de campos de la pregunta
- Procesar guardado de cambios tras modificaciones
- Coordinar navegación entre edición continua y finalización
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarPreguntaView`
- **Repositorio**: Delega operaciones de datos a `PreguntaRepository`

### clases de entidad (entity)

#### PreguntaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de preguntas
- Proporcionar método para obtener pregunta por ID con datos completos
- Implementar actualización de campos de la pregunta
- Validar restricciones de integridad antes de guardar
- Gestionar relaciones con respuestas durante edición

**Colaboraciones**:
- **Control**: Responde a `PreguntasController`
- **Entidad**: Gestiona instancias de `Pregunta`

#### Pregunta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información completa de la pregunta a editar
- Encapsular atributos: asignatura, enunciado, tema, dificultad, habilitada
- Mantener relación con respuestas asociadas
- Validar cambios en campos específicos
- Mantener la integridad de los datos durante modificaciones continuas

**Colaboraciones**:
- **Repositorio**: Es gestionada por `PreguntaRepository`
- **Entidad**: Tiene relación con `Respuesta`

#### Respuesta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar las opciones de respuesta asociadas a una pregunta
- Encapsular atributos: opción, esCorrecta
- Mantener integridad con la pregunta padre durante ediciones

**Colaboraciones**:
- **Pregunta**: Pertenece a una `Pregunta`

## flujo de colaboración principal

### secuencia: editar pregunta

1. **Inicio**: `:Preguntas Abierto` → `EditarPreguntaView.editarPregunta(preguntaId)`
2. **Carga**: `EditarPreguntaView` → `PreguntasController.cargarPreguntaParaEdición(preguntaId)`
3. **Obtención**: `PreguntasController` → `PreguntaRepository.obtenerPorId(preguntaId) : Pregunta`
4. **Presentación**: `EditarPreguntaView` presenta datos completos de la `Pregunta` para edición (asignatura, enunciado, tema, dificultad, respuestas, habilitada)
5. **Modificación**: Docente modifica campos en `EditarPreguntaView`
6. **Guardado**: `EditarPreguntaView` → `PreguntasController.modificarCampos(preguntaId, cambios) : boolean`
7. **Persistencia**: `PreguntasController` → `PreguntaRepository.actualizar(pregunta) : boolean`
8. **Continuación**: 
   - **Edición continua**: Permanece en `EditarPreguntaView` para más modificaciones
   - **Guardar y salir**: `EditarPreguntaView` → **<<include>>** `:Collaboration verPreguntas`
   - **Cancelar edición**: `EditarPreguntaView` → `:Preguntas Abierto` o `:Preguntas Contextuales Abierto`
   - **Eliminar**: `EditarPreguntaView` → **<<include>>** `:Collaboration eliminarPregunta.eliminarPregunta(preguntaId)`
   - **Ver respuestas**: `EditarPreguntaView` → **<<include>>** `:Collaboration verRespuestas.verRespuestas(preguntaId)`

## patrón de edición completa para preguntas - "el gordo"

### edición continua

Este análisis implementa edición completa que:
- **Presenta datos completos**: Todos los campos de la pregunta disponibles para edición (asignatura, enunciado, tema, dificultad, respuestas, habilitada)
- **Permite modificación continua**: Múltiples ciclos de edición sin salir del contexto
- **Guarda cambios incrementales**: Cada modificación puede guardarse independientemente
- **Mantiene sesión activa**: El estado se preserva durante la edición

### responsabilidades de edición continua

**EditarPreguntaView** maneja edición continua:
- **Presenta datos**: Información completa de la pregunta con campos editables
- **Captura modificaciones**: Cambios en cualquier campo de la pregunta
- **Mantiene contexto**: Sesión de edición activa para múltiples modificaciones
- **Permite guardado**: Guardado incremental sin salir del contexto

**PreguntasController** coordina edición continua:
- **Valida cambios**: Verifica que las modificaciones son válidas
- **Procesa incrementalmente**: Guarda cambios específicos sin afectar otros campos
- **Mantiene coherencia**: Verifica integridad durante modificaciones continuas
- **Gestiona navegación**: Permite continuar editando o finalizar

## patrones arquitectónicos aplicados

### patrón MVC para edición de preguntas

- **Model**: `Pregunta` + `PreguntaRepository` (datos de la pregunta y persistencia)
- **View**: `EditarPreguntaView` (edición continua e interacción)
- **Controller**: `PreguntasController` (coordinación y validación de edición)

### patrón Repository con edición continua

- **Abstracción de persistencia**: `PreguntaRepository` encapsula lógica de actualización
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Transacciones**: Puede implementar guardado transaccional por campos
- **Validaciones continuas**: Verifica restricciones en cada modificación

### edición continua con múltiples ciclos

- **Ciclo 1**: Presentar datos → Modificar → Guardar → Continuar
- **Ciclo 2**: Modificar más campos → Guardar → Continuar o Finalizar
- **Flexibilidad**: Docente controla cuándo finalizar la edición
- **Contexto preservado**: Estado se mantiene durante todos los ciclos

## consideraciones de diseño específicas para preguntas

### reutilización del controlador

El diseño permite que `PreguntasController` sea reutilizado:
- **Compartido**: Con crearPregunta(), eliminarPregunta(), verPreguntas() y verRespuestas()
- **Método específico**: editarPregunta() con capacidades de edición continua
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para edición continua de preguntas

### patrón include para navegación flexible

- **Separación de responsabilidades**: editarPregunta() se enfoca en editar
- **Reutilización**: **<<include>>** verRespuestas() evita duplicar funcionalidad de listado de respuestas
- **Múltiples entradas**: Funciona desde múltiples estados o desde creación
- **Navegación controlada**: Permite continuar editando o regresar a lista actualizada

### flexibilidad de edición continua

- **EditarPreguntaView** puede implementar:
  - **Edición por campos**: Modificación campo por campo
  - **Edición en bloque**: Modificación de múltiples campos simultáneamente
  - **Guardado incremental**: Guardado automático o manual por cambios
  - **Validación en tiempo real**: Verificación inmediata de cambios

### experiencia de usuario para "el gordo"

- **Información completa**: Muestra todos los datos editables de la pregunta
- **Edición flexible**: Permite modificar cualquier combinación de campos
- **Sesión persistente**: Mantiene contexto de edición durante múltiples modificaciones
- **Navegación clara**: Opciones explícitas para continuar o finalizar edición

## validaciones de negocio para edición continua

### restricciones de integridad durante edición

**PreguntasController** debe verificar durante cada modificación:
- **Unicidad de enunciado**: Enunciado de la pregunta no duplicado con otra pregunta existente en la misma asignatura
- **Campos obligatorios**: Enunciado no vacío, asignatura asignada
- **Consistencia de respuestas**: Al menos una respuesta correcta si la pregunta es de tipo test

### manejo de errores en edición continua

- **Validación por campo**: Errores específicos para cada campo modificado
- **Preservación de cambios**: Mantener modificaciones válidas aunque otras fallen
- **Continuidad**: Permitir continuar editando después de corregir errores
