package com.ir.wikisearchengine;

import com.ir.wikisearchengine.crawler.Crawler;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WikiSearchEngineApplication implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {
        Crawler crawler = new Crawler();
        var docs = crawler.crawl(new String[]{"https://en.wikipedia.org/wiki/List_of_pharaohs" , "https://en.wikipedia.org/wiki/Pharaoh"}, 10);
        for (var doc : docs) {
            System.out.println(doc.getId() + " " + doc.getUrl() + " " + doc.getText());
            System.out.println("****************************************************************");
            System.out.println("****************************************************************");
            System.out.println("****************************************************************");
            System.out.println("****************************************************************");
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(WikiSearchEngineApplication.class, args);
    }

}

