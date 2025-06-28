package com.github.tennyros.management.specification;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DocumentSpecificationBuilder extends GenericSpecificationBuilder<Document> {

    public DocumentSpecificationBuilder(DocumentFilter filter) {
        add(titleContains(filter.getTitle()));
        add(createdAfter(filter.getCreatedAfter()));
        add(versionSpecifications(filter));
    }

    private Specification<Document> titleContains(String title) {
        return (title == null) ? null : (root, query, cb) -> 
            cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    private Specification<Document> createdAfter(LocalDateTime createdAfter) {
        return (createdAfter == null) ? null : (root, query, cb) -> 
            cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter);
    }

    private Specification<Document> versionSpecifications(DocumentFilter filter) {
        boolean hasVersionFilters = Stream.of(
                filter.getContentType(),
                filter.getMinSize(),
                filter.getMaxSize()
        ).anyMatch(Objects::nonNull);

        if (!hasVersionFilters) return null;

        return (root, query, cb) -> {
            query.distinct(true);
            Join<Document, DocumentVersion> versions = root.join("versions", JoinType.LEFT);
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getContentType() != null) {
                predicates.add(cb.equal(versions.get("contentType"), filter.getContentType()));
            }

            Long min = filter.getMinSize();
            Long max = filter.getMaxSize();

            if (min != null && max != null) {
                predicates.add(cb.between(versions.get("size"), min, max));
            } else if (min != null) {
                predicates.add(cb.greaterThanOrEqualTo(versions.get("size"), min));
            } else if (max != null) {
                predicates.add(cb.lessThanOrEqualTo(versions.get("size"), max));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
