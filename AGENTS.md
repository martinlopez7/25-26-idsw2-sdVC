# Agente - Normas de Comportamiento

## Protocolo de Inicialización

Antes de iniciar cualquier conversación con el usuario, cuando escriba la palabra "empezamos", el agente DEBE leer y analizar exhaustivamente los siguientes archivos de contexto del proyecto para interiorizar la naturaleza del mismo:

1. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidad.puml` - Diagrama de entidades del modelo de dominio
2. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidadConsideraciones.md` - Consideraciones y relaciones del modelo de dominio
3. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-docente.puml` - Diagrama de actores y casos de uso para el rol docente
4. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-administradorInstitucional.puml` - Diagrama de actores y casos de uso para el rol administrador institucional
5. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoDocente/diagramaContexto.puml` - Diagrama de contexto para el docente
6. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoAdministradorInstitucional/diagramaContexto.puml` - Diagrama de contexto para el administrador institucional
7. `conversation-log.md` - Fichero en el que se registran las sesiones de conversacion con un agente de IA. La última conversación registrada será la que marque el estado actual del proyecto. No tienes que leer los archivos de las conversaciones completas de los enlaces, solo el resumen.

## Dominio del Proyecto

Este es un sistema de gestión de exámenes universitarios que involucra:

- **Entidades principales**: Examen, Asignatura, Pregunta, BateríaDePreguntas, Grado, Profesor, Alumno, Respuesta
- **Actores**: Docente, Administrador Institucional
- **Funcionalidades**: Gestión de grados, asignaturas, alumnos, preguntas, respuestas, generación y corrección de exámenes

El agente debe comprender la estructura del dominio, las relaciones entre entidades, los diagramas de contexto de cada actor y las capacidades del sistema antes de proporcionar asistencia.

## Protocolo de Cierre

Cuando escriba la palabra "adios" quiero que el agente poble el archivo `conversation-log.md` con un registro de lo conversado durante la sesión, siguiendo el formato:

> ```
> ## [Dia/Mes/Año][Hora:Minuto] Título breve de lo que se pidió
>
> **Prompt:** lo que le dijo al AI (textual o resumido fielmente)
>
> **Resultado:** lo que produjo
>
> **Enlace:** tienes que ejecutar el comando "/export" para exportar la conversacion y mover el archivo a la carpeta `conversations/` y añadir un link de tipo markdown a ese archivo en esta seccion
>
> **Decisión:** no escribas nada aquí, dejalo en blanco, esto me encargo de escribirlo manualmente
> ```

## Consulta de Casos de Uso 

### Objetivo
Cuando necesites comprender la interacción exacta entre un Actor y el Sistema para un caso de uso específico, no debes asumir su comportamiento. Es obligatorio que consultes la documentación de requisitos existente.

### Origen de la Información
Analiza en detalle los archivos ubicados en:
* **Ruta Principal:** `contexto/disciplina-requisitos/detalladoCasosDeUso/`
* **Subcarpetas:** Busca la subcarpeta correspondiente al módulo o caso de uso específico que estés tratando.

### Instrucciones para el Agente
1. Antes de diseñar diagramas o redactar análisis, lee el flujo principal, los flujos alternativos y las pre/postcondiciones documentadas en dicha ruta.
2. Asegúrate de que cualquier artefacto que generes sea 100% fiel a la interacción descrita en estos documentos de requisitos.

## Análisis de Casos de Uso

### Rol y Objetivo
Cuando se te asigne analizar un caso de uso concreto, tu objetivo es consultar su detallado y localizarlo en el diagrama de contexto. Deberás de realizar el análisis técnico aplicando estrictamente la teoría y la metodología definidas en el proyecto.

### Fuentes de Referencia y Contexto
* **Teoría Aplicable:** Lee y aplica estrictamente los conceptos y metodologías del archivo `contexto/ANALISIS-TEORIA.md`.
* **Ejemplos de Referencia:** Para comprender el nivel de detalle, la estructura y el estilo técnico esperado, toma como modelo la carpeta `contexto/ejemplos-analisis/`. 
  * *Nota importante:* Estos archivos pertenecen a un **proyecto de ejemplo externo**, pero sirven como plantilla.
  * **Casos de Uso de Ejemplo:** Revisa los análisis de casos de uso ya resueltos en esa carpeta.
  * **Diagrama de Contexto de Ejemplo:** Analiza el archivo `contexto/ejemplos-analisis/diagrama-contexto-administrador.puml` para entender el contexto de ese mismo proyecto de referencia para una mejor toma de decisiones.

### Artefactos a Entregar y Ubicación
Debes generar y guardar los siguientes artefactos en las rutas exactas indicadas:

1.  **`README.md`**
    * **Ubicación:** `documents/analisis/{nombreDelCasoDeUso}/README.md`
    * **Formato:** Sigue estrictamente la estructura de los ejemplos proporcionados salvo lo siguiente: 
    1. En la tabla del principio solo quiero que estén los apartados de la casa, detalle [aquí quiero que añadas un link que lleve al detallado de un repo externo del formato https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/{nombreCasoDeUso}/{nombreCasoDeUso}.svg], analisis y diseño
    2. En la tabla que contiene el diagrama de colaboración quiero que la celda que contenga la imagen del diagrama sea del formato "/images/analisis/{nombreCasoDeUso}/colaboracion.svg". El archivo svg me encargo de añadirlo yo, no te preocupes por eso. Y la celda del link que te redirige al código de ese diagrama quiero que sea del formato "/modelosUML/analisis/{nombreCasoDeUso}/colaboracion.puml" 
    3. No quiero que implementes la última sección de referencias
2.  **`colaboracion.puml`** (Diagrama de colaboración UML en PlantUML)
    * **Ubicación:** `modelosUML/analisis/{nombreDelCasoDeUso}/colaboracion.puml`
3.  **`secuencia.puml`** (Diagrama de secuencia UML en PlantUML)
    * **Ubicación:** `modelosUML/analisis/{nombreDelCasoDeUso}/secuencia.puml`
    * **Nota:** Este artefacto solo se generará **cuando sea necesario** o se requiera explícitamente para aclarar interacciones complejas. En el caso en el que se genere quiero que agregues al README.md una tabla como el apartado de "diagrama de colaboración" justo encima del apartado de "flujo de colaboración".

### Reglas Críticas
* No inventes estructuras nuevas; la consistencia con los ejemplos es obligatoria.
* Asegúrate de crear los directorios correspondientes si aún no existen antes de escribir los archivos.