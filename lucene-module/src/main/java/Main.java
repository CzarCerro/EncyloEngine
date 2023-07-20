public class Main {

    public static void main(String[] args) {
    	
    	LuceneService luceneService = new LuceneServiceImpl();
        UtilityService utilityService = new UtilityServiceImpl();
    	
        if (args.length > 0) {
            String functionName = args[0];
            switch (functionName) {
                case "updateIndex":
                    luceneService.updateIndex();
                    break;
                case "searchIndex":
                    String searchType = args[1];
                	String query = args[2];
                	if (searchType != null && query != null) {
                		luceneService.searchIndex(searchType, query);
                	}
                    break;
                case "getTermsOfDocument":
                    int id = Integer.parseInt(args[1]);
                    utilityService.getTermsOfDocument(id);
                    break;
                default:
                    System.out.println("Invalid function name: " + functionName);
            }
        } else {
            System.out.println("No function name provided.");
        }
    }
	
}
