# Generador de Exámenes > cancelarGeneracion > Diseño

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/cancelarGeneracion/cancelarGeneracion.svg)|[Análisis](/documents/analisis/cancelarGeneracion/README.md)|**Diseño**|
> |-|-|-|-|

## Información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Diseño
- **Versión**: 1.0 (Spring Boot + React)
- **Fecha**: 2026-06-04
- **Autor**: Equipo de desarrollo

## Propósito

Detallar la interacción entre los componentes del sistema para cancelar la generación de exámenes, limpiando las plantillas de la sesión y volviendo al estado anterior.

## Diagrama de secuencia de diseño

<div align=center>

|![Diseño: cancelarGeneracion()](/images/diseño/cancelarGeneracion/secuencia.svg)|
|-|
|[Código PlantUML](/modelosUML/diseño/cancelarGeneracion/secuencia.puml)|

</div>

## Participantes

| Participante | Tipo | Responsabilidad |
| :--- | :--- | :--- |
| **Frontend (React)** | Componente | Diálogo de confirmación de cancelación |
| **ExamenesController** | Controller | Recibe petición DELETE `/api/examenes/generar/cancelar`, delega a servicio |
| **ExamenService** | Service | Orquestación de la cancelación |
| **ExamenSessionService** | Service | Limpieza de plantillas de la HttpSession |
| **HttpSession** | Session | Almacenamiento efímero de plantillas |

## Decisiones de diseño

### Operación simple

`cancelarGeneracion()` es la operación inversa de `generarExamenes()`. Simplemente limpia las plantillas almacenadas en la sesión del servidor.

- **DELETE `/api/examenes/generar/cancelar`**: Elimina las plantillas de la sesión
- **Retorno**: 204 No Content

### Colaboración entre servicios

```
ExamenService
└── collaborate ExamenSessionService
    └── limpiarPlantillas() → void
```

### Estados de sesión resultantes

Tras la cancelación, `HttpSession` no contiene `"plantillasExamenes"` (se ha eliminado el atributo).

## Flujo de navegación

1. Docente accede desde `EXAMENES_GENERADOS` o `EXAMENES_GENERADOS_CONTEXTUALES`
2. Sistema muestra diálogo de confirmación
3. Docente confirma → DELETE `/api/examenes/generar/cancelar` → se limpian las plantillas
4. Docente deniega → se mantiene el estado actual
5. **Salidas:**
   - Confirmar (global) → `SISTEMA_DISPONIBLE`
   - Confirmar (contextual) → `ASIGNATURA_ABIERTO`
   - Denegar → `EXAMENES_GENERADOS` o `EXAMENES_GENERADOS_CONTEXTUALES`

## Excepciones HTTP

- **500 Internal Server Error**: Error al limpiar la sesión