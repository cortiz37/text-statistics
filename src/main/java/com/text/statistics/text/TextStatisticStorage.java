package com.text.statistics.text;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class TextStatisticStorage {

    private final Map<String, TextStatistics> textStatisticResources;

    public TextStatisticStorage() {
        this.textStatisticResources = new LinkedHashMap<>();
    }

    public Stream<String> getResources() {
        return textStatisticResources.keySet().stream();
    }

    public TextStatistics getTextStatistics(String resource) {
        if (!textStatisticResources.containsKey(resource)) {
            textStatisticResources.put(resource, new TextStatisticsImpl());
        }
        return textStatisticResources.get(resource);
    }

    public int size() {
        return textStatisticResources.size();
    }
}
