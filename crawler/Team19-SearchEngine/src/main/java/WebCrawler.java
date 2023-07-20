// Implement Multi threading
// Implement Scheduler to crawl pages for refreshing
// Implement Anti-Duplicate pages crawling
// Clean Crawled Pages to get: URL, Content, Page Title AKA Canonical, Preview, Title, Fingerprint
// Compile The Crawled Pages into 1 text file with JSON structure
// If Page doesnt have a Preview tag, remove crawled page

//implement check for duplicate pages in json file
//filter based on keywords(?)


import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
//import com.google.gson.JsonArray;

import org.apache.commons.io.FileUtils;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONObject;
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
    //private static final int MAX_DEPTH = 11;
    private HashSet<String> Hashedurls;
//    private final Set<String> Hashedurls = new HashSet<>();
    private static final String SEED_URL = "https://www.encyclopedia.com";
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
	
    public WebCrawler() {
    	System.gc();
    	//Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	executorService = Executors.newFixedThreadPool(THREAD_SIZE);
    	Hashedurls = new HashSet<String>();
    	crawledPages = new HashSet<>();
    	lock = new Object();
    }
    
    //public void startCrawling(String URL) {
    public void startCrawling(String[] seedUrls) {
    	try {
    		createOutputFileIfNotExists(crawledjson);
    		
    		for (String URL: seedUrls) {
    			getPageLinks(URL, 0);
    		    }
    		//getPageLinks(URL, 0);

    		executorService.awaitTermination(10, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public synchronized void getPageLinks(String URL, int depth){
        
        if (Hashedurls.contains(URL)) {
        	return;
        }
        //if ((!Hashedurls.contains(URL) && (depth < MAX_DEPTH))) {
        	long executorServiceId = Thread.currentThread().getId();
            System.out.println("Thread #" + executorServiceId + " " + "Depth: " + depth + " - " + URL);
            
            Hashedurls.add(URL);
            executorService.submit(() -> {
            //Future<?> futurethreads = executorService.submit(() -> {
            try {	
            	//Hashedurls.add(URL);
                Document document = Jsoup.connect(URL).get();
                String fileName = CRAWLED_FOLDER_NAME + "/" + URL.replaceAll("[^a-zA-Z0-9]", "") + ".html"; // Strips special characters
                
                //Add Logic to check if document.text contains keywords
                storeURLdata(fileName, document);                
                Elements pageURLs = document.select("a[href]");
          
                //depth++;

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
//            try {
//            	futurethreads.get(60, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                // Handle interruption if required
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (TimeoutException e) {
//                futurethreads.cancel(true);
//                e.printStackTrace();
//            }
            //executorService.shutdown();
        }
    //}

    public synchronized static void storeURLdata(String fileName, Document document) throws Exception {
        //final File file = new File(fileName);
        
        String canonical = document.select("link[rel=canonical]").attr("href");
        String title = document.title();
        String description = document.select("meta[name=description]").attr("content");
        
//        System.out.println("T"+ title);
//        System.out.println("C"+ canonical);
//        System.out.println("D" + description);
        
        CrawledPage crawledPage = new CrawledPage(canonical, title, description);
        
//        Path path = Path.of(crawledjson);
//        boolean fileExists = Files.exists(path);
        
        // Serialize page data to JSON
//        String json = gson.toJson(crawledPage);
        // Append JSON to the output file
        writer = Files.newBufferedWriter(Path.of(crawledjson), StandardOpenOption.WRITE);
        synchronized (lock) {
        	
            String jsonContent = Files.readString(Path.of(crawledjson));
            JSONArray jsonArray = new JSONArray(jsonContent);
//            jsonArray.put(gson.toJson(crawledPage));
//            if(!hasValue(jsonArray, "canonical", canonical))
//            {
            JSONObject crawledItem = new JSONObject();
            crawledItem.put("title", title);
            crawledItem.put("description", description);
            crawledItem.put("canonical", canonical);
            jsonArray.put(crawledItem);
            
            String updatedJsonContent = jsonArray.toString();
            
            Object json = mapper.readValue(updatedJsonContent, Object.class);
            String prettyprintjson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); 
            
//            Files.writeString(Path.of(crawledjson), updatedJsonContent);
            writer.write(prettyprintjson);
          // if "[" character is first, then write data only
          // else if "]", replace with "," and write data
          // add a "]" at the end
//          writer.write(",");
//          writer.newLine();
//          writer.write(gson.toJson(crawledPage));
//          writer.write(",");

          writer.close();
            }
//            writer.append(json).append("\n");
//            writer.flush();
        }
        
//        synchronized (lock) {
//        crawledPages.add(crawledPage);
//        }
        
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        try (FileWriter writer = new FileWriter(crawledjson)) {
//            synchronized (lock) {
//                gson.toJson(crawledPages, writer);
//            }
//            System.out.println("Saved crawled pages to JSON file: " + crawledjson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        //FileUtils.writeStringToFile(file, document.outerHtml(), StandardCharsets.UTF_8);
//    }
    
//    private void saveCrawledPagesToJson() {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        try (FileWriter writer = new FileWriter(crawledjson)) {
//            synchronized (lock) {
//                gson.toJson(crawledPages, writer);
//            }
//            System.out.println("Saved crawled data to JSON file: " + crawledjson);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
//    public static boolean hasValue(JSONArray json, String key, String value) {
//        for(int i = 0; i < json.length(); i++) {
//        	JSONObject jsonObject = json.getJSONObject(i);
//        	if (jsonObject.has(key) && jsonObject.getString(key).equals(value)) return true;
//        }
//        return false;
//    }
    
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
//        else
//        {
//        	writer = Files.newBufferedWriter(Path.of(crawledjson), StandardOpenOption.APPEND);
//        }
    }
    
    private static class CrawledPage {
        private String canonical;
        private String title;
        private String description;

        public CrawledPage(String canonical, String title, String description) {
            this.canonical = canonical;
            this.title = title;
            this.description = description;
        }
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get(CRAWLED_FOLDER_NAME));
        //new WebCrawler().startCrawling(SEED_URL);
        new WebCrawler().startCrawling(seedUrls);
        System.out.println("Complete.");
    }
}