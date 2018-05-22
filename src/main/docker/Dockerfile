FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} cool.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cool.jar"]