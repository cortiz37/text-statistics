package com.text.statistics.service;

import com.text.statistics.text.TextStatisticStorage;
import com.text.statistics.text.TextStatistics;
import com.text.statistics.text.WordFrequencyImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class TextService {

    private final TextStatisticStorage textStatisticStorage;
    private final boolean processEmptyLines;
    private final boolean processCollapsePunctuation;
    private final Set<String> skipWords;

    public TextService(TextStatisticStorage textStatisticStorage,
                       @Value("${process.empty.lines}") boolean processEmptyLines,
                       @Value("${process.collapse.punctuation}") boolean processCollapsePunctuation,
                       @Value("${process.skip.words}") Set<String> skipWords) {
        this.textStatisticStorage = textStatisticStorage;
        this.processEmptyLines = processEmptyLines;
        this.processCollapsePunctuation = processCollapsePunctuation;
        this.skipWords = skipWords;
    }

    public void processTextLine(String dataSource, String line) {
        if (line != null) {
            final TextStatistics textStatistics = textStatisticStorage.getTextStatistics(dataSource);
            if (!line.trim().isEmpty() || processEmptyLines) {
                textStatistics.registerLine();
            }
            // assumption: everything between spaces is a candidate word
            String[] words = line.split("\\s");
            for (String word : words) {
                /**
                 * removing punctuation symbols, this operation is affected by `process.collapse.punctuation` property
                 *
                 * if `process.collapse.punctuation` is false:
                 ** replacement will be an space to deal with exceptional situations like:
                 *** 'up--up--up' => 3 instances of 'up' word
                 *** 'http://mysite.domain.com' => 4 different words: 'http', 'mysite', 'domain', 'com'
                 ** but causing:
                 *** 'to-night' => 2 different words: 'to', 'night'
                 *
                 * if `process.collapse.punctuation` is true:
                 ** replacement will remove the punctuation symbol to deal with exceptional situations like:
                 *** 'to-night' => 1 word: 'tonight'
                 ** but causing:
                 *** 'up--up--up' => 1 word 'upupup'
                 *** 'http://mysite.domain.com' => 1 word 'httpmysitedomaincom'
                 */
                String[] candidateWords = word
                    .replaceAll("[^\\p{L} ]", processCollapsePunctuation ? "" : " ")
                    .split("\\s");
                for (String candidateWord : candidateWords) {
                    addWordEntry(textStatistics, candidateWord);
                }
            }
        }
    }

    /**
     * adds an entry if the root word is not empty and is not part of the exclude collection.
     * exclude collection is affected by `process.skip.words` property
     * good candidates for exclusion: 'a', 'the', 'i', 'of', 'and', 'to', 'in', etc
     */
    void addWordEntry(TextStatistics textStatistics, String word) {
        /**
         * root words:
         ** Example -> example
         ** HTTP -> http
         */
        getRootWord(word)
            .filter(r -> !skipWords.contains(r))
            .map(r -> WordFrequencyImpl.builder().word(r).build())
            .ifPresent(textStatistics::registerWord);
    }

    private Optional<String> getRootWord(String text) {
        String root = text.toLowerCase().trim();
        if (root.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(root);
    }
}
