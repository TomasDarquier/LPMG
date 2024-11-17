#!/bin/bash

# Buscar los procesos de Spring Boot y matarlos
pids=$(ps aux | grep 'spring-boot:run' | grep -v grep | awk '{print $2}')

if [ -z "$pids" ]; then
  echo "No se encontraron procesos de Spring Boot en ejecución."
else
  echo "Deteniendo los servicios con los siguientes PIDs: $pids"
  kill $pids
fi
