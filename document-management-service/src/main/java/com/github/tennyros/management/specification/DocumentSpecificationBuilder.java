package com.github.tennyros.management.specification;

import com.github.tennyros.management.dto.request.DocumentFilter;
import com.github.tennyros.management.entity.Document;
import org.springframework.data.jpa.domain.Specification;

public class DocumentSpecificationBuilder {

    private DocumentSpecificationBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Specification<Document> build(DocumentFilter dto) {
        Specification<Document> spec = Specification.where(null);

        spec = spec.and(DocumentSpecification.hasTitle(dto.getTitle()));
        spec = spec.and(DocumentSpecification.createdAfter(dto.getCreatedAfter()));
        spec = spec.and(DocumentSpecification.hasContentType(dto.getContentType()));
        spec = spec.and(DocumentSpecification.hasSizeBetween(dto.getMinSize(), dto.getMaxSize()));
        spec = spec.and(DocumentSpecification.hasVersionNumber(dto.getVersionNumber()));

        return spec;
    }

}
