package com.ir.wikisearchengine.service;

import com.ir.wikisearchengine.indexing.InvertedIndex;
import com.ir.wikisearchengine.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SearchEngine {
    private final DocumentVectorBuilder vectorBuilder;
    private final InvertedIndex invertedIndex;

    public List<SearchResult> search(String query) {
        Map<String, Double> queryVector = vectorBuilder.buildQueryVector(query);
        Map<Integer, Map<String, Double>> docVectors = vectorBuilder.getAllDocumentVectors();

        List<SearchResult> results = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, Double>> entry : docVectors.entrySet()) {
            int docId = entry.getKey();
            Map<String, Double> docVector = entry.getValue();
            double score = cosineSimilarity(queryVector, docVector);
            if (score > 0) {
                results.add(
                        SearchResult.builder()
                                .url(invertedIndex.getDocumentById(docId).getUrl())
                                .score(score)
                                .build()
                );
            }
        }

        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));


        return results;
    }

    private double cosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> terms = new HashSet<>(vec1.keySet());
        terms.retainAll(vec2.keySet());

        double dotProduct = 0.0;
        for (String term : terms) {
            dotProduct += vec1.get(term) * vec2.get(term);
        }

        double vLen1 = 0.0;
        for (double weight : vec1.values()) {
            vLen1 += weight * weight;
        }
        vLen1 = Math.sqrt(vLen1);

        double vLen2 = 0.0;
        for (double weight : vec2.values()) {
            vLen2 += weight * weight;
        }
        vLen2 = Math.sqrt(vLen2);

        if (vLen1 == 0 || vLen2 == 0) return 0.0;
        return dotProduct / (vLen1 * vLen2);
    }
}
