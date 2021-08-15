FROM openjdk:11

LABEL maintainer="anushka.agarwal@nagarro.com"

ADD target/jenkins.assignment-0.0.1-SNAPSHOT.jar jenkins.assignment-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar","jenkins.assignment-0.0.1-SNAPSHOT.jar"]
