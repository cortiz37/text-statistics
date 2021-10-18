package com.text.statistics.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.BiConsumer;

@ExtendWith(MockitoExtension.class)
class RunnerServiceTest {

    @Mock
    private DataReaderService dataReaderService;

    @Mock
    private TextService textService;

    @Mock
    private OutputService outputService;

    private RunnerService runnerService;

    @BeforeEach
    public void setUp() {
        runnerService = new RunnerService(dataReaderService, textService, outputService);
    }

    @Test
    public void shouldRun() {
        runnerService.run("/existing_file_path/file.txt");

        Mockito.verify(dataReaderService, Mockito.times(1)).readAllTexts(ArgumentMatchers.any(BiConsumer.class), ArgumentMatchers.anyString());
        Mockito.verify(outputService, Mockito.times(1)).print(ArgumentMatchers.eq(System.out));
    }

    @Test
    public void shouldNotRunWhenNoParameterProvided() {
        runnerService.run();

        Mockito.verify(dataReaderService, Mockito.never()).readAllTexts(ArgumentMatchers.any(BiConsumer.class), ArgumentMatchers.anyString());
    }

}