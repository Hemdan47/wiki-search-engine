package com.ir.wikisearchengine.startup;
import com.ir.wikisearchengine.crawler.Crawler;
import com.ir.wikisearchengine.indexing.InvertedIndex;
import com.ir.wikisearchengine.model.SearchResult;
import com.ir.wikisearchengine.service.DocumentVectorBuilder;
import com.ir.wikisearchengine.service.SearchEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppStartupRunner implements CommandLineRunner {
    private final Crawler crawler;
    private final DocumentVectorBuilder documentVectorBuilder;
    private final InvertedIndex invertedIndex;

    @Override
    public void run(String... args) {

        var docs = crawler.crawl(new String[]{"https://en.wikipedia.org/wiki/List_of_pharaohs" , "https://en.wikipedia.org/wiki/Pharaoh"}, 30);
        invertedIndex.build(docs);
        documentVectorBuilder.build(invertedIndex);
        System.out.println("Data crawling completed. Crawled " + docs.size() + " documents.");
    }
}
