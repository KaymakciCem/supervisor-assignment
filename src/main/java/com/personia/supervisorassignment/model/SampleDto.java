package com.personia.supervisorassignment.model;

import lombok.Data;

@Data
public class SampleDto {
    private String name;
    private SampleDto sampleDto;

    public SampleDto(String name) {
        this.name = name;
    }
}
