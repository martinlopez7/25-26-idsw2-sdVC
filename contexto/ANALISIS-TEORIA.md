# Análisis - Disciplina de Análisis y Diseño

Esta sección contiene el análisis arquitectónico de los casos de uso especificados, aplicando el patrón MVC y identificando las clases de análisis necesarias para la implementación.

## Contenido de la disciplina

### [Casos de uso - Análisis MVC](casos-uso/README.md)
Análisis completo de cada caso de uso especificado mediante:
- **Clases de análisis**: Boundary, Control, Entity según patrón MVC
- **Diagramas de colaboración**: Interacciones entre clases de análisis
- **Diagramas de secuencia**: Flujo temporal para casos complejos
- **Responsabilidades definidas**: Separación clara de responsabilidades por estereotipo

## Metodología de análisis aplicada

### Patrón MVC sistemático
- **Model (Entity)**: Entidades del dominio y repositorios de datos
- **View (Boundary)**: Clases de interfaz que manejan interacción con actores
- **Controller (Control)**: Coordinación de lógica de negocio y flujo de casos de uso

### Estereotipos de análisis
- **Boundary (Vista)**: `#629EF9` - Clases de interfaz usuario-sistema
- **Control (Controlador)**: `#b5bd68` - Clases de coordinación y lógica
- **Entity (Entidad)**: `#F2AC4E` - Clases de dominio y persistencia
- **Collaboration**: `#CDEBA5` - Referencias a otros casos de uso

### Diagramas de colaboración
- **Estructura de package**: Cada caso de uso como paquete independiente
- **Entradas desde estados**: Conexiones desde estados del diagrama de contexto
- **Salidas con include**: Navegación mediante `<<include>>` a otras colaboraciones
- **Relaciones MVC**: Controlador intermedia entre Vista y Entidades

## Cobertura de análisis

### Casos completamente analizados
- **Gestión del sistema**: iniciarSesion(), cerrarSesion(), completarGestion()
- **Apertura de entidades**: Todos los casos abrirXXX() con patrón de listado
- **CRUD de Programas**: Casos completos con filosofía C→U
- **CRUD de Cursos**: Análisis completo incluyendo secuencias detalladas
- **CRUD de Profesores**: Incluyendo configurarPreferenciasProfesor() especializado
- **CRUD de Edificios**: Con validación de dependencias de aulas
- **CRUD de Aulas**: Con gestión de recursos y validación de horarios
- **CRUD de Recursos**: Con impacto en preferencias de profesores
- **Gestión de Horarios**: Algoritmo de optimización y consulta

### Patrones de colaboración identificados

#### Patrón de apertura (abrirXXX)
- **Vista**: Presenta lista de entidades con opciones de acción
- **Controlador**: Coordina carga de datos y navegación
- **Repositorio**: Proporciona datos de listado paginado o filtrado

#### Patrón "el delgado" (crear)
- **Vista mínima**: Captura datos básicos únicamente
- **Controlador**: Validación y creación inmediata
- **Redirección automática**: `<<include>>` a edición del objeto creado

#### Patrón "el gordo" (editar)
- **Vista completa**: Todos los campos editables con sesión continua
- **Controlador**: Validación incremental y guardado múltiple
- **Navegación flexible**: Continuar editando o regresar a listado

#### Patrón de eliminación segura
- **Vista de confirmación**: Presenta datos y solicita confirmación
- **Controlador**: Validación de dependencias antes de eliminar
- **Repositorio**: Verificación de restricciones de integridad

## Arquitectura emergente

### Separación de responsabilidades por capas
- **Capa de Presentación**: Todas las clases Boundary
- **Capa de Lógica**: Todas las clases Control especializadas por dominio
- **Capa de Datos**: Repositorios especializados y entidades del dominio

### Reutilización de controladores
- **Controladores por entidad**: ProfesorController, CursoController, etc.
- **Métodos especializados**: crear(), editar(), eliminar(), listar()
- **Validaciones centralizadas**: Restricciones de negocio en controladores

### Repositorios especializados
- **Un repositorio por entidad principal**: ProfesorRepository, CursoRepository
- **Métodos de consulta específicos**: obtenerPorId(), listar(), buscar()
- **Validaciones de integridad**: Verificación de dependencias antes de operaciones

## Trazabilidad

### De especificación a análisis
- **Cada caso de uso especificado** tiene su análisis MVC correspondiente
- **Vocabulario consistente**: Mantenimiento del vocabulario puro en análisis
- **Estados mapeados**: Estados de especificación corresponden a responsabilidades de clases

### De análisis a diseño
- **Clases de análisis** serán refinadas en clases de diseño específicas
- **Colaboraciones identificadas** guiarán la definición de interfaces
- **Patrones arquitectónicos** facilitarán decisiones de implementación
