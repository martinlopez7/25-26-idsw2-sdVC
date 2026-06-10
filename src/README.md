# Implementación

## Estructura General

El proyecto sigue una arquitectura **monorepo** con dos módulos principales:

```
src/
├── backend/                     # Spring Boot (Java)
│   └── exam-generator/
└── frontend/                    # React + TypeScript
    └── src/
```

## Backend - Spring Boot

**Paquete base:** `com.martin.exam_generator`

[Enlace](/src/backend/exam-generator/)

### Estructura de directorios

```
exam-generator/src/main/java/com/martin/exam_generator/
├── controller/                  # REST Controllers
├── service/                     # Lógica de negocio
├── repository/                  # Acceso a datos (Spring Data JPA)
├── entities/                    # Entidades JPA
├── dto/                         # Data Transfer Objects
├── security/                    # Configuración Spring Security + JWT
├── exception/                  # Manejo de excepciones
└── config/                      # Configuración general
```

### Entidades JPA

| Entidad | Descripción |
|---------|-------------|
| `Usuario` | Usuarios del sistema (docentes y administradores) |
| `Alumno` | Alumnos registrados |
| `Grado` | Grados académicos |
| `Asignatura` | Asignaturas con batería de preguntas |
| `Pregunta` | Preguntas de examen |
| `Respuesta` | Opciones de respuesta a preguntas |

### Controladores

| Controlador | Endpoints principales |
|-------------|------------------------|
| `AuthController` | `POST /api/auth/login`, `POST /api/auth/logout` |
| `DocentesController` | `GET/POST /api/docentes`, `GET/PUT/DELETE /api/docentes/{id}` |
| `AlumnosController` | `GET/POST /api/alumnos`, `GET/PUT/DELETE /api/alumnos/{id}` |
| `GradosController` | `GET/POST /api/grados`, `GET/PUT/DELETE /api/grados/{id}` |
| `AsignaturasController` | `GET/POST /api/asignaturas`, `GET/PUT/DELETE /api/asignaturas/{id}` |
| `PreguntasController` | `GET/POST /api/preguntas`, `GET/PUT/DELETE /api/preguntas/{id}` |
| `RespuestasController` | `GET/POST /api/respuestas`, `GET/PUT/DELETE /api/respuestas/{id}` |
| `ExamenesController` | `POST /api/examenes/generar`, `POST /api/examenes/asignar`, `POST /api/examenes/corregir` |
| `ConfiguracionController` | `GET /api/configuracion/exportar`, `POST /api/configuracion/importar` |

### Seguridad

- **JWT** para autenticación stateless
- **BCrypt** para hashing de contraseñas
- Filtro `JwtAuthenticationFilter` para validar tokens en cada petición
- Blacklist de tokens para invalidación en cierre de sesión

---

## Frontend - React + TypeScript

[Enlace](/src/frontend/)

### Estructura de directorios

```
frontend/src/
├── components/                  # Componentes React
│   ├── auth/                   # Login, Logout
│   ├── menu/                   # Menús diferenciados por actor
│   ├── docentes/               # Gestión de docentes
│   ├── alumnos/                # Gestión de alumnos
│   ├── grados/                 # Gestión de grados
│   ├── asignaturas/            # Gestión de asignaturas
│   ├── preguntas/             # Gestión de preguntas
│   ├── respuestas/             # Gestión de respuestas
│   ├── examenes/               # Generación, asignación, corrección
│   └── configuracion/          # Importación/Exportación
├── services/                   # Llamadas API REST
├── context/                    # React Context (AuthContext)
├── types/                      # Tipos TypeScript
└── App.tsx                     # Router principal
```

### Servicios API

| Servicio | Métodos |
|----------|---------|
| `authService` | `login()`, `logout()` |
| `docentesService` | `getDocentes()`, `getDocente()`, `createDocente()`, `updateDocente()`, `deleteDocente()` |
| `alumnosService` | `getAlumnos()`, `getAlumno()`, `createAlumno()`, `updateAlumno()`, `deleteAlumno()` |
| `gradosService` | `getGrados()`, `getGrado()`, `createGrado()`, `updateGrado()`, `deleteGrado()` |
| `asignaturasService` | `getAsignaturas()`, `getAsignatura()`, `createAsignatura()`, `updateAsignatura()`, `deleteAsignatura()` |
| `preguntasService` | `getPreguntas()`, `getPregunta()`, `createPregunta()`, `updatePregunta()`, `deletePregunta()` |
| `respuestasService` | `getRespuestas()`, `getRespuesta()`, `createRespuesta()`, `updateRespuesta()`, `deleteRespuesta()` |
| `examenesService` | `generarExamenes()`, `asignarExamenes()`, `corregirExamenes()` |
| `configuracionService` | `exportarConfiguracion()`, `importarConfiguracion()` |

### Autenticación y Autorización

- **AuthContext**: Gestiona estado de autenticación (token JWT, tipoActor)
- **Menús diferenciados**: `AdminMenu.tsx` (docentes) y `DocenteMenu.tsx` (docente)
- Rutas protegidas según tipo de actor
- Token almacenado en LocalStorage

---

## Base de Datos - PostgreSQL

El sistema utiliza **Spring Data JPA + Hibernate** como ORM.

---

[Volver](/README.md)