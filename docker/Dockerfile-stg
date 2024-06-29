FROM amazoncorretto:11-alpine-jdk
EXPOSE 8080
COPY ./build/libs/Qtudy-server-0.0.1-SNAPSHOT.jar /app.jar
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=stage"]
