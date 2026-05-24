# 25-26-idsw2-sdVC > crearGrado > Análisis

> |[🏠️](/documents/README.md)|[ 📊](https://github.com/martinlopez7/25-26-IdSw1-SdR/blob/main/documents/casos-de-uso/detalladoCasosDeUso/crearGrado/crearGrado.svg)|[Analisis](/documents/analisis/crearGrado/README.md)|Diseño|Desarrollo|Pruebas|
> |-|-|-|-|-|-|-|

## información del artefacto

- **Proyecto**: 25-26-idsw2-sdVC - Sistema de Generación de Exámenes Universitarios
- **Fase RUP**: Elaboration (Elaboración)
- **Disciplina**: Análisis y Diseño
- **Versión**: 1.0
- **Fecha**: 2024-05-24
- **Autor**: Equipo de desarrollo

## propósito

Análisis de colaboración del caso de uso `crearGrado()` mediante el patrón MVC, identificando las clases de análisis, sus responsabilidades y colaboraciones necesarias para implementar la filosofía C→U como "el delgado".

## diagrama de colaboración

<div align=center>

|![Análisis: crearGrado()](/images/analisis/crearGrado/colaboracion.svg)|
|-|
|Código fuente: [colaboracion.puml](/modelosUML/analisis/crearGrado/colaboracion.puml)|

</div>

## clases de análisis identificadas

### clases de vista (boundary)

#### CrearGradoView
**Estereotipo**: Vista (Boundary)  
**Responsabilidades**:
- Recibir la solicitud de creación de grado nuevo
- Presentar formulario simple para datos mínimos
- Capturar nombre y código del grado
- Manejar creación y transferencia automática a edición

**Colaboraciones**:
- **Entrada**: Recibe `crearGrado()` desde `:Grados Abierto`
- **Control**: Se comunica con `GradosController`
- **Salida**: Transfiere control a `:Collaboration EditarGrado`

### clases de control

#### GradosController
**Estereotipo**: Control  
**Responsabilidades**:
- Coordinar la creación de grado con datos mínimos
- Validar nombre único del grado
- Transferir control a editarGrado()

**Colaboraciones**:
- **Vista**: Responde a solicitudes de `CrearGradoView`
- **Repositorio**: Delega creación a `GradoRepository`

### clases de entidad (entity)

#### GradoRepository
**Estereotipo**: Entidad  
**Responsabilidades**:
- Abstraer la creación de nuevos grados
- Verificar unicidad del nombre y código
- Persistir grado con datos mínimos

**Colaboraciones**:
- **Control**: Responde a `GradosController`
- **Entidad**: Crea nuevas instancias de `Grado`

#### Grado
**Estereotipo**: Entidad  
**Responsabilidades**:
- Representar grado académico recién creado
- Encapsular datos mínimos: título, código
- Validar información básica
- Prepararse para edición completa

**Colaboraciones**:
- **Repositorio**: Es creado por `GradoRepository`

## flujo de colaboración principal

### secuencia: crear grado (delgado)

1. **Inicio**: `:Grados Abierto` → `CrearGradoView.crearGrado()`
2. **Solicitud**: `CrearGradoView` presenta formulario de datos mínimos
3. **Captura**: Docente proporciona nombre y código en `CrearGradoView`
4. **Creación**: `CrearGradoView` → `GradosController.crearGradoMinimo(nombre, codigo)`
5. **Validación**: `GradosController` → `GradoRepository.crear(nombre, codigo) : Grado`
6. **Persistencia**: `GradoRepository` crea nueva instancia de `Grado`
7. **Transferencia**: `CrearGradoView` → `:Collaboration EditarGrado.editarGrado(gradoNuevo)`

## aplicación de filosofía C→U

### rol de "el delgado"

Este análisis implementa `crearGrado()` como "el delgado" que:
- **Minimiza complejidad**: Solo captura datos esenciales
- **Transfiere inmediatamente**: Pasa control a editarGrado()
- **Reutiliza funcionalidad**: No duplica lógica de edición

### responsabilidades minimalistas

**CrearGradoView** es deliberadamente simple:
- **No incluye**: Formulario completo de edición
- **Solo maneja**: Nombre, código y creación
- **Transfiere**: Inmediatamente a EditarGradoView

**GradosController** maneja creación básica:
- **Crear entidad**: Con datos mínimos únicamente
- **Validar esencial**: Solo unicidad del nombre y código

## patrón MVC aplicado

- **Model**: `Grado` + `GradoRepository` (creación mínima)
- **View**: `CrearGradoView` (formulario simple)
- **Controller**: `GradosController` (coordinación básica)

### transferencia de control

- **Patrón C→U**: CrearGradoView transfiere a EditarGradoView
- **Estado compartido**: Grado recién creado pasa como parámetro
- **Experiencia fluida**: Usuario no regresa a lista intermedia

## diferencias con editarGrado()

### simplicidad vs complejidad

**crearGrado() - "El delgado":**
- **Vista**: Formulario simple con campos mínimos
- **Controlador**: Lógica mínima de creación
- **Flujo**: Rápido y directo hacia edición

**editarGrado() - "El gordo":**
- **Vista**: Formulario completo multi-campo
- **Controlador**: Lógica completa de modificación
- **Flujo**: Edición completa con múltiples opciones

### colaboraciones complementarias

- **crearGrado()**: Produce input para editarGrado()
- **editarGrado()**: Consume y completa lo iniciado por crearGrado()