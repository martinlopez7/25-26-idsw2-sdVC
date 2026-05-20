# Agente - Normas de Comportamiento

## Protocolo de Inicialización

Antes de iniciar cualquier conversación con el usuario, el agente DEBE leer y analizar exhaustivamente los siguientes archivos de contexto del proyecto para interiorizar la naturaleza del mismo:

1. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidad.puml` - Diagrama de entidades del modelo de dominio
2. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidadConsideraciones.md` - Consideraciones y relaciones del modelo de dominio
3. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-docente.svg` - Diagrama de actores y casos de uso para el rol docente
4. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-administradorInstitucional.svg` - Diagrama de actores y casos de uso para el rol administrador institucional
5. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoDocente/diagramaContexto.puml` - Diagrama de contexto para el docente
6. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoAdministradorInstitucional/diagramaContexto.puml` - Diagrama de contexto para el administrador institucional

## Dominio del Proyecto

Este es un sistema de gestión de exámenes universitarios que involucra:

- **Entidades principales**: Examen, Asignatura, Pregunta, BateríaDePreguntas, Grado, Profesor, Alumno, Respuesta
- **Actores**: Docente, Administrador Institucional
- **Funcionalidades**: Gestión de grados, asignaturas, alumnos, preguntas, respuestas, generación y corrección de exámenes

El agente debe comprender la estructura del dominio, las relaciones entre entidades, los diagramas de contexto de cada actor y las capacidades del sistema antes de proporcionar asistencia.

## Protocolo de Cierre

Cuando escriba la palabra "adios" quiero que pobles el archivo `conversation-log.md` con un registro de lo conversado, siguiendo el formato:

> ```
> ## [Dia/Mes/Año][Hora:Minuto] Título breve de lo que se pidió
>
> **Prompt:** lo que le dijo al AI (textual o resumido fielmente)
>
> **Resultado:** lo que produjo
> ```