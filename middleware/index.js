const express = require('express');
const path = require("path");
const { exec } = require('child_process');
const app = express();
const port = 5000;

app.use(express.static(path.join(__dirname, '../web-application/build')));

const luceneLocation = './lucene-module';



/**
 * GET /search
 * Search for results using the Lucene search engine.
 *
 * Query Parameters:
 * - query (required): The search query string.
 *
 * Response:
 * - 200: The search was successful. Returns an array containing the search results.
 * - 500: An error occurred during search.
 */
app.get('/search', (req, res) => {
  const query = req.query.query;

  //TODO: Additional logic to clean query

  exec(`cd .. && cd ${luceneLocation} && java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex ${query}`, (error, stdout) => {
    if (error) {
      console.error(`Error executing command: ${error.message}`);
      return res.status(500).send('An error occurred during search.');
    }

    const searchResults = stdout.trim();
    res.status(200).send(searchResults);
  });
});

app.use((req, res, next) => {
  res.sendFile(path.join(__dirname, "..", "my-app", "build", "index.html"));
});

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});
