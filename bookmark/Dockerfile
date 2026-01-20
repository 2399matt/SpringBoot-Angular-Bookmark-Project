FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-ea
COPY --from=build /app/target/bookmark.jar /bookmark.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "bookmark.jar"]
