#!/bin/bash

urls=(
  "https://discovery-service-jp0a.onrender.com/"
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
  "https://api-gateway-q3iu.onrender.com/swagger-ui/index.html"
)

echo "Activando servicios de Render..."

for url in "${urls[@]}"; do
  (
    for intento in 1 2 3; do
      http_code=$(curl -L --max-time 300 -s -o /dev/null -w "%{http_code}" "$url")

      if [[ "$http_code" == "200" || "$http_code" == "401" || "$http_code" == "403" ]]; then
        echo "$http_code - ACTIVO - $url"
        break
      fi

      echo "$http_code - intento $intento - $url"
      sleep 20
    done
  ) &
done

wait

echo "Proceso terminado."
echo "Revisa Eureka y Swagger."
