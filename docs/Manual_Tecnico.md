# Manual Técnico

# Sistema de Reserva de Canchas Deportivas

## 1. Introducción

El Sistema de Reserva de Canchas Deportivas fue desarrollado utilizando una arquitectura basada en microservicios con Spring Boot y Spring Cloud.

Cada funcionalidad del sistema se implementó como un servicio independiente, permitiendo desacoplar la lógica de negocio, facilitar el mantenimiento y mejorar la escalabilidad de la aplicación.

El sistema fue desplegado utilizando Render como plataforma para los microservicios y Railway como proveedor de bases de datos MySQL.

---

# 2. Arquitectura del sistema

La solución está compuesta por doce servicios:

- Discovery Service
- API Gateway
- User Service
- Recinto Service
- Cancha Service
- Horario Service
- Reserva Service
- Precio Service
- Pago Service
- Notificación Service
- Reseña Service
- Mantenimiento Service

Todos los servicios se registran automáticamente en Netflix Eureka y son consumidos mediante Spring Cloud Gateway.

La comunicación entre microservicios se realiza utilizando Spring Cloud LoadBalancer y RestClient.

---

# 3. Tecnologías utilizadas

## Backend

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Spring Validation
- Spring Security
- Spring HATEOAS

## Arquitectura

- Netflix Eureka
- Spring Cloud Gateway
- Spring Cloud LoadBalancer
- RestClient

## Persistencia

- MySQL
- Flyway

## Documentación

- Swagger OpenAPI

## Contenedores

- Docker
- Docker Compose

## Despliegue

- Render
- Railway

---

# 4. Organización de los microservicios

Todos los microservicios utilizan una estructura similar:

- Controller
- DTO
- Service
- Repository
- Entity
- Config
- Exception

Esta organización facilita el mantenimiento del código y mantiene una separación clara entre la lógica de negocio, la persistencia y la exposición de los servicios.

---

# 5. Comunicación entre microservicios

Los servicios utilizan RestClient para realizar consultas a otros microservicios.

Para evitar depender de direcciones IP o URLs fijas, todas las llamadas utilizan nombres registrados en Eureka.

Spring Cloud LoadBalancer resuelve automáticamente el servicio correspondiente.

Ejemplos:

- Reserva consulta usuarios, horarios y canchas.
- Pago consulta reservas.
- Precio consulta canchas.
- Horario consulta canchas.
- Cancha consulta recintos.

---

# 6. Persistencia

Cada microservicio mantiene su propia base de datos.

Las migraciones son administradas mediante Flyway, permitiendo mantener un control de versiones sobre la estructura de las tablas.

---

# 7. Despliegue

Durante el desarrollo se utilizó Docker Compose para ejecutar el proyecto localmente.

Para el entorno remoto se utilizó:

- Render para alojar los microservicios.
- Railway para alojar las bases de datos MySQL.

Esta separación permitió mantener la persistencia de la información independientemente del ciclo de vida de los microservicios.

---

# 8. Documentación de APIs

Todos los endpoints del sistema se documentan mediante Swagger OpenAPI.

La documentación se encuentra centralizada a través del API Gateway, permitiendo consultar desde un único punto todos los servicios disponibles.

---

# 9. Conclusión

La arquitectura implementada permitió desarrollar una solución modular, escalable y fácil de mantener, utilizando herramientas actuales para el desarrollo de aplicaciones distribuidas.

El proyecto integra seguridad, documentación, persistencia, comunicación entre microservicios y despliegue en la nube, cumpliendo con los objetivos definidos para la asignatura.