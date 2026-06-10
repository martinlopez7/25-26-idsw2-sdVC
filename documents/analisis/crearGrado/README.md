# crearGrado > Análisis

> |[🏠️](/README.md)|[Volver](/documents/analisis/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearGrado/crearGrado.svg)|**Análisis**|[Diseño](/documents/diseño/crearGrado/README.md)|
> |-|-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearGrado()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar la creación rápida de grados con filosofía C→U.

## diagrama de colaboración

<div align=center>

|![Análisis: crearGrado()](/images/analisis/crearGrado/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearGrado/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearGradoView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de grado nuevo
- Presentar formulario con datos mínimos del grado (nombre, código)
- Permitir solicitar crear grado o cancelar creación
- Transferir automáticamente a edición completa tras creación

**Colaboraciones**:
- **Entrada**: Recibe `crearGrado()` desde `:Grados Abierto`
- **Control**: Se comunica con `GradosController`
- **Salida**: **<<include>>** `:Collaboration EditarGrado` para completar datos

### clases de control

#### GradosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar el proceso de creación rápida de grado
- Validar datos mínimos proporcionados por el docente
- Verificar unicidad del nombre y código
- Crear grado con información básica en el sistema
- Coordinar transferencia inmediata a edición completa
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearGradoView`
- **Repositorio**: Delega operaciones de datos a `GradoRepository`

### clases de entidad (entity)

#### GradoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de grados
- Implementar creación de grado con datos mínimos
- Verificar unicidad de nombre y código
- Persistir grado con datos básicos
- Preparar grado para edición completa

**Colaboraciones**:
- **Control**: Responde a `GradosController`
- **Entidad**: Gestiona instancias de `Grado`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información mínima del grado a crear
- Encapsular atributos esenciales: título, código
- Validar datos mínimos proporcionados
- Mantener estado preparado para edición completa
- Aplicar reglas de negocio para creación rápida

**Colaboraciones**:
- **Repositorio**: Es gestionado por `GradoRepository`

## flujo de colaboración

### secuencia de operaciones

1. **Inicio**: `:Grados Abierto` → `CrearGradoView.crearGrado()`
2. **Solicitud**: `CrearGradoView` → `GradosController.iniciarCreacion()`
3. **Presentación**: `CrearGradoView` presenta formulario con campos mínimos
4. **Validación**: `CrearGradoView` → `GradosController.validarDatosMinimos(nombre, codigo)`
5. **Verificación**: `GradosController` → `GradoRepository.verificarUnicidad(nombre, codigo)`
6. **Datos**: Docente proporciona título y código en `CrearGradoView`
7. **Creación**: `CrearGradoView` → `GradosController.crearGrado(datosMinimos)`
8. **Persistencia**: `GradosController` → `GradoRepository.crear(nombre, codigo) : Grado`
9. **Transferencia**: `CrearGradoView` → **<<include>>** `:Collaboration EditarGrado.editarGrado(gradoNuevo)`

### opciones de navegación disponibles

- **<<include>>** `editarGrado(gradoNuevo)` → `:Grado Abierto`
- **cancelar** → `:Grados Abierto`

## patrón de creación rápida para grados - "el delgado"

### creación con filosofía C→U

Este análisis implementa creación rápida que:
- **Solicita datos mínimos**: Solo información esencial para crear el grado
- **Crea inmediatamente**: Grado funcional con datos básicos
- **Transfiere automáticamente**: Redirige a edición completa sin interrupciones
- **Aplica filosofía C→U**: Create→Update para eficiencia máxima

### responsabilidades de creación rápida

**CrearGradoView** maneja creación mínima:
- **Presenta formulario**: Solo campos esenciales del grado
- **Captura datos**: Información mínima necesaria para crear
- **Valida entrada**: Verificación básica antes de enviar
- **Transfiere automáticamente**: Redirige a edición completa tras creación

**GradosController** coordina creación rápida:
- **Valida mínimos**: Verifica que datos esenciales estén completos
- **Verifica unicidad**: Confirma que nombre y código no existan
- **Crea eficientemente**: Proceso de creación optimizado
- **Gestiona transferencia**: Coordina redirección a edición automática

## patrones arquitectónicos aplicados

### patrón MVC para creación de grados

- **Model**: `Grado` + `GradoRepository` (creación y persistencia mínima)
- **View**: `CrearGradoView` (formulario mínimo y transferencia)
- **Controller**: `GradosController` (coordinación y validación de creación)

### patrón Repository con creación rápida

- **Abstracción de creación**: `GradoRepository` encapsula lógica de creación mínima
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Validaciones esenciales**: Verifica solo restricciones críticas
- **Preparación para edición**: Deja grado listo para completar datos

### filosofía C→U con transferencia automática

- **Create rápido**: Creación con datos mínimos indispensables
- **Update inmediato**: Transferencia automática a edición completa
- **Sin interrupciones**: Flujo continuo desde creación hasta edición
- **Eficiencia máxima**: Minimiza pasos y fricción del usuario

## consideraciones de diseño específicas para grados

### reutilización del controlador

El diseño permite que `GradosController` sea reutilizado:
- **Compartido**: Con editarGrado(), eliminarGrado() y verGrados()
- **Método específico**: crearGrado() con capacidades de creación rápida
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para creación mínima de grados

### patrón include para transferencia automática

- **Separación de responsabilidades**: crearGrado() se enfoca en crear
- **Reutilización**: **<<include>>** editarGrado() evita duplicar funcionalidad de edición
- **Transferencia fluida**: Paso automático de creación a edición
- **Contexto preservado**: Grado recién creado se abre inmediatamente en edición

### flexibilidad de creación rápida

- **CrearGradoView** puede implementar:
  - **Validación en tiempo real**: Verificación inmediata de campos
  - **Sugerencias inteligentes**: Autocompletado basado en datos existentes

## validación de datos mínimos

### restricciones durante creación

**GradosController** debe verificar durante creación:
- **Unicidad de nombre**: Título del grado no duplicado
- **Unicidad de código**: Código del grado no existente
- **Completitud básica**: Todos los campos obligatorios no vacíos
- **Formato válido**: Datos con formato básico válido

### manejo de errores en creación rápida

- **Validación inmediata**: Errores mostrados en tiempo real
- **Preservación de datos**: Mantener información ingresada válida
- **Corrección ágil**: Permitir corrección sin perder contexto
- **Alternativas**: Sugerir valores válidos cuando sea posible