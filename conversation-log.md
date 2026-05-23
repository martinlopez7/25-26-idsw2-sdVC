# Conversation log

## [20/5/2026][11:57] Crear archivo AGENTS.md con protocolo de inicialización

**Prompt:** queria que crees un archivo AGENTS.md en el que especifiques que antes de empezar a conversar lo primero que hagas sea interiorizar la naturaleza del proyecto leyendo los archivos de contexto listados

**Resultado:** Se leyó el contenido de los 6 archivos de contexto especificados (diagrama de entidades, consideraciones del modelo, diagramas de actores y casos de uso para docente y administrador institucional, y diagramas de contexto). Se creó el archivo AGENTS.md en la raíz del proyecto con el protocolo de inicialización que indica al agente leer estos archivos antes de cualquier conversación.

**Decisión:** Esto se aceptó y además se añadieron manualmente unas cuantas cosas. Se modificó el protocolo de inicialización: se añadió el archivo de `conversation-log.md` y se modificó para ejecutarlo cuando se escribiese la palabra "empezamos". Además, se añadió también un protocolo de cierre que se ejecuta con la palabra "adios" para poblar el conversation-log.md utilizando el formato propuesto por el profesor y el apartado "enlace", el cual te redirigió a la exportación en formato markdown de la sesión. Se añadió un protocolo para que cuando el agente necesite información de un caso de uso concreto analizase su detallado y un protocolo para que el agente sepa realizar el análisis de un caso de uso.

## [21/05/2026][10:14] Revisión de interiorización de la naturaleza del sistema y análisis de verGrados()

**Prompt:** empezar (empezamos) → Pidió una explicación de los diagramas de contexto de los actores del sistema → Luego preguntó sobre la relación entre crearDocente e iniciarSesion → Preguntó por los estados "contextuales" del diagrama de contexto del docente → Solicitó analizar el caso de uso verGrados

**Resultado:** Se interiorizó el modelo de dominio y los casos de uso. Se explicaron los diagramas de contexto de Docente y Admin Institucional. Se explicó la relación de precedencia entre crearDocente e iniciarSesion (crearDocente produce las credenciales que iniciarSesion consume). Se identificó que los estados "contextuales" son aquellos que pertenecen a una asignatura específica (vs. globales). Se realizó el análisis MVC completo de verGrados() generando README.md, colaboracion.puml y secuencia.puml.

**Enlace:** [session-ses_1bae.md](conversations/session-ses_1bae.md)

**Decisión:** El análisis que ha hecho del caso de uso verGrados() se ha aceptado. Lo único que se ha modificado es un pequeño error del diagrama de colaboración, concretamente las opciones de navegación disponibles.

## [21/05/2026][13:25] Análisis de verAsignaturas()

**Prompt:** empezar → Analiza el caso de uso verAsignaturas

**Resultado:** Se realizó el análisis MVC completo de verAsignaturas() generando los siguientes artefactos:
- documents/analisis/verAsignaturas/README.md
- modelosUML/analisis/verAsignaturas/colaboracion.puml

El análisis identificó las clases de análisis (VerAsignaturasView, AsignaturasController, AsignaturaRepository, Asignatura, Grado), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto.

**Enlace:** [session-ses_1b5c.md](conversations/session-ses_1b5c.md)

**Decisión:** El análisis que ha hecho del caso de uso verAsiganuras() se ha aceptado. Lo único que se ha modificado son unos errores menores del diagrama de colaboración: se añadió una opción de navegación disponible (importarAsignatura) y se añadió un estado del sistema desde el que se recibe el caso de uso (Asignatura_Abierto). 

## [22/05/2026][10:25] Análisis de iniciarSesion()

**Prompt:** empezar → Analiza el caso de uso iniciarSesion → Lo has hecho muchisimo mas complicado de lo que realmente es. Fijate en el ejemplo ya hecho

**Resultado:** Se realizó el análisis MVC de iniciarSesion() generando:
- documents/analisis/iniciarSesion/README.md
- modelosUML/analisis/iniciarSesion/colaboracion.puml

El análisis inicial fue demasiado detallado. Tras feedback, se simplificó siguiendo el mismo patrón que verGrados(), con estructura más concisa y diagrama de colaboración más simple.

**Enlace:** [session-ses_1b14.md](conversations/session-ses_1b14.md)

**Decisión:** El análisis que hizo la primera vez fue muy complejo pero al decirle en un segundo prompt que no se complicase tanto y que siguiese el patrón de los ejemplos dados lo modificó todo y lo hizo bien. El único error fue que en el diagrama de colaboración no especificó desde que estado se ejecutaba el caso de uso.

## [22/05/2026][11:10] Análisis de cerrarSesion()

**Prompt:** empezar → Analiza el caso de uso cerrarSesion → Fijate en el analisis de contexto\ejemplos-analisis\cerrarSesion\ y sigue ese mismo patrón

**Resultado:** Se realizó el análisis MVC de cerrarSesion() generando:
- documents/analisis/cerrarSesion/README.md
- modelosUML/analisis/cerrarSesion/colaboracion.puml

El análisis siguió el patrón del ejemplo externo, incluyendo SesionRepository como clase Model pura, con flujos principal (cierre confirmado) y alternativo (cierre cancelado). Se identificaron las clases: CerrarSesionView, CerrarSesionController, SesionRepository, Sesion y Usuario.

**Enlace:** [session-ses_1b12.md](conversations/session-ses_1b12.md)

**Decisión:** El análisis que hizo la primera vez fue muy complejo pero al decirle en un segundo prompt que siguiese el patrón del ejemplo de cerrarSesion de sigHor lo hizo bien. La corrección que hice fue una simplificación del diagrama de colaboración.

## [22/05/2026][11:49] Análisis de verAlumnos()

**Prompt:** empezar → Analiza el caso de uso verAlumnos

**Resultado:** Se realizó el análisis MVC de verAlumnos() generando:
- documents/analisis/verAlumnos/README.md
- modelosUML/analisis/verAlumnos/colaboracion.puml

El análisis identificó las clases de análisis (VerAlumnosView, AlumnosController, AlumnoRepository, Alumno), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto.

**Enlace:** [session-ses_1b0f.md](conversations/session-ses_1b0f.md)

**Decisión:** El análisis que hizo a la primera fue prácticamente perfecto. Sin embargo tuve que arreglar un pequeño error de un estado previo (ALUMNOS_ABIERTO) desde el que no se puede ejecutar el caso de uso y remplazarlo por el estado previo (ALUMNO_ABIERTO) desde el que si que se puede ejecutar este caso de uso.

## [23/05/2026][10:08] Análisis de verDocentes()

**Prompt:** empezar → Analiza el caso de uso verDocentes

**Resultado:** Se realizó el análisis MVC de verDocentes() generando:
- documents/analisis/verDocentes/README.md
- modelosUML/analisis/verDocentes/colaboracion.puml

El análisis identificó las clases de análisis (VerDocentesView, DocentesController, DocenteRepository, Docente), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto.

**Enlace:** [session-ses_1ac2.md](conversations/session-ses_1ac2.md)

**Decisión:** El análisis que ha hecho del caso de uso verDocentes() se ha aceptado. Lo único que se ha modificado es un error menor del diagrama de colaboración: se añadió un estado del sistema desde el que se ejecuta el caso de uso (Docente_Abierto). 

## [23/05/2026][10:26] Análisis de verPreguntas()

**Prompt:** empezar → Analiza el caso de uso verPreguntas → Añademe el diagrama de secuencia al README, tal y como esta especificado en @AGENTS.md

**Resultado:** Se realizó el análisis MVC de verPreguntas() generando:
- documents/analisis/verPreguntas/README.md
- modelosUML/analisis/verPreguntas/colaboracion.puml
- modelosUML/analisis/verPreguntas/secuencia.puml

El análisis identificó las clases de análisis (VerPreguntasView, PreguntasController, PreguntaRepository, Pregunta, Respuesta, Asignatura), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. También se añadió la sección de diagrama de secuencia al README.md siguiendo el formato especificado en AGENTS.md.

**Enlace:** [session-ses_1ac1.md](conversations/session-ses_1ac1.md)

**Decisión:** 
