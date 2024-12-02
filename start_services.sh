#!/bin/bash

# Lista de directorios donde se encuentran los microservicios
services=("discovery-service" "gateway" "user-service" "request-service" "init-service" "generation-service" "code-service" "download-service")

# iniciar el config-server sin perfil
echo "Iniciando el config-server..."
cd config-server
./mvnw spring-boot:run >/dev/null 2>&1 &
cd - || exit
sleep 3

# Iterar sobre cada servicio y ejecutar el comando en su directorio correspondiente
for service in "${services[@]}"; do
  echo "Iniciando el servicio $service con perfil 'local'..."
  cd $service || {
    echo "No se pudo acceder al directorio $service"
    exit 1
  }

  # Ejecutar el comando de Maven para iniciar el servicio en segundo plano y redirigir la salida a /dev/null
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=local >/dev/null 2>&1 &

  # Volver al directorio anterior
  cd - || exit

  # Esperar 2 segundos antes de iniciar el siguiente servicio
  sleep 3
done

# Esperar a que todos los servicios se inicien
wait
echo "Todos los servicios se están ejecutando."
