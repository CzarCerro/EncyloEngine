

public class SearchResult {
    private String canonical;
    private String title;
    private String description;
    private float relevanceScore;

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
    
    public float setRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(float relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    @Override
    public String toString() {
        return "{" +
                "\"url\":\"" + canonical + "\"" +
                ", \"title\":\"" + title + "\"" +
                ", \"content\":\"" + description + "\"" +
                ", \"relevanceScore\":\"" + relevanceScore + "\"" +
                "}";
    }
}

