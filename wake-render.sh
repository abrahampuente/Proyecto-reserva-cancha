#!/bin/bash

EUREKA_URL="https://discovery-service-jp0a.onrender.com/"

services=(
  "https://user-service-1f5s.onrender.com/v3/api-docs"
  "https://recinto-service.onrender.com/v3/api-docs"
  "https://cancha-service.onrender.com/v3/api-docs"
  "https://horario-service.onrender.com/v3/api-docs"
  "https://reserva-service-hzej.onrender.com/v3/api-docs"
  "https://precio-service.onrender.com/v3/api-docs"
  "https://pago-service-d1je.onrender.com/v3/api-docs"
  "https://notificacion-service-nyuo.onrender.com/v3/api-docs"
  "https://resena-service-p65a.onrender.com/v3/api-docs"
  "https://mantenimiento-service-s2qp.onrender.com/v3/api-docs"
)

GATEWAY_URL="https://api-gateway-q3iu.onrender.com/swagger-ui/index.html"

echo "1. Despertando Eureka..."

for intento in 1 2 3 4 5; do
  codigo=$(curl -L --max-time 300 -s -o /dev/null -w "%{http_code}" "$EUREKA_URL")
  echo "Eureka intento $intento: $codigo"

  if [ "$codigo" = "200" ]; then
    echo "Eureka está activo."
    break
  fi

  sleep 20
done

echo "Esperando 30 segundos para que Eureka termine de iniciar..."
sleep 30

echo "2. Despertando microservicios..."

for url in "${services[@]}"; do
  (
    for intento in 1 2 3; do
      codigo=$(curl -L --max-time 300 -s -o /dev/null -w "%{http_code}" "$url")

      if [ "$codigo" = "200" ] || [ "$codigo" = "401" ] || [ "$codigo" = "403" ]; then
        echo "$codigo - ACTIVO - $url"
        break
      fi

      echo "$codigo - intento $intento - $url"
      sleep 20
    done
  ) &
done

wait

echo "Esperando 45 segundos para que los servicios se registren en Eureka..."
sleep 45

echo "3. Despertando API Gateway..."

codigo=$(curl -L --max-time 300 -s -o /dev/null -w "%{http_code}" "$GATEWAY_URL")
echo "$codigo - $GATEWAY_URL"

echo "Proceso terminado."
echo "Revisa Eureka:"
echo "$EUREKA_URL"