FROM azul/zulu-openjdk-alpine:17.0.5 AS builder
USER root

ENV LANG en_US.UTF-8
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF-8

WORKDIR /src

COPY . .

RUN ./gradlew bootJar

FROM azul/zulu-openjdk-alpine:17.0.5-jre

ENV LANG en_US.UTF-8
ENV JAVA_TOOL_OPTIONS -Dfile.encoding=UTF-8

WORKDIR /app

COPY --from=builder /src/build/libs/pos-1.0.0.jar ./pos-1.0.0.jar

EXPOSE 8080

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

ENTRYPOINT ["java", "-jar", "/app/pos-1.0.0.jar"]