# crearDocente > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearDocente/crearDocente.svg)|**Análisis**|[Diseño](/documents/diseño/crearDocente/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Gestión de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearDocente()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar la creación rápida de docentes con filosofía C→U.

## diagrama de colaboración

<div align=center>

|![Análisis: crearDocente()](/images/analisis/crearDocente/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearDocente/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearDocenteView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de docente nuevo
- Presentar formulario con datos mínimos del docente (nombre, apellidos, DNI, nombre de usuario, email, password)
- Permitir solicitar crear docente o cancelar creación
- Transferir automáticamente a edición completa tras creación

**Colaboraciones**:
- **Entrada**: Recibe `crearDocente()` desde `:Docentes Abierto`
- **Control**: Se comunica con `DocentesController`
- **Salida**: **<<include>>** `:Collaboration EditarDocente` para completar datos

### clases de control

#### DocentesController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar el proceso de creación rápida de docente
- Validar datos mínimos proporcionados por el administrador
- Crear docente con información básica en el sistema
- Coordinar transferencia inmediata a edición completa
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearDocenteView`
- **Repositorio**: Delega operaciones de datos a `DocenteRepository`

### clases de entidad (entity)

#### DocenteRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de docentes
- Implementar creación de docente con datos mínimos
- Generar credenciales de acceso si es necesario
- Validar unicidad de DNI, nombre de usuario y email
- Preparar docente para edición completa

**Colaboraciones**:
- **Control**: Responde a `DocentesController`
- **Entidad**: Gestiona instancias de `Docente`

#### Docente
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información mínima del docente a crear
- Encapsular atributos esenciales: nombre, apellidos, DNI, nombre de usuario, email, password
- Validar datos mínimos proporcionados
- Mantener estado preparado para edición completa
- Aplicar reglas de negocio para creación rápida

**Colaboraciones**:
- **Repositorio**: Es gestionado por `DocenteRepository`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Docentes Abierto` → `CrearDocenteView.crearDocente()`
2. **Solicitud**: `CrearDocenteView` → `DocentesController.iniciarCreacion()`
3. **Presentación**: `CrearDocenteView` presenta formulario con campos mínimos
4. **Datos**: Administrador proporciona nombre, apellidos, DNI, nombre de usuario, email, password en `CrearDocenteView`
5. **Creación**: `CrearDocenteView` → `DocentesController.crearDocente(datosMinimos)`
6. **Validación**: `DocentesController` → `DocenteRepository.validarDatosMinimos(datos)`
7. **Persistencia**: `DocentesController` → `DocenteRepository.crear(docente) : Docente`
8. **Transferencia**: `CrearDocenteView` → **<<include>>** `:Collaboration EditarDocente.editarDocente(docenteNuevo)`

### opciones de navegación disponibles

- **<<include>>** `editarDocente(docenteNuevo)` → `:Docente Abierto`
- **cancelar** → `:Docentes Abierto`

## patrón de creación rápida para docentes - "el delgado"

### creación con filosofía C→U

Este análisis implementa creación rápida que:
- **Solicita datos mínimos**: Solo información esencial para crear el docente
- **Crea inmediatamente**: Docente funcional con datos básicos
- **Transfiere automáticamente**: Redirige a edición completa sin interrupciones
- **Aplica filosofía C→U**: Create→Update para eficiencia máxima

### responsabilidades de creación rápida

**CrearDocenteView** maneja creación mínima:
- **Presenta formulario**: Solo campos esenciales del docente
- **Captura datos**: Información mínima necesaria para crear
- **Valida entrada**: Verificación básica antes de enviar
- **Transfiere automáticamente**: Redirige a edición completa tras creación

**DocentesController** coordina creación rápida:
- **Valida mínimos**: Verifica que datos esenciales estén completos
- **Crea eficientemente**: Proceso de creación optimizado
- **Gestiona transferencia**: Coordina redirección a edición automática
- **Mantiene coherencia**: Verifica integridad durante creación

## patrones arquitectónicos aplicados

### patrón MVC para creación de docentes

- **Model**: `Docente` + `DocenteRepository` (creación y persistencia mínima)
- **View**: `CrearDocenteView` (formulario mínimo y transferencia)
- **Controller**: `DocentesController` (coordinación y validación de creación)

### patrón Repository con creación rápida

- **Abstracción de creación**: `DocenteRepository` encapsula lógica de creación mínima
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Validaciones esenciales**: Verifica solo restricciones críticas
- **Preparación para edición**: Deja docente listo para completar datos

### filosofía C→U con transferencia automática

- **Create rápido**: Creación con datos mínimos indispensables
- **Update inmediato**: Transferencia automática a edición completa
- **Sin interrupciones**: Flujo continuo desde creación hasta edición
- **Eficiencia máxima**: Minimiza pasos y fricción del usuario

## consideraciones de diseño específicas para docentes

### reutilización del controlador

El diseño permite que `DocentesController` sea reutilizado:
- **Compartido**: Con editarDocente(), eliminarDocente() y verDocentes()
- **Método específico**: crearDocente() con capacidades de creación rápida
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para creación mínima de docentes

### patrón include para transferencia automática

- **Separación de responsabilidades**: crearDocente() se enfoca en crear
- **Reutilización**: **<<include>>** editarDocente() evita duplicar funcionalidad de edición
- **Transferencia fluida**: Paso automático de creación a edición
- **Contexto preservado**: Docente recién creado se abre inmediatamente en edición

### flexibilidad de creación rápida

- **CrearDocenteView** puede implementar:
  - **Validación en tiempo real**: Verificación inmediata de campos
  - **Generación automática**: Password auto-generado si se omite
  - **Sugerencias inteligentes**: Autocompletado basado en datos existentes

## validación de datos mínimos

### restricciones durante creación

**DocentesController** debe verificar durante creación:
- **Unicidad de DNI**: DNI del docente no duplicado
- **Unicidad de nombre de usuario**: Nombre de usuario no existente
- **Unicidad de email**: Email no registrado en el sistema
- **Completitud básica**: Todos los campos obligatorios no vacíos
- **Formato válido**: Datos con formato básico válido (email, DNI)

### manejo de errores en creación rápida

- **Validación inmediata**: Errores mostrados en tiempo real
- **Preservación de datos**: Mantener información ingresada válida
- **Corrección ágil**: Permitir corrección sin perder contexto
- **Alternativas**: Sugerir valores válidos cuando sea posible