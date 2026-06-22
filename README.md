# Sistema de Reserva de Canchas Deportivas

## Descripción General

Sistema desarrollado bajo una arquitectura de microservicios utilizando Spring Boot y Spring Cloud. La plataforma permite la administración de recintos deportivos, canchas, horarios, reservas, pagos, notificaciones y reseñas mediante servicios independientes que se comunican a través de un API Gateway y son descubiertos dinámicamente mediante Netflix Eureka.

El objetivo principal del proyecto es demostrar la implementación de una arquitectura escalable, desacoplada y mantenible, aplicando buenas prácticas de desarrollo backend, seguridad, documentación y despliegue mediante contenedores Docker.

---

# Integrantes

* Pablo Acuña
* Benjamin Cea
* Abraham Puente

---

# Arquitectura de la Solución

La solución se basa en una arquitectura distribuida de microservicios compuesta por:

* API Gateway
* Discovery Service (Netflix Eureka)
* User Service
* Recinto Service
* Cancha Service
* Horario Service
* Reserva Service
* Pago Service
* Precio Service
* Notificación Service
* Reseña Service

Todos los servicios se registran automáticamente en Eureka y son consumidos a través de un único punto de entrada utilizando Spring Cloud Gateway.

---

# Tecnologías Utilizadas

### Backend

* Java 17
* Spring Boot
* Spring Data JPA
* Hibernate
* Spring Validation
* Spring Security
* Spring HATEOAS

### Arquitectura de Microservicios

* Netflix Eureka Discovery Server
* Eureka Client
* Spring Cloud Gateway

### Persistencia

* MySQL 8
* Flyway Migration

### Documentación

* Swagger OpenAPI

### Contenedores

* Docker
* Docker Compose

### Gestión de Dependencias

* Maven

---

# Funcionalidades Implementadas

### Gestión de Usuarios

* Registro de usuarios
* Administración de perfiles
* Gestión de roles

### Gestión de Recintos

* Administración de recintos deportivos
* Asociación de canchas a recintos

### Gestión de Canchas

* Creación y administración de canchas
* Consulta de disponibilidad

### Gestión de Horarios

* Configuración de horarios disponibles
* Control de disponibilidad

### Gestión de Reservas

* Creación de reservas
* Modificación de reservas
* Cancelación de reservas

### Gestión de Pagos

* Registro y validación de pagos

### Notificaciones

* Confirmaciones de reserva
* Avisos y recordatorios

### Reseñas

* Comentarios y calificaciones de usuarios

---

# Seguridad

La solución incorpora Spring Security utilizando autenticación Basic Auth.

## Roles Disponibles

| Rol     | Descripción                          |
| ------- | ------------------------------------ |
| ADMIN   | Administración completa del sistema  |
| DUENIO  | Administración de recintos y canchas |
| CLIENTE | Gestión de reservas                  |

## Usuarios de Prueba

| Usuario | Contraseña | Rol     |
| ------- | ---------- | ------- |
| admin   | admin123   | ADMIN   |
| duenio  | duenio123  | DUENIO  |
| cliente | cliente123 | CLIENTE |

---

# Manejo de Errores

Todos los microservicios implementan respuestas estandarizadas para:

* 400 Bad Request
* 401 Unauthorized
* 403 Forbidden
* 404 Not Found
* 500 Internal Server Error

Ejemplo:

```json
{
  "timestamp": "2026-06-22T12:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tienes permisos para acceder a este recurso",
  "path": "/api/canchas"
}
```

---

# Documentación API

La documentación de todos los microservicios se encuentra centralizada mediante Swagger OpenAPI.

### Swagger UI

http://localhost:8080/swagger-ui.html

---

# Discovery Service

Netflix Eureka permite el descubrimiento automático de servicios.

### URL

http://localhost:8761

Beneficios:

* Registro automático de servicios.
* Descubrimiento dinámico.
* Eliminación de URLs fijas.
* Mayor escalabilidad y mantenibilidad.

---

# API Gateway

El Gateway actúa como punto único de entrada para todos los clientes.

### URL

http://localhost:8080

Funciones principales:

* Enrutamiento centralizado.
* Integración con Eureka.
* Seguridad.
* Acceso unificado a los microservicios.

---

# Despliegue con Docker Compose

## Levantar toda la arquitectura

Si existen cambios en el código:

```bash
docker compose up -d --build
```

Si no existen cambios:

```bash
docker compose up -d
```

## Detener toda la arquitectura

```bash
docker compose down
```

---

# Servicios Desplegados

| Servicio             | Puerto |
| -------------------- | ------ |
| API Gateway          | 8080   |
| User Service         | 8081   |
| Recinto Service      | 8082   |
| Cancha Service       | 8083   |
| Horario Service      | 8084   |
| Reserva Service      | 8085   |
| Pago Service         | 8086   |
| Precio Service       | 8087   |
| Notificación Service | 8088   |
| Reseña Service       | 8089   |
| Discovery Service    | 8761   |
| MySQL                | 3307   |

---

# Estado del Proyecto

Proyecto académico desarrollado para la asignatura de Desarrollo Full Stack utilizando una arquitectura moderna basada en microservicios, aplicando conceptos de descubrimiento de servicios, API Gateway, seguridad, documentación centralizada y despliegue mediante contenedores Docker.
