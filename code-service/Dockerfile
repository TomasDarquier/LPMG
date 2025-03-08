FROM eclipse-temurin:17-jdk-alpine
LABEL authors="tomasdarquier"

WORKDIR /app

COPY wait-for-it.sh /usr/bin/wait-for-it.sh
COPY target/code-service-0.0.1-SNAPSHOT.jar app.jar

# Copiar los archivos de resources/dynamic-files al contenedor
RUN mkdir -p /app/dynamic-files
COPY src/main/resources/dynamic-files/ /app/dynamic-files/

RUN apk add --no-cache bash && chmod +x /usr/bin/wait-for-it.sh

ENTRYPOINT ["sh", "-c", "sleep 10 && /usr/bin/wait-for-it.sh config:8888 -- java -jar app.jar"]
