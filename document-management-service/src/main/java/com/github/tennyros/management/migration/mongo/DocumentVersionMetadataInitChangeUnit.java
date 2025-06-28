package com.github.tennyros.management.migration.mongo;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

/**
 * Mongock change unit for initializing document_version_metadata collection with unique index.
 */
@ChangeLog(order = "001")
public class DocumentVersionMetadataInitChangeUnit {

    private static final String COLLECTION_NAME = "document_version_metadata";

    @ChangeSet(order = "001", id = "initCollection", author = "vadim")
    public void initCollection(MongockTemplate mongoTemplate) {
        if (!mongoTemplate.collectionExists(COLLECTION_NAME)) {
            mongoTemplate.createCollection(COLLECTION_NAME);
        }

        mongoTemplate.indexOps(COLLECTION_NAME).ensureIndex(
                new Index().on("documentVersionId", Sort.Direction.ASC).unique()
        );
    }
}