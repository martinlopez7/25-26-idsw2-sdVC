# Agente - Normas de Comportamiento

## Protocolo de Inicialización

Antes de iniciar cualquier conversación con el usuario, cuando escriba la palabra "empezamos", el agente DEBE leer y analizar exhaustivamente los siguientes archivos de contexto del proyecto para interiorizar la naturaleza del mismo:

1. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidad.puml` - Diagrama de entidades del modelo de dominio
2. `contexto/modelo-de-dominio/diagramas/diagramaEntidad/diagramaEntidadConsideraciones.md` - Consideraciones y relaciones del modelo de dominio
3. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-docente.puml` - Diagrama de actores y casos de uso para el actor docente
4. `contexto/disciplina-requisitos/encontrarActoresYCasosDeUso/actoresYCasosDeUso-administradorInstitucional.puml` - Diagrama de actores y casos de uso para el actor administrador institucional
5. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoDocente/diagramaContexto.puml` - Diagrama de contexto para el actor docente
6. `contexto/disciplina-requisitos/diagramasDeContexto/diagramaDeContextoAdministradorInstitucional/diagramaContexto.puml` - Diagrama de contexto para el actor administrador institucional
7. `conversation-log.md` - Fichero en el que se registran las sesiones de conversacion con un agente de IA. La última conversación registrada será la que marque el estado actual del proyecto. No tienes que leer los archivos de las conversaciones completas de los enlaces, solo el resumen.

## Dominio del Proyecto

Este es un sistema de generación de exámenes universitarios que involucra:

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
> **Enlace:** tienes que añadir un link de tipo markdown a la carpeta `conversations/`, la conversación ya la exporto yo y la muevo a la carpeta conversatiosn manualmente y completo el link al archivo
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
Cuando se te asigne analizar un caso de uso concreto, tu objetivo es consultar su detallado y tenerlo localizado en el diagrama de contexto. Deberás de realizar el análisis técnico aplicando estrictamente la teoría y la metodología definidas en el proyecto:

### Fuentes de Referencia y Contexto
* **Teoría Aplicable:** Lee y aplica estrictamente los conceptos y metodologías del archivo `contexto/ANALISIS-TEORIA.md`.
* **Ejemplos de Referencia:** Para comprender el nivel de detalle, la estructura y el estilo técnico esperado, toma como modelo la carpeta `contexto/ejemplos-analisis/`, en donde encontrarás casos de uso ya analizados. 
  * *Nota importante:* Estos archivos pertenecen a un **proyecto de ejemplo externo**, pero sirven como plantilla.
  * **Casos de Uso de Ejemplo:** Revisa los análisis de casos de uso resueltos en esa carpeta, ya que algunos son muy similares a los que deberás de analizar.
  * **Diagrama de Contexto de Ejemplo:** Analiza también el archivo `contexto/ejemplos-analisis/diagrama-contexto-administrador.puml` para entender el contexto de ese mismo proyecto de referencia para ver como se relaciona con los casos de uso ya analizados de ejemplo.

### Artefactos a Entregar y Ubicación
Debes generar y guardar los siguientes artefactos en las rutas exactas indicadas:

1.  **`README.md`**
    * **Ubicación:** `documents/analisis/{nombreDelCasoDeUso}/README.md`
    * **Formato:** Sigue estrictamente la estructura de los ejemplos proporcionados salvo lo siguiente: 
    1. En la tabla del principio solo quiero que estén los apartados de la casa (🏠️), detalle [aquí quiero que añadas un link que lleve al detallado de un repo externo del formato https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/{nombreCasoDeUso}/{nombreCasoDeUso}.svg], analisis y diseño
    2. En la tabla que contiene el diagrama de colaboración quiero que la celda que contenga la imagen del diagrama sea del formato "/images/analisis/{nombreCasoDeUso}/colaboracion.svg". El archivo svg me encargo de añadirlo yo manualmente. Y la celda del link que te redirige al código de ese diagrama quiero que sea del formato "/modelosUML/analisis/{nombreCasoDeUso}/colaboracion.puml" 
    3. No quiero que implementes la última sección de referencias
2.  **`colaboracion.puml`** (Diagrama de colaboración UML en PlantUML)
    * **Ubicación:** `modelosUML/analisis/{nombreDelCasoDeUso}/colaboracion.puml`
3.  **`secuencia.puml`** (Diagrama de secuencia UML en PlantUML)
    * **Ubicación:** `modelosUML/analisis/{nombreDelCasoDeUso}/secuencia.puml`
    * **Nota:** Este artefacto solo se generará **cuando sea necesario** o se requiera explícitamente para aclarar interacciones complejas. En el caso en el que se genere quiero que agregues al README.md una tabla como el apartado de "diagrama de colaboración" justo encima del apartado de "flujo de colaboración".

### Reglas Críticas
* No inventes estructuras nuevas; la consistencia con los ejemplos es obligatoria.
* Asegúrate de crear los directorios correspondientes si aún no existen antes de escribir los archivos.

## Diseño de Casos de Uso

### Rol y Objetivo
Cuando se te asigne diseñar un caso de uso concreto, tu objetivo es consultar su detallado, su análisis y tenerlo localizado en el diagrama de contexto. Deberás de realizar el diseño técnico aplicando estrictamente la metodología definidas en el proyecto:

### Fuentes de Referencia y Contexto
* **Stack tecnológico:** El stack tecnológico que se va a utilizar está explicado en `documents/diseño/README.md`
* **Diagrama entidad-relación del sistema** El diagrama entidad-relación del sistema que se va a utilizar está explicado en `modelosUML/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.puml` (solo están las primaryKeys y foreignKeys)
* **Ejemplos de Referencia:** Para comprender el nivel de detalle, la estructura y el estilo técnico esperado, toma como modelo la carpeta `contexto/ejemplos-diseño/casos-uso/`, en donde encontrarás algunos casos de uso ya diseñados. 
  * *Nota importante:* Estos archivos pertenecen a un **proyecto de ejemplo externo**, pero sirven como plantilla.
  * **Casos de Uso de Ejemplo:** Revisa los ejemplos de diseños de casos de uso resueltos en esa carpeta y los ya diseñados previamente (se encuentran en `modelosUML/diseño`), ya que algunos son muy similares a los que deberás de analizar.
  * **Diagrama de Contexto de Ejemplo:** Analiza también el archivo `contexto/ejemplos-analisis/diagrama-contexto-administrador.puml` para entender el contexto de ese mismo proyecto de referencia para ver como se relaciona con los casos de uso ya diseñados de ejemplo.

  ### Artefactos a Entregar y Ubicación
Debes generar y guardar los siguientes artefactos en las rutas exactas indicadas:

1.  **`README.md`**
    * **Ubicación:** `documents/diseño/{nombreDelCasoDeUso}/README.md`
    * **Formato:** Sigue estrictamente la estructura de los ejemplos proporcionados salvo lo siguiente: 
    1. En la tabla del principio solo quiero que estén los apartados de la casa (🏠️), detalle [aquí quiero que añadas un link que lleve al detallado de un repo externo del formato https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/{nombreCasoDeUso}/{nombreCasoDeUso}.svg], analisis [aquí quiero que añadas un link que lleve al README del analisis de este mismo repositorio] y diseño
    2. En la parte que contiene la imagen del diagrama de secuancia quiero que el link sea del formato "/images/diseño/{nombreCasoDeUso}/secuencia.svg". El archivo svg me encargo de añadirlo yo manualmente. Y el link que te redirige al código de ese diagrama quiero que sea del formato "/modelosUML/diseño/{nombreCasoDeUso}/secuencia.puml" 
2.  **`secuencia.puml`** (Diagrama de secuencia UML en PlantUML)
    * **Ubicación:** `modelosUML/diseño/{nombreDelCasoDeUso}/secuencia.puml`

### Reglas Críticas
* No inventes estructuras nuevas; la consistencia con los ejemplos es obligatoria.
* Asegúrate de crear los directorios correspondientes si aún no existen antes de escribir los archivos.
* Los diseños deben de ser coherentes entre sí ya que todo forma parte de un mismo sistema.

## Implementación de Casos de Uso

### Rol y Objetivo
Cuando se te asigne implementar un caso de uso concreto, tu objetivo es consultar su diseño técnico y traducirlo a código funcional siguiendo el stack tecnológico del proyecto y las metodologías definidas.

### Stack Tecnológico
El stack tecnológico que se va a utilizar está explicado en `documents/diseño/README.md`:
* **Backend:** Java + Spring Boot 4.x (API RESTful, Spring Security, Spring Data JPA)
* **Frontend:** React 18+ + TypeScript (componentes y hooks)
* **Base de Datos:** PostgreSQL con ORM (Spring Data JPA + Hibernate)

El diagrama entidad-relación del sistema que se va a utilizar está explicado en `modelosUML/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.puml` (solo están las primaryKeys y foreignKeys)

### Estructura del Proyecto
El proyecto se estructura en dos módulos principales:

```
src/
├── backend/                    # Spring Boot
│   └── exam-generator/src/main/java/com/martin/exam_generator/
│       ├── controller/         # REST Controllers
│       ├── service/            # Lógica de negocio
│       ├── repository/         # Acceso a datos (JPA)
│       ├── entities/           # Entidades JPA
│       ├── dto/                # Data Transfer Objects
│       ├── security/           # Configuración Spring Security
│       └── exception/          # Manejo de excepciones
└── frontend/                   # React + TypeScript
    └── src/
        ├── components/         # Componentes React (por crear)
        ├── hooks/              # Custom hooks (por crear)
        ├── services/           # Llamadas API REST (por crear)
        ├── types/              # Tipos TypeScript (por crear)
        ├── context/            # React Context (por crear)
        ├── App.tsx             # Componente principal
        └── index.tsx           # Punto de entrada
```

**Paquete base:** `com.martin.exam_generator`

### Artefactos a Entregar y Ubicación
Debes generar y guardar los siguientes artefactos en las rutas exactas indicadas:

1. **Backend - Controller**
   * **Ubicación:** `src/backend/exam-generator/src/main/java/com/martin/exam_generator/controller/{NombreEntidad}Controller.java`
   * **Formato:** Anotaciones Spring (@RestController, @RequestMapping, @GetMapping, @PostMapping, etc.)
   * **Responsabilidad:** Recibir peticiones HTTP, delegar a servicio, retornar respuestas JSON

2. **Backend - Service**
   * **Ubicación:** `src/backend/exam-generator/src/main/java/com/martin/exam_generator/service/{NombreEntidad}Service.java`
   * **Formato:** Anotaciones Spring (@Service, @Transactional)
   * **Responsabilidad:** Contener la lógica de negocio, validar reglas de negocio, coordinar repositorios

3. **Backend - Repository**
   * **Ubicación:** `src/backend/exam-generator/src/main/java/com/martin/exam_generator/repository/{NombreEntidad}Repository.java`
   * **Formato:** Extender JpaRepository (Spring Data JPA)
   * **Responsabilidad:** Operaciones CRUD básicas y consultas personalizadas con JPQL

4. **Backend - Entidad JPA**
   * **Ubicación:** `src/backend/exam-generator/src/main/java/com/martin/exam_generator/entities/{NombreEntidad}.java`
   * **Formato:** Anotaciones JPA (@Entity, @Table, @Column, @ManyToOne, @OneToMany, etc.)
   * **Responsabilidad:** Mapear la entidad a la tabla correspondiente en PostgreSQL

5. **Backend - DTOs**
   * **Ubicación:** `src/backend/exam-generator/src/main/java/com/martin/exam_generator/dto/`
   * **Archivos:** `{NombreEntidad}Request.java`, `{NombreEntidad}Response.java`
   * **Formato:** POJOs con lombok (@Data, @NoArgsConstructor, @AllArgsConstructor)
   * **Responsabilidad:** Transferir datos entre capas, validar peticiones entrantes

6. **Frontend - Componente**
   * **Ubicación:** `frontend/src/components/{modulo}/{NombreEntidad}*.tsx`
   * **Formato:** Componente React funcional con hooks
   * **Responsabilidad:** Renderizar interfaz de usuario, manejar estado local, llamar a servicios

7. **Frontend - Servicio API**
   * **Ubicación:** `frontend/src/services/{nombreEntidad}Service.ts`
   * **Formato:** Funciones que consumen la API REST (fetch o axios)
   * **Responsabilidad:** Abstraer llamadas HTTP, retornar Promises al componente

### Orden de Implementación Recomendado
Para cada caso de uso, sigue este orden de implementación:

1. **Backend - Entidad JPA** (modelo de dominio → tabla PostgreSQL)
2. **Backend - Repository** (operaciones de acceso a datos)
3. **Backend - DTOs** (estructura de datos para transfer)
4. **Backend - Service** (lógica de negocio del caso de uso)
5. **Backend - Controller** (endpoints REST)
6. **Frontend - Servicio API** (consumo del endpoint)
7. **Frontend - Componente** (interfaz de usuario)

### Reglas Críticas
* Antes de implementar, consulta el diseño en `documents/diseño/{nombreDelCasoDeUso}/`, el análisis en `documents/analisis/{nombreDelCasoDeUso}/` y el detallado en `contexto/disciplina-requisitos/detalladoCasosDeUso/{nombreDelCasoDeUso}`
* Mantén consistencia con los casos de uso ya implementados del mismo módulo
* El frontend debe seguir el flujo de navegación definido en los diagramas de contexto (`contexto/disciplina-requisitos/diagramasDeContexto/`)
* Utiliza los mismos patrones de nomenclatura y estructura ya establecidos en el proyecto