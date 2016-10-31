package pl.edu.agh.ki.bd.htmlIndexer.model;


public class Sentence {

    private long id;
    private String content;
    private String url;

    public Sentence() {
    }

    public Sentence(String content) {
        this.setContent(content);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}