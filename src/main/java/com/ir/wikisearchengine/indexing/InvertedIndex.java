package com.ir.wikisearchengine.indexing;

import com.ir.wikisearchengine.model.Doc;
import com.ir.wikisearchengine.model.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Component
public class InvertedIndex {
    private final List<Doc> docs = new ArrayList<>();
    private final Map<String, List<Posting>> index = new HashMap<>();
    private int documentCount = 0;

    public void build(List<Doc> docs) {
        this.documentCount = docs.size();
        for (Doc doc : docs) {
            addDocument(doc);
        }
    }

    public void addDocument(Doc doc) {
        docs.add(doc);
        int documentId = doc.getId();
        String content = doc.getText();
        this.documentCount++;
        List<String> tokens = Tokenizer.tokenize(content);

        for (String token : tokens) {
            List<Posting> postings;
            if (index.containsKey(token)) {
                postings = index.get(token);
            } else {
                postings = new ArrayList<>();
                index.put(token, postings);
            }

            boolean found = false;
            for (Posting posting : postings) {
                if (posting.getDocumentId() == documentId) {
                    posting.incrementFrequency();
                    found = true;
                    break;
                }
            }

            if (!found) {
                postings.add(new Posting(documentId));
            }
        }
    }

    public List<Posting> search(String word) {
        String stemmed = Stemmer.stem(word);
        return index.getOrDefault(stemmed, Collections.emptyList());
    }

    public void printIndex() {
        for (Map.Entry<String, List<Posting>> entry : index.entrySet()) {
            String token = entry.getKey();
            List<Posting> postings = entry.getValue();

            System.out.print(token + " -> ");

            for (Posting posting : postings) {
                System.out.print(posting + " ");
            }

            System.out.println();
        }
    }

    public void merge(InvertedIndex otherIndex) {
        for (Map.Entry<String, List<Posting>> entry : otherIndex.index.entrySet()) {
            String term = entry.getKey();
            List<Posting> otherPostings = entry.getValue();

            // Check if the term exists in the current index
            if (index.containsKey(term)) {
                List<Posting> currentPostings = index.get(term);

                // Merge the postings lists
                for (Posting otherPosting : otherPostings) {
                    boolean found = false;
                    for (Posting currentPosting : currentPostings) {
                        // If documentId exists in both lists, increment the frequency
                        if (currentPosting.getDocumentId() == otherPosting.getDocumentId()) {
                            currentPosting.incrementFrequency();
                            found = true;
                            break;
                        }
                    }

                    // If documentId was not found, add the new posting
                    if (!found) {
                        currentPostings.add(otherPosting);
                    }
                }
            } else {
                // If the term does not exist in the current index, add it
                index.put(term, new ArrayList<>(otherPostings));
            }
        }

        // Update the document count after merging
        this.documentCount = Math.max(this.documentCount, otherIndex.getDocumentCount());
    }

    public Doc getDocumentById(int docId) {
        for (Doc doc : docs) {
            if (doc.getId() == docId) {
                return doc;
            }
        }
        return null;
    }
}
