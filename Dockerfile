FROM maven:3.9.6-eclipse-temurin-23-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests -e -X

FROM eclipse-temurin:23-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080
CMD ["java", "-jar", "app.jar"] 