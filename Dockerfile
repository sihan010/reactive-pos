FROM azul/zulu-openjdk-alpine:17.0.5-jre AS builder
WORKDIR /src
COPY . .
RUN ./gradlew bootJar

FROM azul/zulu-openjdk-alpine:17.0.5-jre
WORKDIR /app
COPY --from=builder /src/build/libs/pos-1.0.0.jar ./pos-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/pos-1.0.0.jar"]