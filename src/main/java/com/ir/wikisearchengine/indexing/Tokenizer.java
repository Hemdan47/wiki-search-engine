package com.ir.wikisearchengine.indexing;
import java.util.ArrayList;

import java.util.List;

public class Tokenizer {

    public static List<String> tokenize(String text) {
        String[] tokens = text.split("\\W+");
        List<String> result = new ArrayList<>();

        for (String token : tokens) {
            if (valid(token)) {
                token = token.toLowerCase();
                result.add(Stemmer.stem(token));
            }
        }
        return result;
    }

    private static Boolean valid(String token) {
        List<String> stopWords = List.of("the", "to", "be", "for", "from", "in", "into", "by", "or", "and", "that");
        return token.length() >= 2 && !stopWords.contains(token.toLowerCase());
    }

}
