FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD boot-1.0.0.jar cool.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/cool.jar"]