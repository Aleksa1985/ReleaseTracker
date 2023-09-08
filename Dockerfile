FROM maven:3.8.4 AS builder
WORKDIR /app
COPY . .
RUN mvn dependency:go-offline -B
RUN mvn clean package


FROM openjdk:17-alpine as runner
WORKDIR /app
COPY --from=builder /app/service/target/service-0.0.1-SNAPSHOT.jar .
COPY --from=builder /app/wait-for-db.sh .

RUN chmod +x wait-for-db.sh

EXPOSE 8080
CMD ["java", "-jar", "service-0.0.1-SNAPSHOT.jar"]