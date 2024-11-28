FROM eclipse-temurin:17-jdk-alpine
LABEL authors="tomasdarquier"

WORKDIR /app

COPY target/discovery-service-0.0.1-SNAPSHOT.jar app.jar

# Instalar bash, copiar y preparar el script wait-for-it en un solo paso
COPY wait-for-it.sh /usr/bin/wait-for-it.sh
RUN apk add --no-cache bash && chmod +x /usr/bin/wait-for-it.sh

# Definir el punto de entrada
ENTRYPOINT ["sh", "-c", "/usr/bin/wait-for-it.sh config:8888 -- java -jar app.jar"]

