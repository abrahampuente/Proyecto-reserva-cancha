# Manual de Usuario

# Sistema de Reserva de Canchas Deportivas

## 1. Introducción

El Sistema de Reserva de Canchas Deportivas permite administrar el proceso completo de reserva de canchas deportivas mediante una arquitectura de microservicios.

Este manual explica el flujo básico para utilizar las principales funcionalidades del sistema utilizando la documentación Swagger.

---

# 2. Acceso al sistema

## Entorno local

API Gateway

```
http://localhost:8080
```

Swagger

```
http://localhost:8080/swagger-ui/index.html
```

---

## Entorno desplegado

API Gateway

```
https://api-gateway-q3iu.onrender.com
```

Swagger

```
https://api-gateway-q3iu.onrender.com/swagger-ui/index.html
```

---

# 3. Usuarios de prueba

| Usuario | Contraseña | Rol |
|----------|------------|------|
| admin | admin123 | ADMIN |
| duenio | duenio123 | DUENIO |
| cliente | cliente123 | CLIENTE |

---

# 4. Flujo recomendado de uso

Para probar correctamente el sistema se recomienda seguir el siguiente orden.

## Paso 1. Crear un usuario

Ingresar al User Service y registrar un usuario.

Dependiendo de la prueba, el usuario puede tener rol:

- ADMIN
- DUENIO
- CLIENTE

---

## Paso 2. Crear un recinto

Registrar un recinto deportivo asociado a un usuario con rol DUENIO.

---

## Paso 3. Crear una cancha

Registrar una cancha utilizando el identificador del recinto creado anteriormente.

---

## Paso 4. Crear un horario

Registrar un horario disponible para la cancha.

---

## Paso 5. Registrar un precio

Asignar un precio a la cancha.

---

## Paso 6. Crear una reserva

Registrar una reserva utilizando:

- Usuario CLIENTE
- Cancha
- Horario

---

## Paso 7. Registrar un pago

Crear un pago asociado a la reserva.

---

## Paso 8. Registrar una reseña

Una vez realizada la reserva, el usuario puede ingresar una reseña para la cancha.

---

## Paso 9. Registrar un mantenimiento

Crear un mantenimiento para una cancha indicando:

- Fecha
- Técnico
- Estado
- Descripción

---

## Paso 10. Crear una notificación

Registrar una notificación para un usuario.

---

# 5. Verificación de servicios

Antes de comenzar las pruebas se recomienda verificar que todos los microservicios se encuentren registrados en Eureka.

```
https://discovery-service-jp0a.onrender.com
```

Todos los servicios deben aparecer con estado **UP**.

---

# 6. Activación de Render

Si algún servicio se encuentra suspendido por inactividad, ejecutar el script incluido en el proyecto.

```
./wake-render.sh
```

El script activa automáticamente los servicios desplegados en Render.

---

# 7. Documentación de APIs

Toda la documentación se encuentra centralizada mediante Swagger OpenAPI.

Desde Swagger es posible:

- Consultar endpoints.
- Ejecutar pruebas.
- Revisar parámetros.
- Visualizar respuestas.
- Probar cada microservicio de manera independiente.

---

# 8. Consideraciones

Para que las operaciones funcionen correctamente es importante respetar las relaciones entre los microservicios.

Por ejemplo:

- Una cancha requiere un recinto existente.
- Un horario requiere una cancha existente.
- Una reserva requiere un usuario, una cancha y un horario.
- Un pago requiere una reserva.
- Una reseña requiere un usuario y una cancha.

---

# 9. Conclusión

El sistema permite gestionar el ciclo completo de una reserva deportiva mediante una arquitectura de microservicios, utilizando un punto único de acceso a través del API Gateway y documentación centralizada mediante Swagger.