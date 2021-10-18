package com.text.statistics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RunnerService implements CommandLineRunner {

    private final DataReaderService dataReaderService;
    private final TextService textService;
    private final OutputService outputService;

    public RunnerService(DataReaderService dataReaderService, TextService textService, OutputService outputService) {
        this.dataReaderService = dataReaderService;
        this.textService = textService;
        this.outputService = outputService;
    }

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            dataReaderService.readAllTexts(textService::processTextLine, args);
            outputService.print(System.out);
        } else {
            log.error("at least 1 parameter is required (text resource)");
        }
    }
}
