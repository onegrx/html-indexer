package pl.edu.agh.ki.bd.htmlIndexer.model;


public class Sentence {

    private long id;
    private String content;
    private ProcessedUrl processedUrl;

    public Sentence() {
    }

    public Sentence(String content) {
        this.setContent(content);
    }

    public Sentence(String content, ProcessedUrl processedUrl) {
        this.content = content;
        this.processedUrl = processedUrl;
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

    public ProcessedUrl getProcessedUrl() {
        return processedUrl;
    }

    public void setProcessedUrl(ProcessedUrl processedUrl) {
        this.processedUrl = processedUrl;
    }
}
