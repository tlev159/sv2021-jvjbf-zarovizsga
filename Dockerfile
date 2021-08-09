FROM adoptopenjdk:16-jre-hotspot as builder
RUN mkdir /application
WORKDIR application
COPY target/sv2021-jvjbf-zarovizsga-0.0.1-SNAPSHOT.jar sv2021-jvjbf-zarovizsga.jar
RUN java -Djarmode=layertools -jar sv2021-jvjbf-zarovizsga.jar extract

FROM adoptopenjdk:16-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", \"org.springframework.boot.loader.JarLauncher"]