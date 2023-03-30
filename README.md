# immo


docker build : 

    docker build -t immo-studi .

docker run : 
    
    docker run -v /app/immo:/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi

    docker run -v /var/app/immo:/private/Users/minhbuu/photosimmo/ -p 8080:3333 -t immo-studi
    
    docker run -p 8080:3333 -t immo-studi   


