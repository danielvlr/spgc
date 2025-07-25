FROM maven:3.9.6-eclipse-temurin-21-alpine AS mvn-build
WORKDIR /app/build
COPY . .
RUN mvn -e -X install -Dmaven.test.skip=true

FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

RUN chmod 777 /workspace

EXPOSE 8080
COPY --from=mvn-build /app/build/target/*.jar app.jar
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
