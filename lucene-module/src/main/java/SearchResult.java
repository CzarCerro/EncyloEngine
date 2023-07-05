

public class SearchResult {
    private String canonical;
    private String title;
    private String description;

    public String getUrl() {
        return canonical;
    }

    public void setUrl(String url) {
        this.canonical = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return description;
    }

    public void setContent(String content) {
        this.description = content;
    }
    
    @Override
    public String toString() {
        return "{" +
                "\"url\":\"" + canonical + "\"" +
                ", \"title\":\"" + title + "\"" +
                ", \"content\":\"" + description + "\"" +
                "}";
    }
}

