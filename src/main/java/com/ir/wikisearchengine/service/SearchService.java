package com.ir.wikisearchengine.service;

import com.ir.wikisearchengine.dto.SearchResponseDto;
import com.ir.wikisearchengine.model.SearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchEngine searchEngine;

    public SearchResponseDto search(String query , int page) {
        List<SearchResult> resultList = searchEngine.search(query);
        int pageSize = 10;
        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, resultList.size());

        List<SearchResult> results;
        if (fromIndex >= resultList.size()) {
            results = new ArrayList<>();
        }
        else{
            results = resultList.subList(fromIndex, toIndex);
        }

        return SearchResponseDto.builder()
                .currentPage(page)
                .totalPages((int)Math.ceil((double) resultList.size() / pageSize))
                .result(results)
                .build();
    }
}
