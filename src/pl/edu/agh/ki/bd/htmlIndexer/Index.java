package pl.edu.agh.ki.bd.htmlIndexer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.query.criteria.internal.path.CollectionAttributeJoin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

        elements.stream().filter(element -> element.ownText().trim().length() > 1).forEach(element -> {
            for (String sentenceContent : element.ownText().split("\\. ")) {
                Sentence sentence = new Sentence(sentenceContent, processedUrl);
                session.persist(sentence);
            }
        });

        //Is it necessary?
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

    public void printSortedWebsites() {
        Session session = HibernateUtils.getSession();

        Query query = session.createQuery("select p.url, count(*)\n" +
                "from ProcessedUrl p\n" +
                "     join p.sentences s\n" +
                "group by p.url\n" +
                "order by count(*) desc");

        List list = query.list();

        Map<String, Long> websiteSentenceMap = new HashMap<>();

        List<Object[]> sentencedWebsites = (List<Object[]>) list;
        for (Object[] sW : sentencedWebsites) {
            String url = (String) sW[0];
            Long sentenceCount = (Long) sW[1];
            websiteSentenceMap.put(url, sentenceCount);
        }

        websiteSentenceMap.forEach((url, count) ->
                System.out.println(count + " sentences found on " + url));

    }
}
