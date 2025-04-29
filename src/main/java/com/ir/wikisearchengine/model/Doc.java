package com.ir.wikisearchengine.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doc {
    private int id;
    private String url;
    private String text;
}
