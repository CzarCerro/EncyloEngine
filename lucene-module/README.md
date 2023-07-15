mvn clean package

Command to make a search
```
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex car
```

Command to get terms of a document
```
java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar getTermsOfDocument 5
```