import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import com.google.gson.Gson;

public class LuceneServiceImpl implements LuceneService{
	
    private static final Path indexPath = Paths.get("index");
    StandardAnalyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
    Gson gson = new Gson();
    
    
    //Reads txt file and updates index
    @Override
    public void updateIndex() {
        try (IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), config)) {
            String jsonData = Files.readString(Paths.get("encyclopediadata.json"));
            
            SearchResult[] dataArray = gson.fromJson(jsonData, SearchResult[].class);
            
            for (SearchResult searchResult : dataArray) {
                String id = searchResult.getUrl(); // Use URL as the document ID
                Term idTerm = new Term("id", id); // Term representing the document ID
                
                Document document = new Document();
                document.add(new StringField("id", id, Field.Store.YES)); // Store the document ID
                document.add(new TextField("canonical", searchResult.getUrl(), Field.Store.YES));
                document.add(new TextField("title", searchResult.getTitle(), Field.Store.YES));
                document.add(new TextField("description", searchResult.getContent(), Field.Store.YES));
                
                indexWriter.updateDocument(idTerm, document); // Update the document with the given ID
            }

            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




	//Returns documents corresponding to the query
    @Override
    public void searchIndex(String query) {
        List<SearchResult> searchResults = new ArrayList<>();

        // UPDATE INDEX
        updateIndex();

        try (DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(indexPath))) {
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            QueryBuilder queryBuilder = new QueryBuilder(analyzer);

            TopDocs topDocs = indexSearcher.search(queryBuilder.createPhraseQuery("description", query), 10);

            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document resultDoc = indexSearcher.doc(scoreDoc.doc);
                SearchResult searchResult = new SearchResult();
                searchResult.setUrl(resultDoc.get("canonical"));
                searchResult.setTitle(resultDoc.get("title"));
                searchResult.setContent(resultDoc.get("description").replace("\n", " ").replace("\"", "'"));

                searchResults.add(searchResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(searchResults.toString()); // Output to command line for node.js api to read
    }
}
