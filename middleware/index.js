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
  const queryType = req.query.queryType.toLowerCase();

  let cleanedQuery = query.replace(/[^a-zA-Z0-9\s]/g, '');
  console.log("Search " + queryType + " with search term: " + cleanedQuery);
  cleanedQuery = cleanedQuery.toLowerCase();
  cleanedQuery = cleanedQuery.replace(/\s+/g, '+');
  const childProcess = exec(`cd .. && cd ${luceneLocation} && java -jar target/lucene-module-0.0.1-SNAPSHOT-jar-with-dependencies.jar searchIndex ${queryType} ${cleanedQuery}`, { encoding: 'latin1' });

  let stdout = '';

  childProcess.stdout.on('data', (data) => {
    stdout += data;
  });

  childProcess.stderr.on('data', (data) => {
    console.error(`Error executing command: ${data}`);
  });

  childProcess.on('close', (code) => {
    if (code !== 0) {
      return res.status(500).send('An error occurred during search.');
    }

    const parsedResults = JSON.parse(stdout);
    res.status(200).json(parsedResults);
  });
});

app.use((req, res, next) => {
  res.sendFile(path.join(__dirname, "..", "web-application", "build", "index.html"));
});

app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});
