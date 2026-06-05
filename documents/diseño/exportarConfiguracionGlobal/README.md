# Sistema de Generación de Exámenes > exportarConfiguracionGlobal > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/exportarConfiguracionGlobal/exportarConfiguracionGlobal.svg)|[Análisis](/documents/analisis/exportarConfiguracionGlobal/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-03
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema (Frontend React, Controller, Service, Repository) para exportar la configuración global del sistema en un archivo JSON que pueda ser importado por otro docente, manteniendo la integridad referencial entre entidades.

## Diagrama de secuencia de diseño

<div align=center>

|![Diagrama de Secuencia](/images/diseño/exportarConfiguracionGlobal/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/exportarConfiguracionGlobal/secuencia.puml)|

</div>

## Formato del archivo de exportación

El archivo de exportación será un JSON con la siguiente estructura jerárquica:

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

### Orden de exportación

La exportación sigue un **orden jerárquico** para garantizar la integridad referencial al importar:

1. **Grados** → Primero, sin dependencias
2. **Asignaturas** → Dependen de grados (relación Many-to-Many)
3. **Alumnos** → Dependen de grados y docentes
4. **Preguntas** → Dependen de asignaturas y docentes (las respuestas van incluidas por composición 1:*)

## Participantes

- **Frontend (React + TypeScript)**: Componente `ExportarConfiguracionGlobalComponent` con diálogo de confirmación.
- **ConfiguracionController**: Endpoint REST `GET /api/configuracion/exportar` protegido.
- **ConfiguracionService**: Lógica de negocio para orquestar la exportación.
- **GradoRepository, AsignaturaRepository, AlumnoRepository, PreguntaRepository, RespuestaRepository**: Acceso a datos de cada entidad.
- **Base de Datos (PostgreSQL)**: Consultas para obtener todos los elementos del docente autenticado.

## Decisiones de diseño

- **Formato JSON**: Estándar, legible, ampliamente soportado y fácil de parsear.
- **Estructura jerárquica**: Permite mantener relaciones entre entidades durante la importación.
- **Orden de exportación por dependencias**: Grados → Asignaturas → Alumnos → Preguntas (con respuestas incluidas).
- **Respuestas incluidas en preguntas**: Al ser composición 1:*, las respuestas se exportan junto con su pregunta padre.
- **Metadata de exportación**: Incluye versión, fecha y docente origen para trazabilidad.
- **Exportación global obligatoria**: Todos los elementos juntos debido a las restricciones de integridad referencial.
- **El docente autenticado extrae su ID del JWT** para filtrar sus elementos propios.