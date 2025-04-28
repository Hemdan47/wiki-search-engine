package com.ir.wikisearchengine.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    private static final String WIKIPEDIA_PREFIX = "https://en.wikipedia.org";
    private static final int TIMEOUT = 1000;

    public List<Doc> crawl(String[] seeds , int maxPages) {
        Set<String> visited = new HashSet<>();
        List<Doc> documents = new ArrayList<>();
        Queue<String> queue = new LinkedList<>(Arrays.asList(seeds));

        int id = 0;

        while (!queue.isEmpty() && documents.size() < maxPages) {
            String url = queue.poll();

            if (visited.contains(url)) {
                continue;
            }

            try {
                Document doc = Jsoup.connect(url).timeout(TIMEOUT).get();

                String text = doc.body().text();
                documents.add(
                        Doc.builder().
                                id(id++).
                                url(url).
                                text(text).
                                build()
                );
                visited.add(url);


                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String href = link.attr("abs:href");
                    if (isValidWikipediaLink(href) && !visited.contains(href)) {
                        queue.add(href);
                    }
                }

            } catch (Exception e) {
                System.err.println("Failed to fetch: " + url);
                e.printStackTrace();
            }
        }

        return documents;
    }

    private boolean isValidWikipediaLink(String url) {
        return url.startsWith(WIKIPEDIA_PREFIX + "/wiki/") &&
                //ignore https:
                !url.substring(6).contains(":") &&
                !url.contains("#");
    }
}
