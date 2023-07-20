import org.apache.lucene.util.BytesRef;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import java.nio.file.Paths;
import org.apache.lucene.document.Document;
import java.io.IOException;

public class UtilityServiceImpl implements UtilityService{

    @Override
    public void getTermsOfDocument(int id) {
        String stringIndexPath = "index";
        System.out.println("Reading index");
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(stringIndexPath)));
            Document document = reader.document(id);
            String description = document.get("description");

            if (description != null) {
                EnglishAnalyzer analyzer = new EnglishAnalyzer();
                TokenStream tokenStream = analyzer.tokenStream("", description);
    
                CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
                tokenStream.reset();
    
                while (tokenStream.incrementToken()) {
                    String term = termAttribute.toString();
                    System.out.println("Term: " + term);
                }
    
                tokenStream.end();
                tokenStream.close();
                analyzer.close();
            }
            
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}