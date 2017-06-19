FROM maven:3-alpine

# Creating Application Source Code Directory
RUN mkdir -p /usr/src/app

# Setting Home Directory for containers
WORKDIR /usr/src/app

# Copying src code to Container
COPY . /usr/src/app

# Building From Source Code
RUN mvn clean package

# Setting Persistent drive
VOLUME ["/kotlin-data"]

# Exposing Port
EXPOSE 4567

ENTRYPOINT java -jar ./target/MDMTool-1.0-SNAPSHOT-jar-with-dependencies.jar 

