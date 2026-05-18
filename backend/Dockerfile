FROM maven:3.9.11-eclipse-temurin-25 AS build

WORKDIR /workspace

COPY --from=ai_sql_starter . /workspace/libs/ai-sql-spring-boot-starter
COPY . /workspace/backend

RUN mvn -f /workspace/libs/ai-sql-spring-boot-starter/pom.xml -DskipTests install

RUN mvn -f /workspace/backend/pom.xml -DskipTests package

FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /workspace/backend/target/ai-sql-demo-0.1.0.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=container

EXPOSE 4001

ENTRYPOINT ["java", "--enable-preview", "-jar", "/app/app.jar"]
