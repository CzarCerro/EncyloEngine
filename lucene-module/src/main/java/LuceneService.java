public interface LuceneService {
	void updateIndex();
	void searchIndex(String queryType, String query);
}
