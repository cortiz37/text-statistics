package com.text.statistics.service;

import com.text.statistics.text.TextStatisticStorage;
import com.text.statistics.text.TextStatistics;
import com.text.statistics.text.TextStatisticsImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class TextServiceTest {

    @Mock
    private TextStatisticStorage textStatisticStorage;

    private TextService textService;

    @BeforeEach
    public void setUp() {
        textService = new TextService(textStatisticStorage, false, false, Collections.singleton("exclusion"));
    }

    @Test
    public void shouldProcessTextLine() {
        TextStatistics textStatistics = new TextStatisticsImpl();

        Mockito.when(textStatisticStorage.getTextStatistics(ArgumentMatchers.anyString())).thenReturn(textStatistics);

        textService.processTextLine("/external-file", "this is a line");

        Assertions.assertEquals(1, textStatistics.numberOfLines());
        Assertions.assertEquals(4, textStatistics.numberOfWords());
    }

    @Test
    public void shouldAddWordEntry() {
        TextStatistics textStatistics = new TextStatisticsImpl();

        textService.addWordEntry(textStatistics, "Example");

        Assertions.assertEquals(1, textStatistics.numberOfWords());
    }

    @Test
    public void shouldNotAddWordEntryWhenRootWordIsEmpty() {
        TextStatistics textStatistics = new TextStatisticsImpl();

        textService.addWordEntry(textStatistics, "  ");

        Assertions.assertEquals(0, textStatistics.numberOfWords());
    }

    @Test
    public void shouldNotAddWordEntryWhenRootWordIsPartOfExclusion() {
        TextStatistics textStatistics = new TextStatisticsImpl();

        textService.addWordEntry(textStatistics, "exclusion");

        Assertions.assertEquals(0, textStatistics.numberOfWords());
    }
}