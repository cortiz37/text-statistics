package com.text.statistics.text;

import java.util.*;

public class TextStatisticsImpl implements TextStatistics {

    private final Map<String, WordFrequency> wordStatistics;
    private final TreeMap<Integer, Set<String>> wordLengths;

    private long numberOfLines;

    private long higherFrequency;

    public TextStatisticsImpl() {
        wordStatistics = new LinkedHashMap<>();
        wordLengths = new TreeMap<>();
    }

    @Override
    public List<WordFrequency> topWords(int n) {
        /*
         * plain array to guarantee O(n) as complexity time,
         * instead of sorting the map causing O(n * log n)
         */
        Set<WordFrequency>[] index = new Set[(int) higherFrequency + 1];
        wordStatistics.forEach((k, v) -> {
            if (index[(int) v.frequency()] == null) {
                index[(int) v.frequency()] = new LinkedHashSet<>();
            }
            index[(int) v.frequency()].add(v);
        });

        List<WordFrequency> words = new ArrayList<>();
        for (int i = index.length - 1; i >= 0; i--) {
            Set<WordFrequency> wordFrequencies = index[i];
            if(wordFrequencies != null) {
                for (WordFrequency wordFrequency : wordFrequencies) {
                    words.add(wordFrequency);
                    n --;
                    if(n == 0) {
                        return words;
                    }
                }
            }
        }
        return words;
    }

    @Override
    public List<String> longestWords(int n) {
        List<String> words = new ArrayList<>();
        /*
         * using TreeMap: self-sorted structure.
         * hash access for each item, guaranteeing O(n) as complexity time
         */
        for (Integer key : wordLengths.descendingMap().keySet()) {
            for (String word : wordLengths.get(key)) {
                words.add(word);
                n--;
                if (n == 0) {
                    return words;
                }
            }
        }
        return words;
    }

    @Override
    public long numberOfWords() {
        return wordStatistics.values()
            .stream()
            .mapToLong(WordFrequency::frequency)
            .sum();
    }

    @Override
    public long numberOfLines() {
        return numberOfLines;
    }

    @Override
    public void registerWord(WordFrequency wordFrequency) {
        registerStatistics(wordFrequency);
        registerLength(wordFrequency);
    }

    @Override
    public void registerLine() {
        numberOfLines++;
    }

    private void registerStatistics(WordFrequency wordFrequency) {
        long frequency = !wordStatistics.containsKey(wordFrequency.word()) ? 1 : wordStatistics.get(wordFrequency.word()).frequency() + 1;
        if (frequency > higherFrequency) {
            higherFrequency = frequency;
        }
        WordFrequency entry = WordFrequencyImpl.builder()
            .frequency(frequency)
            .word(wordFrequency.word())
            .build();
        wordStatistics.put(wordFrequency.word(), entry);
    }

    private void registerLength(WordFrequency wordFrequency) {
        int length = wordFrequency.word().length();
        if (!wordLengths.containsKey(length)) {
            wordLengths.put(length, new LinkedHashSet<>());
        }
        wordLengths.get(length).add(wordFrequency.word());
    }
}
