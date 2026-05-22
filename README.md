# 🏟️ Sistema de Reserva de Canchas Deportivas
### Arquitectura de Microservicios — Spring Boot

---

## 👥 Integrantes del Equipo

| Nombre | GitHub |
|--------|--------|
| Benjamin Cea | @BenjaminCeainfor |
| Abraham Puente | @Abrahampuente |
| Pablo Acuña | @Pabloacuna-06 |

---

## 📋 Descripción del Proyecto

Sistema backend basado en arquitectura de microservicios para la gestión integral de reservas de canchas deportivas.

El sistema permite administrar usuarios, recintos, canchas, horarios, reservas, precios, pagos, notificaciones, mantenimientos y reseñas de forma independiente, desacoplada y escalable.

Cada microservicio posee:

- Responsabilidad única
- Base de datos H2 independiente
- Seguridad con Spring Security
- Migraciones con Flyway
- Validaciones con Bean Validation
- Manejo centralizado de excepciones
- Comunicación REST entre microservicios mediante RestClient

---

## 🧩 Microservicios Implementados

| # | Microservicio | Puerto | Descripción |
|---|---------------|--------|-------------|
| 1 | user-service | 8081 | Gestión de usuarios |
| 2 | recinto-service | 8082 | Gestión de recintos deportivos |
| 3 | cancha-service | 8083 | Gestión de canchas |
| 4 | horario-service | 8084 | Gestión de horarios |
| 5 | reserva-service | 8085 | Gestión de reservas |
| 6 | precio-service | 8086 | Gestión de precios |
| 7 | pago-service | 8087 | Gestión de pagos |
| 8 | notificacion-service | 8088 | Gestión de notificaciones |
| 9 | resena-service | 8089 | Gestión de reseñas |
| 10 | mantenimiento-service | 8090 | Gestión de mantenimientos |

---

## ⚙️ Tecnologías Utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- H2 Database
- Flyway
- Maven
- Lombok
- REST API
- Postman
- IntelliJ IDEA
- RestClient

---

## 🏗️ Arquitectura Interna de Cada Microservicio

```text
cl.duoc.[nombre]service
├── client/
│   └── Comunicación REST con otros microservicios
├── config/
│   └── Configuración de seguridad
├── controller/
│   └── Endpoints REST
├── dto/
│   ├── Request
│   └── Response
├── exception/
│   ├── Excepciones personalizadas
│   └── GlobalExceptionHandler
├── model/
│   └── Entidades JPA
├── repository/
│   └── Persistencia con JPA
└── service/
    └── Lógica de negocio
```

---

## 🔌 Endpoints REST

Cada microservicio expone endpoints REST según su responsabilidad:

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /api/[entidad] | Crear recurso |
| GET | /api/[entidad] | Obtener todos |
| GET | /api/[entidad]/{id} | Obtener por ID |
| PUT | /api/[entidad]/{id} | Actualizar |
| DELETE | /api/[entidad]/{id} | Eliminación lógica o actualización de estado |

Endpoints internos de validación:

```text
/api/users/{id}/exists
/api/users/{id}/role
/api/recintos/{id}/exists
/api/canchas/{id}/exists
/api/horarios/{id}/exists
/api/reservas/{id}/exists
```

---

## 🔐 Seguridad

El sistema implementa autenticación Basic Auth con Spring Security y control por roles.

### Roles

**ADMIN**
- Administración general del sistema

**DUENIO**
- Gestión de recintos
- Gestión de canchas
- Gestión de horarios
- Gestión de precios
- Gestión de mantenimientos

**CLIENTE**
- Crear reservas
- Realizar pagos
- Crear reseñas

### Credenciales de prueba

```text
admin / admin123
duenio / duenio123
cliente / cliente123
```

---

## 🗄️ Base de Datos

Cada microservicio utiliza una base de datos H2 independiente basada en archivo.

| Microservicio | JDBC URL |
|---------------|----------|
| user-service | jdbc:h2:file:./data/usersdb |
| recinto-service | jdbc:h2:file:./data/recintosdb |
| cancha-service | jdbc:h2:file:./data/canchasdb |
| horario-service | jdbc:h2:file:./data/horariosdb |
| reserva-service | jdbc:h2:file:./data/reservadb |
| precio-service | jdbc:h2:file:./data/preciodb |
| pago-service | jdbc:h2:file:./data/pagodb |
| notificacion-service | jdbc:h2:file:./data/notificacionesdb |
| resena-service | jdbc:h2:file:./data/resenasdb |
| mantenimiento-service | jdbc:h2:file:./data/mantenimientosdb |

Consola H2:

```text
http://localhost:[puerto]/h2-console
```

---

## 🔗 Comunicación entre Microservicios

Los servicios se comunican mediante llamadas REST usando RestClient.

### Dependencias

- cancha-service → recinto-service
- horario-service → cancha-service
- precio-service → cancha-service
- reserva-service → user-service + cancha-service + horario-service
- pago-service → reserva-service
- resena-service → user-service + cancha-service
- mantenimiento-service → cancha-service

### Ejemplo real

Flujo de creación de reserva:

```text
reserva-service
   ↓
user-service
   ↓
cancha-service
   ↓
horario-service
```

Validaciones realizadas:

- Usuario existe
- Cancha existe
- Horario existe
- Reserva no duplicada

---

## 🧪 Ejemplo de Prueba

### Crear Reserva

```http
POST http://localhost:8085/api/reservas
```

Body:

```json
{
  "usuarioId": 5,
  "canchaId": 4,
  "horarioId": 4,
  "fechaReserva": "2026-05-25T18:00:00"
}
```

---

## ⚠️ Manejo de Errores

Se implementa manejo centralizado mediante `GlobalExceptionHandler`.

Errores soportados:

- 400 Bad Request
- 403 Forbidden
- 404 Not Found
- 409 Conflict

Excepciones personalizadas:

- BusinessRuleException
- ResourceNotFoundException
- DuplicateResourceException

---

## 🧾 Funcionalidades Implementadas

- Arquitectura de microservicios
- CRUD independiente por servicio
- Seguridad con roles
- Persistencia con JPA
- Migraciones con Flyway
- DTOs
- Validaciones de negocio
- Comunicación REST entre microservicios
- Manejo de errores centralizado
- H2 Console
- Borrado lógico mediante estados
- Testing manual con Postman

---

## 🚀 Ejecución del Proyecto

### Requisitos

- Java 17
- IntelliJ IDEA
- Maven
- Postman

### Pasos

#### 1. Clonar repositorio

```bash
git clone https://github.com/abrahampuente/Proyecto-reserva-cancha.git
```

#### 2. Abrir en IntelliJ

Abrir carpeta raíz del proyecto.

#### 3. Esperar descarga de dependencias Maven

#### 4. Ejecutar microservicios

Ejecutar:

```text
UserServiceApplication
RecintoServiceApplication
CanchaServiceApplication
HorarioServiceApplication
ReservaServiceApplication
PrecioServiceApplication
PagoServiceApplication
NotificacionServiceApplication
ResenaServiceApplication
MantenimientoServiceApplication
```

#### 5. Verificar ejecución

Ejemplo:

```text
Started ReservaServiceApplication on port 8085
```

#### 6. Probar endpoints con Postman

Usar Basic Auth.

---

## 📁 Organización del Repositorio

```text
Proyecto-reserva-cancha/
├── user-service/
├── recinto-service/
├── cancha-service/
├── horario-service/
├── reserva-service/
├── precio-service/
├── pago-service/
├── notificacion-service/
├── resena-service/
├── mantenimiento-service/
└── README.md
```

---

## 🧩 Flujo Funcional de Demo

1. Crear usuario dueño
2. Crear usuario cliente
3. Crear recinto
4. Crear cancha
5. Crear horario
6. Crear precio
7. Crear reserva
8. Crear pago
9. Crear notificación
10. Crear reseña
11. Crear mantenimiento

---

## 🔮 Mejoras Futuras

- API Gateway
- JWT Authentication
- Docker
- Docker Compose
- PostgreSQL
- Swagger / OpenAPI
- OpenFeign
- Centralized Logging
- Monitoring
- CI/CD Pipelines
