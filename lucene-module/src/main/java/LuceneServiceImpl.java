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

public class LuceneServiceImpl implements LuceneService{
	
    Path indexPath = Paths.get("index");
    StandardAnalyzer analyzer = new StandardAnalyzer();
    IndexWriterConfig config = new IndexWriterConfig(analyzer);
	
	@Override
	public void updateIndex() {

        try (IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), config)) {
            Document document = new Document();
            document.add(new TextField("content", "This is the sample document content", Field.Store.YES));
            indexWriter.addDocument(document);
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void searchIndex() {
        // Searching the index
		String testQuery="sample document";
		
        try (DirectoryReader directoryReader = DirectoryReader.open(FSDirectory.open(indexPath))) {
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            QueryBuilder queryBuilder = new QueryBuilder(analyzer);
            Query query = queryBuilder.createPhraseQuery("content", testQuery);
            TopDocs topDocs = indexSearcher.search(query, 10);

            System.out.println("Search Results:");
            Arrays.stream(topDocs.scoreDocs)
                    .map(hit -> {
                        try {
                            return indexSearcher.doc(hit.doc);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .map(resultDoc -> {
                        return resultDoc.get("content");
                    })
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void deleteDocument() {
		// TODO Auto-generated method stub
	}
}
