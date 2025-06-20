package com.github.tennyros.management.mapper;

import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.management.entity.DocumentSignature;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DocumentSignatureMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documentVersion", ignore = true)
    DocumentSignature toEntity(SaveSignatureRequest dto);
}
