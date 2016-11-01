package pl.edu.agh.ki.bd.htmlIndexer.model;

import java.util.Date;
import java.util.Set;

/**
 * Created by onegrx on 31.10.16.
 */
public class ProcessedUrl {
    private long id;
    private String url;
    private Date date;
    private Set<Sentence> sentences;

    public ProcessedUrl() {
    }

    public ProcessedUrl(String url, Date date) {
        this.url = url;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(Set<Sentence> sentences) {
        this.sentences = sentences;
    }
}

