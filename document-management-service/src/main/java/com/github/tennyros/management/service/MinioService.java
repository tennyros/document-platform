package com.github.tennyros.management.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    String upload(MultipartFile file);

    InputStream download(String objectName);

    void delete(String objectName);

}
