FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

#Этот файл описывает, как Docker должен запускать наше приложение
#Spring Boot . Он использует Java 11 от  AdoptOpenJDK
#и копирует JAR-файл приложения в application.jar .
#Затем он запускает этот JAR-файл, чтобы запустить
#наше приложение Spring Boot.