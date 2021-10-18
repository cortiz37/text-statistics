package com.text.statistics.service;

import com.text.statistics.text.TextStatisticStorage;
import com.text.statistics.text.TextStatistics;
import com.text.statistics.text.TextStatisticsImpl;
import com.text.statistics.text.WordFrequencyImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class OutputServiceTest {

    @Mock
    private TextStatisticStorage textStatisticStorage;

    private OutputService outputService;

    @BeforeEach
    public void setUp() {
        outputService = new OutputService(textStatisticStorage, 1, 2);
    }

    @Test
    public void shouldPrintStatistics() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        TextStatistics textStatistics = new TextStatisticsImpl();
        textStatistics.registerWord(WordFrequencyImpl.builder().word("hello").build());
        textStatistics.registerWord(WordFrequencyImpl.builder().word("world").build());
        textStatistics.registerWord(WordFrequencyImpl.builder().word("world").build());
        textStatistics.registerLine();
        final String resourceName = "story.txt";

        Mockito.when(textStatisticStorage.getResources()).thenReturn(Stream.of(resourceName));
        Mockito.when(textStatisticStorage.getTextStatistics(ArgumentMatchers.eq(resourceName))).thenReturn(textStatistics);

        outputService.print(printStream);

        String printResult = new String(outputStream.toByteArray());

        Assertions.assertTrue(printResult.contains("Resource: story.txt"));
        Assertions.assertTrue(printResult.contains("[hello]"));
        Assertions.assertTrue(printResult.contains("[world (2), hello (1)]"));
    }
}