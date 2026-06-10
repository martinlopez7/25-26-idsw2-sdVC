# Stack tecnológico seleccionado

Para la construcción del sistema de generación de exámenes universitarios, se ha seleccionado una arquitectura de **Single Page Application (SPA)** con API REST, priorizando la separación de responsabilidades.

### Backend: Java + Spring Boot
*   **Framework**: Spring Boot 4.0.6
*   **Ventajas**: Ecosistema maduro, inyección de dependencias robusta, Spring Security para autenticación/autorización, Spring Data JPA para persistencia, documentación extensa.
*   **Rol**: Exponer la lógica de negocio y acceso a datos a través de una API RESTful.

### Frontend: React + TypeScript
*   **Framework**: React 19.2.6
*   **Lenguaje**: TypeScript nativo.
*   **Estilos**: Bootstrap o Tailwind CSS (a definir en implementación).
*   **Rol**: Interfaz de usuario interactiva y gestión del estado de la aplicación con componentes y hooks.

### Base de Datos: PostgreSQL
*   **Motor**: PostgreSQL.
*   **ORM**: Spring Data JPA con Hibernate.
*   **Ventajas**: Robusto, excelente manejo de relaciones entre entidades, facilmente migrable a otros motores.

## Diagrama de arquitectura

| ![Diagrama de arquitectura](/images/diseño/diagramaArquitectura/diagramaArquitectura.svg) |
| --------- |
| [Código UML](/modelosUML/diseño/diagramaArquitectura/diagramaArquitectura.puml)|

## Dependencias

```xml
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.12.5</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.12.5</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.12.5</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.15.2</version>
		</dependency>
		<dependency>
            <groupId>com.fasterxml.jackson.datatype</groupId>
            <artifactId>jackson-datatype-jsr310</artifactId>
            <version>2.15.2</version>
        </dependency>
        <dependency>
            <groupId>com.github.librepdf</groupId>
            <artifactId>openpdf</artifactId>
            <version>1.3.30</version>
        </dependency>
        <dependency>
            <groupId>org.apache.pdfbox</groupId>
            <artifactId>pdfbox</artifactId>
            <version>3.0.5</version>
        </dependency>
    </dependencies>
```

## Diagrama entidad-relación del sistema

| ![Diagrama](/images/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.svg) |
| --------- |
| [Código UML](/modelosUML/diseño/diagramaEntidadRelacion/diagramaEntidadRelacion.puml)|

---

[Volver](/documents/diseño/README.md)

