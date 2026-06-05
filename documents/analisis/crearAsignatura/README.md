# Generador de Exámenes > crearAsignatura > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearAsignatura/crearAsignatura.svg)|**Análisis**|[Diseño](/documents/diseño/crearAsignatura/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Generador de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2026-05-25
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearAsignatura()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar creación rápida de asignaturas con filosofía C→U.

## diagrama de colaboración

<div align=center>

|![Análisis: crearAsignatura()](/images/analisis/crearAsignatura/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearAsignatura/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearAsignaturaView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de asignatura nueva
- Presentar solicitud de datos mínimos de la asignatura
- Permitir solicitar crear asignatura o cancelar creación
- Transferir inmediatamente a edición completa tras creación

**Colaboraciones**:
- **Entrada**: Recibe `crearAsignatura()` desde `:Asignaturas Abierto`
- **Control**: Se comunica con `AsignaturaController`
- **Salida**: **&lt;&lt;include&gt;&gt;** `:Collaboration EditarAsignatura` para completar datos

### clases de control

#### AsignaturaController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la creación de asignatura con datos mínimos
- Validar nombre, código y curso académico (obligatorios)
- Verificar que no existe asignatura duplicada
- Manejar la lógica de creación básica
- Servir como intermediario entre la vista y el repositorio

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearAsignaturaView`
- **Repositorio**: Delega operaciones de datos a `AsignaturaRepository`

### clases de entidad (entity)

#### AsignaturaRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer el acceso a datos de asignaturas
- Implementar creación de asignatura con datos mínimos
- Verificar unicidad del código de asignatura
- Generar ID único para la nueva asignatura
- Crear automáticamente la BateríaDePreguntas asociada

**Colaboraciones**:
- **Control**: Responde a `AsignaturaController`
- **Entidad**: Gestiona instancias de `Asignatura` y `BateriaDePreguntas`

#### Asignatura
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la información mínima de la asignatura a crear
- Encapsular atributos: título (obligatorio), código (obligatorio), curso académico (obligatorio)
- Validar datos mínimos requeridos
- Mantener la integridad de los datos durante creación
- Permitir completar información en edición posterior

**Colaboraciones**:
- **Repositorio**: Es gestionado por `AsignaturaRepository`

#### BateriaDePreguntas
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar la batería de preguntas asociada a la asignatura
- Crearse automáticamente al crear la asignatura
- Inicializarse vacía, lista para recibir preguntas

**Colaboraciones**:
- **Repositorio**: Es creada por `AsignaturaRepository` junto con la asignatura

## flujo de colaboración principal

### secuencia: crear asignatura

1. **Inicio**: `:Asignaturas Abierto` → `CrearAsignaturaView.crearAsignatura()`
2. **Presentación**: `CrearAsignaturaView` presenta solicitud de datos mínimos de la asignatura
3. **Datos**: Docente proporciona título, código y curso académico (obligatorios)
4. **Validación**: `CrearAsignaturaView` → `AsignaturaController.validarDatosMínimos(título, código, cursoAcadémico)`
5. **Verificación**: `AsignaturaController` → `AsignaturaRepository.verificarUnicidad(código)`
6. **Creación**: `AsignaturaController` → `AsignaturaRepository.crear(título, código, cursoAcadémico) : Asignatura`
7. **Persistencia**: `AsignaturaRepository` crea `Asignatura` con datos mínimos y su `BateriaDePreguntas` asociada
8. **Transferencia**: `CrearAsignaturaView` → **&lt;&lt;include&gt;&gt;** `:Collaboration EditarAsignatura.editarAsignatura(asignaturaNueva)`

## diagrama de secuencia

<div align=center>

|![Secuencia: crearAsignatura()](/images/analisis/crearAsignatura/secuencia.svg)|
|-|
|Código fuente: [secuencia.puml](/modelosUML/analisis/crearAsignatura/secuencia.puml)|

</div>

## patrón de creación rápida para asignaturas

### filosofía C→U (Crear→Editar)

Este análisis implementa creación minimalista que:
- **Datos mínimos**: Solo título, código y curso académico (obligatorios)
- **Creación básica**: Asignatura válida con información esencial
- **Transferencia inmediata**: Pasa automáticamente a edición completa
- **Eficiencia**: "El delgado" permite crear rápido, "el gordo" completa

### responsabilidades de creación mínima

**CrearAsignaturaView** maneja creación rápida:
- **Presenta solicitud**: Datos mínimos obligatorios únicamente
- **Valida entrada**: Título, código y curso académico no vacíos
- **Transfiere control**: Pasa inmediatamente a edición completa

**AsignaturaController** coordina creación:
- **Valida mínimos**: Verifica que campos obligatorios no estén vacíos
- **Controla unicidad**: Verifica que no existe asignatura con mismo código
- **Procesa creación**: Crea asignatura básica válida
- **Facilita transferencia**: Prepara asignatura para edición completa

## patrones arquitectónicos aplicados

### patrón MVC para creación de asignaturas

- **Model**: `Asignatura` + `AsignaturaRepository` + `BateriaDePreguntas` (datos mínimos y creación)
- **View**: `CrearAsignaturaView` (solicitud de datos mínimos e interacción)
- **Controller**: `AsignaturaController` (coordinación y validación académica básica)

### patrón Repository con creación académica

- **Abstracción de creación**: `AsignaturaRepository` encapsula lógica de creación
- **Separación de responsabilidades**: Controlador no conoce detalles de persistencia
- **Flexibilidad**: Puede implementar diferentes estrategias de creación
- **Validaciones académicas**: Verifica unicidad y restricciones básicas

### transferencia automática C→U para asignaturas

- **Creación mínima**: Solo datos esenciales para asignatura válida
- **Transferencia inmediata**: **&lt;&lt;include&gt;&gt;** editarAsignatura() automático
- **Completar en edición**: "El gordo" maneja información académica completa
- **Eficiencia de flujo**: Minimiza pasos para crear asignatura completa

## consideraciones de diseño específicas para asignaturas

### reutilización del controlador

El diseño permite que `AsignaturaController` sea reutilizado:
- **Compartido**: Con editarAsignatura() y eliminarAsignatura()
- **Método específico**: crearAsignatura() con validaciones mínimas propias
- **Consistencia**: Mismo patrón de comunicación con repositorio
- **Validaciones**: Específicas para creación académica

### patrón include para edición

- **Separación de responsabilidades**: crearAsignatura() se enfoca en crear básico
- **Reutilización**: **&lt;&lt;include&gt;&gt;** editarAsignatura() evita duplicar funcionalidad de edición
- **Flujo natural**: Creación → Edición automática
- **Experiencia continua**: Usuario no percibe discontinuidad

### creación de batería de preguntas

- **Automática**: `AsignaturaRepository` crea `BateriaDePreguntas` junto con la asignatura
- **Vacía inicial**: La batería se crea sin preguntas, lista para ser poblada
- **Relación de composición**: La batería pertenece exclusivamente a esa asignatura

### experiencia de usuario académica

- **Simplicidad**: Solo pide información esencial (título, código, curso académico)
- **Rapidez**: Mínimos pasos para crear asignatura válida
- **Continuidad**: Transferencia automática a edición completa
- **Claridad**: Usuario entiende que debe completar en edición

## validaciones de negocio académicas

### restricciones de integridad curricular mínima

**AsignaturaController** debe verificar:
- **Título obligatorio**: Campo título no puede estar vacío
- **Código obligatorio**: Campo código no puede estar vacío
- **Curso académico obligatorio**: Campo curso académico no puede estar vacío
- **Unicidad**: No existe otra asignatura con el mismo código
- **Formato básico**: Código cumple requisitos mínimos de formato
- **Permisos de docente**: Docente autorizado para crear asignaturas

### manejo de errores académicos

- **Campos vacíos**: Mensaje informativo sobre campos obligatorios
- **Asignatura duplicada**: Explicación de conflicto con asignatura existente
- **Error de sistema**: Manejo graceful de fallos de persistencia

## diferencias con otros casos CRUD de asignaturas

### crearAsignatura() vs editarAsignatura()

**crearAsignatura():**
- **Objetivo**: Creación básica con datos mínimos
- **Interacción**: Solicitud mínima + transferencia automática
- **Validaciones**: Restricciones básicas de creación
- **Resultado**: Asignatura básica creada + edición completa automática

**editarAsignatura():**
- **Objetivo**: Modificación de datos académicos completos
- **Interacción**: Lectura + escritura múltiple de todos los campos
- **Validaciones**: Restricciones académicas de contenido completo
- **Resultado**: Asignatura actualizada con información completa

### complementariedad CRUD para asignaturas

- **crearAsignatura()**: "El delgado" - añade asignatura básica al sistema
- **editarAsignatura()**: "El gordo" - completa y modifica información académica
- **eliminarAsignatura()**: Remueve asignaturas del sistema
- **verAsignaturas()**: Lista y selecciona asignaturas


