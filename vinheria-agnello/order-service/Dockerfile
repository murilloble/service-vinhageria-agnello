FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /work
COPY pom.xml .
COPY order-service/pom.xml ./order-service/pom.xml
COPY order-service/src ./order-service/src
RUN mvn -f order-service/pom.xml -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /work/order-service/target/order-service-1.0.0.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 3002
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"] 
