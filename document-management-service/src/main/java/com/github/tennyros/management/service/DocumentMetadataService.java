package com.github.tennyros.management.service;

import com.github.tennyros.management.entity.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentMetadataService {

    void saveDocumentWithVersion(MultipartFile file, Document document, String objectName);

    void deleteDocumentWithVersion(String objectName);

}
