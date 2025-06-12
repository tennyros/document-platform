package com.github.tennyros.management.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "document_version_metadata")
public class DocumentVersionMetadata {

    @Id
    private String id;

    @Indexed(unique = true)
    private Long documentVersionId;

    private Map<String, Object> attributes = new HashMap<>();
}
