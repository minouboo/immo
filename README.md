# immo


docker build : 

    docker build -t immo-studi .

docker run : 
    
    docker run -v /app/immo:/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi

    docker run -v /var/app/immo:/private/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi
    
    docker run -p 8080:3333 -t immo-studi   


dockerfile:
    
    FROM eclipse-temurin:17-jdk-focal
 
    WORKDIR /app

    COPY .mvn/ .mvn
    COPY mvnw pom.xml ./
    RUN ./mvnw dependency:go-offline

    COPY src ./src

    ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/immo.jar"]

    EXPOSE 3333

