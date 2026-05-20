# 🏟️ Sistema de Reserva de Canchas Deportivas
### Arquitectura de Microservicios — Spring Boot 4.0.6

---

## 👥 Integrantes del Equipo

| Nombre | GitHub |
|--------|--------|
| Benjamin Cea | @BenjaminCeainfor |
| Abraham Puente | @Abrahampuente |
| Pablo Acuña | @Pabloacuna-06 |

---

## 📋 Descripción del Proyecto

Sistema backend basado en arquitectura de microservicios para la gestión integral de reservas de canchas deportivas. Permite administrar usuarios, recintos, canchas, horarios, reservas, precios, pagos, notificaciones, mantenimiento y reseñas de manera independiente y escalable.

Cada microservicio es completamente autónomo, con su propia base de datos H2, capa de seguridad con Spring Security (Basic Auth), migraciones con Flyway, validaciones con Bean Validation y manejo centralizado de excepciones.

---

## 🧩 Microservicios Implementados

| # | Microservicio | Puerto | Descripción |
|---|---------------|--------|-------------|
| 1 | user-service | 8081 | Gestión de usuarios del sistema |
| 2 | recinto-service | 8082 | Gestión de recintos deportivos |
| 3 | cancha-service | 8083 | Gestión de canchas por recinto |
| 4 | horario-service | 8084 | Gestión de horarios disponibles |
| 5 | reserva-service | 8085 | Gestión de reservas de canchas |
| 6 | precio-service | 8086 | Gestión de precios por cancha |
| 7 | pago-service | 8087 | Gestión de pagos de reservas |
| 8 | notificacion-service | 8088 | Gestión de notificaciones |
| 9 | mantenimiento-service | 8089 | Gestión de mantenimiento de canchas |
| 10 | resena-service | 8090 | Gestión de reseñas y calificaciones |

---

## ⚙️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Web** — API REST
- **Spring Data JPA + Hibernate** — Persistencia
- **H2 Database** — Base de datos en memoria
- **Flyway** — Migraciones de base de datos
- **Spring Security** — Autenticación Basic Auth
- **Bean Validation (JSR 380)** — Validaciones
- **Lombok** — Reducción de código boilerplate
- **SLF4J** — Logs estructurados
- **Maven** — Gestión de dependencias
- **RestClient** — Comunicación HTTP entre microservicios

---

## 🏗️ Estructura de cada Microservicio

```
cl.duoc.[nombre]service
├── config/
│   └── SecurityConfig.java
├── controller/
│   └── [Entidad]Controller.java
├── dto/
│   ├── [Entidad]Request.java
│   └── [Entidad]Response.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── model/
│   └── [Entidad].java
├── repository/
│   └── [Entidad]Repository.java
└── service/
    └── [Entidad]Service.java
```

---

## 🔌 Endpoints REST disponibles (por microservicio)

Cada microservicio expone los siguientes endpoints:

| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | /api/[entidad] | Crear nuevo recurso |
| GET | /api/[entidad] | Obtener todos los recursos |
| GET | /api/[entidad]/{id} | Obtener recurso por ID |
| PUT | /api/[entidad]/{id} | Actualizar recurso |
| DELETE | /api/[entidad]/{id} | Eliminar recurso |

---

## 🔐 Seguridad

Todos los endpoints están protegidos con **Basic Auth**:

```
Usuario: admin
Contraseña: admin123
```

La consola H2 está disponible en `/h2-console` sin autenticación.

---

## 🗄️ Base de Datos

Cada microservicio usa una base de datos H2 en memoria independiente:

| Microservicio | JDBC URL |
|---------------|----------|
| user-service | jdbc:h2:mem:userdb |
| recinto-service | jdbc:h2:mem:recintodb |
| cancha-service | jdbc:h2:mem:canchadb |
| horario-service | jdbc:h2:mem:horariodb |
| reserva-service | jdbc:h2:mem:reservadb |
| precio-service | jdbc:h2:mem:preciodb |
| pago-service | jdbc:h2:mem:pagodb |
| notificacion-service | jdbc:h2:mem:notificaciondb |
| mantenimiento-service | jdbc:h2:mem:mantenimientodb |
| resena-service | jdbc:h2:mem:resenadb |

---

## 🚀 Pasos para ejecutar el proyecto

### Requisitos previos

- Java 17 instalado
- IntelliJ IDEA
- Maven
- Postman (para pruebas)

### Pasos

1. **Clonar el repositorio**
```bash
git clone https://github.com/BenjaminCeainfor/[nombre-repo].git
```

2. **Abrir en IntelliJ IDEA**
   - File → Open → seleccionar la carpeta del microservicio
   - Esperar que Maven descargue las dependencias

3. **Ejecutar cada microservicio**
   - Abrir la clase `[Nombre]ServiceApplication.java`
   - Clic en el botón ▶️ verde

4. **Verificar que levantó correctamente**
   - Debe aparecer: `Started [Nombre]ServiceApplication on port [puerto]`

5. **Probar con Postman**
   - Usar Basic Auth: `admin` / `admin123`
   - Ejemplo POST:
```json
POST http://localhost:8085/api/reservas
{
  "usuarioId": 1,
  "canchaId": 2,
  "horarioId": 3,
  "fechaReserva": "2025-06-15T10:00:00",
  "estado": "PENDIENTE"
}
```

---

## 🔗 Comunicación entre Microservicios

Los microservicios se comunican entre sí mediante **RestClient** a través de HTTP interno entre puertos locales. Por ejemplo, `reserva-service` consulta datos de `cancha-service` y `horario-service` para validar disponibilidad.

---

## 📁 Organización del Repositorio

```
proyecto-microservicios/
├── user-service/
├── recinto-service/
├── cancha-service/
├── horario-service/
├── reserva-service/
├── precio-service/
├── pago-service/
├── notificacion-service/
├── mantenimiento-service/
├── resena-service/
└── README.md
```

---

## ✅ Funcionalidades Implementadas

- ✅ Arquitectura de microservicios independientes
- ✅ CRUD completo por microservicio
- ✅ Persistencia con JPA + Hibernate
- ✅ Migraciones con Flyway
- ✅ Validaciones con Bean Validation
- ✅ Manejo centralizado de excepciones con @RestControllerAdvice
- ✅ Logs estructurados con SLF4J
- ✅ Seguridad con Spring Security (Basic Auth)
- ✅ Respuestas con ResponseEntity y códigos HTTP correctos
- ✅ Separación por capas: Controller → Service → Repository
- ✅ DTOs para comunicación entre capas
- ✅ Comunicación entre microservicios con RestClient
- ✅ Control de versiones con Git y GitHub
