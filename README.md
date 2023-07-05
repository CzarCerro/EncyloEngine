# EncyloEngine

## Running the web application
```
cd web-application
npm install
npm start
```
## Testing Lucene
Replace {SEARCH-TERM} with your search query. Example: java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex car
```
cd lucene-module
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex {SEARCH-TERM}
```
## Overall startup pipeline
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
