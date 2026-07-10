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
  "https://api-gateway-q3iu.onrender.com/swagger-ui/index.html"
)

echo "Activando servicios de Render..."

for url in "${urls[@]}"; do
  (
    http_code=$(curl -L --max-time 300 -s -o /dev/null -w "%{http_code}" "$url")
    echo "$http_code - $url"
  ) &
done

wait

echo "Solicitudes terminadas."
echo "Espera unos minutos y revisa Eureka."
