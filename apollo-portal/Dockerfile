FROM openjdk:8-jre-alpine
LABEL maintainer="qct <tango.qct@gmail.com>"

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} apollo-portal.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/apollo-portal.jar"]
