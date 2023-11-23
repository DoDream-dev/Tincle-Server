FROM openjdk:17-jdk
ENV APP_HOME=/home/ubuntu/Tinqle-Server
WORKDIR $APP_HOME
COPY build/libs/*.jar tinqle-server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=dev","tinqle-server.jar"]
