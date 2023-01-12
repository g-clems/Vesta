FROM gradle:7.6.0-jdk17 as builder

WORKDIR /build
COPY ./ /build

RUN gradle shadowjar

FROM amazoncorretto:17-alpine

WORKDIR /home/Vesta
COPY --from=builder /build/build/libs/*.jar /Vesta.jar

ENTRYPOINT ["java","-jar","/Vesta.jar"]