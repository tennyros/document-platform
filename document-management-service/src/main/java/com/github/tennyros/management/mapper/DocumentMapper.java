package com.github.tennyros.management.mapper;

import com.github.tennyros.management.dto.response.DocumentResponse;
import com.github.tennyros.management.dto.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(DocumentUploadRequest dto);

    DocumentResponse toDto(Document document);

}
