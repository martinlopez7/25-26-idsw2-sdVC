# editarRespuesta > Analisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/editarRespuesta/editarRespuesta.svg)|**Analisis**|[Diseño](/documents/diseño/editarRespuesta/README.md)|
> |-|-|-|-|-|

## informacion del artefacto

- **Proyecto**: Sistema de Generacion de Examenes Universitarios
- **Fase RUP**: Elaboration (Elaboracion)
- **Disciplina**: Analisis y Diseno
- **Version**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## proposito

Analisis de colaboracion del caso de uso `editarRespuesta()` mediante el patron MVC, identificando las clases de analisis, sus responsabilidades y colaboraciones necesarias para implementar edicion completa de respuestas con capacidad de modificacion continua de todos sus campos.

## diagrama de colaboracion

<div align=center>

|![Analisis: editarRespuesta()](/images/analisis/editarRespuesta/colaboracion.svg)|
|-|
|Codigo fuente: [colaboracion.puml](/modelosUML/analisis/editarRespuesta/colaboracion.puml)|

</div>

## clases de analisis identificadas

### clases de vista (boundary)

#### EditarRespuestaView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de edicion de respuesta
- Interactuar con el controlador para obtener datos completos de la respuesta
- Presentar datos de edicion (contenido, esCorrecta)
- Permitir solicitar modificacion de campos especificos
- Mantener sesion de edicion activa para modificaciones continuas
- Permitir solicitar guardar cambios, cancelar edicion o eliminar respuesta

**Colaboraciones**:
- **Entrada**: Recibe `editarRespuesta(respuestaId)` desde multiples estados (`:RESPUESTAS_ABIERTO`, `:RESPUESTA_ABIERTO`, `:RESPUESTAS_CONTEXTUALES_ABIERTO`, `:RESPUESTA_CONTEXTUAL_ABIERTO`) o desde `:Collaboration crearRespuesta`
- **Control**: Se comunica con `RespuestasController`
- **Salida**: **<<include>>** `:Collaboration verRespuestas`, `:Collaboration eliminarRespuesta`

### clases de control

#### RespuestasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la carga de datos de la respuesta para edicion
- Validar que la respuesta existe y es editable
- Manejar la logica de modificacion de campos de la respuesta
- Procesar guardado de cambios tras modificaciones
- Coordinar navegacion entre edicion continua y finalizacion
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `EditarRespuestaView`
- **Repositorio**: Delega operaciones de datos a `RespuestaRepository`

### clases de entidad (entity)

#### RespuestaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de respuestas
- Proporcionar metodo para obtener respuesta por ID con datos completos
- Implementar actualizacion de campos de la respuesta
- Validar restricciones de integridad antes de guardar

**Colaboraciones**:
- **Control**: Responde a `RespuestasController`
- **Entidad**: Gestiona instancias de `Respuesta`

#### Respuesta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la informacion completa de la respuesta a editar
- Encapsular atributos: contenido, esCorrecta
- Validar cambios en campos especificos
- Mantener la integridad de los datos durante modificaciones continuas

**Colaboraciones**:
- **Repositorio**: Es gestionada por `RespuestaRepository`
- **Entidad**: Tiene relacion con `Pregunta`

#### Pregunta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la pregunta a la que pertenece la respuesta
- Proporcionar contexto para la edicion de respuestas

**Colaboraciones**:
- **Composicion**: `Respuesta` pertenece a esta entidad por composicion (1 *-- many)

## flujo de colaboracion principal

### secuencia: editar respuesta

1. **Inicio**: `:RESPUESTAS_ABIERTO` o `:RESPUESTA_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO` o `:RESPUESTA_CONTEXTUAL_ABIERTO` -> `EditarRespuestaView.editarRespuesta(respuestaId)`
2. **Carga**: `EditarRespuestaView` -> `RespuestasController.cargarRespuestaParaEdicion(respuestaId)`
3. **Obtencion**: `RespuestasController` -> `RespuestaRepository.obtenerPorId(respuestaId) : Respuesta`
4. **Presentacion**: `EditarRespuestaView` presenta datos completos de la `Respuesta` para edicion (contenido, esCorrecta)
5. **Modificacion**: Docente modifica campos en `EditarRespuestaView`
6. **Guardado**: `EditarRespuestaView` -> `RespuestasController.modificarCampos(respuestaId, cambios) : boolean`
7. **Persistencia**: `RespuestasController` -> `RespuestaRepository.actualizar(respuesta) : boolean`
8. **Continuacion**: 
   - **Edicion continua**: Permanece en `EditarRespuestaView` para mas modificaciones
   - **Guardar y salir**: `EditarRespuestaView` -> **<<include>>** `:Collaboration verRespuestas`
   - **Cancelar edicion**: `EditarRespuestaView` -> `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO`
   - **Eliminar**: `EditarRespuestaView` -> **<<include>>** `:Collaboration eliminarRespuesta.eliminarRespuesta(respuestaId)`

## patron de edicion completa para respuestas - "el gordo"

### edicion continua

Este analisis implementa edicion completa que:
- **Presenta datos completos**: Todos los campos de la respuesta disponibles para edicion (contenido, esCorrecta)
- **Permite modificacion continua**: Multiples ciclos de edicion sin salir del contexto
- **Guarda cambios incrementales**: Cada modificacion puede guardarse independientemente
- **Mantiene sesion activa**: El estado se preserva durante la edicion

### responsabilidades de edicion continua

**EditarRespuestaView** maneja edicion continua:
- **Presenta datos**: Informacion completa de la respuesta con campos editables
- **Captura modificaciones**: Cambios en cualquier campo de la respuesta
- **Mantiene contexto**: Sesion de edicion activa para multiples modificaciones
- **Permite guardado**: Guardado incremental sin salir del contexto

**RespuestasController** coordina edicion continua:
- **Valida cambios**: Verifica que las modificaciones son validas
- **Procesa incrementalmente**: Guarda cambios especificos sin afectar otros campos
- **Mantiene coherencia**: Verifica integridad durante modificaciones continuas
- **Gestiona navegacion**: Permite continuar editando o finalizar

## patrones arquitectonicos aplicados

### patron MVC para edicion de respuestas

- **Model**: `Respuesta` + `RespuestaRepository` + `Pregunta` (datos de la respuesta y persistencia)
- **View**: `EditarRespuestaView` (edicion continua e interaccion)
- **Controller**: `RespuestasController` (coordinacion y validacion de edicion)

### patron Repository con edicion continua

- **Abstraccion de persistencia**: `RespuestaRepository` encapsula logica de actualizacion
- **Separacion de responsabilidades**: Controlador no conoce detalles de persistencia
- **Transacciones**: Puede implementar guardado transaccional por campos
- **Validaciones continuas**: Verifica restricciones en cada modificacion

### edicion continua con multiples ciclos

- **Ciclo 1**: Presentar datos -> Modificar -> Guardar -> Continuar
- **Ciclo 2**: Modificar mas campos -> Guardar -> Continuar o Finalizar
- **Flexibilidad**: Docente controla cuando finalizar la edicion
- **Contexto preservado**: Estado se mantiene durante todos los ciclos

## consideraciones de diseno especificas para respuestas

### reutilizacion del controlador

El diseno permite que `RespuestasController` sea reutilizado:
- **Compartido**: Con crearRespuesta(), eliminarRespuesta() y verRespuestas()
- **Metodo especifico**: editarRespuesta() con capacidades de edicion continua
- **Consistencia**: Mismo patron de comunicacion con repositorio
- **Validaciones**: Especificas para edicion continua de respuestas

### patron include para navegacion flexible

- **Separacion de responsabilidades**: editarRespuesta() se enfoca en editar
- **Reutilizacion**: **<<include>>** verRespuestas() evita duplicar funcionalidad de listado de respuestas
- **Multiples entradas**: Funciona desde multiples estados o desde creacion
- **Navegacion controlada**: Permite continuar editando o regresar a lista actualizada

### flexibilidad de edicion continua

- **EditarRespuestaView** puede implementar:
  - **Edicion por campos**: Modificacion campo por campo
  - **Edicion en bloque**: Modificacion de multiples campos simultaneamente
  - **Guardado incremental**: Guardado automatico o manual por cambios
  - **Validacion en tiempo real**: Verificacion inmediata de cambios

### experiencia de usuario para "el gordo"

- **Informacion completa**: Muestra todos los datos editables de la respuesta
- **Edicion flexible**: Permite modificar cualquier combinacion de campos
- **Sesion persistente**: Mantiene contexto de edicion durante multiples modificaciones
- **Navegacion clara**: Opciones explicitas para continuar o finalizar edicion

## validaciones de negocio para edicion continua

### restricciones de integridad durante edicion

**RespuestasController** debe verificar durante cada modificacion:
- **Contenido no vacio**: El texto de la respuesta no puede estar vacio
- **esCorrecta definido**: Debe indicarse si la respuesta es correcta o no

### manejo de errores en edicion continua

- **Validacion por campo**: Errores especificos para cada campo modificado
- **Preservacion de cambios**: Mantener modificaciones validas aunque otras fallen
- **Continuidad**: Permitir continuar editando despues de corregir errores

## opciones de navegacion disponibles

| Accion | Destino |
|--------|---------|
| **guardar** | `:RESPUESTA_ABIERTO` o `:RESPUESTA_CONTEXTUAL_ABIERTO` |
| **cancelar edicion** | `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO` |
| **eliminar** | `:RESPUESTAS_ABIERTO` o `:RESPUESTAS_CONTEXTUALES_ABIERTO` |
| **ver respuestas** | **<<include>>** `:Collaboration verRespuestas` |

## diferencias con otros casos CRUD de respuestas

### editarRespuesta() vs crearRespuesta()

**editarRespuesta():**
- **Objetivo**: Modificacion de datos existentes con edicion continua
- **Interaccion**: Multiples ciclos de edicion en sesion activa
- **Validaciones**: Preservar integridad con datos relacionados existentes
- **Resultado**: Respuesta actualizada con posibilidad de continuar editando

**crearRespuesta():**
- **Objetivo**: Entrada de datos nuevos con validacion inicial
- **Interaccion**: Formulario de creacion con redireccion a edicion
- **Validaciones**: Verificar uniqueness y formato de datos nuevos
- **Resultado**: Respuesta creada y redirigida a edicion

### complementariedad CRUD para respuestas

- **crearRespuesta()**: Crea nuevas respuestas y redirige a edicion
- **editarRespuesta()**: Modifica respuestas existentes con edicion continua
- **eliminarRespuesta()**: Remueve respuestas con confirmacion segura
- **verRespuestas()**: Lista y selecciona respuestas para edicion