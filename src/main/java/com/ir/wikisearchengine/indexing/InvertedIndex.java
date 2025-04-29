package com.ir.wikisearchengine.indexing;

import com.ir.wikisearchengine.crawler.Doc;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class InvertedIndex {

    private final Map<String, List<Posting>> index = new HashMap<>();

    public InvertedIndex(List<Doc> docs) {
        for (Doc doc : docs) {
            addDocument(doc.getId(), doc.getText());
        }
    }

    public void addDocument(int documentId, String content) {
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

    public Map<String, List<Posting>> getIndex() {
        return index;
    }
}
