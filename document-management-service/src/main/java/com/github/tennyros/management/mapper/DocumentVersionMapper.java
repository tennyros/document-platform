package com.github.tennyros.management.mapper;

import com.github.tennyros.management.dto.version.DocumentVersionResponse;
import com.github.tennyros.management.entity.DocumentVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DocumentVersionMapper {

    @Mapping(target = "documentId", expression = "java(document.getDocument().getId())")
    DocumentVersionResponse toDto(DocumentVersion document);
}
