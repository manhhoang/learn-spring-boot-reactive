FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD gs-spring-boot-docker-0.1.0.jar spring-boot-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /spring-boot-0.0.1-SNAPSHOT.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/spring-boot-0.0.1-SNAPSHOT.jar"]