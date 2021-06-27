FROM openjdk:11.0.7-jre-slim

WORKDIR /user/app

ADD target/grpc-intro-1.0-SNAPSHOT.jar app.jar

EXPOSE 6565

ENTRYPOINT java -jar app.jar