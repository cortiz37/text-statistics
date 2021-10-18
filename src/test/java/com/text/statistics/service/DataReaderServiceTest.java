package com.text.statistics.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
class DataReaderServiceTest {

    private DataReaderService dataReaderService;

    @Test
    public void shouldReadFile() throws IOException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String source = classLoader.getResource("basic.txt").getFile();
        dataReaderService = new DataReaderService(true);

        StringBuilder stringBuilder = new StringBuilder();

        dataReaderService.readText((k, v) -> stringBuilder.append(v), source);

        Assertions.assertTrue(stringBuilder.indexOf("Lorem ipsum dolor sit amet") != -1);
    }

    @Test
    public void shouldNotReadFile() {
        String source = "/non_existing_path/_" + System.currentTimeMillis() + "_bad_" + new Random().nextInt(1_000) + ".txt";

        dataReaderService = new DataReaderService(true);

        Assertions.assertThrows(MalformedURLException.class, () -> dataReaderService.readText((k, v) -> {}, source));
    }
}