# crearPregunta > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearPregunta/crearPregunta.svg)|**Análisis**|[Diseño](/documents/diseño/crearPregunta/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-26
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearPregunta()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar la filosofía C→U como "el delgado".

## diagrama de colaboración

<div align=center>

|![Análisis: crearPregunta()](/images/analisis/crearPregunta/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearPregunta/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearPreguntaView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de pregunta nueva
- Presentar formulario con datos mínimos de la pregunta (asignatura, enunciado, tema, dificultad)
- Permitir solicitar crear pregunta o cancelar creación
- Transferir automáticamente a edición completa tras creación

**Colaboraciones**:
- **Entrada**: Recibe `crearPregunta()` desde `:PREGUNTAS_ABIERTO` o `:PREGUNTAS_CONTEXTUALES_ABIERTO`
- **Control**: Se comunica con `PreguntasController`
- **Salida**: **<<include>>** `:Collaboration EditarPregunta` para completar datos

### clases de control

#### PreguntasController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar el proceso de creación rápida de pregunta
- Validar datos mínimos proporcionados por el docente
- Verificar unicidad del enunciado
- Crear pregunta con información básica en el sistema
- Coordinar transferencia inmediata a edición completa
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearPreguntaView`
- **Repositorio**: Delega operaciones de datos a `PreguntaRepository`

### clases de entidad (entity)

#### PreguntaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de preguntas
- Implementar creación de pregunta con datos mínimos
- Verificar unicidad del enunciado
- Persistir pregunta con datos básicos
- Preparar pregunta para edición completa

**Colaboraciones**:
- **Control**: Responde a `PreguntasController`
- **Entidad**: Gestiona instancias de `Pregunta`

#### Pregunta
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información mínima de la pregunta a crear
- Encapsular atributos esenciales: asignatura, enunciado, tema, dificultad
- Validar datos mínimos proporcionados
- Mantener estado preparado para edición completa
- Aplicar reglas de negocio para creación rápida

**Colaboraciones**:
- **Repositorio**: Es gestionado por `PreguntaRepository`
- **Asociación**: Se asocia con `Asignatura`

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la asignatura asociada a la pregunta
- Proporcionar contexto para la creación de preguntas

**Colaboraciones**:
- **Asociación**: `Pregunta` se asocia con esta entidad

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:PREGUNTAS_ABIERTO` o `:PREGUNTAS_CONTEXTUALES_ABIERTO` → `CrearPreguntaView.crearPregunta()`
2. **Solicitud**: `CrearPreguntaView` → `PreguntasController.iniciarCreacion()`
3. **Presentación**: `CrearPreguntaView` presenta formulario con campos mínimos
4. **Validación**: `CrearPreguntaView` → `PreguntasController.validarDatosMinimos(asignatura, enunciado, tema, dificultad)`
5. **Verificación**: `PreguntasController` → `PreguntaRepository.verificarUnicidadEnunciado(enunciado)`
6. **Datos**: Docente proporciona asignatura, enunciado, tema y dificultad en `CrearPreguntaView`
7. **Creación**: `CrearPreguntaView` → `PreguntasController.crearPregunta(datosMinimos)`
8. **Persistencia**: `PreguntasController` → `PreguntaRepository.crear(datosMinimos) : Pregunta`
9. **Transferencia**: `CrearPreguntaView` → **<<include>>** `:Collaboration EditarPregunta.editarPregunta(preguntaNueva)`

### opciones de navegación disponibles

- **<<include>>** `editarPregunta(preguntaNueva)` → `:PREGUNTA_ABIERTO` o `:PREGUNTA_CONTEXTUAL_ABIERTO`
- **cancelar** → `:PREGUNTAS_ABIERTO` o `:PREGUNTAS_CONTEXTUALES_ABIERTO`

## patrón de creación rápida para preguntas - "el delgado"

### creación con filosofía C→U

Este análisis implementa creación rápida que:
- **Solicita datos mínimos**: Solo información esencial para crear la pregunta
- **Crea inmediatamente**: Pregunta funcional con datos básicos
- **Transfiere automáticamente**: Redirige a edición completa sin interrupciones
- **Aplica filosofía C→U**: Create→Update para eficiencia máxima

### responsabilidades de creación rápida

**CrearPreguntaView** maneja creación mínima:
- **Presenta formulario**: Solo campos esenciales de la pregunta
- **Captura datos**: Información mínima necesaria para crear
- **Valida entrada**: Verificación básica antes de enviar
- **Transfiere automáticamente**: Redirige a edición completa tras creación

**PreguntasController** coordina creación rápida:
- **Valida mínimos**: Verifica que datos esenciales estén completos
- **Verifica unicidad**: Confirma que enunciado no exista
- **Crea eficientemente**: Proceso de creación optimizado
- **Gestiona transferencia**: Coordina redirección a edición automática

## patrones arquitectónicos aplicados

### patrón MVC para creación de preguntas

- **Model**: `Pregunta` + `PreguntaRepository` + `Asignatura` (creación y persistencia mínima)
- **View**: `CrearPreguntaView` (formulario mínimo y transferencia)
- **Controller**: `PreguntasController` (coordinación y validación de creación)

### patrón Repository con creación rápida

- **Abstracción de creación**: `PreguntaRepository` encapsula lógica de creación mínima
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Validaciones esenciales**: Verifica solo restricciones críticas
- **Preparación para edición**: Deja pregunta lista para completar datos

### filosofía C→U con transferencia automática

- **Create rápido**: Creación con datos mínimos indispensables
- **Update inmediato**: Transferencia automática a edición completa
- **Sin interrupciones**: Flujo continuo desde creación hasta edición
- **Eficiencia máxima**: Minimiza pasos y fricción del usuario

## consideraciones de diseño específicas para preguntas

### reutilización del controlador

El diseño permite que `PreguntasController` sea reutilizado:
- **Compartido**: Con editarPregunta(), eliminarPregunta() y verPreguntas()
- **Método específico**: crearPregunta() con capacidades de creación rápida
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para creación mínima de preguntas

### patrón include para transferencia automática

- **Separación de responsabilidades**: crearPregunta() se enfoca en crear
- **Reutilización**: **<<include>>** editarPregunta() evita duplicar funcionalidad de edición
- **Transferencia fluida**: Paso automático de creación a edición
- **Contexto preservado**: Pregunta recién creada se abre inmediatamente en edición

### flexibilidad de creación rápida

- **CrearPreguntaView** puede implementar:
  - **Validación en tiempo real**: Verificación inmediata de campos
  - **Validación de enunciado**: Formato y unicidad en tiempo real
  - **Selección de dificultad**: Desplegable con opciones BAJA, MEDIA, ALTA

## validación de datos mínimos

### restricciones durante creación

**PreguntasController** debe verificar durante creación:
- **Unicidad de enunciado**: Enunciado de la pregunta no duplicado
- **Completitud básica**: Asignatura, enunciado, tema y dificultad no vacíos
- **Dificultad válida**: Valor dentro del ENUM (BAJA, MEDIA, ALTA)
- **Restricciones críticas**: Solo validaciones esenciales para crear

### manejo de errores en creación rápida

- **Validación inmediata**: Errores mostrados en tiempo real
- **Preservación de datos**: Mantener información ingresada válida
- **Corrección ágil**: Permitir corrección sin perder contexto
- **Alternativas**: Sugerir valores válidos cuando sea posible