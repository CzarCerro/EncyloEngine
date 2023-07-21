public interface LuceneService {
	void updateIndex();
	void searchIndex(String searchType, String query, boolean wordnetEnabled);
}
