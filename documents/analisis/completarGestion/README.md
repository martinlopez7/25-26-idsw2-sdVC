# completarGestion > Análisis

> |[🏠️](/README.md)|[Detalle](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/completarGestion/completarGestion.svg)|**Análisis**|[Diseño](/documents/diseño/completarGestion/README.md)|
> |-|-|-|-|

## información del artefacto

- **Proyecto**: Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboración
- **Disciplina**: Análisis
- **Versión**: 1.0
- **Fecha**: 2026-05-23
- **Autor**: Equipo de desarrollo

## propósito

Análisis del caso de uso `completarGestion()` mediante diagrama de colaboración MVC. Este caso de uso funciona como **navegación directa al menú principal** del actor autenticado.

## naturaleza del caso de uso

`completarGestion()` es un caso de uso de **navegación pura** que simplemente redirige al menú principal del actor:

- **Desde cualquier estado del sistema**, el usuario puede invocar `completarGestion()`
- **El resultado es siempre el mismo**: mostrar el menú principal correspondiente al actor autenticado
- **Sin lógica de negocio**: No hay procesamiento, validación ni operaciones sobre entidades
- **Sin colaboradores complejos**: Solo una vista que muestra el menú según el tipo de actor

## diagrama de colaboración

<div align=center>

|![Análisis: completarGestion()](/images/analisis/completarGestion/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/completarGestion/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases view (azul #629EF9)

| Clase | Responsabilidad | Derivación |
|-|-|-|
| **CompletarGestionView** | Muestra el menú principal del actor autenticado | Wireframe del menú |

### clases controller (verde #b5bd68)

| Clase | Responsabilidad | Caso de uso |
|-|-|-|
| **CompletarGestionController** | Control de navegación al menú | completarGestion() |

## flujo de colaboración

1. **Inicio**: Usuario invoca `completarGestion()` desde cualquier estado
2. **Delegación**: `CompletarGestionView` → `CompletarGestionController.solicitarMenu()`
3. **Determinación**: El sistema determina el tipo de actor autenticado
4. **Navegación**: Se muestra el menú correspondiente al actor (AdminMenu o DocenteMenu)

## opciones de navegación disponibles

#### Para Actor Docente

- **verGrados()** → Menú de grados
- **verAsignaturas()** → Menú de asignaturas
- **verAlumnos()** → Menú de alumnos
- **verPreguntas()** → Menú de preguntas
- **generarExamenes()** → Generación de exámenes
- **corregirExamenes()** → Corrección de exámenes
- **importarConfiguracionGlobal()** → Importar configuración
- **exportarConfiguracionGlobal()** → Exportar configuración
- **cerrarSesion()** → Cerrar sesión

#### Para Actor Administrador Institucional

- **verDocentes()** → Menú de docentes
- **cerrarSesion()** → Cerrar sesión

## características del análisis

### navegación pura
- No hay entidades involucradas
- No hay lógica de negocio
- Solo determinación del tipo de actor para mostrar el menú correcto

### multi-actor
A diferencia de otros casos de uso, `completarGestion()` es compartido por ambos actores del sistema:
- **Docente**: Acceso a funcionalidades de gestión académica y exámenes
- **Administrador Institucional**: Acceso a gestión de docentes

### sin complejidad
- Sin clases model (entidad)
- Sin repositorios
- Sin colaboradores complejos
- Solo View + Controller para navegación