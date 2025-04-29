package com.ir.wikisearchengine.indexing;

import opennlp.tools.stemmer.PorterStemmer;

public class Stemmer {
    private static final PorterStemmer stemmer = new PorterStemmer();

    public static String stem(String word) {
        return stemmer.stem(word);
    }
}
