package com.ir.wikisearchengine.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Posting {
    private final int documentId;
    private int frequency;

    public Posting(int documentId) {
        this.documentId = documentId;
        this.frequency = 1;
    }

    public void incrementFrequency() {
        frequency++;
    }
}
