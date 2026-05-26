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

**Decisión:** El análisis que ha hecho del caso de uso verPreguntas() se ha aceptado. Lo único que se ha modificado es un error menor del diagrama de colaboración: se añadió la opción de navegación disponible editarAsignatura(), la cual se puede ejecutar cuando el sistema se encuentra en el estado "PREGUNTAS_CONTEXTUALES_ABIERTO". 

## [23/05/2026][10:50] Análisis de verRespuestas()

**Prompt:** empezar → Analiza el caso de uso verRespuestas

**Resultado:** Se realizó el análisis MVC de verRespuestas() generando:
- documents/analisis/verRespuestas/README.md
- modelosUML/analisis/verRespuestas/colaboracion.puml
- modelosUML/analisis/verRespuestas/secuencia.puml

El análisis identificó las clases de análisis (VerRespuestasView, RespuestasController, RespuestaRepository, Respuesta, Pregunta), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. También se añadió la sección de diagrama de secuencia al README.md siguiendo el formato especificado en AGENTS.md.

**Enlace:** [session-ses_1ac0.md](conversations/session-ses_1ac0.md)

**Decisión:** El análisis que ha hecho del caso de uso verRespuestas() se ha aceptado. Lo único que se ha modificado es un error menor del diagrama de colaboración: se eliminó la opción de navegación disponible completarGestion (la cual te dirigía al estado SISTEMA_DISPONIBLE) porque esto no está especificado en el diagrama de contexto del sistema.

## [23/05/2026][11:15] Análisis de completarGestion()

**Prompt:** empezar → Analiza el caso de uso completarGestion, ten en cuenta que este caso de uso esta presente en ambos actores

**Resultado:** Se realizó el análisis MVC de completarGestion() generando:
- documents/analisis/completarGestion/README.md
- modelosUML/analisis/completarGestion/colaboracion.puml

El análisis identificó las clases de análisis (CompletarGestionView, CompletarGestionController, Sesion, OpcionesMenu, PermisosRepository), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. Se identificó que es el único caso de uso compartido por ambos actores (Docente y Administrador Institucional), con un diagrama de colaboración que muestra todas las opciones disponibles para cada actor.

**Enlace:** [session-ses_1abe.md](conversations/session-ses_1abe.md)

**Decisión:** El análisis que ha hecho del caso de uso completarGestion() se modificó bastante. El error estuvo en que la IA interpretó que todos los casos de uso CRUD de las entidades del sistema eran colaboradores de completarGestion(), pero esto no es así, ya que solo lo son los casos de uso ver{Recurso}(), generarExamenes(), corregirExamenes(), importarConfiguracionGlobal(), exportarConfiguracionGlobal() y cerrarSesion(). Es decir, solo los casos de uso que se pueden ejecutar inmediatamente después de haber ejecutado completarGestion().

## [24/05/2026][10:57] Análisis de crearDocente()

**Prompt:** empezar → Analiza el caso de uso crearDocente

**Resultado:** Se realizó el análisis MVC de crearDocente() generando:
- documents/analisis/crearDocente/README.md
- modelosUML/analisis/crearDocente/colaboracion.puml

El análisis siguió el patrón "el delgado" (creación rápida con filosofía C→U), identificando las clases: CrearDocenteView, DocentesController, DocenteRepository, Docente. El flujo incluye creación mínima con transferencia automática a edición mediante <<include>> editarDocente().

**Enlace:** [session-ses_1a6d.md](conversations/session-ses_1a6d.md)

**Decisión:** El análisis que ha hecho del caso de uso crearDocente() se aceptó al completo.

## [24/05/2026][11:07] Análisis de editarDocente()

**Prompt:** empezar → Analiza el caso de uso editarDocente

**Resultado:** Se realizó el análisis MVC de editarDocente() generando:
- documents/analisis/editarDocente/README.md
- modelosUML/analisis/editarDocente/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarDocenteView, DocentesController, DocenteRepository, Docente. El flujo incluye carga de datos del docente, presentación para edición continua, guardado incremental y navegación flexible mediante <<include>> verDocentes().

**Enlace:** [session-ses_1a6c.md](conversations/session-ses_1a6c.md)

**Decisión:** El análisis que ha hecho del caso de uso editarDocente() se ha aceptado. Lo único que se ha modificado es un error menor del diagrama de colaboración:  se añadió la opción de navegación disponible eliminarDocente(), la cual se puede ejecutar cuando el sistema se encuentra en el estado "DOCENTE_ABIERTO". También se añadió el caso de uso colaborador `crearDocente()` como entrada a editarDocente().

## [24/05/2026][11:33] Análisis de eliminarDocente()

**Prompt:** empezar → Analiza el caso de uso eliminarDocente

**Resultado:** Se realizó el análisis MVC de eliminarDocente() generando:
- documents/analisis/eliminarDocente/README.md
- modelosUML/analisis/eliminarDocente/colaboracion.puml

El análisis siguió el patrón de eliminación segura con confirmación previa, identificando las clases: EliminarDocenteView, DocentesController, DocenteRepository, Docente. El flujo incluye carga de datos del docente, presentación de información con advertencia de eliminación irreversible, confirmación/cancelación por parte del administrador, y navegación mediante <<include>> verDocentes(). Doble entrada desde DOCENTES_ABIERTO o DOCENTE_ABIERTO.

**Enlace:** [session-ses_1a6b.md](conversations/session-ses_1a6b.md)

**Decisión:** El análisis que ha hecho del caso de uso eliminarDocente() se aceptó al completo. Lo único que se ha eliminado es una tabla del README sobre las opciones de navegación que sobraba ya que solo añadía ruido.

## [24/05/2026][12:41] Análisis de exportarConfiguracionGlobal()

**Prompt:** empezar → Analiza el caso de uso exportarConfiguracionGlobal

**Resultado:** Se realizó el análisis MVC de exportarConfiguracionGlobal() generando:
- documents/analisis/exportarConfiguracionGlobal/README.md
- modelosUML/analisis/exportarConfiguracionGlobal/colaboracion.puml

El análisis identificó las clases de análisis (ExportarConfiguracionGlobalView, ExportarConfiguracionGlobalController, AlumnoRepository, GradoRepository, AsignaturaRepository, PreguntaRepository), sus responsabilidades, colaboraciones y opciones de navegación. El usuario solicitó dos correcciones: (1) cambiar el repositorio genérico ConfiguracionGlobalRepository por los repositorios específicos de cada entidad, y (2) modificar el flujo para que la exportación sea global (todos los elementos) en lugar de selectiva, debido a las dependencias entre entidades (preguntas→asignaturas, asignaturas→grados).

**Enlace:** [session-ses_1a6a.md](conversations/session-ses_1a6a.md)

**Decisión:** El análisis inicial tenía un error conceptual: se creó un repositorio genérico (ConfiguracionGlobalRepository) cuando lo correcto era usar los repositorios específicos de cada entidad (AlumnoRepository, GradoRepository, etc.) que ya existen en el sistema. Esto se corrigió. Además, se modificó el análisis para indicar que la exportación es global y obligatoria (todos los elementos juntos), no selectiva, debido a las restricciones de integridad referencial entre entidades.

## [24/05/2026][12:55] Análisis de importarConfiguracionGlobal()

**Prompt:** empezar → Analiza el caso de uso importarConfiguracionGlobal, basandote en el previamente hecho "exportarConfiguracionGlobal"

**Resultado:** Se realizó el análisis MVC de importarConfiguracionGlobal() generando:
- documents/analisis/importarConfiguracionGlobal/README.md
- modelosUML/analisis/importarConfiguracionGlobal/colaboracion.puml

El análisis siguió la misma estructura que exportarConfiguracionGlobal(), pero con las adaptaciones necesarias para la importación: sentido de flujo inverso, orden secuencial de importación (alumnos → grados → asiganturas → preguntas) para mantener integridad referencial.

**Enlace:** [session-ses_1a66.md](conversations/session-ses_1a66.md)

**Decisión:** El análisis que ha hecho del caso de uso importarConfiguracionGlobal() se aceptó pero tuvo un pequeño error: así como para exportar tenia que "obtener" los datos de los repositorios, para importar tendrá que añadir los datos a los repositorios y devolver void, cosa que no cambió respecto al caso de uso exportarConfiguracionGlobal().

## [24/05/2026][13:18] Análisis de crearGrado()

**Prompt:** empezar → Analiza el caso de uso crearGrado

**Resultado:** Se realizó el análisis MVC de crearGrado() generando:
- documents/analisis/crearGrado/README.md
- modelosUML/analisis/crearGrado/colaboracion.puml

El análisis siguió el patrón "el delgado" (creación rápida con filosofía C→U), identificando las clases: CrearGradoView, GradosController, GradoRepository, Grado. El flujo incluye creación mínima con datos mínimos (título, código) y transferencia automática a edición mediante <<include>> editarGrado().

**Enlace:** [session-ses_1a64.md](conversations/session-ses_1a64.md)

**Decisión:** El análisis que hizo del caso de uso crearGrado() tuvo algunos errores. En el diagrama de colaboración no siguió bien el formato deseado y lo tuve que modificar manualmente.

## [25/05/2026][11:30] Análisis de editarGrado()

**Prompt:** empezar → Analiza el caso de uso editarGrado

**Resultado:** Se realizó el análisis MVC de editarGrado() generando:
- documents/analisis/editarGrado/README.md
- modelosUML/analisis/editarGrado/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarGradoView, GradosController, GradoRepository, Grado. El flujo incluye carga de datos del grado, presentación para edición continua (nombre, código, lista de alumnos), guardado incremental y navegación flexible mediante <<include>> verGrados() y <<include>> eliminarGrado().

**Enlace:** [session-ses_1a18.md](conversations/session-ses_1a18.md)

**Decisión:** El análisis que hizo del caso de uso editarGrado() se aceptó pero tuvo un pequeño error. En el diagrama de colaboración he añadido la relación Grado → Alumno, ya que un grado esta compuesto de alumnos, por lo que al editar un grado la entidad `Alumno` debe de estar presente.

## [25/05/2026][11:52] Análisis de eliminarGrado()

**Prompt:** analiza el caso de uso eliminarGrado

**Resultado:** Se realizó el análisis MVC de eliminarGrado() generando:
- documents/analisis/eliminarGrado/README.md
- modelosUML/analisis/eliminarGrado/colaboracion.puml

El análisis identificó las clases de análisis (EliminarGradoView, GradosController, GradoRepository, Grado, Alumno), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón de eliminación segura con confirmación previa. Doble entrada desde GRADOS_ABIERTO o GRADO_ABIERTO.

**Enlace:** [session-ses_1a17.md](conversations/session-ses_1a17.md)

**Decisión:** El análisis que hizo del caso de uso eliminarGrado se aceptó.

## [25/05/2026][12:10] Análisis de crearAlumno()

**Prompt:** analiza el caso de uso crearAlumno

**Resultado:** Se realizó el análisis MVC de crearAlumno() generando:
- documents/analisis/crearAlumno/README.md
- modelosUML/analisis/crearAlumno/colaboracion.puml

El análisis siguió el patrón "el delgado" (creación rápida con filosofía C→U), identificando las clases: CrearAlumnoView, AlumnosController, AlumnoRepository, Alumno. El flujo incluye creación mínima con datos esenciales (nombre, apellidos, DNI) y transferencia automática a edición mediante <<include>> editarAlumno().

**Enlace:** [session-export.md](conversations/session-ses_1a16.md)

**Decisión:** El análisis que hizo del caso de uso crearAlumno se aceptó.

## [25/05/2026][12:25] Análisis de editarAlumno()

**Prompt:** analiza el caso de uso editarAlumno

**Resultado:** Se realizó el análisis MVC de editarAlumno() generando:
- documents/analisis/editarAlumno/README.md
- modelosUML/analisis/editarAlumno/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarAlumnoView, AlumnosController, AlumnoRepository, Alumno. El flujo incluye carga de datos del alumno, presentación para edición continua (DNI, nombre, apellidos), guardado incremental y navegación flexible mediante <<include>> verAlumnos() y <<include>> eliminarAlumno(). Triple entrada desde ALUMNOS_ABIERTO, ALUMNO_ABIERTO o desde crearAlumno().

**Enlace:** [session-ses_1a15.md](conversations/session-ses_1a15.md)

**Decisión:** El análisis que hizo del caso de uso editarAlumno se aceptó.

## [25/05/2026][12:31] Análisis de eliminarAlumno()

**Prompt:** analiza el caso de uso eliminarAlumno

**Resultado:** Se realizó el análisis MVC de eliminarAlumno() generando:
- documents/analisis/eliminarAlumno/README.md
- modelosUML/analisis/eliminarAlumno/colaboracion.puml

El análisis siguió el patrón de eliminación segura con confirmación previa, identificando las clases: EliminarAlumnoView, AlumnosController, AlumnoRepository, Alumno. El flujo incluye carga de datos del alumno, presentación de información con advertencia de eliminación irreversible, confirmación/cancelación por parte del docente, y navegación mediante <<include>> verAlumnos(). Doble entrada desde ALUMNOS_ABIERTO o ALUMNO_ABIERTO.

**Enlace:** [session-ses_1a19.md](conversations/session-ses_1a19.md)

**Decisión:** El análisis que hizo del caso de uso eliminarAlumno se aceptó.

## [25/05/2026][12:45] Análisis de crearAsignatura()

**Prompt:** analiza el caso de uso crearAsignatura

**Resultado:** Se realizó el análisis MVC de crearAsignatura() generando:
- documents/analisis/crearAsignatura/README.md
- modelosUML/analisis/crearAsignatura/colaboracion.puml
- modelosUML/analisis/crearAsignatura/secuencia.puml

El análisis siguió el patrón "el delgado" (filosofía C→U), identificando las clases: CrearAsignaturaView, AsignaturaController, AsignaturaRepository, Asignatura, BateriaDePreguntas, Grado, GradoRepository. El flujo incluye creación mínima con datos esenciales (título, código, curso académico), creación automática de la BateríaDePreguntas asociada y transferencia automática a edición mediante <<include>> editarAsignatura().

**Enlace:** [session-ses_1a14.md](conversations/session-ses_1a14.md)

**Decisión:** El análisis que hizo del caso de uso crearAsignatura se modificó. En el diagrama de colaboración dio a entender que cuando se crea una asignatura se le asigna un solo grado, pero esto no es así, una asignatura puede tener asociada mas de un grado. Además, esta asociación no se hace en la creación, si no en la edición, que se ejecuta inmediatamente después de proporcionar los datos mínimos de la asignatura.

## [26/05/2026][09:17] Análisis de editarAsignatura()

**Prompt:** empezar → Analiza el caso de uso editarAsignatura

**Resultado:** Se realizó el análisis MVC de editarAsignatura() generando:
- documents/analisis/editarAsignatura/README.md
- modelosUML/analisis/editarAsignatura/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarAsignaturaView, AsignaturaController, AsignaturaRepository, GradoRepository, Asignatura, Grado, BateriaDePreguntas. El flujo incluye carga de datos completos de la asignatura, presentación del formulario de edición con todos los campos (título, código, curso académico, grados asociados, número de preguntas), edición continua con guardado incremental y navegación flexible (guardar, ver preguntas, generar examen, eliminar, cancelar). Múltiples puntos de entrada: desde ASIGNATURAS_ABIERTO, ASIGNATURA_ABIERTO, PREGUNTAS_CONTEXTUALES_ABIERTO, EXAMENES_ASIGNADOS_CONTEXTUALES, o desde crearAsignatura() mediante C→U.

**Enlace:** [session-ses_19ce.md](conversations/session-ses_19ce.md)

**Decisión:** El análisis que hizo del caso de uso editarAsignatura se modificó bastante. Las relaciones entre clases de tipo entity se modificaron porque no eran muy claras. Además las salidas disponibles del caso de uso estaban mal, por lo que las tuve que poner manualmente.

## [26/05/2026][09:49] Análisis de eliminarAsignatura()

**Prompt:** empezar → Analiza el caso de uso eliminarAsignatura

**Resultado:** Se realizó el análisis MVC de eliminarAsignatura() generando:
- documents/analisis/eliminarAsignatura/README.md
- modelosUML/analisis/eliminarAsignatura/colaboracion.puml

El análisis siguió el patrón de eliminación segura con confirmación previa, identificando las clases: EliminarAsignaturaView, AsignaturasController, AsignaturaRepository, Asignatura. El flujo incluye carga de datos de la asignatura, presentación de información con advertencia de eliminación irreversible (título, código, curso académico, batería de preguntas), confirmación/cancelación por parte del docente, y navegación mediante <<include>> verAsignaturas(). Doble entrada desde ASIGNATURAS_ABIERTO o ASIGNATURA_ABIERTO.

**Enlace:** [session-ses_19cc.md](conversations/session-ses_19cc.md)

**Decisión:** El análisis que hizo del caso de uso eliminarAsignatura se aceptó.

## [26/05/2026][09:58] Análisis de crearPregunta()

**Prompt:** empezar → Analiza el caso de uso crearPregunta

**Resultado:** Se realizó el análisis MVC de crearPregunta() generando:
- documents/analisis/crearPregunta/README.md
- modelosUML/analisis/crearPregunta/colaboracion.puml

El análisis siguió el patrón "el delgado" (filosofía C→U), identificando las clases: CrearPreguntaView, PreguntasController, PreguntaRepository, Pregunta, Asignatura. El flujo incluye creación mínima con datos esenciales (asignatura, enunciado, tema, dificultad), verificación de unicidad del enunciado y transferencia automática a edición mediante <<include>> editarPregunta(). Doble entrada desde PREGUNTAS_ABIERTO o PREGUNTAS_CONTEXTUALES_ABIERTO.

**Enlace:** [session-ses_19cb.md](conversations/session-ses_19cb.md)

**Decisión:** El análisis que hizo del caso de uso crearPregunta se aceptó mayoritariamente. El único error que se corrigió fueron unas salidas del caso de uso que no tenían mucho sentido ya que ya estaban especificadas con la transferencia inmediata a edición. Además se añadio el parámetro `asigantuiraId` cuando el caso de uso se ejecuta desde las preguntas contextuales de una asignatura.

## [26/05/2026][10:16] Análisis de editarPregunta()

**Prompt:** empezar → Analiza el caso de uso editarPregunta

**Resultado:** Se realizó el análisis MVC de editarPregunta() generando:
- documents/analisis/editarPregunta/README.md
- modelosUML/analisis/editarPregunta/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarPreguntaView, PreguntasController, PreguntaRepository, Pregunta, Respuesta. El flujo incluye carga de datos completos de la pregunta (asignatura, enunciado, tema, dificultad, respuestas, habilitada), presentación para edición continua, guardado incremental y navegación flexible. Múltiples puntos de entrada: desde PREGUNTAS_ABIERTO, PREGUNTAS_CONTEXTUALES_ABIERTO, PREGUNTA_ABIERTO, PREGUNTA_CONTEXTUAL_ABIERTO, RESPUESTAS_ABIERTO, RESPUESTAS_CONTEXTUALES_ABIERTO, o desde crearPregunta() mediante C→U.

**Enlace:** [session-ses_19ca.md](conversations/session-ses_19ca.md)

**Decisión:** 
