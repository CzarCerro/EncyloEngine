# EncyloEngine

## Overall startup pipeline
Run the following commands to setup and run the entire system.
```
cd web-application
npm install
npm run build
cd ..
cd middleware
npm install
node index.js
```
Next, access localhost:5000 to interact with the webpage

## Testing Lucene
Replace {SEARCH-TERM} with your search query. Example: java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex car
```
cd lucene-module
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex {SEARCH-TERM}
```