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

**Decisión:** El análisis que hizo del caso de uso editarPregunta se aceptó mayoritariamente. El único error que hubo fue que en el diagrama de colaboración añadió mas casos de uso colaborativos para ejecutar después de editarPregunta() de los que realmente hay.

## [26/05/2026][10:45] Análisis de eliminarPregunta()

**Prompt:** empezar → Analiza el caso de uso eliminarPregunta

**Resultado:** Se realizó el análisis MVC de eliminarPregunta() generando:
- documents/analisis/eliminarPregunta/README.md
- modelosUML/analisis/eliminarPregunta/colaboracion.puml

El análisis siguió el patrón de eliminación segura con confirmación previa, identificando las clases: EliminarPreguntaView, PreguntasController, PreguntaRepository, Pregunta. El flujo incluye carga de datos de la pregunta, presentación de información con advertencia de eliminación irreversible (enunciado, tema, dificultad), confirmación/cancelación por parte del docente, y navegación mediante <<include>> verPreguntas(). Cuádruple entrada desde PREGUNTAS_ABIERTO, PREGUNTAS_CONTEXTUALES_ABIERTO, PREGUNTA_ABIERTO o PREGUNTA_CONTEXTUAL_ABIERTO.

**Enlace:** [session-ses_19c8.md](conversations/session-ses_19c8.md)

**Decisión:** El análisis que hizo del caso de uso eliminarPregunta no fue del todo correcto. Se olvidó de que las entidades Pregunta y Respuesta tienen una relación de composición, es decir, cuando la pregunta muere la respuesta también, por lo que tuve que modificarlo.

## [26/05/2026][11:07] Análisis de crearRespuesta()

**Prompt:** empezar → Analiza el caso de uso crearRespuesta

**Resultado:** Se realizó el análisis MVC de crearRespuesta() generando:
- documents/analisis/crearRespuesta/README.md
- modelosUML/analisis/crearRespuesta/colaboracion.puml

El análisis siguió el patrón "el delgado" (filosofía C→U), identificando las clases: CrearRespuestaView, RespuestasController, RespuestaRepository, Respuesta, Pregunta. La entidad Pregunta se incluyó por la relación de composición (1 *-- many) que existe entre Pregunta y Respuesta. El flujo incluye creación mínima con datos esenciales (contenido, esCorrecta) y transferencia automática a edición mediante <<include>> editarRespuesta(). Doble entrada desde RESPUESTAS_ABIERTO o RESPUESTAS_CONTEXTUALES_ABIERTO.

**Enlace:** [session-ses_19c7.md](conversations/session-ses_19c7.md)

**Decisión:** El análisis que hizo del caso de uso crearRespuesta se aceptó.

## [26/05/2026][11:17] Análisis de editarRespuesta()

**Prompt:** empezar → Analiza el caso de uso editarRespuesta

**Resultado:** Se realizó el análisis MVC de editarRespuesta() generando:
- documents/analisis/editarRespuesta/README.md
- modelosUML/analisis/editarRespuesta/colaboracion.puml

El análisis siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando las clases: EditarRespuestaView, RespuestasController, RespuestaRepository, Respuesta, Pregunta. El flujo incluye carga de datos de la respuesta, presentación para edición continua (contenido, esCorrecta), guardado incremental y navegación flexible. Múltiples puntos de entrada: desde RESPUESTAS_ABIERTO, RESPUESTA_ABIERTO, RESPUESTAS_CONTEXTUALES_ABIERTO, RESPUESTA_CONTEXTUAL_ABIERTO, o desde crearRespuesta() mediante C→U.

**Enlace:** [session-ses_19c9.md](conversations/session-ses_19c9.md)

**Decisión:** El análisis que hizo del caso de uso editarRespuesta fue correcto. Sin embargo, en el README se definió que solo una respuesta de cada pregunta puede ser correcta, lo cual he modificado porque cabe la posibilidad de que haya mas de una respuesta correcta.

## [26/05/2026][11:55] Análisis de eliminarRespuesta()

**Prompt:** analiza el caso de uso eliminarRespuesta

**Resultado:** Se realizó el análisis MVC de eliminarRespuesta() generando:
- documents/analisis/eliminarRespuesta/README.md
- modelosUML/analisis/eliminarRespuesta/colaboracion.puml

El análisis siguió el patrón de eliminación segura con confirmación previa, identificando las clases: EliminarRespuestaView, RespuestasController, RespuestaRepository, Respuesta, Pregunta. El flujo incluye carga de datos de la respuesta, presentación de información con advertencia de eliminación irreversible (contenido, correcta/incorrecta), confirmación/cancelación por parte del docente, y navegación mediante <<include>> verRespuestas(). Doble entrada desde RESPUESTAS_ABIERTO o RESPUESTAS_CONTEXTUALES_ABIERTO.

**Enlace:** [session-ses_19c4.md](conversations/session-ses_19c4.md)

**Decisión:** El análisis que hizo del caso de uso eliminarRespuesta se aceptó. El único error fue que en el diagrama de colaboración faltaban dos estados previos del sistema desde los que se pueden ejecutar el caso de uso, por lo que se añadieron manualmente.

## [26/05/2026][12:51] Análisis de generarExamenes()

**Prompt:** empezar → Analiza el caso de uso generarExamenes, en este caso si que quiero que hagas un diagrama de secuencia → Corrección: la dinámica es que la configuración es por grado y depende de los alumnos matriculados de cada grado en la asignatura

**Resultado:** Se realizó el análisis MVC de generarExamenes() generando:
- documents/analisis/generarExamenes/README.md
- modelosUML/analisis/generarExamenes/colaboracion.puml
- modelosUML/analisis/generarExamenes/secuencia.puml

El análisis inicial no interpretó correctamente la dinámica del caso de uso. Tras corrección del usuario, se entendió que: (1) al seleccionar asignatura, el sistema obtiene los grados asociados y cuántos alumnos de cada grado hay matriculados; (2) la configuración de exámenes es por grado, no global; (3) los límites de exámenes y tipos de examen dependen del número de alumnos matriculados de cada grado en la asignatura. Se corrigieron los tres artefactos añadiendo la entidad Alumno, la llamada obtenerAlumnosPorGrado() y la validación de límites por grado.

**Enlace:** [session-ses_19c3.md](conversations/session-ses_19c3.md)

**Decisión:** El análisis que hizo del caso de uso generarExamenes tuvo errores por lo que se le planteó el contexto de que algunos de los parámetros de configuración son diferentes por grado.

## [27/05/2026][09:35] Análisis de cancelarGeneracion()

**Prompt:** empezar → Analiza el caso de uso cancelarGeneracion, fijate en el protocolo de analisis de @AGENTS.md

**Resultado:** Se realizó el análisis MVC de cancelarGeneracion() generando:
- documents/analisis/cancelarGeneracion/README.md
- modelosUML/analisis/cancelarGeneracion/colaboracion.puml

El análisis identificó las clases de análisis (CancelarGeneracionView, ExamenesController, ExamenRepository, Examen), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. El caso de uso tiene doble entrada (EXAMENES_GENERADOS, EXAMENES_GENERADOS_CONTEXTUALES) y doble salida según la decisión del docente (confirmar → SISTEMA_DISPONIBLE o ASIGNATURA_ABIERTO; rechazar → estado actual).

**Enlace:** [session-ses_197a.md](conversations/session-ses_197a.md)

**Decisión:** El análisis que hizo del caso de uso cancelarGeneracion se aceptó. El único fallo fue que interpretó que el caso de uso se podia ejecutar desde el estado `ASIGNATURA_ABIERTO`, lo cual no es cierto y se modificó.

## [27/05/2026][10:02] Análisis de asignarExamenes()

**Prompt:** empezar → Analiza el caso de uso asignarExamenes, el cual es inmediatamente posterior a generarExamenes

**Resultado:** Se realizó el análisis MVC de asignarExamenes() generando:
- documents/analisis/asignarExamenes/README.md
- modelosUML/analisis/asignarExamenes/colaboracion.puml

El análisis identificó las clases de análisis (AsignarExamenesView, ExamenesController, ExamenRepository, Examen, Alumno, Grado), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. El caso de uso tiene doble entrada (EXAMENES_GENERADOS, EXAMENES_GENERADOS_CONTEXTUALES) y triple salida según la decisión del docente (confirmar → EXAMENES_ASIGNADOS o EXAMENES_ASIGNADOS_CONTEXTUALES; cancelar → estado de generación; error → reintentar).

**Enlace:** [session-ses_1979.md](conversations/session-ses_1979.md)

**Decisión:** El análisis que hizo del caso de uso asignarExamenes se aceptó. Las modificaciones que se hicieron fueron porque se centró demasiado en flujos alternativos del caso de uso.

## [27/05/2026][10:15] Análisis de corregirExamenes()

**Prompt:** empezar → Analiza el caso de uso corregirExamenes

**Resultado:** Se realizó el análisis MVC de corregirExamenes() generando:
- documents/analisis/corregirExamenes/README.md
- modelosUML/analisis/corregirExamenes/colaboracion.puml

El análisis identificó las clases de análisis (CorregirExamenesView, ExamenesController, ExamenRepository, Examen, Pregunta, Respuesta), sus responsabilidades, colaboraciones y opciones de navegación siguiendo el patrón metodológico del proyecto. El flujo principal: obtener exámenes pendientes → docente introduce respuestas → comparar con clave de corrección → guardar resultados → retornar a EXAMENES_CORREGIDOS.

**Enlace:** [session-ses_1978.md](conversations/session-ses_1978.md)

**Decisión:** El análisis que hizo del caso de uso corregirExamenes se aceptó.

## [27/05/2026][11:12] Diseño de iniciarSesion()

**Prompt:** empezar → Diseña el caso de uso iniciarSesion

**Resultado:** Se realizó el diseño MVC de iniciarSesion() generando:
- documents/diseño/iniciarSesion/README.md
- modelosUML/diseño/iniciarSesion/secuencia.puml

El diseño siguió el stack tecnológico del proyecto (Spring Boot + React + PostgreSQL), utilizando JWT para autenticación stateless y BCrypt para hashing de contraseñas. Se identificaron los participantes: Frontend (React), AuthController, AuthService, UsuarioRepository, JwtTokenProvider y Base de Datos (PostgreSQL). El token JWT incluye el tipo de actor (Docente o Administrador Institucional) para gestión de permisos en frontend.

**Enlace:** [session-ses_1975.md](conversations/session-ses_1975.md)

**Decisión:** Se aceptó el diseño de iniciarSesion

## [27/05/2026][11:20] Diseño de cerrarSesion()

**Prompt:** empezar → Diseña el caso de uso cerrarSesion

**Resultado:** Se realizó el diseño MVC de cerrarSesion() generando:
- documents/diseño/cerrarSesion/README.md
- modelosUML/diseño/cerrarSesion/secuencia.puml

El diseño siguió el stack tecnológico del proyecto (Spring Boot + React + PostgreSQL), detallando la interacción entre Frontend (React), AuthController, AuthService, JwtTokenProvider y SecurityContextLogoutHandler para invalidar el token JWT y cerrar la sesión del usuario.

**Enlace:** [session-ses_1974.md](conversations/session-ses_1974.md)

**Decisión:** Se aceptó el diseño de cerrarSesion. Sin embargo he añadido al diagrama de secuencia la alternativa de que el usuario cancele la acción de cerrarSesion.

## [27/05/2026][11:50] Diseño de crearDocente()

**Prompt:** empezar → Diseña el caso de uso crearDocente

**Resultado:** Se realizó el diseño MVC de crearDocente() generando:
- documents/diseño/crearDocente/README.md
- modelosUML/diseño/crearDocente/secuencia.puml

El diseño siguió el patrón "el delgado" (C→U) con creación rápida y transferencia automática a edición. El usuario identificó un error conceptual: el docente creado debía guardarse en `UsuarioRepository` (compartido con `iniciarSesion`) y no en un `DocenteRepository`, ya que el docente necesita esas credenciales para autenticarse. Se revisaron los tres diseños (crearDocente, iniciarSesion, cerrarSesion) para asegurar consistencia en el flujo de datos desde la creación hasta la autenticación.

**Enlace:** [session-ses_1972.md](conversations/session-ses_1972.md)

**Decisión:** Al principio el diseño fue erróneo porque la IA interpretó que `Docente` es una entidad genérica del sistema. Pero no es así ya que es la entidad que posteriormente va a usar el actor Docente para ejecutar el caso de uso iniciarSesion, por lo que se guardará en el repositorio de usuarios.

## [27/05/2026][12:12] Diseño de verDocentes()

**Prompt:** empezar → Diseña el caso de uso verDocentes

**Resultado:** Se realizó el diseño MVC de verDocentes() generando:
- documents/diseño/verDocentes/README.md
- modelosUML/diseño/verDocentes/secuencia.puml

El diseño siguió el stack tecnológico del proyecto (Spring Boot + React + PostgreSQL), identificando los participantes: Frontend (React) con DocentesListComponent, DocentesController con endpoints GET /api/docentes y GET /api/docentes?filtro={criterio}, UsuarioService, UsuarioRepository y Base de Datos (PostgreSQL). Se utilizó el repositorio compartido con iniciarSesion (UsuarioRepository) ya que los docentes son usuarios con tipo='DOCENTE'.

**Enlace:** [session-ses_1971.md](conversations/session-ses_1971.md)

**Decisión:** Se aceptó el diseño del caso de uso verDocentes.

## [27/05/2026][12:15] Diseño de editarDocente()

**Prompt:** empezar → Diseña el caso de uso editarDocente

**Resultado:** Se realizó el diseño MVC de editarDocente() generando:
- documents/diseño/editarDocente/README.md
- modelosUML/diseño/editarDocente/secuencia.puml

El diseño siguió el patrón "el gordo" (edición continua con múltiples ciclos de guardado). Participantes: Frontend (React) con UsuarioFormComponent en modo edición, DocentesController con endpoints GET/PUT /api/docentes/{id}, UsuarioService con lógica de validación y actualización, UsuarioRepository y Base de Datos (PostgreSQL). Validación de datos únicos (DNI, username, email) y manejo de errores con 400 y 404.

**Enlace:** [session-ses_1970.md](conversations/session-ses_1970.md)

**Decisión:** Se aceptó el diseño del caso de uso editarDocente

## [28/05/2026][09:06] Diseño de eliminarDocente()

**Prompt:** empezar → Diseña el caso de uso eliminarDocente

**Resultado:** Se realizó el diseño MVC de eliminarDocente() generando:
- documents/diseño/eliminarDocente/README.md
- modelosUML/diseño/eliminarDocente/secuencia.puml

El diseño siguió el patrón de eliminación segura con confirmación previa, utilizando UsuarioRepository (compartido con iniciarSesion) para la persistencia. Se incluyeron los participantes: Frontend (React) con modal de confirmación, DocentesController con endpoint DELETE /api/docentes/{id}, UsuarioService con lógica de eliminación y validación de existencia, UsuarioRepository y Base de Datos (PostgreSQL). Se manejó el error de integridad referencial (409 Conflict) cuando el docente tiene asignaturas asociadas.

**Enlace:** [session-ses_1929.md](conversations/session-ses_1929.md)

## [28/05/2026][09:33] Diseño de completarGestion()

**Prompt:** empezar → Diseña el caso de uso completarGestion

**Resultado:** Se realizó el diseño MVC de completarGestion() generando:
- documents/diseño/completarGestion/README.md
- modelosUML/diseño/completarGestion/secuencia.puml

El diseño identificó los participantes: Frontend (React) con MenuPrincipalComponent, AuthController con endpoint GET /api/auth/menu, AuthService con lógica para determinar opciones según tipo de actor (DOCENTE o ADMINISTRADOR_INSTITUCIONAL), y Sesion (JWT). Las opciones se determinan stateless en el servidor según el tipo de actor extraído del JWT, sin necesidad de consulta a base de datos.

**Enlace:** [session-ses_1928.md](conversations/session-ses_1928.md)

**Decisión:** El diseño inicial de completarGestion no era coherente con iniciarSesion y cerrarSesion porque usaba `Sesion` como entidad separada cuando el estado de autenticación es el JWT en LocalStorage, y porque no quedaba claro cómo se extraía el tipo de actor. Se corrigió para usar `JwtTokenProvider.extractTipoActor(token)` y eliminar la referencia a `Sesion` como entidad.

## [28/05/2026][10:22] Diseño de verAlumnos()

**Prompt:** empezar → Diseña el caso de uso verAlumnos

**Resultado:** Se realizó el diseño MVC de verAlumnos() generando:
- documents/diseño/verAlumnos/README.md
- modelosUML/diseño/verAlumnos/secuencia.puml

El diseño siguió el patrón de verDocentes() adaptándolo a la entidad Alumno, utilizando AlumnosController con endpoints GET /api/alumnos y GET /api/alumnos?filtro={criterio}, AlumnoService para lógica de negocio, y AlumnoRepository para persistencia.

**Enlace:** [session-ses_1925.md](conversations/session-ses_1925.md)

**Decisión:** El diseño que realizó del caso de uso verAlumnos fue mal interpretado. Interpretó que el sistema debe de mostrar todos los alumnos del sistema, pero lo que se busca es que solo muestre los alumnos del docente autenticado. Por lo que se modificó.

## [28/05/2026][10:53] Diseño de crearAlumno()

**Prompt:** empezar → Diseña el caso de uso crearAlumno

**Resultado:** Se realizó el diseño MVC de crearAlumno() generando:
- documents/diseño/crearAlumno/README.md
- modelosUML/diseño/crearAlumno/secuencia.puml

El diseño siguió la filosofía C→U (Create→Update), creando alumno con datos mínimos (nombre, apellidos, DNI) y transfiriendo automáticamente a `editarAlumno()`. Utiliza `AlumnoRepository` (no `UsuarioRepository` como en crearDocente) ya que los alumnos no se autentican en el sistema.

**Enlace:** [session-ses_1924.md](conversations/session-ses_1924.md)

**Decisión:** El diseño que que realizó del caso de uso crearAlumno fue impreciso ya que no asociaba el alumno creado con el docente autenticado. Esto se solucionó sacando el docenteId del token de autenticación y asociandolo al alumno.

## [28/05/2026][11:40] Diseño de editarAlumno()

**Prompt:** empezar → Diseña el caso de uso editarAlumno → Escribe los apartados participantes y decisiones de diseño del readme con el mismo formato que el de editarDocente basandote en el diagrama de secuencia

**Resultado:** Se realizó el diseño MVC de editarAlumno() generando:
- documents/diseño/editarAlumno/README.md
- modelosUML/diseño/editarAlumno/secuencia.puml

El diseño siguió el patrón "el gordo" (edición continua con múltiples ciclos de guardado). Participantes: Frontend (React) con AlumnoFormComponent en modo edición, AlumnosController con endpoints GET/PUT /api/alumnos/{id}, AlumnoService con lógica de validación y actualización, AlumnoRepository, DocenteRepository para verificar pertenencia al docente autenticado, y Base de Datos (PostgreSQL). Validación de datos únicos (DNI) y verificación de pertenencia del alumno al docente extraído del JWT.

**Enlace:** [session-ses_1922.md](conversations/session-ses_1922.md)

**Decisión:** Se aceptó el diseño del caso de uso editarAlumno

## [28/05/2026][12:04] Diseño de eliminarAlumno()

**Prompt:** empezar → Diseña el caso de uso eliminarAlumno, fijate en los diseños de crearAlumno, verAlumnos y editarAlumno

**Resultado:** Se realizó el diseño MVC de eliminarAlumno() generando:
- documents/diseño/eliminarAlumno/README.md
- modelosUML/diseño/eliminarAlumno/secuencia.puml

El diseño siguió el patrón de eliminación segura con confirmación previa (modal de React), identificando los participantes: Frontend (React) con modal de confirmación, AlumnosController con endpoint DELETE /api/alumnos/{id}, AlumnoService con lógica de eliminación y verificación de pertenencia al docente autenticado, AlumnoRepository y Base de Datos (PostgreSQL). Se incluyeron verificaciones de existencia del alumno y pertenencia al docente extraído del JWT.

**Enlace:** [session-ses_1920.md](conversations/session-ses_1920.md)

**Decisión:** El diseño del caso de uso eliminarAlumno se aceptó

## [29/05/2026][09:04] Inicialización de proyectos e implementación de iniciarSesion

**Prompt:** empezar → Usuario quiso inicializar proyectos Spring Boot y React → Modificar protocolo de implementación en AGENTS.md según estructura de carpetas → Implementar iniciarSesion → Probar y verificar que respeta el diseño

**Resultado:** Se modificó el protocolo de implementación en AGENTS.md para reflejar la estructura de carpetas real del proyecto (backend: `com.martin.exam_generator`, frontend: `src/` con components, hooks, services, types, context). Se crearon los proyectos: Spring Boot (usuario lo creó manualmente) y React + TypeScript (create-react-app con axios, react-router-dom, bootstrap).

Se implementó el caso de uso iniciarSesion y cerrarSesion generando:
- Backend: Usuario entity, UsuarioRepository, LoginRequest, JwtResponse, JwtTokenProvider, SecurityConfig (con CORS), AuthService, AuthController (login + logout), GlobalExceptionHandler, DataInitializer (usuario admin con password BCrypt)
- Frontend: AuthContext, authService, LoginPage, LogoutButton, App.tsx (router con rutas protegidas)

Se corrigieron imports en LoginPage.tsx y LogoutButton.tsx (ruta relativa incorrecta). Se añadió configuración CORS a SecurityConfig para permitir peticiones de localhost:3000.

Se verificó que la implementación respeta el diseño: endpoint POST /api/auth/login, LoginRequest/JwtResponse DTOs, BCrypt password verification, JWT con username y tipoActor, manejo de excepciones 401, token en LocalStorage.

**Enlace:** [session-ses_18fe.md](conversations/session-ses_18fe.md)

**Decisión:** Se aceptó la implementación de iniciarSesion

## [29/05/2026][09:42] Implementación de cerrarSesion()

**Prompt:** empezar → implementa el caso de uso cerrarSesion

**Resultado:** Se implementó el caso de uso cerrarSesion() generando:
- Backend: JwtTokenProvider - añadí blacklist de tokens con `invalidateToken()` y verificación en `validateToken()`, AuthService - implementé método `logout()` que extrae el token del header y lo añade a la blacklist
- Frontend: App.tsx - añadí import y uso de LogoutButton en el menú principal, LogoutButton.tsx - añadí confirmación con `window.confirm()` antes de cerrar sesión

**Enlace:** [session-ses_18d6.md](conversations/session-ses_18d6.md)

**Decisión:** El caso de uso cerrarSesion se aceptó.

## [29/05/2026][10:25] Implementación de verDocentes() y menús diferenciados por actor

**Prompt:** este sistema tiene dos actores, administrador institucional y docente. cuando cualquiera de los dos se autentica en el caso de uso iniciarSesion se le devuelve un token que contiene el tipo de actor que es. quiero que dependiendo del tipo de actor que sea, el front end devuelva una pagina principal distinta, ya que los casos de uso que ejecuta cada actor son distintos. que habria que hacer para implementar el caso de uso verDocentes

**Resultado:** Se implementó:
- Backend: UsuarioDTO, UsuarioService, DocentesController (GET /api/docentes), UsuarioRepository con métodos findByTipoActor() y findByTipoActorAndCriterio()
- Frontend: api.ts (axios con interceptor), docentesService, DocentesListComponent, AdminMenu.tsx y DocenteMenu.tsx, App.tsx con rutas separadas /menu-admin y /menu-docente, AdminRoute para proteger rutas por rol
- AuthContext exportó AuthUser type
- LoginPage redirige según tipoActor

**Enlace:** [session-ses_18d4.md](conversations/session-ses_18d4.md)

**Decisión:** Para redirigir a los actores a sus páginas principales respectivas tuve dos opciones: la primera una vez el usuario se autenticase dependiendo del tipo de actor que sea (información que queda almacenada en el token) redirigirles a ambos a la misma página pero rendirizar distintos componentes (uno para el admin y otro para el docente) y la segunda opción fue directamente redirigirles a páginas distintas. Me decanté por la segunda.

## [29/05/2026][12:26] Implementación de crearDocente() y corrección de JwtAuthenticationFilter

**Prompt:**  empezar → implementa el caso de uso crearDocente → Error 403 al crear docente, el endpoint no se alcanzaba

**Resultado:** Se implementó crearDocente() generando:
- Backend: UsuarioCreateDTO (con validaciones), campos adicionales en la entidad Usuario (nombre, apellidos, dni, email), UsuarioRepository con existsByUsername/Dni/Email, UsuarioService.crearDocente(), DocentesController POST /api/docentes, GlobalExceptionHandler para IllegalArgumentException
- Frontend: docentesService.crearDocente(), UsuarioFormComponent con formulario validado, ruta /docentes/crear en App.tsx

El error 403 se debía a que faltaba el JwtAuthenticationFilter. Sin él, el backend no procesaba el token JWT del frontend. Se creó el filtro y se configuró en SecurityConfig para extraer y validar el token del header Authorization.

**Enlace:** [session-ses_18d1.md](conversations/session-ses_18d1.md)

**Decisión:** La implementación del caso de uso crearDocente() se aceptó. Sin embargo la IA se olvidó de encriptar contraseña del docente, por lo que para ser fieles a su diseño se corrigió.

## [29/05/2026][12:55] Corrección de verDocentes() de acuerdo a su diseño

En el diseño del caso de uso verDocentes especificaba que el filtro se usaba para buscar docentes según su nombre, apellido y dni. Sin embargo me he despistado y la implementación solo permitía filtrar por nombre de usuario, por lo que lo he corregido para ser fieles al diseño.

## [29/05/2026][13:37] Implementación de editarDocente()

**Prompt:** empezar → me gustaria implementar el caso de uso editarDocente

**Resultado:** Se implementó editarDocente() generando:
- Backend: UsuarioUpdateDTO (campos nombre, apellidos, dni, username, email, password opcional), EntityNotFoundException, GlobalExceptionHandler actualizado para 404, DocentesController con GET/PUT /api/docentes/{id}, UsuarioService con obtenerDocentePorId() y actualizarDocente()
- Frontend: docentesService actualizado con obtenerDocentePorId() y actualizarDocente(), UsuarioFormComponent refactorizado para soportar modo edición con useParams (URL /docentes/editar/:id), App.tsx con ruta /docentes/editar/:id

**Enlace:** [session-ses_18c9.md](conversations/session-ses_18c9.md)

**Decisión:** La implementación del caso de uso editar se aceptó. Sin embargo, tal y como definimos en el requisitado, análisis y diseño, cuando se crea un docente se debe transferir el sistema a modo edición automáticamente, por lo que se corrigió.

## [29/05/2026][14:07] Implementación de eliminarDocente()

**Prompt:** empezar → me gustaria implementar el caso de uso eliminarDocente

**Resultado:** Se implementó eliminarDocente() generando:
- Backend: GlobalExceptionHandler - añadida handler para DataIntegrityViolationException → 409 Conflict, UsuarioService.eliminarDocente(Long id), DocentesController DELETE /api/docentes/{id} → 204 No Content
- Frontend: docentesService.eliminarDocente(id), EliminarDocenteComponent.tsx (nuevo componente con carga de datos, confirmación y eliminación), App.tsx con ruta /docentes/eliminar/:id protegida con AdminRoute

**Enlace:** [session-ses_18c6.md](conversations/session-ses_18c6.md)

**Decisión:** La implementación del caso de uso eliminarDocente() se aceptó. Sin embargo, tal y como definimos en el requisitado, se puede eliminar tanto en el listado de docentes como cuando tienes el docente abierto, por lo que este segundo "camino" de eliminación se añadió manualmente. Además, para ser fieles al diseño del caso de uso, al eliminar un docente se elimina por medio de su ID.

## [30/05/2026][10:10] Implementación de verAlumnos()

**Prompt:** empezar → me gustaria implementar el caso de uso verAlumnos del actor docente

**Resultado:** Se implementó verAlumnos() generando:
- Backend: Alumno entity (con docenteId), AlumnoRepository (findByDocenteId, findByDocenteIdAndCriterio), AlumnoDTO, AlumnoService (obtenerAlumnosDelDocente, filtrarAlumnos), AlumnosController GET /api/alumnos/mios
- Frontend: alumnosService.getAlumnos(), AlumnosListComponent.tsx, ruta /alumnos en App.tsx

**Modificaciones al JWT:** Para filtrar alumnos por el docente autenticado, se añadió docenteId al token JWT:
- JwtTokenProvider.generateToken() - añadido parámetro docenteId y claim "docenteId"
- JwtTokenProvider.extractDocenteId()
- AuthService.login() - pasa usuario.getId() como docenteId

**Enlace:** [session-ses_1882.md](conversations/session-ses_1882.md)

**Decisión:** Se aceptó la implementación del caso de uso verAlumnos.

## [30/05/2026][14:35] Implementación de crearAlumno()

**Prompt:** empezar → me gustaria implementar el caso de uso crearAlumno

**Resultado:** Se implementó crearAlumno() generando:
- Backend: AlumnoCreateDTO (con validaciones @NotBlank, @Pattern para DNI), AlumnosController POST /api/alumnos, AlumnoService.crearAlumno() con verificación de DNI único por docente, AlumnoRepository.existsByDocenteIdAndDni()
- Frontend: alumnosService.crearAlumno() y actualizarAlumno(), AlumnoFormComponent.tsx (formulario reutilizable para crear/editar), rutas /alumnos/crear y /alumnos/editar/:id en App.tsx

**Enlace:** [session-ses_1880.md](conversations/session-ses_1880.md)

**Decisión:** Se aceptó la implementación de crearAlumno. El único error que hubo fue a la hora de validar un alumno a la hora de guardarlo, al principio se implementó existsByDni() pero lo correcto es existsByDocenteIdAndDni(), ya que dos docentes distintos pueden tener al mismo alumno guardado en el sistema con el mismo DNI.

## [30/05/2026][15:20] Implementación de editarAlumno()

**Prompt:** empezar → me gustaria implementar el caso de uso editarAlumno

**Resultado:** Se implementó editarAlumno() generando:
- Backend: AlumnoUpdateDTO (idéntico a AlumnoCreateDTO con validaciones @NotBlank, @Pattern para DNI), AlumnoRepository.existsByDocenteIdAndDniAndIdNot() para verificar DNI único excluyendo el propio registro, AlumnoService.obtenerAlumnoPorId(), obtenerAlumnoPorIdYVerificarPertenecia() y actualizarAlumno() con verificación de pertenencia al docente autenticado, AlumnosController GET /{id} y PUT /{id}
- Frontend: alumnosService.getAlumnoById(), AlumnoFormComponent.tsx modificado para cargar datos por ID cuando está en modo edición (isEditing=true) usando useParams, con estado de carga loadingData

**Enlace:** [session-ses_1871.md](conversations/session-ses_1871.md)

**Decisión:** Se aceptó la implementación del caso de uso editarAlumno. El único error fue que al darle al botón "Guardar cambios" en el front, te redirigía a la misma página de edición y no al listado de alumnos, por lo que se modificó.

## [30/05/2026][16:27] Implementación de eliminarAlumno()

**Prompt:** empezar → me gustaria implementar el caso de uso eliminarAlumno

**Resultado:** Se implementó eliminarAlumno() generando:
- Backend: AlumnoService.eliminarAlumno(Long id, Long docenteId) con verificación de existencia y pertenencia al docente autenticado, AlumnosController DELETE /api/alumnos/{id} → 204 No Content
- Frontend: alumnosService.eliminarAlumno(id), EliminarAlumnoComponent.tsx (componente con carga de datos del alumno, modal de confirmación con advertencia, y eliminación), App.tsx con ruta /alumnos/eliminar/:id protegida con ProtectedRoute
- Punto adicional: Botón "Eliminar" en AlumnoFormComponent.tsx visible solo en modo edición (ALUMNO_ABIERTO), permitiendo eliminar desde el estado ASIGNATURA_ABIERTO

**Enlace:** [session-ses_186d.md](conversations/session-ses_186d.md)

**Decisión:** Se aceptó la implementación de eliminarAlumno

## [30/05/2026][16:48] Diseño de verGrados()

**Prompt:** empezar → Diseña el caso de uso verGrados, quiero que te bases en el diseño de verAlumnos 

**Resultado:** Se realizó el diseño MVC de verGrados() generando:
- documents/diseño/verGrados/README.md
- modelosUML/diseño/verGrados/secuencia.puml

El diseño siguió el patrón de verAlumnos(), con endpoint `GET /api/grados/mios` que filtra automáticamente por el `docenteId` extraído del JWT. Los criterios de filtrado son título y código del grado.

**Enlace:** [session-ses_186a.md](conversations/session-ses_186a.md)

**Decisión:** Se aceptó el diseño de verGrados

## [30/05/2026][16:58] Diseño de crearGrado()

**Prompt:** empezar → Diseña el caso de uso crearGrado

**Resultado:** Se realizó el diseño MVC de crearGrado() generando:
- documents/diseño/crearGrado/README.md
- modelosUML/diseño/crearGrado/secuencia.puml

El diseño siguió la filosofía C→U (Create→Update), transfiriendo automáticamente a `editarGrado()` tras la creación. Los participantes son: Frontend (GradoFormComponent), GradosController (POST /api/grados), GradoService y GradoRepository.

**Enlace:** [session-ses_1869.md](conversations/session-ses_1869.md)

**Decisión:** Se aceptó el diseño de crearGrado

## [30/05/2026][17:20] Diseño de editarGrado()

**Prompt:** empezar → diseña el caso de uso editarGrado

**Resultado:** Se realizó el diseño MVC de editarGrado() generando:
- documents/diseño/editarGrado/README.md
- modelosUML/diseño/editarGrado/secuencia.puml

El diseño siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando los participantes: Frontend (React) con GradoFormComponent, GradosController con endpoints GET/PUT /api/grados/{id}, GradoService con lógica de validación y actualización, GradoRepository y AlumnoRepository para persistencia, y Base de Datos (PostgreSQL).

Se incluyeron endpoints adicionales para gestión de alumnos por grado:
- GET /api/alumnos/sin-grado (obtener alumnos disponibles)
- PUT /api/grados/{id}/alumnos/{alumnoId} (asignar alumno al grado)
- DELETE /api/grados/{id}/alumnos/{alumnoId} (desasignar alumno del grado)

**Enlace:** [session-ses_1870.md](conversations/session-ses_1870.md)

**Decisión:** El diseño inicial se corrigió según las indicaciones del usuario: un alumno solo puede estar en un grado, la información de pertenencia se guarda en la entidad Alumno (columna grado_id), y al añadir un alumno solo se muestran aquellos que no tienen grado asignado (grado_id IS NULL).

## [30/05/2026][17:32] Diseño de eliminarGrado()

**Prompt:** empezar → Diseña el caso de uso eliminarGrado

**Resultado:** Se realizó el diseño MVC de eliminarGrado() generando:
- documents/diseño/eliminarGrado/README.md
- modelosUML/diseño/eliminarGrado/secuencia.puml

El diseño siguió el patrón de eliminación segura con confirmación previa, identificando los participantes: Frontend (React) con modal de confirmación, GradosController con endpoint DELETE /api/grados/{id}, GradoService con lógica de eliminación y validación de existencia, GradoRepository y Base de Datos (PostgreSQL). Se incluyeron verificaciones de existencia del grado y manejo de integridad referencial (409 Conflict si tiene alumnos o asignaturas asociadas).

**Enlace:** [session-ses_1868.md](conversations/session-ses_1868.md)

**Decisión:** Se aceptó el diseño de eliminarGrado

## [31/05/2026][10:47] Corrección de análisis y diseño de completarGestion

**Decisión:** Me he dado cuenta de que el caso de uso completarGestion no es tan complejo como inicialmente pensé. Es simplemente un caso de uso que redirige al menú principal, por lo que simplifiqué el análisis y diseño.

## [31/05/2026][11:19] Corrección de diseño de eliminarDocente

**Decisión:** Me he dado cuenta de que el diseño del caso de uso eliminarDocente no refleja lo que realmente quiero que haga. En el caso en el que se quiera eliminar a un docente y este tenga entidades asociadas a él (alumnos, grados, asignaturas, etc...) quiero que estas entidades asociadas también se eliminen, por lo que modifiqué su diseño.

## [31/05/2026][11:41] Corrección de diseño de eliminarGrado

**Decisión:** Me he dado cuenta de que el diseño del caso de uso eliminarGrado no refleja lo que realmente quiero que haga. En el caso en el que se quiera eliminar un grado y este tenga entidades asociadas a él (alumnos o asignaturas) al eliminarlo quiero que estas entidades asociadas dejen de tener referencia a ese grado (es decir, grado_id = null).

## [31/05/2026][12:13] Implementación de verGrados()

**Prompt:** empezar → me gustaria implementar el caso de uso verGrados

**Resultado:** Se implementó verGrados() generando:
- Backend: Grado entity (campos id, titulo, codigo, docenteId), GradoDTO, GradoRepository (findByDocenteId, findByDocenteIdAndCriterio con filtrado por titulo y codigo), GradoService (obtenerGradosDelDocente, filtrarGradosDelDocente), GradosController GET /api/grados/mios
- Frontend: gradosService.getGrados(), GradosListComponent.tsx, ruta /grados en App.tsx

**Enlace:** [session-ses_182a.md](conversations/session-ses_182a.md)

**Decisión:** Se aceptó la implementación de verGrados.

## [31/05/2026][12:39] Implementación de crearGrado()

**Prompt:** empezar → me gustaria implementar el caso de uso crearGrado

**Resultado:** Se implementó crearGrado() generando:
- Backend: GradoCreateDTO (con validaciones @NotBlank, @Size), métodos de unicidad en GradoRepository (existsByDocenteIdAndTitulo, existsByDocenteIdAndCodigo y sus variantes AndIdNot), GradoService con crearGrado(), obtenerGradoPorIdYVerificarPertenecia() y actualizarGrado(), GradosController con endpoints POST /api/grados, PUT /api/grados/{id}, GET /api/grados/{id}
- Frontend: gradosService con crearGrado(), getGradoById() y actualizarGrado(), GradoFormComponent.tsx (formulario reutilizable para crear/editar), rutas /grados/crear y /grados/editar/:id en App.tsx

El flujo sigue la filosofía C→U (Create→Update): crear grado con datos mínimos y transferir automáticamente a editarGrado() para completar datos.

**Enlace:** [session-ses_1827.md](conversations/session-ses_1827.md)

**Decisión:** La implementación del caso de uso crearGrado() se aceptó.

## [31/05/2026][13:02] Implementación de editarGrado()

**Prompt:** empezar → me gustaría implementar el caso de uso editarGrado

**Resultado:** Se implementó editarGrado() generando:
- Backend: Alumno entity - añadido campo gradoId (Long, nullable), AlumnoRepository - añadidos métodos findByGradoId() y findByDocenteIdAndGradoIsNull() (este último con @Query JPQL), GradoDTO - añadido campo alumnos (List<AlumnoDTO>), AlumnoDTO - añadido campo gradoId, GradoUpdateDTO - creado con validaciones, GradoService - añadidos métodos obtenerGradoPorId(), obtenerAlumnosSinGrado(), anadirAlumnoAGrado(), quitarAlumnoDeGrado(), actualizarGrado() y validarUnicidadTituloYCodigo(), GradosController - añadidos endpoints GET /{id}, PUT /{id}, GET /sin-grado, PUT /{id}/alumnos/{alumnoId}, DELETE /{id}/alumnos/{alumnoId}
- Frontend: gradosService - actualizados tipos GradoDTO (con alumnos) y AlumnoDTO (con gradoId), añadidos métodos getAlumnosSinGrado(), anadirAlumnoAGrado(), quitarAlumnoDeGrado(), EditarGradoComponent.tsx - creado (formulario de edición continua, lista de alumnos con botón quitar, modal para añadir alumnos), App.tsx - actualizada ruta /grados/editar/:id para usar EditarGradoComponent

**Enlace:** [session-ses_1826.md](conversations/session-ses_1826.md)

**Decisión:** La implementación del caso de uso editarGrado() se aceptó.

## [31/05/2026][13:29] Implementación de eliminarGrado()

**Prompt:** empezar → me gustaria implementar el caso de uso eliminarGrado 

**Resultado:** Se implementó el caso de uso eliminarGrado() generando:
- Backend: GradoService - método eliminarGrado(Long id), GradosController - endpoint DELETE /api/grados/{id}
- Frontend: gradosService - método eliminarGrado(id), EliminarGradoComponent.tsx, App.tsx - ruta /grados/eliminar/:id

Tras probar, se detectó un bug: al eliminar un grado, los alumnos asociados mantenían grado_id en lugar de pasar a null. Se corrigió modificando la entidad Alumno para usar @ManyToOne con @OnDelete(action = OnDeleteAction.SET_NULL) en lugar de un simple @Column Long gradoId. Se actualizaron AlumnoRepository (cambio en query JPQL de a.gradoId IS NULL a a.grado IS NULL), GradoService (métodos anadirAlumnoAGrado, quitarAlumnoDeGrado y eliminarGrado para usar la relación grado en lugar de gradoId), y AlumnoDTO (a dto.setGradoId(alumno.getGrado() != null ? alumno.getGrado().getId() : null)).

**Enlace:** [session-ses_1824.md](conversations/session-ses_1824.md)

**Decisión:** La implementación de eliminarGrado() se aceptó.

## [01/06/2026][10:05] Error de cohesión en el diseño e implementación de editarGrado

**Decisión:** Analicé el diseño e implementación del caso de uso editarGrado desde el punto de vista de la cohesión. El hecho de que GradoService se comunique directamente con AlumnoRepository me parece poco cohesivo. Me parecería mejor que GradoService se comunicase con AlumnoService y que esta se comunicase con AlumnoRepository, por lo que se modificó el diseño del caso de uso.

## [01/06/2026][10:45] Diseño de verAsignaturas()

**Prompt:** empezar → Diseña el caso de uso verAsignaturas

**Resultado:** Se realizó el diseño MVC de verAsignaturas() generando:
- documents/diseño/verAsignaturas/README.md
- modelosUML/diseño/verAsignaturas/secuencia.puml

El diseño siguió el patrón de verGrados(), con endpoint `GET /api/asignaturas/mias` que filtra automáticamente por el `docenteId` extraído del JWT. Los criterios de filtrado son título, código y curso académico.

**Enlace:** [session-ses_17da.md](conversations/session-ses_17da.md)

**Decisión:** Se aceptó el diseño de verAsignaturas

## [01/06/2026][11:03] Diseño de crearAsignatura()

**Prompt:** empezar → Diseña el caso de uso crearAsignatura

**Resultado:** Se realizó el diseño MVC de crearAsignatura() generando:
- documents/diseño/crearAsignatura/README.md
- modelosUML/diseño/crearAsignatura/secuencia.puml

El diseño siguió la filosofía C→U (Create→Update), creando asignatura con datos mínimos (título, código, curso académico) y transfiriendo automáticamente a `editarAsignatura()`. Participantes: Frontend (React), AsignaturasController, AsignaturaService, AsignaturaRepository y PostgreSQL. El docenteId se extrae del JWT para asociar la asignatura al docente autenticado.

**Enlace:** [session-ses_17db.md](conversations/session-ses_17db.md)

**Decisión:** El diseño que realizó del caso de uso crearAsignatura se aceptó.

## [01/06/2026][11:17] Diseño de editarAsignatura()

**Prompt:** empezar → Diseña el caso de uso editarAsignatura, basándote en el diseño de editarGrado. Además, ten en cuenta que solo puedes asociar un alumno a una asignatura si este alumno pertenece a alguno de los grados asociados de la asignatura.

**Resultado:** Se realizó el diseño MVC de editarAsignatura() generando:
- documents/diseño/editarAsignatura/README.md
- modelosUML/diseño/editarAsignatura/secuencia.puml

El diseño siguió el patrón "el gordo" (edición continua con múltiples ciclos), identificando los participantes: Frontend (React) con formulario de edición, AsignaturasController con endpoints GET/PUT /api/asignaturas/{id}, AsignaturaService con lógica de validación, GradoService para gestión de grados asociados, AlumnoService para matriculación (con la restricción de que un alumno solo puede matricularse si pertenece a algún grado de la asignatura), AsignaturaRepository, GradoRepository, AlumnoRepository y Base de Datos (PostgreSQL). Se definieron los endpoints para gestionar alumnos disponibles, matricular y desmatricular. Validación de unicidad de título/código y verificación de pertenencia del alumno a un grado de la asignatura.

**Enlace:** [session-ses_17d9.md](conversations/session-ses_17d9.md)

**Decisión:** El diseño que realizó del caso de uso editarAsignatura se aceptó.

## [01/06/2026][11:22] Diseño de eliminarAsignatura()

**Prompt:** empezar → Diseña el caso de uso eliminarAsignatura

**Resultado:** Se realizó el diseño MVC de eliminarAsignatura() generando:
- documents/diseño/eliminarAsignatura/README.md
- modelosUML/diseño/eliminarAsignatura/secuencia.puml

El diseño siguió el patrón de eliminación segura con confirmación previa, identificando los participantes: Frontend (React) con modal de confirmación, AsignaturasController con endpoint DELETE /api/asignaturas/{id}, AsignaturaService con lógica de eliminación, validación de existencia y pertenencia al docente autenticado extraído del JWT, AsignaturaRepository y Base de Datos (PostgreSQL). Se incluyeron verificaciones de existencia del grado y manejo de integridad referencial (409 Conflict si tiene preguntas o exámenes asociadas).

**Enlace:** [session-ses_17d8.md](conversations/session-ses_17d8.md)

**Decisión:** Se aceptó el diseño del caso de uso eliminarAsignatura.

## [01/06/2026][11:52] Implementación de verAsignaturas()

**Prompt:** empezar → me gustaria implementar el caso de uso verAsignaturas

**Resultado:** Se implementó verAsignaturas() generando:
- Backend: Asignatura entity (campos id, titulo, codigo, cursoAcademico, docenteId, grados), AsignaturaDTO, AsignaturaRepository (findByDocenteId, findByDocenteIdAndCriterio con filtrado por titulo, codigo y cursoAcademico), AsignaturaService (obtenerAsignaturasDelDocente, filtrarAsignaturas), AsignaturasController GET /api/asignaturas/mias
- Frontend: asignaturasService.getAsignaturas(), AsignaturasListComponent.tsx, ruta /asignaturas en App.tsx

El diseño siguió el patrón de verGrados() y verAlumnos(), filtrando automáticamente por el `docenteId` extraído del JWT.

**Enlace:** [session-ses_17d7.md](conversations/session-ses_17d7.md)

**Decisión:** Se aceptó la implementación de verAsignaturas.

## [01/06/2026][12:07] Implementación de crearAsignatura()

**Prompt:** empezar → me gustaria implementar el caso de uso crearAsignatura 

**Resultado:** Se implementó crearAsignatura() generando:
- Backend: AsignaturaCreateDTO.java (con validaciones @NotBlank, @Size), AsignaturaService - métodos crearAsignatura() y actualizarAsignatura(), AsignaturasController - endpoints POST /api/asignaturas y PUT /api/asignaturas/{id}
- Frontend: asignaturasService - métodos crearAsignatura() y actualizarAsignatura(), AsignaturaFormComponent.tsx (formulario reutilizable para crear/editar), rutas /asignaturas/crear y /asignaturas/editar/:id en App.tsx

**Enlace:** [session-ses_17d6.md](conversations/session-ses_17d6.md)

**Decisión:** Se aceptó la implementación de crearAsignatura().

## [01/06/2026][13:28] Implementación de editarAsignatura()

**Prompt:** empezar → me gustaria implementar el caso de uso editarAsignatura

**Resultado:** Se implementó editarAsignatura() generando:
- Backend: GradoDTO.java y AlumnoDTO.java (nuevos), AsignaturaDTO.java actualizado con campos grados y alumnos, AsignaturaUpdateDTO.java (nuevo), Asignatura.java y Alumno.java con relación many-to-many para matriculación, AsignaturaRepository con métodos existsByIdAndDocenteId y existsByCodigoAndDocenteIdAndIdNot, AlumnoRepository con findAvailableAlumnosForAsignatura, AsignaturaService reescrito con métodos: obtenerAsignaturaPorId, actualizarAsignatura, getAlumnosDisponibles, addAlumno, removeAlumno, AsignaturasController con PUT /api/asignaturas/{id}, GET /api/asignaturas/{id}/alumnos-disponibles, POST /api/asignaturas/{id}/alumnos/{alumnoId}, DELETE /api/asignaturas/{id}/alumnos/{alumnoId}
- Frontend: asignaturasService.ts ampliado con interfaces DTO y métodos para gestión de alumnos, AsignaturaFormComponent.tsx reescrito para soportar modo edición completo con gestión de grados y matriculación de alumnos (modal para matricular)

**Enlace:** [session-ses_17d5.md](conversations/session-ses_17d5.md)

**Decisión:** La implementación de editarAsignatura() se aceptó.

## [01/06/2026][13:50] Implementación de eliminarAsignatura()

**Prompt:** empezar → me gustaría implementar el caso de uso eliminarAsignatura → el usuario modificó el diseño indicando que si la asignatura tiene relaciones (alumnos y grados asociados), estas entidades relacionadas perderán su relación con la asignatura pero no se eliminarán → quiero implementar también la eliminación desde la página de edición de asignatura

**Resultado:** Se implementó el caso de uso eliminarAsignatura() generando:
- Backend: AsignaturaService.java - método `eliminarAsignatura(id, docenteId)` que limpia relaciones con alumnos y grados antes de eliminar (getAlumnos().clear(), getGrados().clear()), AsignaturasController.java - endpoint DELETE /api/asignaturas/{id} → 204 No Content
- Frontend: asignaturasService.ts - método `eliminarAsignatura(id)`, EliminarAsignaturaComponent.tsx - componente con modal de confirmación mostrando título, código y curso académico, App.tsx - ruta /asignaturas/eliminar/:id
- Diseño: secuencia.puml actualizado para reflejar la limpieza de relaciones antes de la eliminación
- AsignaturaFormComponent.tsx - añadido botón "Eliminar" que redirige a /asignaturas/eliminar/:id

**Enlace:** [session-ses_17d0.md](conversations/session-ses_17d0.md)

**Decisión:** El diseño inicial no contemplaba el comportamiento de las relaciones al eliminar. S specificó que al eliminar una asignatura con alumnos y grados asociados, estas entidades relacionadas deben perder su relación con la asignatura pero no eliminarse. Se implementó limpiando las listas de alumnos y grados antes de llamar a delete(). Además se implementó el camino de eliminación desde la página de edición de asignatura.

## [01/06/2026][20:49] Refactorización de editarAsignatura() para mejorar cohesión entre servicios

Analicé el diseño e implementación del caso de uso editarAsignatura desde el punto de vista de la cohesión. El hecho de que AsignaturaService se comunique directamente con los repositorios de otras entidades (Grado y Alumno) me parece poco cohesivo. Me parecería mejor que los servicios se comunicasen entre ellos, por lo que se modificó el diseño del caso de uso.

## [02/06/2026][10:05] Diseño de verPreguntas()

**Prompt:** empezar → Diseña el caso de uso verPreguntas

**Resultado:** Se realizó el diseño MVC de verPreguntas() generando:
- documents/diseño/verPreguntas/README.md
- modelosUML/diseño/verPreguntas/secuencia.puml

El diseño siguió el patrón de verAlumnos() y verDocentes(), con endpoint `GET /api/preguntas/mias` que filtra automáticamente por el `docenteId` extraído del JWT. Los criterios de filtrado son enunciado, tema y dificultad.

**Enlace:** [session-ses_178a.md](conversations/session-ses_178a.md)

**Decisión:** El diseño del caso de uso verPreguntas se aceptó. Sin embargo, este caso de uso se puede ejecutar también desde el estado ASIGNATURA_ABIERTO, por lo que hay que crear un camino para mostrar todas las preguntas de una asignatura en concreto, por lo que se implementó.

## [02/06/2026][10:18] Diseño de crearPregunta()

**Prompt:** empezar → diseña el caso de uso crearPregunta

**Resultado:** Se realizó el diseño MVC de crearPregunta() generando:
- documents/diseño/crearPregunta/README.md
- modelosUML/diseño/crearPregunta/secuencia.puml

El diseño siguió la filosofía C→U (Create→Update), identificando los participantes: Frontend (React) con PreguntaFormComponent, PreguntasController con endpoint POST /api/preguntas, PreguntaService con lógica de creación y verificación de unicidad de enunciado, PreguntaRepository y AsignaturaRepository para persistencia. La pregunta se crea con datos mínimos (asignatura, enunciado, tema, dificultad) y se transfiere automáticamente a editarPregunta() tras la creación. Se verifica unicidad del enunciado dentro de la misma asignatura y la asociación de la pregunta al docente extraído del JWT.

**Enlace:** [session-ses_1789.md](conversations/session-ses_1789.md)

**Decisión:** El diseño de crearPregunta tenóia un error de cohesión, ya que la clase PreguntaService colaboraba directamente con AsignaturaRepository, lo cual a mi parecer no es correcto. Se corrigió el diseño haciendo que PreguntaService colaborase con AsignaturaService y AsignaturaService con AsignaturaRepository

## [02/06/2026][11:35] Diseño de casos de uso editarPregunta()

**Prompt:** empezar → Quiero que se diseñe el casos de uso editarPregunta
**Resultado:** Se realizó el diseño MVC de editarPregunta() generando:
- documents/diseño/editarPregunta/README.md
- modelosUML/diseño/editarPregunta/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de editarPregunta

## [02/06/2026][11:50] Diseño de caso de uso eliminarPregunta()

**Prompt:** empezar → Quiero que se diseñe el casos de uso eliminarPregunta
**Resultado:** Se realizó el diseño MVC de eliminarPregunta() generando:
- documents/diseño/eliminarPregunta/README.md
- modelosUML/diseño/eliminarPregunta/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de eliminarPregunta

## [02/06/2026][11:58] Diseño de caso de uso verRespuestas()

**Prompt:** empezar → Quiero que se diseñe el caso de uso verRespuestas
**Resultado:** Se realizó el diseño MVC de verRespuestas() generando:
- documents/diseño/verRespuestas/README.md
- modelosUML/diseño/verRespuestas/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de verRespuestas

## [02/06/2026][12:08] Diseño de caso de uso crearRespuesta()

**Prompt:** empezar → Quiero que se diseñe el caso de uso crearRespuesta
**Resultado:** Se realizó el diseño MVC de crearRespuesta() generando:
- documents/diseño/crearRespuesta/README.md
- modelosUML/diseño/crearRespuesta/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de crearRespuesta

## [02/06/2026][12:17] Diseño de caso de uso editarRespuesta()

**Prompt:** empezar → Quiero que se diseñe el caso de uso editarRespuesta
**Resultado:** Se realizó el diseño MVC de editarRespuesta() generando:
- documents/diseño/editarRespuesta/README.md
- modelosUML/diseño/editarRespuesta/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de editarRespuesta

## [02/06/2026][12:24] Diseño de caso de uso eliminarRespuesta()

**Prompt:** empezar → Quiero que se diseñe el caso de uso eliminarRespuesta
**Resultado:** Se realizó el diseño MVC de eliminarRespuesta() generando:
- documents/diseño/eliminarRespuesta/README.md
- modelosUML/diseño/eliminarRespuesta/secuencia.puml

**Enlace:** [session-ses_1788.md](conversations/session-ses_1788.md)

**Decisión:** Se aceptó el diseño de eliminarRespuesta
