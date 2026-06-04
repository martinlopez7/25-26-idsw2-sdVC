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

Detallar la interacción entre los componentes del sistema para la corrección de exámenes realizados por alumnos. El docente sube un único PDF donde cada página corresponde al examen respondido por un alumno. El sistema cuenta las páginas del PDF, genera una nota aleatoria (1-10) por cada página/alumno, y retorna los resultados.

## Diagrama de secuencia de diseño

<div align=center>

|![Diseño: corregirExamenes()](/images/diseño/corregirExamenes/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/corregirExamenes/secuencia.puml)|

</div>

## Participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Presenta formulario de subida de PDF, muestra resultados con notas |
| **ExamenesController** | Controller | Recibe POST `/api/examenes/corregir`, coordina flujo |
| **CorreccionService** | Service | Cuenta páginas del PDF, genera notas aleatorias 1-10 |
| **PDFReader** | Librería externa | Lee y cuenta páginas de un PDF (Apache PDFBox o similar) |

## Decisiones de diseño

### Corrección simplificada

Dado que la corrección automática con IA de reconocimiento de imagen es demasiado compleja para este proyecto académico, el profesor indicó que se implemente de forma simplificada:

- El docente sube un único PDF donde cada página es el examen de un alumno
- El sistema cuenta las páginas del PDF (X páginas = X alumnos)
- El sistema genera X notas aleatorias del 1 al 10
- Se retornan los resultados

### Flujo de corrección

1. **Entrada**: Desde `SISTEMA_DISPONIBLE` (menú del docente)
2. **Subida de PDF**: Docente selecciona y sube un PDF con exámenes resueltos
3. **Procesamiento**: `CorreccionService` cuenta las páginas del PDF
4. **Generación de notas**: Para cada página, genera una nota aleatoria del 1 al 10
5. **Resultado**: Retorna lista de resultados con número de página y nota
6. **Salida**: `completarGestion()` → `SISTEMA_DISPONIBLE`

### API Endpoints

- **POST `/api/examenes/corregir`**: Body: multipart/form-data con un PDF. Retorna `List<ResultadoCorreccionDTO>`.

### DTOs

**Response:**
- **ResultadoCorreccionDTO**: `numeroPagina` (1-indexed), `nota` (1-10 aleatorio)

### Flujo de navegación

1. Docente accede desde `SISTEMA_DISPONIBLE`
2. docente selecciona PDF con exámenes resueltos
3. Clic en "Corregir"
4. Sistema procesa y muestra resultados (número de página + nota)
5. `completarGestion()` → `SISTEMA_DISPONIBLE`

## Validaciones

- **PDF válido**: El archivo debe ser un PDF legible
- **PDF no vacío**: El PDF debe tener al menos una página

## Excepciones HTTP

- **400 Bad Request**: PDF inválido o vacío