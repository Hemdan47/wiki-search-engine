package com.ir.wikisearchengine;

import com.ir.wikisearchengine.crawler.Crawler;
import com.ir.wikisearchengine.service.DocumentVectorBuilder;
import com.ir.wikisearchengine.indexing.InvertedIndex;
import com.ir.wikisearchengine.service.SearchEngine;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WikiSearchEngineApplication{
    public static void main(String[] args) {
        SpringApplication.run(WikiSearchEngineApplication.class, args);
    }

}

