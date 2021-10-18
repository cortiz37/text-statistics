package com.text.statistics.text;

import lombok.Builder;

@Builder
public class WordFrequencyImpl implements WordFrequency {

    private String word;
    private long frequency;

    @Override
    public String word() {
        return word;
    }

    @Override
    public long frequency() {
        return frequency;
    }
}
