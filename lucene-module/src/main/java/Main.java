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
                	if (query != null) {
                		luceneService.searchIndex(query);
                	}
                    break;
                default:
                    System.out.println("Invalid function name: " + functionName);
            }
        } else {
            System.out.println("No function name provided.");
        }
    }
	
}
