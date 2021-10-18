package com.text.statistics.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

@Service
@Slf4j
public class DataReaderService {

    private final boolean tryAllResources;

    public DataReaderService(@Value("${try.all.resources}") boolean tryAllResources) {
        this.tryAllResources = tryAllResources;
    }

    public void readAllTexts(BiConsumer<String, String> lineConsumer, String... dataSources) {
        // removing duplicates
        final LinkedHashSet<String> uniqueDataSources = new LinkedHashSet<>(Arrays.asList(dataSources));
        for (String dataSource : uniqueDataSources) {
            try {
                long current = System.currentTimeMillis();
                log.info(String.format("Starting reading: %s", dataSource));
                readText(lineConsumer, dataSource);
                log.info(String.format("Read completed: %s, took: %s ms", dataSource, System.currentTimeMillis() - current));
            } catch (IllegalArgumentException | IOException | URISyntaxException e) {
                log.error(String.format("error reading resource: %s", dataSource), e);
                if (tryAllResources) {
                    continue;
                }
                break;
            }
        }
    }

    /**
     * try from local file, otherwise try from url.
     * reading line by line, streaming way.
     */
    protected void readText(BiConsumer<String, String> lineConsumer, String dataSource) throws IOException, URISyntaxException {
        Path filePath = Paths.get(dataSource);
        if (filePath.toFile().exists()) {
            tryFromFile(lineConsumer, filePath);
            return;
        }
        tryFromURL(lineConsumer, dataSource);
    }

    private void tryFromURL(BiConsumer<String, String> lineConsumer, String dataSource) throws IOException, URISyntaxException {
        log.info(String.format("reading url: %s", dataSource));
        URL url = new URL(dataSource).toURI().toURL(); //additional check to ensure URL is valid
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            IOUtils.lineIterator(urlConnection.getInputStream(), Charset.defaultCharset())
                .forEachRemaining(line -> lineConsumer.accept(dataSource, line));
        } finally {
            urlConnection.disconnect();
        }
    }

    private void tryFromFile(BiConsumer<String, String> lineConsumer, Path path) throws IOException {
        log.info(String.format("reading file: %s", path.toFile().getPath()));
        Files.lines(path).forEach(line -> lineConsumer.accept(path.toFile().getPath(), line));
    }
}