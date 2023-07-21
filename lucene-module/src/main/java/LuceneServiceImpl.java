import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.synonym.SynonymGraphFilterFactory;
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
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import com.google.gson.Gson;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;

public class LuceneServiceImpl implements LuceneService{
	
    private static final Path indexPath = Paths.get("index");

    Gson gson = new Gson();
    
    private final int RESULT_LIMIT = 50;
    
    //Reads txt file and updates index
    @Override
    public void updateIndex() {
        try (IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), setConfig())) {
            String jsonData = Files.readString(Paths.get("encyclopediadata.json"));
            
            SearchResult[] dataArray = gson.fromJson(jsonData, SearchResult[].class);

            indexWriter.deleteAll();
            
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
    public void searchIndex(String searchType, String query, boolean wordnetEnabled) {
        String[] queryWords = query.split("\\+");
    
        boolean multiSearch = false;

        Set<String> existingIDSet = new HashSet<>();

        List<SearchResult> searchResults = new ArrayList<>();
        List<SearchResult> prioritizedSearchResults = new ArrayList<>();
        List<SearchResult> descriptionSearchResults = new ArrayList<>();
    
        try (DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(indexPath))) {
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            
            Analyzer queryAnalyzer = customAnalyzer(wordnetEnabled);
            QueryBuilder queryBuilder = new QueryBuilder(queryAnalyzer); //Synonym Analyzer
            
            //QueryBuilder queryBuilder = new QueryBuilder(analyzer); //Default Analyzer
    
            BooleanQuery.Builder prioritizedQueryBuilder = new BooleanQuery.Builder();
            BooleanQuery.Builder descriptionQueryBuilder = new BooleanQuery.Builder();
    
            for (String word : queryWords) {
                try {
                    if (searchType.equals("all")) {
                        multiSearch = true;
                        Query titleQuery = queryBuilder.createPhraseQuery("title", word);
                        Query descriptionQuery = queryBuilder.createPhraseQuery("description", word);
                        prioritizedQueryBuilder.add(titleQuery, BooleanClause.Occur.SHOULD);
                        descriptionQueryBuilder.add(descriptionQuery, BooleanClause.Occur.SHOULD);
                    } else {
                        Query termQuery = queryBuilder.createPhraseQuery(searchType, word);
                        prioritizedQueryBuilder.add(termQuery, BooleanClause.Occur.SHOULD);
                    }
                } catch (NullPointerException e) {
                    // Handle null pointer exceptions
                }
            }
    
            // Build separate boolean queries for 'title' and 'description'
            Query titleQuery = prioritizedQueryBuilder.build();
            Query descriptionQuery = descriptionQueryBuilder.build();
    
            // Search for 'title' query first and get the results
            TopDocs topTitleDocs = indexSearcher.search(titleQuery, RESULT_LIMIT);
            int remainingResultCount = RESULT_LIMIT;
    
            // Process the results for 'title' search
            for (ScoreDoc scoreDoc : topTitleDocs.scoreDocs) {
                Document resultDoc = indexSearcher.doc(scoreDoc.doc);
                SearchResult searchResult = new SearchResult();
                searchResult.setUrl(resultDoc.get("canonical"));
                searchResult.setTitle(resultDoc.get("title").replace("\n", " ").replace("\"", "'"));
                searchResult.setContent(resultDoc.get("description").replace("\n", " ").replace("\"", "'"));
                searchResult.setRelevanceScore(scoreDoc.score);
                
                existingIDSet.add(resultDoc.get("id"));

                searchResults.add(searchResult);
                remainingResultCount--;
            }
    
            // Check if more results are needed from 'description' search
            if (remainingResultCount > 0 && multiSearch) {
                // Search for 'description' query to get the remaining results

                TopDocs topDescriptionDocs = indexSearcher.search(descriptionQuery, Integer.MAX_VALUE);

                // Process the results for 'description' search
                for (ScoreDoc scoreDoc : topDescriptionDocs.scoreDocs) {
                    if (remainingResultCount == 0) {
                        break;
                    }
                        Document resultDoc = indexSearcher.doc(scoreDoc.doc);

                        if(!existingIDSet.contains(resultDoc.get("id"))) { //Check if description ID already exists in existingIDSet

                            SearchResult searchResult = new SearchResult();
                            searchResult.setUrl(resultDoc.get("canonical"));
                            searchResult.setTitle(resultDoc.get("title").replace("\n", " ").replace("\"", "'"));
                            searchResult.setContent(resultDoc.get("description").replace("\n", " ").replace("\"", "'"));
                            searchResult.setRelevanceScore(scoreDoc.score);
                            searchResults.add(searchResult);

                            remainingResultCount--;
                    }
                }
            }
    
            System.out.println(searchResults.toString()); // Output to command line for node.js API to read
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //Set and return indexWriter configuration 
    private static IndexWriterConfig setConfig() throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(customAnalyzer(false));
        return config;
    }

    // Create CustomAnalyzer for Query Expansion with Synonyms
    private static CustomAnalyzer customAnalyzer(boolean wordnetEnabled) throws IOException {
        CustomAnalyzer.Builder builder = CustomAnalyzer.builder()
                .withTokenizer(StandardTokenizerFactory.class)
                .addTokenFilter(EnglishPossessiveFilterFactory.class)
                .addTokenFilter(LowerCaseFilterFactory.class)
                .addTokenFilter(StopFilterFactory.class)
                .addTokenFilter(PorterStemFilterFactory.class);
    
        if (wordnetEnabled) {
            Map<String, String> sargs = new HashMap<>();
            sargs.put("synonyms", "wn_s.pl");
            sargs.put("format", "wordnet");
            builder.addTokenFilter(SynonymGraphFilterFactory.class, sargs);
        }
    
        return builder.build();
    }
    
}
