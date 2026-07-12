# Sistema de Reserva de Canchas Deportivas

## Descripción general

Sistema backend desarrollado bajo una arquitectura de microservicios utilizando Spring Boot y Spring Cloud.

La plataforma permite administrar usuarios, recintos deportivos, canchas, horarios, reservas, precios, pagos, notificaciones, reseñas y mantenimientos. Cada funcionalidad se encuentra separada en un microservicio independiente.

Los servicios se registran en Netflix Eureka y se consumen desde un punto de entrada centralizado mediante Spring Cloud Gateway. La comunicación interna entre microservicios utiliza RestClient, Eureka Discovery y Spring Cloud LoadBalancer.

El proyecto puede ejecutarse localmente mediante Docker Compose y también se encuentra desplegado en la nube utilizando Render para los servicios y Railway para las bases de datos MySQL.

---

## Integrantes

- Pablo Acuña
- Benjamin Cea
- Abraham Puente

---

## Objetivo del proyecto

Desarrollar una solución distribuida que permita gestionar el proceso completo de reserva de canchas deportivas, desde la creación del usuario y del recinto hasta el pago, la reseña, el mantenimiento y la notificación.

El proyecto aplica:

- Arquitectura de microservicios.
- Patrón Controller-Service-Repository.
- Persistencia con JPA e Hibernate.
- Migraciones con Flyway.
- Validaciones y reglas de negocio.
- Manejo centralizado de excepciones.
- Seguridad con Spring Security.
- Comunicación entre servicios.
- Descubrimiento dinámico mediante Eureka.
- API Gateway.
- Documentación mediante Swagger/OpenAPI.
- Contenedores Docker.
- Despliegue remoto.

---

## Arquitectura

El proyecto contiene 12 servicios:

- 10 microservicios de negocio.
- 1 Discovery Service.
- 1 API Gateway.

```text
                       Cliente
                          |
                          v
                    API Gateway
                          |
                          v
                   Eureka Discovery
                          |
       ------------------------------------------------
       |        |         |        |        |         |
       v        v         v        v        v         v
     User    Recinto    Cancha   Horario  Reserva   Precio
                          |         |        |
                          |         |        v
                          |         |       Pago
                          |         |
                          v         v
                       Reseña   Mantenimiento

                    Notificación
```

El API Gateway actúa como punto único de entrada. Eureka permite que los microservicios se localicen utilizando nombres lógicos, evitando depender de direcciones como `localhost` o IP fijas.

---

## Microservicios

| Servicio | Responsabilidad |
|---|---|
| Discovery Service | Registro y descubrimiento de los servicios mediante Netflix Eureka. |
| API Gateway | Punto único de entrada y enrutamiento hacia los microservicios. |
| User Service | Administración de usuarios, perfiles y roles. |
| Recinto Service | Gestión de recintos deportivos y sus datos principales. |
| Cancha Service | Administración de canchas asociadas a recintos. |
| Horario Service | Gestión de horarios disponibles para las canchas. |
| Reserva Service | Creación, actualización y cancelación de reservas. |
| Precio Service | Administración de precios asociados a las canchas. |
| Pago Service | Registro y validación de pagos de reservas. |
| Notificación Service | Gestión de notificaciones para los usuarios. |
| Reseña Service | Registro de comentarios y calificaciones. |
| Mantenimiento Service | Gestión de mantenimientos de las canchas. |

---

## Tecnologías utilizadas

### Backend

- Java 17.
- Spring Boot.
- Spring Data JPA.
- Hibernate.
- Spring Validation.
- Spring Security.
- Spring HATEOAS.
- RestClient.

### Arquitectura distribuida

- Netflix Eureka Discovery Server.
- Eureka Client.
- Spring Cloud Gateway.
- Spring Cloud LoadBalancer.

### Persistencia

- MySQL.
- H2 para desarrollo y pruebas locales en algunos servicios.
- Flyway para control de migraciones.

### Documentación

- Swagger.
- OpenAPI.
- Swagger centralizado en el API Gateway.

### Despliegue y contenedores

- Docker.
- Docker Compose.
- Render.
- Railway.

### Gestión del proyecto

- Maven.
- Git.
- GitHub.

---

## Patrón de organización

Los microservicios se encuentran organizados mediante el patrón CSR:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Base de datos
```

Además, se utilizan:

- DTOs para entrada y salida de datos.
- Modelos JPA para persistencia.
- Repositorios basados en `JpaRepository`.
- `ControllerAdvice` para el manejo centralizado de errores.
- Bean Validation para validar solicitudes.
- SLF4J para registrar eventos y errores.

---

## Comunicación entre microservicios

Los microservicios que necesitan consultar información de otros servicios utilizan RestClient y Spring Cloud LoadBalancer.

Ejemplos:

- Cancha consulta la existencia de un recinto.
- Horario consulta la existencia de una cancha.
- Reserva valida usuario, cancha y horario.
- Pago valida una reserva.
- Precio valida una cancha.
- Reseña valida usuario y cancha.
- Mantenimiento valida una cancha.

La comunicación utiliza nombres registrados en Eureka:

```java
http://USER-SERVICE
http://RECINTO-SERVICE
http://CANCHA-SERVICE
http://HORARIO-SERVICE
http://RESERVA-SERVICE
```

Esto permite evitar URLs fijas entre los servicios.

---

## Seguridad

El proyecto utiliza Spring Security con autenticación Basic Auth.

### Roles disponibles

| Rol | Descripción |
|---|---|
| ADMIN | Administración general del sistema. |
| DUENIO | Administración de recintos, canchas y mantenimientos. |
| CLIENTE | Creación de reservas, pagos y reseñas. |

### Usuarios de prueba

| Usuario | Contraseña | Rol |
|---|---|---|
| admin | admin123 | ADMIN |
| duenio | duenio123 | DUENIO |
| cliente | cliente123 | CLIENTE |

Las credenciales pueden variar dependiendo del microservicio y de su configuración de seguridad.

---

## Manejo de errores

Los microservicios implementan respuestas controladas mediante manejadores globales de excepciones.

Entre las respuestas utilizadas se encuentran:

- `400 Bad Request`
- `401 Unauthorized`
- `403 Forbidden`
- `404 Not Found`
- `500 Internal Server Error`

Ejemplo:

```json
{
  "error": "Business Rule Violation",
  "message": "Solo un usuario CLIENTE o ADMIN puede crear reservas",
  "timestamp": "2026-07-12T01:41:25",
  "status": 400
}
```

---

## Persistencia y bases de datos

Cada microservicio de negocio utiliza una base de datos independiente.

En el entorno local se utiliza MySQL mediante Docker. Para el despliegue remoto, las bases de datos MySQL están alojadas en Railway.

### ¿Por qué Railway?

Render se utiliza para ejecutar los servicios web, mientras que Railway se utiliza como proveedor de las bases de datos.

Esta separación permite:

- Mantener la persistencia fuera de los contenedores de los microservicios.
- Conservar la información cuando un servicio se reinicia o vuelve a desplegarse.
- Administrar las conexiones mediante variables de entorno.
- Mantener separada la capa de aplicación de la capa de datos.

Las credenciales de acceso no se incluyen en el repositorio.

---

## Migraciones con Flyway

Cada microservicio contiene sus scripts de migración en:

```text
src/main/resources/db/migration
```

Ejemplo:

```text
V1__create_tables.sql
V2__add_audit_fields.sql
```

Flyway valida y aplica las migraciones al iniciar cada servicio.

---

## Puertos locales

| Servicio | Puerto |
|---|---:|
| API Gateway | 8080 |
| User Service | 8081 |
| Recinto Service | 8082 |
| Cancha Service | 8083 |
| Horario Service | 8084 |
| Reserva Service | 8085 |
| Precio Service | 8086 |
| Pago Service | 8087 |
| Notificación Service | 8088 |
| Reseña Service | 8089 |
| Mantenimiento Service | 8090 |
| Discovery Service | 8761 |
| MySQL local | 3307 |

---

## Ejecución local con Docker Compose

### Requisitos

- Java 17.
- Docker Desktop.
- Docker Compose.
- Git.

### Clonar el proyecto

```bash
git clone https://github.com/abrahampuente/Proyecto-reserva-cancha.git
cd Proyecto-reserva-cancha
```

### Construir y levantar los servicios

```bash
docker compose up -d --build
```

Si las imágenes ya están construidas:

```bash
docker compose up -d
```

### Ver el estado

```bash
docker compose ps
```

### Ver los logs

```bash
docker compose logs -f
```

### Detener el sistema

```bash
docker compose down
```

---

## Acceso local

### Eureka

```text
http://localhost:8761
```

### Swagger centralizado

```text
http://localhost:8080/swagger-ui/index.html
```

### API Gateway

```text
http://localhost:8080
```

---

## Despliegue remoto

Los servicios se encuentran desplegados como Web Services independientes en Render.

Cada servicio obtiene su configuración mediante variables de entorno. Esto permite usar el mismo código en desarrollo local y en la nube.

Las propiedades principales son:

```text
PORT
DB_URL
DB_USERNAME
DB_PASSWORD
EUREKA_URL
API_GATEWAY_URL
```

Ejemplo de configuración:

```env
DB_URL=jdbc:mysql://HOST:PUERTO/NOMBRE_BASE
DB_USERNAME=usuario
DB_PASSWORD=contraseña
EUREKA_URL=https://discovery-service-jp0a.onrender.com/eureka/
API_GATEWAY_URL=https://api-gateway-q3iu.onrender.com
```

No se deben publicar contraseñas ni credenciales reales en el repositorio.

---

## URLs públicas

| Servicio | URL |
|---|---|
| Discovery Service | https://discovery-service-jp0a.onrender.com |
| API Gateway | https://api-gateway-q3iu.onrender.com |
| User Service | https://user-service-1f5s.onrender.com |
| Recinto Service | https://recinto-service.onrender.com |
| Cancha Service | https://cancha-service.onrender.com |
| Horario Service | https://horario-service.onrender.com |
| Reserva Service | https://reserva-service-hzej.onrender.com |
| Precio Service | https://precio-service.onrender.com |
| Pago Service | https://pago-service-d1je.onrender.com |
| Notificación Service | https://notificacion-service-nyuo.onrender.com |
| Reseña Service | https://resena-service-p65a.onrender.com |
| Mantenimiento Service | https://mantenimiento-service-s2qp.onrender.com |

---

## Swagger remoto

La documentación centralizada está disponible en:

```text
https://api-gateway-q3iu.onrender.com/swagger-ui/index.html
```

Desde esta interfaz se pueden consultar y probar los endpoints de los microservicios.

---

## Eureka remoto

El panel de registro de servicios se encuentra en:

```text
https://discovery-service-jp0a.onrender.com/
```

En este panel se puede verificar qué microservicios están registrados y su estado.

---

## Activación de servicios en Render

Los servicios del plan gratuito de Render pueden suspenderse después de un periodo sin actividad.

Para facilitar la activación se agregó el archivo:

```text
wake-render.sh
```

### Ejecutar el script

Desde la raíz del proyecto:

```bash
chmod +x wake-render.sh
./wake-render.sh
```

El script realiza el siguiente proceso:

1. Activa Eureka.
2. Espera a que el Discovery Server termine de iniciar.
3. Activa los microservicios.
4. Espera su registro en Eureka.
5. Activa el API Gateway.

El proceso puede tardar varios minutos debido al inicio de los servicios gratuitos.

Después de ejecutarlo:

1. Abrir Eureka.
2. Confirmar que los servicios aparezcan como `UP`.
3. Abrir Swagger.
4. Ejecutar las pruebas.

---

## Flujo recomendado de prueba

Para probar el sistema completo se recomienda seguir este orden:

```text
1. Crear un usuario con rol DUENIO.
2. Crear un recinto utilizando el ID del dueño.
3. Crear una cancha asociada al recinto.
4. Crear un horario para la cancha.
5. Crear un usuario con rol CLIENTE.
6. Crear una reserva con el cliente, cancha y horario.
7. Crear un precio asociado a la cancha.
8. Registrar un pago asociado a la reserva.
9. Crear una reseña utilizando el cliente y la cancha.
10. Registrar un mantenimiento para la cancha.
11. Crear una notificación.
```

Este flujo fue probado mediante el Swagger centralizado en el entorno remoto.

---

## Estado final del proyecto

El ecosistema se encuentra desplegado y operativo.

Se validó:

- Registro de los servicios en Eureka.
- Enrutamiento mediante el API Gateway.
- Swagger centralizado.
- Comunicación entre microservicios.
- Persistencia en bases de datos MySQL.
- Migraciones Flyway.
- Seguridad y roles.
- Reglas de negocio.
- Creación de datos en los 10 microservicios de negocio.
- Ejecución mediante Docker.
- Despliegue remoto en Render.
- Bases de datos alojadas en Railway.

---

## Repositorio

```text
https://github.com/abrahampuente/Proyecto-reserva-cancha
```

Proyecto desarrollado para la asignatura **Desarrollo Full Stack I - DSY1103**.