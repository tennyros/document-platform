package com.github.tennyros.management.mapper;

import com.github.tennyros.management.dto.DocumentUploadRequestDto;
import com.github.tennyros.management.model.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(DocumentUploadRequestDto dto);

}
