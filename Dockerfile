# Dockerfile commands
FROM node:14 as web-app-builder
WORKDIR /app/web-application
COPY web-application/package*.json ./
RUN npm install
COPY web-application/ ./
RUN npm run build

FROM maven:3.6 as lucene-builder
WORKDIR /app/lucene-module
COPY lucene-module/ ./
RUN mvn clean package

FROM node:14
RUN apt-get update && apt-get install -y default-jre

WORKDIR /app/middleware
COPY middleware/package*.json ./
RUN npm install
COPY middleware/ ./

COPY --from=web-app-builder /app/web-application/build /app/web-application/build
COPY --from=lucene-builder /app/lucene-module/target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar /app/lucene-module/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar
EXPOSE 5000

CMD java -jar /app/lucene-module/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar updateIndex & node index.js
