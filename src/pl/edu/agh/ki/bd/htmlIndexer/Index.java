package pl.edu.agh.ki.bd.htmlIndexer;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.edu.agh.ki.bd.htmlIndexer.model.ProcessedUrl;
import pl.edu.agh.ki.bd.htmlIndexer.model.Sentence;
import pl.edu.agh.ki.bd.htmlIndexer.persistence.HibernateUtils;

public class Index {
    public void indexWebPage(String url) throws IOException {

        final Session session = HibernateUtils.getSession();
        final Transaction transaction = session.beginTransaction();

        ProcessedUrl processedUrl = new ProcessedUrl(url, new Date());

        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.body().select("*");

        for (Element element : elements) {
            if (element.ownText().trim().length() > 1) {
                for (String sentenceContent : element.ownText().split("\\. ")) {
                    Sentence sentence = new Sentence(sentenceContent, processedUrl);
                    session.persist(sentence);
                }
            }
        }

        session.persist(processedUrl);

        transaction.commit();
        session.close();
    }

    public List<String> findSentencesByWords(String words) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        String query = "%" + words.replace(" ", "%") + "%";
        List<Sentence> sentences = session.createQuery("select s from Sentence s where s.content like :query", Sentence.class)
                .setParameter("query", query).getResultList();

        List<String> result = sentences.stream().map(s -> s.getContent() + " -- " + s.getProcessedUrl().getUrl())
                .collect(Collectors.toCollection(LinkedList::new));


        transaction.commit();
        session.close();

        return result;
    }

    public List<String> findSentencesLongerThan(int min) {
        Session session = HibernateUtils.getSession();
        Transaction transaction = session.beginTransaction();

        List<String> result = session.createQuery("select s.content from Sentence s where length(s.content) > :min", String.class)
                .setParameter("min", min).getResultList();

        transaction.commit();
        session.close();

        return result;
    }

}
