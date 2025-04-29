package com.ir.wikisearchengine.dto;

import com.ir.wikisearchengine.model.SearchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchResponseDto {
    private int currentPage;
    private int totalPages;
    private List<SearchResult> result;
}
