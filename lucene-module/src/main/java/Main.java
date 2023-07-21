public class Main {

    public static void main(String[] args) {
    	
    	
    	LuceneService luceneService = new LuceneServiceImpl();
        UtilityService utilityService = new UtilityServiceImpl();
    	
        if (args.length > 0) {
            String functionName = args[0];
            try {
                switch (functionName) {
                    case "updateIndex":
                        luceneService.updateIndex();
                        break;
                    case "searchIndex":
                        if (args.length < 4) {
                            throw new IllegalArgumentException("Insufficient arguments for searchIndex function.");
                        }
                        String searchType = args[1];
                        String query = args[2];
                        boolean wordnetEnabled = parseBooleanArgument(args[3]);
                        luceneService.searchIndex(searchType, query, wordnetEnabled);
                        break;
                    case "getTermsOfDocument":
                        if (args.length < 2) {
                            throw new IllegalArgumentException("Insufficient arguments for getTermsOfDocument function.");
                        }
                        int id = Integer.parseInt(args[1]);
                        utilityService.getTermsOfDocument(id);
                        break;
                    default:
                        System.out.println("Invalid function name: " + functionName);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid numeric argument. Please provide a valid integer value.");
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error: An unexpected error occurred.");
                e.printStackTrace();
            }
        } else {
            System.out.println("No function name provided.");
        }
    }

    private static boolean parseBooleanArgument(String arg) {
        try {
            return Boolean.parseBoolean(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid argument for boolean value: " + arg);
        }
    }
}
