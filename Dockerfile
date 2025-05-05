FROM openjdk:17-jdk-slim

LABEL version="1.0" description="Eatza Customer Service"

COPY ./target/customer-service.jar customer-service.jar

# Below command does the same work as COPY
# Apart from this it can extract files from a tar file as well when provided
# ADD ./target/customer-service.jar customer-service.jar

# Keep default port, the same can be altered when executing the image
EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "customer-service.jar" ]
CMD ["-Dspring.profile.active=local"]