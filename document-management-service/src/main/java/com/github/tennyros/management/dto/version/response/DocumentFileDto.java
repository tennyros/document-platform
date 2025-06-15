package com.github.tennyros.management.dto.version.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFileDto {

    private String filename;
    private String contentType;
    private long size;
    private InputStream inputStream;
}
