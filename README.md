Real Estate Application developed in Java Springboot, PostgreSQL and Thymeleaf Bootstrap

# immo
commande pour création target:
    ./mvnw package

docker build : 

    docker build --platform linux/amd64 -t immostudi .
    docker build . -t immostudi   

docker run : 
    
    docker run -v /app/immo:/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi

    docker run -v /var/app/immo:/private/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi
    
    docker run -p 4444:4444 -t immostudi  


dockerfile:
    
    FROM eclipse-temurin:17-jdk-focal
 
    WORKDIR /app

    COPY .mvn/ .mvn
    COPY mvnw pom.xml ./
    RUN ./mvnw dependency:go-offline

    COPY src ./src

    CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.profiles=docker"]

    EXPOSE 4444

