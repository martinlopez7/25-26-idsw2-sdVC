# Sistema de Generación de Exámenes > importarConfiguracionGlobal > Diseño

> |[🏠️](/README.md)|[Volver](/documents/diseño/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/importarConfiguracionGlobal/importarConfiguracionGlobal.svg)|[Análisis](/documents/analisis/importarConfiguracionGlobal/README.md)|**Diseño**|
> |-|-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-03
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para importar la configuración global del sistema desde un archivo JSON previamente exportado, sustituyendo todos los datos existentes del docente autenticado.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/importarConfiguracionGlobal/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/importarConfiguracionGlobal/secuencia.puml)|

</div>

## Formato del archivo de importación

El archivo de importación es un JSON con la misma estructura que el de exportación:

```json
{
  "version": "1.0",
  "fechaExportacion": "2026-06-03T10:30:00Z",
  "docenteOrigen": {
    "id": 1,
    "nombre": "María",
    "apellidos": "García"
  },
  "elementos": {
    "grados": [...],
    "asignaturas": [...],
    "alumnos": [...],
    "preguntas": [
      {
        "id": 1,
        "enunciado": "¿Qué es...?",
        "tema": "Tema 1",
        "dificultad": "FACIL",
        "respuestas": [
          { "opcion": "A", "esCorrecta": true },
          { "opcion": "B", "esCorrecta": false }
        ]
      }
    ]
  }
}
```

### Orden de importación

La importación sigue un **orden jerárquico** para garantizar la integridad referencial:

1. **Grados** → Primero, sin dependencias
2. **Asignaturas** → Dependen de grados (relación Many-to-Many)
3. **Alumnos** → Dependen de grados y docentes
4. **Preguntas** → Dependen de asignaturas y docentes (las respuestas van incluidas por composición 1:*)

### Estrategia de reemplazo

El proceso usa **Reemplazar todo**: antes de importar se eliminan todos los datos existentes del docente autenticado en orden inverso:

1. **Preguntas** → (cascading elimina respuestas asociadas)
2. **Asignaturas**
3. **Alumnos**
4. **Grados**

## Participantes

- **Frontend (React + TypeScript)**: Componente `ImportarConfiguracionGlobalComponent` con selector de archivo y diálogo de confirmación.
- **ConfiguracionController**: Endpoint REST `POST /api/configuracion/importar` protegido con JWT.
- **ConfiguracionService**: Lógica de negocio para orquestar el reemplazo e importación.
- **GradoRepository, AsignaturaRepository, AlumnoRepository, PreguntaRepository, RespuestaRepository**: Acceso a datos para eliminación e inserción.
- **Base de Datos (PostgreSQL)**: Operaciones de eliminación e inserción.

## Decisiones de diseño

- **Reemplazar todo**: Se eliminan todos los datos del docente antes de importar, garantizando un estado limpio.
- **Orden de eliminación inverso**: Preguntas → Asignaturas → Alumnos → Grados (para mantener integridad referencial).
- **Orden de importación**: Grados → Asignaturas → Alumnos → Preguntas (orden directo para reconstruir).
- **Transaccionalidad**: El proceso completo es atómico; si falla algo, se hace rollback de toda la operación.
- **Validación de formato**: Se verifica que el archivo JSON tenga la estructura esperada antes de procesar.
- **El docente autenticado extrae su ID del JWT** para identificar qué datos reemplazar.