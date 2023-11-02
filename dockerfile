# FROM openjdk:17
# WORKDIR /app
# CMD ["./gradlew", "clean", "bootJar"]
# COPY build/libs/*.jar app.jar

# EXPOSE 8081
# ENTRYPOINT ["java", "-jar", "/app.jar"]


# FROM gradle:jdk17-graal-jammy AS build
# COPY --chown=gradle:gradle . /home/gradle/src
# WORKDIR /home/gradle/src
# RUN gradle build --no-daemon 

# FROM openjdk:17
# EXPOSE 8081
# RUN mkdir /app
# COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]


#Build stage
FROM gradle:latest AS BUILD
WORKDIR /usr/app/
COPY . . 
RUN gradle build -x test

# Package stage
FROM openjdk:latest
ENV JAR_NAME=ie-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME  