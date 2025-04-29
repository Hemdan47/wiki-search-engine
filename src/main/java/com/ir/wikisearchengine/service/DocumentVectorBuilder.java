package com.ir.wikisearchengine.service;

import com.ir.wikisearchengine.indexing.InvertedIndex;
import com.ir.wikisearchengine.indexing.Tokenizer;
import com.ir.wikisearchengine.model.Posting;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocumentVectorBuilder {
    private InvertedIndex index;
    private int totalDocuments = 0;
    private final Map<Integer, Map<String, Double>> documentVectors = new HashMap<>();

    public void build(InvertedIndex index){
        if(this.index != null){
            this.index.merge(index);
        }
        else {
            this.index = index;
        }

        this.totalDocuments = index.getDocumentCount();
        this.documentVectors.clear();
        buildVectors();
    }

    private void buildVectors() {

        for (String term : index.getIndex().keySet()) {
            List<Posting> postings = index.getIndex().get(term);
            int df = postings.size();
            double idf = computeIdf(df);

            for (Posting posting : postings) {
                int docId = posting.getDocumentId();
                int tf = posting.getFrequency();
                double tfIdf = computeTf(tf) * idf;

                Map<String, Double> vector = documentVectors.get(docId);
                if (vector == null) {
                    vector = new HashMap<>();
                    documentVectors.put(docId, vector);
                }

                vector.put(term, tfIdf);
            }
        }
    }

    private double computeTf(int tf) {
        if (tf == 0) return 0;
        return 1 + Math.log10(tf);
    }

    private double computeIdf(int df) {
        if (df == 0) return 0;
        return Math.log10((double) totalDocuments / df);
    }


    public Map<String, Double> getDocumentVector(int docId) {
        return documentVectors.getOrDefault(docId, new HashMap<>());
    }


    public Map<Integer, Map<String, Double>> getAllDocumentVectors() {
        return documentVectors;
    }

    public void printDocumentVectors() {
        for (Map.Entry<Integer, Map<String, Double>> entry : documentVectors.entrySet()) {
            int docId = entry.getKey();
            Map<String, Double> vector = entry.getValue();

            System.out.println("Document ID: " + docId);
            for (Map.Entry<String, Double> termEntry : vector.entrySet()) {
                String term = termEntry.getKey();
                double weight = termEntry.getValue();
                System.out.printf("   %-15s -> %.5f%n", term, weight);
            }
            System.out.println();
        }
    }


    public Map<String, Double> buildQueryVector(String query) {
        List<String> tokens = Tokenizer.tokenize(query);
        Map<String, Integer> tfMap = new HashMap<>();

        for (String token : tokens) {
            tfMap.put(token, tfMap.getOrDefault(token, 0) + 1);
        }

        Map<String, Double> queryVector = new HashMap<>();
        for (String term : tfMap.keySet()) {
            int tf = tfMap.get(term);
            int df = index.getIndex().containsKey(term) ? index.getIndex().get(term).size() : 0;
            if (df == 0) continue;

            double tfWeight = computeTf(tf);
            double idf = computeIdf(df);

            queryVector.put(term, tfWeight * idf);
        }

        return queryVector;
    }
}
