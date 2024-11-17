#!/bin/bash

# Lista de directorios donde se encuentran los microservicios
services=("config-server" "discovery-service" "gateway" "user-service" "request-service" "init-service" "generation-service" "code-service" "download-service")

# Iterar sobre cada servicio y ejecutar el comando en su directorio correspondiente
for service in "${services[@]}"; do
  echo "Iniciando el servicio $service..."
  cd $service || {
    echo "No se pudo acceder al directorio $service"
    exit 1
  }

  # Ejecutar el comando de Maven para iniciar el servicio en segundo plano y redirigir la salida a /dev/null
  ./mvnw spring-boot:run >/dev/null 2>&1 &

  # Volver al directorio anterior
  cd - || exit

  # Esperar 2 segundos antes de iniciar el siguiente servicio
  sleep 3
done

# Esperar a que todos los servicios se inicien
wait
echo "Todos los servicios se están ejecutando."
