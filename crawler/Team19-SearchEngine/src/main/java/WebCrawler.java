import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebCrawler{

    private static final String CRAWLED_FOLDER_NAME = "crawledpages";
    private HashSet<String> Hashedurls;
    
    //Base URL to be crawled from
    private static final String SEED_URL = "https://www.encyclopedia.com";
    
    //Set Seed Frontier Queue for links related to the keywords
    private static final String[] seedUrls = {
    	      "https://www.encyclopedia.com/social-sciences/encyclopedias-almanacs-transcripts-and-maps/computers",
    	      "https://www.encyclopedia.com/places/britain-ireland-france-and-low-countries/british-and-irish-political-geography/glasgow",
    	      "https://www.encyclopedia.com/humanities/dictionaries-thesauruses-pictures-and-press-releases/united",
    	      "https://www.encyclopedia.com/science-and-technology/biology-and-genetics/biology-general/kingdom",
    	      "https://www.encyclopedia.com/literature-and-arts/journalism-and-publishing/libraries-books-and-printing/library",
    	      "https://www.encyclopedia.com/earth-and-environment/atmosphere-and-weather/weather-and-climate-terms-and-concepts/fog",
    	      "https://www.encyclopedia.com/social-sciences/applied-and-social-sciences-magazines/empires",
    	      "https://www.encyclopedia.com/science/news-wires-white-papers-and-books/doctor-specialist",
    	      "https://www.encyclopedia.com/science/encyclopedias-almanacs-transcripts-and-maps/hospital-modern-history",
    	      "https://www.encyclopedia.com/social-sciences-and-law/sociology-and-social-reform/sociology-general-terms-and-concepts/bachelor",
    	      "https://www.encyclopedia.com/science-and-technology/computers-and-electrical-engineering/computers-and-computing/degree",
    	      "https://www.encyclopedia.com/media/encyclopedias-almanacs-transcripts-and-maps/internet",
    	      "https://www.encyclopedia.com/humanities/dictionaries-thesauruses-pictures-and-press-releases/things",
    	      "https://www.encyclopedia.com/social-sciences-and-law/law/law/information",
    	      "https://www.encyclopedia.com/humanities/dictionaries-thesauruses-pictures-and-press-releases/info-0",
    	      "https://www.encyclopedia.com/psychology/encyclopedias-almanacs-transcripts-and-maps/retrieval-processes-memory",
    	      "https://www.encyclopedia.com/retrieve",
    	      "https://www.encyclopedia.com/science-and-technology/astronomy-and-space-exploration/astronomy-general/universe",
    	      "https://www.encyclopedia.com/history/dictionaries-thesauruses-pictures-and-press-releases/university-overview"
    	    };
    private static final int THREAD_SIZE = 100;
    private final ExecutorService executorService;
    private static Object lock = new Object();
    private static Set<CrawledPage> crawledPages;
	final static String crawledjson = "encyclopediadata.json";
	private static BufferedWriter writer;
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static ObjectMapper mapper = new ObjectMapper();
	
    // Initialize Web Crawler Class
    public WebCrawler() {
    	System.gc();
    	executorService = Executors.newFixedThreadPool(THREAD_SIZE);
    	Hashedurls = new HashSet<String>();
    	crawledPages = new HashSet<>();
    	lock = new Object();
    }
    
    //Function that Initializes the crawling process
    public void startCrawling(String[] seedUrls) {
    	try {
    		
    		//Create File to store data
    		createOutputFileIfNotExists(crawledjson);
    		
    		//Begin Crawling in the URL Queue
    		for (String URL: seedUrls) {
    			getPageLinks(URL, 0);
    		    }

    		executorService.awaitTermination(10, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    //Retrieves data and subsequent URLs to be crawled from pages
    public synchronized void getPageLinks(String URL, int depth){
        
    	//If the URL has already been crawled, exit the function
        if (Hashedurls.contains(URL)) {
        	return;
        }
        	//Initialize Multi-threaded Crawling
        	long executorServiceId = Thread.currentThread().getId();
            System.out.println("Thread #" + executorServiceId + " " + "Depth: " + depth + " - " + URL);
            
            Hashedurls.add(URL);
            executorService.submit(() -> {
            try {
            	//Get data on current page
                Document document = Jsoup.connect(URL).get();
                String fileName = CRAWLED_FOLDER_NAME + "/" + URL.replaceAll("[^a-zA-Z0-9]", "") + ".html"; // Strips special characters
                
                //Add Logic to check if document.text contains keywords
                storeURLdata(fileName, document);                
                Elements pageURLs = document.select("a[href]");
                
                //Recursive Crawling the other page URLs
                for (Element page : pageURLs) {
                    String pageURL = page.attr("abs:href");
                    if (!(pageURL.startsWith(SEED_URL))) continue;
                    
                    getPageLinks(page.attr("abs:href"), depth+1); //Recursive crawl
                }
            }   
            catch (Exception e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
         });
        }

    //Write the Crawled Data into the JSON file
    public synchronized static void storeURLdata(String fileName, Document document) throws Exception {
        
        String canonical = document.select("link[rel=canonical]").attr("href");
        String title = document.title();
        String description = document.select("meta[name=description]").attr("content");
        
        CrawledPage crawledPage = new CrawledPage(canonical, title, description);
        
        //Append JSON to the output file
        writer = Files.newBufferedWriter(Path.of(crawledjson), StandardOpenOption.WRITE);
        
        //Ensures that only 1 thread can access the file to write data at a time
        synchronized (lock) {
        	
            String jsonContent = Files.readString(Path.of(crawledjson));
            JSONArray jsonArray = new JSONArray(jsonContent);
            JSONObject crawledItem = new JSONObject();
            crawledItem.put("title", title);
            crawledItem.put("description", description);
            crawledItem.put("canonical", canonical);
            jsonArray.put(crawledItem);
            
            String updatedJsonContent = jsonArray.toString();
            
            Object json = mapper.readValue(updatedJsonContent, Object.class);
            String prettyprintjson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); 
            
            writer.write(prettyprintjson);
            writer.close();
            }
        }
    
    //Checks if a JSON file exists to be created
    private void createOutputFileIfNotExists(String outputFilePath) throws IOException {
        Path path = Path.of(outputFilePath);
        if (!Files.exists(path)) {
            Files.createFile(path);
            writer = Files.newBufferedWriter(Path.of(crawledjson), StandardOpenOption.APPEND);
            writer.write("[");
            writer.newLine();
            writer.write("]");
            writer.close();
        }
    }
    
    //Class to get data on Individual Crawled Pages
    private static class CrawledPage {
        private String canonical;
        private String title;
        private String description;

        //Function to parse the URL, Title, and Description of the page that is crawled
        public CrawledPage(String canonical, String title, String description) {
            this.canonical = canonical;
            this.title = title;
            this.description = description;
        }
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get(CRAWLED_FOLDER_NAME));
        new WebCrawler().startCrawling(seedUrls);
        System.out.println("Complete.");
    }
}