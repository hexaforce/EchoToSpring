FROM openjdk:8-jdk-slim-buster

RUN cp -p /usr/share/zoneinfo/Japan /etc/localtime
ENV JAVA_OPTS=-Duser.timezone=Asia/Tokyo

COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-Duser.timezone=JST", "-jar", "app.jar"]
