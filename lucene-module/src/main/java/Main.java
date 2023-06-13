import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
    	
    	LuceneService luceneService = new LuceneServiceImpl();
    	
        if (args.length > 0) {
            String functionName = args[0];
            switch (functionName) {
                case "updateIndex":
                    luceneService.updateIndex();
                    break;
                case "searchIndex":
                	String query = args[1];
                	if (args[1] != null) {
                		luceneService.searchIndex(args[1]);
                	}
                    break;
                case "deleteDocument":
                    luceneService.deleteDocument();
                    break;
                default:
                    System.out.println("Invalid function name: " + functionName);
            }
        } else {
            System.out.println("No function name provided.");
        }
    }
	
}
