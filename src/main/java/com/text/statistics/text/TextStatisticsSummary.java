package com.text.statistics.text;

import lombok.Getter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TextStatisticsSummary {

    private final List<List<String>> longestWords = new LinkedList<>();
    private final List<List<WordFrequency>> topWords = new LinkedList<>();

    @Getter
    private long numberOfWords;

    @Getter
    private long numberOfLines;

    public List<String> getLongestWords(int n) {
        return longestWords.stream()
            .flatMap(List::stream)
            .sorted(Comparator.comparing(String::length).reversed())
            .distinct()
            .limit(n)
            .collect(Collectors.toList());
    }

    public List<WordFrequency> getTopWords(int n) {
        return topWords.stream()
            .flatMap(List::stream)
            .sorted(Comparator.comparing(WordFrequency::frequency).reversed())
            .distinct()
            .limit(n)
            .collect(Collectors.toList());
    }

    public void incrementLongestWords(List<String> value) {
        longestWords.add(value);
    }

    public void incrementTopWords(List<WordFrequency> value) {
        topWords.add(value);
    }

    public void incrementNumberOfWords(long value) {
        numberOfWords += value;
    }

    public void incrementNumberOfLines(long value) {
        numberOfLines += value;
    }
}
