# Generador de Exámenes > asignarExamenes > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/asignarExamenes/asignarExamenes.svg)|[Análisis](/documents/analisis/asignarExamenes/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-04
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controllers, Services, Session) para la asignación de plantillas de examen a alumnos destinatarios, generación de clave de corrección y descarga de PDFs. Los exámenes son efímeros: se asignan en memoria, se descargan y se mantienen en sesión para corrección posterior.

## Diagrama de secuencia de diseño

<div align=center>

|![Diseño: asignarExamenes()](/images/diseño/asignarExamenes/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/asignarExamenes/secuencia.puml)|

</div>

## Participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Muestra formulario de asignación con alumnos por grado, inicia descarga de PDFs |
| **ExamenesController** | Controller | Recibe petición POST `/api/examenes/asignar`, coordina flujo |
| **ExamenService** | Service | Orquestación: recupera plantillas, asigna alumnos, genera claves, coordina descarga |
| **AlumnoService** | Service | Proporciona alumnos por grado matriculados en la asignatura |
| **ExamenSessionService** | Service | Recuperación de plantillas desde HttpSession, actualización con alumno asignado y clave de corrección |
| **PdfGenerationService** | Service | Generación de PDF para cada examen asignado |
| **Base de Datos (PostgreSQL)** | Entidad | Consulta de alumnos (solo lectura para asignación) |

## Decisiones de diseño

### Arquitectura efímera (continuación)

Este diseño es continuación de `generarExamenes()`. Los exámenes ya existen en la `HttpSession` como `PlantillaExamen` con `clavePendiente=true`. En `asignarExamenes()`:

1. Se recupera cada plantilla de la sesión
2. Se asocia un alumno destinatario
3. Se genera la **clave de corrección definitiva** (hash con: preguntas + respuestas correctas + alumno)
4. Se actualiza la plantilla en sesión (clavePendiente=false)
5. Se genera y descarga el PDF
6. Se marca la plantilla como asignada

### Clave de corrección

La clave de corrección es un hash MD5/SHA que identifica inequívocamente:
- Secuencia de preguntas del examen
- Respuestas correctas de cada pregunta
- Alumno destinatario

```
claveCorreccion = hashMD5(
  pregunta1.id + respuestaCorrecta1 +
  pregunta2.id + respuestaCorrecta2 +
  ...
  alumno.id
)
```

Esta clave permite en `corregirExamenes()` verificar que el examen subido corresponde al original.

### Colaboración entre servicios

```
ExamenService
├── collaborate ExamenSessionService
│   ├── obtenerPlantillas() → List<PlantillaExamen>
│   ├── actualizarPlantilla(plantilla) → void
│   └── marcarComoAsignada(plantillaId) → void
├── collaborate AlumnoService
│   └── obtenerAlumnosPorGrado(asignaturaId) → Map<GradoDTO, List<AlumnoDTO>>
├── collaborate PdfGenerationService
│   └── generarPdf(plantilla, alumno, claveCorreccion) → byte[]
```

### Flujo de asignación

1. **Entrada**: Desde `generarExamenes()` vía `<<include>>` o directamente desde `EXAMENES_GENERADOS`
2. **Recuperación**: `ExamenService` obtiene plantillas de la sesión via `ExamenSessionService`
3. **Carga de alumnos**: `ExamenService` solicita a `AlumnoService` los alumnos por grado de la asignatura
4. **Asignación**: Para cada plantilla:
   - Seleccionar alumno destinatario
   - Generar clave de corrección definitiva
   - Actualizar plantilla en sesión
   - Generar PDF
   - Añadir a lista de descarga
5. **Descarga**: El frontend recibe los PDFs y_trigger la descarga
6. **Salida**: Estado `EXAMENES_ASIGNADOS` → `completarGestion()` o volver a `ASIGNATURA_ABIERTO` (contextual)

### Estados de sesión actualizados

```
HttpSession (docente)
└── "plantillasExamenes" : List<PlantillaExamen>
    ├── 0: PlantillaExamen(id="uuid-1", asignatura, evaluacion, preguntas[],
                           alumnoId=123, claveCorreccion="abc123", clavePendiente=false, asignada=true)
    ├── 1: PlantillaExamen(id="uuid-2", ..., alumnoId=456, claveCorreccion="def456", ...)
    └── ...
```

### API Endpoints

- **GET `/api/examenes/plantillas`**: Recupera las plantillas generadas de la sesión actual. Retorna `List<PlantillaExamenDTO>`.
- **GET `/api/examenes/{plantillaId}/pdf`**: Genera y descarga el PDF de un examen específico con su clave de corrección.
- **POST `/api/examenes/asignar`**: Body: `{ plantillaId, alumnoId }`. Actualiza la plantilla con el alumno asignado y genera la clave de corrección.
- **POST `/api/examenes/asignar-todos`**: Body: `{ asignaciones: [{plantillaId, alumnoId}, ...] }`. Asigna todos los exámenes de una vez.
- **GET `/api/examenes/asignados/pdf-lote`**: Genera un ZIP con todos los PDFs de los exámenes asignados.

### DTOs

**Request/Response:**
- **AsignarExamenesRequest**: `{ plantillaId, alumnoId }` o `{ asignaciones: [{plantillaId, alumnoId}, ...] }`
- **PlantillaExamenDTO**: `id`, `asignaturaId`, `evaluacion`, `numPreguntas`, `alumnoId` (null si no asignado), `claveCorreccion` (null si no asignado), `asignada` (boolean)
- **AlumnoPorGradoDTO**: `gradoId`, `gradoNombre`, `alumnos` → `List<AlumnoDTO>`

### Flujo de navegación

1. Docente accede desde `EXAMENES_GENERADOS` o `EXAMENES_GENERADOS_CONTEXTUALES`
2. Sistema carga plantillas de la sesión y muestra alumnos por grado
3. Docente puede:
   - Asignar examen a alumno (seleccionar de la lista)
   - Asignar todos automáticamente (1:1 o distribución)
   - Cancelar asignación → `cancelarAsignacion()` → vuelve a generarExamenes() o Menu
4. Confirmar → generar PDFs → descargar → estado `EXAMENES_ASIGNADOS`
5. Desde `EXAMENES_ASIGNADOS`: `completarGestion()` → `SISTEMA_DISPONIBLE` o `ASIGNATURA_ABIERTO`

## Validaciones

- **Plantilla existe**: La plantilla debe existir en la sesión
- **Alumno válido**: El alumno debe estar matriculado en la asignatura
- **Alumno no asignado**: Un alumno no puede recibir dos exámenes
- **Límites**: No se pueden asignar más exámenes que alumnos disponibles por grado

## Excepciones HTTP

- **400 Bad Request**: Datos de asignación inválidos
- **404 Not Found**: Plantilla no encontrada en sesión
- **409 Conflict**: Alumno ya tiene un examen asignado en esta generación