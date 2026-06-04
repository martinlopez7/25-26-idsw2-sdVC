# Generador de Exámenes > corregirExamenes > Diseño

> |[🏠️](/documents/diseño/README.md)|[📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/corregirExamenes/corregirExamenes.svg)|[Análisis](/documents/analisis/corregirExamenes/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-04
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema para la corrección de exámenes realizados por alumnos. Los exámenes fueron generados y asignados en sesiones anteriores (efímeras), por lo que se recuperan de la HttpSession del docente. La corrección es simplificada: el sistema devuelve notas aleatorias del 1 al 10 tras procesar los PDFs subidos.

## Diagrama de secuencia de diseño

<div align=center>

|![Diseño: corregirExamenes()](/images/diseño/corregirExamenes/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/corregirExamenes/secuencia.puml)|

</div>

## Participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Muestra exámenes asignados, permite subir PDFs resueltos y mostrar resultados |
| **ExamenesController** | Controller | Recibe petición POST `/api/examenes/corregir`, coordina flujo |
| **ExamenService** | Service | Orquestación: recupera exámenes de sesión, coordina corrección |
| **ExamenSessionService** | Service | Recuperación de exámenes asignados desde HttpSession |
| **CorreccionService** | Service | Procesa PDFs y genera notas aleatorias (implementación simplificada) |
| **HttpSession** | Session | Almacenamiento de exámenes asignados pendientes de corrección |

## Decisiones de diseño

### Corrección simplificada

Dado que la corrección automática con IA de reconocimiento de imagen es demasiado compleja para este proyecto académico, el profesor indicó que se implemente de forma simplificada:

- El docente sube los PDFs resueltos por los alumnos
- El sistema asigna una nota aleatoria del 1 al 10 a cada examen
- Esta nota se muestra como "resultado" de la corrección

### Flujo de corrección

1. **Entrada**: Desde `SISTEMA_DISPONIBLE` (menú del docente)
2. **Recuperación**: `ExamenService` obtiene exámenes asignados de la sesión via `ExamenSessionService`
3. **Visualización**: Se muestran los exámenes pendientes de corrección (alumno, clave de corrección, fecha de asignación)
4. **Subida de PDFs**: Docente sube PDF con exámenes resueltos (agrupados o individuales)
5. **Procesamiento**: `CorreccionService` procesa cada PDF y genera nota aleatoria 1-10
6. **Resultado**: Se muestran las notas asignadas
7. **Limpieza**: Opcionalmente se pueden limpiar los exámenes de la sesión tras corrección
8. **Salida**: `EXAMENES_CORREGIDOS` → `completarGestion()` → `SISTEMA_DISPONIBLE`

### Colaboración entre servicios

```
ExamenService
├── collaborate ExamenSessionService
│   └── obtenerExamenesAsignados() → List<PlantillaExamen>
├── collaborate CorreccionService
│   └── procesarExamenesResueltos(pdfs) → List<ResultadoCorreccion>
```

### Estados de sesión

Los exámenes permanecen en la HttpSession después de `asignarExamenes()` hasta que son corregidos o hasta que expira la sesión.

```
HttpSession (docente)
└── "plantillasExamenes" : List<PlantillaExamen>
    ├── 0: PlantillaExamen(..., alumnoId=123, claveCorreccion="abc123", corregido=false)
    └── ...
```

### API Endpoints

- **GET `/api/examenes/asignados`**: Recupera los exámenes asignados pendientes de corrección. Retorna `List<ExamenAsignadoDTO>`.
- **POST `/api/examenes/corregir`**: Body: multipart/form-data con PDFs de exámenes resueltos. Retorna `List<ResultadoCorreccionDTO>` con notas aleatorias 1-10.
- **DELETE `/api/examenes/asignados`**: Limpia los exámenes asignados de la sesión tras corrección. Retorna 204 No Content.

### DTOs

**Response:**
- **ExamenAsignadoDTO**: `plantillaId`, `alumnoId`, `alumnoNombre`, `claveCorreccion`, `fechaAsignacion`
- **ResultadoCorreccionDTO**: `plantillaId`, `alumnoId`, `alumnoNombre`, `nota` (1-10 aleatorio), `observaciones`
- **CorreccionMasivaRequest**: `pdfs` (lista de archivos)

### Flujo de navegación

1. Docente accede desde `SISTEMA_DISPONIBLE`
2. Sistema carga exámenes asignados de la sesión
3. Docente puede:
   - Subir PDFs resueltos para corrección
   - Ver notas resultantes
   - Limpiar exámenes de sesión
4. Confirmar → `EXAMENES_CORREGIDOS`
5. `completarGestion()` → `SISTEMA_DISPONIBLE`

## Validaciones

- **Exámenes en sesión**: Deben existir exámenes asignados en la sesión
- **PDF válido**: El archivo debe ser un PDF legible
- **Coincidencia**: Se intenta verificar que el PDF corresponda a la clave de corrección (simplificado)

## Excepciones HTTP

- **400 Bad Request**: No hay exámenes en sesión o PDFs inválidos
- **404 Not Found**: Examen específico no encontrado en sesión
- **409 Conflict**: Examen ya corregido anteriormente