FROM openjdk:19-slim-buster
COPY "./target/backend-reactivo.jar" "app.jar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]