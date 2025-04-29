package com.ir.wikisearchengine;

import com.ir.wikisearchengine.crawler.Crawler;
import com.ir.wikisearchengine.indexing.InvertedIndex;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WikiSearchEngineApplication implements CommandLineRunner{
    @Override
    public void run(String... args) throws Exception {
        Crawler crawler = new Crawler();
        var docs = crawler.crawl(new String[]{"https://en.wikipedia.org/wiki/List_of_pharaohs" , "https://en.wikipedia.org/wiki/Pharaoh"}, 5);
        InvertedIndex index = new InvertedIndex(docs);
        var temp = index.getIndex();
        temp.forEach((k, v) -> {
            System.out.print(k + ":  ");
            v.forEach(posting -> System.out.print("    " + posting));
            System.out.println();
        });

    }

    public static void main(String[] args) {

        SpringApplication.run(WikiSearchEngineApplication.class, args);
    }

}

