package com.github.tennyros.management.specification;

import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.time.LocalDateTime;

@UtilityClass
public class DocumentSpecification {

    public static Specification<Document> hasTitle(String title) {
        return (root, query, cb) ->
                title == null ? null : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Document> createdAfter(LocalDateTime date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), date);
    }

    public static Specification<Document> hasContentType(String contentType) {
        return (root, query, cb) -> {
            if (contentType == null) return null;
            Join<Document, DocumentVersion> versions = root.join("versions", JoinType.LEFT);
            return cb.equal(versions.get("contentType"), contentType);
        };
    }

    public static Specification<Document> hasSizeBetween(Long minSize, Long maxSize) {
        return (root, query, cb) -> {
            Join<Document, DocumentVersion> versions = root.join("versions", JoinType.LEFT);
            if (minSize != null && maxSize != null)
                return cb.between(versions.get("size"), minSize, maxSize);
            else if (minSize != null)
                return cb.greaterThanOrEqualTo(versions.get("size"), minSize);
            else if (maxSize != null)
                return cb.lessThanOrEqualTo(versions.get("size"), maxSize);
            return null;
        };
    }

    public static Specification<Document> hasVersionNumber(Long versionNumber) {
        return (root, query, cb) -> {
            if (versionNumber == null) return null;
            Join<Document, DocumentVersion> versions = root.join("versions", JoinType.LEFT);
            return cb.equal(versions.get("versionNumber"), versionNumber);
        };
    }

}
