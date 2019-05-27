FROM openjdk:8
EXPOSE 8080
ADD ./target/etf-qr-service-1.0.jar ./etf-qr-service-1.0.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+0530","etf-qr-service-1.0.jar"]	