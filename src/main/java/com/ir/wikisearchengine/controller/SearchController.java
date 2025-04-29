package com.ir.wikisearchengine.controller;

import com.ir.wikisearchengine.dto.SearchResponseDto;
import com.ir.wikisearchengine.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public SearchResponseDto search(@RequestParam String query , @RequestParam int page) {
        return searchService.search(query , page);
    }
}
