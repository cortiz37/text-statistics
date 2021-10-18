package com.text.statistics.service;

import com.text.statistics.text.TextStatisticStorage;
import com.text.statistics.text.TextStatistics;
import com.text.statistics.text.TextStatisticsSummary;
import com.text.statistics.text.WordFrequency;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutputService {

    private final TextStatisticStorage textStatisticStorage;

    private final int longestWords;
    private final int mostFrequentWords;

    public OutputService(TextStatisticStorage textStatisticStorage,
                         @Value("${output.longest.words}") int longestWords,
                         @Value("${output.most.frequent.words}") int mostFrequentWords) {
        this.textStatisticStorage = textStatisticStorage;
        this.longestWords = longestWords;
        this.mostFrequentWords = mostFrequentWords;
    }

    public void print(PrintStream printStream) {
        TextStatisticsSummary summary = new TextStatisticsSummary();
        textStatisticStorage.getResources()
            .peek(r -> printSeparator(printStream, '='))
            .peek(r -> printStream.printf("Resource: %s\n", r))
            .map(textStatisticStorage::getTextStatistics)
            .forEach(ts -> printTextStatistics(printStream, ts, summary));

        if (textStatisticStorage.size() > 1) {
            printSummary(printStream, summary);
        }
    }

    private void printTextStatistics(PrintStream printStream, TextStatistics textStatistics, TextStatisticsSummary textStatisticsSummary) {
        long numberOfWords = textStatistics.numberOfWords();
        long numberOfLines = textStatistics.numberOfLines();
        final List<String> longestWords = textStatistics.longestWords(this.longestWords);
        final List<WordFrequency> topWords = textStatistics.topWords(mostFrequentWords);

        textStatisticsSummary.incrementNumberOfWords(numberOfWords);
        textStatisticsSummary.incrementNumberOfLines(numberOfLines);
        textStatisticsSummary.incrementLongestWords(longestWords);
        textStatisticsSummary.incrementTopWords(topWords);

        printFormattedStatistics(printStream, numberOfWords, numberOfLines, longestWords, topWords);
    }

    private void printSummary(PrintStream printStream, TextStatisticsSummary textStatisticsSummary) {
        printSeparator(printStream, '=');
        printStream.println("Summary");
        printFormattedStatistics(
            printStream,
            textStatisticsSummary.getNumberOfWords(),
            textStatisticsSummary.getNumberOfLines(),
            textStatisticsSummary.getLongestWords(longestWords),
            textStatisticsSummary.getTopWords(mostFrequentWords)
        );
    }

    private void printFormattedStatistics(PrintStream printStream, long numberOfWords, long numberOfLines, List<String> longestWords, List<WordFrequency> topWords) {
        printStream.printf("Number of words: %15s\n", numberOfWords);
        printStream.printf("Number of lines: %15s\n", numberOfLines);
        printSeparator(printStream, '-');
        printStream.printf("%s longest words: %s\n", this.longestWords, longestWords);
        printSeparator(printStream, '-');
        printStream.printf("%s most frequent words: %s\n\n", mostFrequentWords, getPrintableWordFrequencies(topWords));
    }

    private List<String> getPrintableWordFrequencies(List<WordFrequency> wordFrequencies) {
        return wordFrequencies.stream()
            .map(wf -> String.format("%s (%s)", wf.word(), wf.frequency()))
            .collect(Collectors.toList());
    }

    private void printSeparator(PrintStream printStream, Character base) {
        printStream.println(String.join("", Collections.nCopies(88, base.toString())));
    }
}