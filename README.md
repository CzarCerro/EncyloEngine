# EncyloEngine

## Overall startup pipeline
Run the following commands to setup and run the entire system.
```
cd web-application
npm install
npm run build
cd ..
cd lucene-module
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar updateIndex
mvn clean package
cd ..
cd middleware
npm install
node index.js
```
Next, access localhost:5000 to interact with the webpage

## Testing Lucene
Go to lucene-module directory
```
cd lucene-module
```

Replace {SEARCH-TERM} with your search query. 
```
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex all {SEARCH-TERM} false
```

Command to compile
```
mvn clean package
```

Command to update index
```
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar updateIndex
```

Command to get terms of a document
```
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar getTermsOfDocument 5
```