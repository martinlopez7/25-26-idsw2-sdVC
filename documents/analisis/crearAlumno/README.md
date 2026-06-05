# crearAlumno > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearAlumno/crearAlumno.svg)|**Análisis**|[Diseño](/documents/diseño/crearAlumno/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearAlumno()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar la creación rápida de alumnos con filosofía C→U.

## diagrama de colaboración

<div align=center>

|![Análisis: crearAlumno()](/images/analisis/crearAlumno/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearAlumno/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearAlumnoView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de alumno nuevo
- Presentar formulario con datos mínimos del alumno (nombre, apellidos, DNI)
- Permitir solicitar crear alumno o cancelar creación
- Transferir automáticamente a edición completa tras creación

**Colaboraciones**:
- **Entrada**: Recibe `crearAlumno()` desde `:Alumnos Abierto`
- **Control**: Se comunica con `AlumnosController`
- **Salida**: **<<include>>** `:Collaboration EditarAlumno` para completar datos

### clases de control

#### AlumnosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar el proceso de creación rápida de alumno
- Validar datos mínimos proporcionados por el docente
- Verificar unicidad del DNI
- Crear alumno con información básica en el sistema
- Coordinar transferencia inmediata a edición completa
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearAlumnoView`
- **Repositorio**: Delega operaciones de datos a `AlumnoRepository`

### clases de entidad (entity)

#### AlumnoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de alumnos
- Implementar creación de alumno con datos mínimos
- Verificar unicidad de DNI
- Persistir alumno con datos básicos
- Preparar alumno para edición completa

**Colaboraciones**:
- **Control**: Responde a `AlumnosController`
- **Entidad**: Gestiona instancias de `Alumno`

#### Alumno
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información mínima del alumno a crear
- Encapsular atributos esenciales: nombre, apellidos, DNI
- Validar datos mínimos proporcionados
- Mantener estado preparado para edición completa
- Aplicar reglas de negocio para creación rápida

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AlumnoRepository`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Alumnos Abierto` → `CrearAlumnoView.crearAlumno()`
2. **Solicitud**: `CrearAlumnoView` → `AlumnosController.iniciarCreacion()`
3. **Presentación**: `CrearAlumnoView` presenta formulario con campos mínimos
4. **Validación**: `CrearAlumnoView` → `AlumnosController.validarDatosMinimos(nombre, apellidos, dni)`
5. **Verificación**: `AlumnosController` → `AlumnoRepository.verificarUnicidadDNI(dni)`
6. **Datos**: Docente proporciona nombre, apellidos y DNI en `CrearAlumnoView`
7. **Creación**: `CrearAlumnoView` → `AlumnosController.crearAlumno(datosMinimos)`
8. **Persistencia**: `AlumnosController` → `AlumnoRepository.crear(nombre, apellidos, dni) : Alumno`
9. **Transferencia**: `CrearAlumnoView` → **<<include>>** `:Collaboration EditarAlumno.editarAlumno(alumnoNuevo)`

### opciones de navegación disponibles

- **<<include>>** `editarAlumno(alumnoNuevo)` → `:Alumno Abierto`
- **cancelar** → `:Alumnos Abierto`

## patrón de creación rápida para alumnos - "el delgado"

### creación con filosofía C→U

Este análisis implementa creación rápida que:
- **Solicita datos mínimos**: Solo información esencial para crear el alumno
- **Crea inmediatamente**: Alumno funcional con datos básicos
- **Transfiere automáticamente**: Redirige a edición completa sin interrupciones
- **Aplica filosofía C→U**: Create→Update para eficiencia máxima

### responsabilidades de creación rápida

**CrearAlumnoView** maneja creación mínima:
- **Presenta formulario**: Solo campos esenciales del alumno
- **Captura datos**: Información mínima necesaria para crear
- **Valida entrada**: Verificación básica antes de enviar
- **Transfiere automáticamente**: Redirige a edición completa tras creación

**AlumnosController** coordina creación rápida:
- **Valida mínimos**: Verifica que datos esenciales estén completos
- **Verifica unicidad**: Confirma que DNI no exista
- **Crea eficientemente**: Proceso de creación optimizado
- **Gestiona transferencia**: Coordina redirección a edición automática

## patrones arquitectónicos aplicados

### patrón MVC para creación de alumnos

- **Model**: `Alumno` + `AlumnoRepository` (creación y persistencia mínima)
- **View**: `CrearAlumnoView` (formulario mínimo y transferencia)
- **Controller**: `AlumnosController` (coordinación y validación de creación)

### patrón Repository con creación rápida

- **Abstracción de creación**: `AlumnoRepository` encapsula lógica de creación mínima
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Validaciones esenciales**: Verifica solo restricciones críticas
- **Preparación para edición**: Deja alumno listo para completar datos

### filosofía C→U con transferencia automática

- **Create rápido**: Creación con datos mínimos indispensables
- **Update inmediato**: Transferencia automática a edición completa
- **Sin interrupciones**: Flujo continuo desde creación hasta edición
- **Eficiencia máxima**: Minimiza pasos y fricción del usuario

## consideraciones de diseño específicas para alumnos

### reutilización del controlador

El diseño permite que `AlumnosController` sea reutilizado:
- **Compartido**: Con editarAlumno(), eliminarAlumno() y verAlumnos()
- **Método específico**: crearAlumno() con capacidades de creación rápida
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para creación mínima de alumnos

### patrón include para transferencia automática

- **Separación de responsabilidades**: crearAlumno() se enfoca en crear
- **Reutilización**: **<<include>>** editarAlumno() evita duplicar funcionalidad de edición
- **Transferencia fluida**: Paso automático de creación a edición
- **Contexto preservado**: Alumno recién creado se abre inmediatamente en edición

### flexibilidad de creación rápida

- **CrearAlumnoView** puede implementar:
  - **Validación en tiempo real**: Verificación inmediata de campos
  - **Validación de DNI**: Formato y unicidad en tiempo real
  - **Sugerencias inteligentes**: Autocompletado basado en datos existentes

## validación de datos mínimos

### restricciones durante creación

**AlumnosController** debe verificar durante creación:
- **Unicidad de DNI**: DNI del alumno no duplicado
- **Completitud básica**: Nombre, apellidos y DNI no vacíos
- **Formato válido**: DNI con formato correcto
- **Restricciones críticas**: Solo validaciones esenciales para crear

### manejo de errores en creación rápida

- **Validación inmediata**: Errores mostrados en tiempo real
- **Preservación de datos**: Mantener información ingresada válida
- **Corrección ágil**: Permitir corrección sin perder contexto
- **Alternativas**: Sugerir valores válidos cuando sea posible
