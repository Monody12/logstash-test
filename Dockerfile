FROM openjdk:22-jdk
WORKDIR "/app"
ADD "build/libs/logstash-test-0.0.1-SNAPSHOT.jar" "app.jar"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Duser.timezone=GMT+08", "app.jar"]
