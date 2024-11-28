FROM eclipse-temurin:17-jdk-alpine
LABEL authors="tomasdarquier"

WORKDIR /app

COPY target/user-service-0.0.1-SNAPSHOT.jar app.jar

COPY wait-for-it.sh /usr/bin/wait-for-it.sh
RUN apk add --no-cache bash && chmod +x /usr/bin/wait-for-it.sh

ENTRYPOINT ["sh", "-c", "/usr/bin/wait-for-it.sh config:8888 -- java -jar app.jar"]
