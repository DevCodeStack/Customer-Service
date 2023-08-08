FROM openjdk:8

COPY ./target/Customer-Service.jar customerservice.jar

EXPOSE 8082

CMD ["java","-jar","-Dspring.profile.active=local","customerservice.jar"]