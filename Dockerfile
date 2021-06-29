FROM maven:3-adoptopenjdk-11

COPY . /user/app

WORKDIR /user/app

RUN mvn clean

RUN mvn compile

RUN mvn package

CMD ["pwd"]

ENTRYPOINT java -jar target/prpc-snakes-and-ladders-1.0-SNAPSHOT.jar

EXPOSE 6565

