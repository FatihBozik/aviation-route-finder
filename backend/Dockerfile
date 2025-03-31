FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR application
COPY . ./
RUN ./gradlew clean build
RUN cp ./build/libs/sbm-query-ws-0.0.0.jar application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM eclipse-temurin:21-jdk-alpine
WORKDIR application
MAINTAINER bozikfatih@gmail.com

VOLUME /tmp

ENV TZ=Europe/Istanbul

RUN apk add --no-cache tini \
  && apk add --no-cache tzdata \
  && cp /usr/share/zoneinfo/${TZ} /etc/localtime \
  && echo "${TZ}" > /etc/timezone \
  && addgroup -S appgroup && adduser -S appuser -G appgroup

USER appuser
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["/sbin/tini", "--", "sh", "-c", "exec java -Dlog4j2.disable.jmx=true org.springframework.boot.loader.JarLauncher"]
