# Generador de Exámenes > generarExamenes > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/generarExamenes/generarExamenes.svg)|[Análisis](/documents/analisis/generarExamenes/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-04
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controllers, Services, Repositories, Session) para la generación efímera de plantillas de examen. Los exámenes se generan en memoria (sesión del servidor), sin persistencia en BD, y se transfieren inmediatamente a `asignarExamenes()`.

## Diagrama de secuencia de diseño

<div align=center>

|![Diseño: generarExamenes()](/images/diseño/generarExamenes/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/generarExamenes/secuencia.puml)|

</div>

## Participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Formulario de configuración con asignatura, evaluación, temas, número de preguntas y parámetros por grado |
| **ExamenesController** | Controller | Recibe petición POST `/api/examenes/generar`, extrae docenteId del JWT, delega a ExamenService |
| **ExamenService** | Service | Orquestación de la generación: coordina servicios colaboradores, genera plantillas en memoria, almacena en sesión |
| **AsignaturaService** | Service | Proporciona asignatura verificada y grados asociados con sus alumnos matriculados |
| **PreguntaService** | Service | Proporciona preguntas filtradas por asignatura y temas, clasificadas por dificultad |
| **ExamenSessionService** | Service | Gestión del almacenamiento y recuperación de plantillas en la HttpSession |
| **ExamenesRepository** | Repository | Acceso a datos de exámenes (solo para verificación de existencia si aplica) |
| **Base de Datos (PostgreSQL)** | Entidad | Persistencia de entidades existentes (solo lectura durante generación) |

## Decisiones de diseño

### Arquitectura efímera con servicios cohesivos

**Principio**: Cada servicio es responsable de su dominio. Los servicios collaboratean entre sí manteniendo su cohesión.

- **ExamenService** orquesta la generación pero no accede a repositorios directamente
- **AsignaturaService** proporciona todo lo relacionado con asignatura, grados y alumnos
- **PreguntaService** proporciona todo lo relacionado con preguntas de la batería
- **ExamenSessionService** gestiona el almacenamiento efímero en sesión

### Colaboración entre servicios

```
ExamenService
├── collaborate AsignaturaService
│   └── obtenerAsignaturaConGradosYAlumnos(asignaturaId, docenteId)
│       → AsignaturaDTO (con grados y conteo de alumnos por grado)
├── collaborate PreguntaService
│   └── obtenerPreguntasPorAsignaturaYTemas(asignaturaId, temas)
│       → List<PreguntaDTO> (clasificadas por dificultad)
├── collaborate ExamenSessionService
│   ├── almacenarPlantillas(plantillas) → void
│   └── obtenerPlantillas() → List<PlantillaExamen>
```

### Parámetros por grado

La generación es configurable por cada grado asociado a la asignatura:

| Parámetro | Descripción | Límite |
| :--- | :--- | :--- |
| `numExamenes` | Número de plantillas a generar para el grado | 1..alumnosMatriculadosDelGrado |
| `numTiposExamen` | Variantes diferentes (mismo contenido, orden diferente) | 1..alumnosMatriculadosDelGrado |
| `proporcionDificultad` | Porcentaje de preguntas fáciles, medias, difíciles | 0..100% (suma = 100%) |

### Selección de preguntas

El algoritmo de generación (dentro de ExamenService):
1. Recibe preguntas clasificadas por dificultad de PreguntaService
2. Para cada plantilla de examen:
   - Selecciona preguntas según la proporción de dificultad configurada por grado
   - Aleatoriza el orden de preguntas y opciones de respuesta
   - Crea plantilla con clave de corrección pendiente

### API Endpoints

- **POST `/api/examenes/generar**`: Genera las plantillas de examen en memoria. Body: `GenerarExamenesRequest`. Retorna `GenerarExamenesResponse` con IDs de plantillas almacenadas en sesión.
- **DELETE `/api/examenes/cancelar****: Cancela la generación actual, limpia las plantillas de la sesión. Retorna 204 No Content.

### DTOs

**Request/Response:**
- **GenerarExamenesRequest**: `asignaturaId`, `evaluacion` (enum), `temas` (lista), `numPreguntas`, `configPorGrado` (mapa gradoId → ConfigGrado)
- **ConfigGrado**: `numExamenes`, `numTiposExamen`, `proporcionDificultad` (facil/medio/dificil)
- **GenerarExamenesResponse**: `plantillaExamenIds` (lista), `totalExamenesGenerados`

**Internos:**
- **AsignaturaConGradosDTO**: `id`, `titulo`, `codigo`, `evaluacion`, `grados` → lista de **GradoConAlumnosDTO**
- **GradoConAlumnosDTO**: `id`, `titulo`, `codigo`, `numAlumnos`
- **PreguntaDTO**: `id`, `enunciado`, `tema`, `dificultad`, `respuestas`
- **PlantillaExamen**: Objeto en memoria con `id`, `asignaturaId`, `evaluacion`, `preguntas[]`, `clavePendiente=true`

### Flujo de navegación

1. Docente accede desde `SISTEMA_DISPONIBLE` o `ASIGNATURA_ABIERTO`
2. Sistema presenta formulario de generación (datos cargados via API de asignaturas)
3. Docente selecciona: asignatura, evaluación, tema(s), número de preguntas
4. Para cada grado de la asignatura: configurar numExamenes, numTiposExamen, proporción dificultad
5. Docente confirma → POST `/api/examenes/generar`
6. ExamenService orquesta la generación collaborando con AsignaturaService y PreguntaService
7. ExamenService almacena plantillas en sesión via ExamenSessionService
8. Sistema transfiere a `<<include>> asignarExamenes()` con los IDs de plantillas
9. Si docente cancela → DELETE `/api/examenes/cancelar` → ExamenSessionService limpia la sesión

### Estados de sesión

```
HttpSession (docente)
└── "plantillasExamenes" : List<PlantillaExamen>
    ├── 0: PlantillaExamen(id="uuid-1", asignatura, evaluacion, preguntas[], clavePendiente=true)
    ├── 1: PlantillaExamen(id="uuid-2", asignatura, evaluacion, preguntas[], clavePendiente=true)
    └── ...
```

### Clave de corrección pendiente

Durante `generarExamenes()` las plantillas tienen `clavePendiente = true`. La clave definitiva (hash con datos del alumno) se genera en `asignarExamenes()`.

## Validaciones

- **Asignatura requerida**: Debe existir y pertenecer al docente autenticado
- **Evaluación requerida**: Debe ser un valor del enum `EvaluacionExamen`
- **Temas requeridos**: Al menos un tema seleccionado
- **Número de preguntas > 0**: Valor positivo
- **Límites por grado**: `numExamenes` y `numTiposExamen` entre 1 y número de alumnos del grado en la asignatura
- **Preguntas suficientes**: La batería debe tener al menos `numPreguntas` preguntas para los temas seleccionados

## Excepciones HTTP

- **400 Bad Request**: Datos inválidos o insuficientes
- **404 Not Found**: Asignatura no existe o no pertenece al docente
- **409 Conflict**: No hay suficientes preguntas en la batería para los criterios seleccionados