# Fase de diseño

## Propósito
Esta fase tiene como objetivo definir la arquitectura del sistema, la selección tecnológica y el diseño detallado de los componentes para guiar la implementación.

## Stack tecnológico seleccionado

Para la construcción del sistema de generación de exámenes universitarios, se ha seleccionado una arquitectura de **Single Page Application (SPA)** con API REST, priorizando la separación de responsabilidades y el valor didáctico.

### Backend: Java + Spring Boot
*   **Framework**: Spring Boot 3.x.
*   **Ventajas**: Ecosistema maduro, inyección de dependencias robusta, Spring Security para autenticación/autorización, Spring Data JPA para persistencia, documentación extensa.
*   **Rol**: Exponer la lógica de negocio y acceso a datos a través de una API RESTful.

### Frontend: React + TypeScript
*   **Framework**: React 18+.
*   **Lenguaje**: TypeScript nativo.
*   **Estilos**: Bootstrap o Tailwind CSS (a definir en implementación).
*   **Rol**: Interfaz de usuario interactiva y gestión del estado de la aplicación con componentes y hooks.

### Base de Datos: PostgreSQL
*   **Motor**: PostgreSQL.
*   **ORM**: Spring Data JPA con Hibernate.
*   **Ventajas**: Robusto, excelente manejo de relaciones entre entidades, estándar académico, facilmente migrable a otros motores.

## Diagrama entidad-relación del sistema

| ![Diagrama](/images/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.svg) |
| --------- |
| [Código UML](/modelosUML/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.puml)|

