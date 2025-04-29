package com.ir.wikisearchengine.startup;
import com.ir.wikisearchengine.crawler.Crawler;
import com.ir.wikisearchengine.indexing.InvertedIndex;
import com.ir.wikisearchengine.indexing.Stemmer;
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

        String[] defaultSeeds = {"https://en.wikipedia.org/wiki/Main_Page"};
        int defaultMaxDocuments = 100;


        String[] seeds;
        if (args.length < 2) {
            seeds = defaultSeeds;
        } else {
            seeds = new String[args.length - 1];
            System.arraycopy(args, 0, seeds, 0, args.length - 1);
        }

        int maxDocuments;
        if (args.length < 1 || !args[args.length - 1].matches("\\d+")) {
            maxDocuments = defaultMaxDocuments;
        } else {
            maxDocuments = Integer.parseInt(args[args.length - 1]);
        }

        var docs = crawler.crawl(seeds, maxDocuments);
        invertedIndex.build(docs);
        documentVectorBuilder.build(invertedIndex);
        System.out.println("Data crawling completed. Crawled " + docs.size() + " documents.");
    }
}
