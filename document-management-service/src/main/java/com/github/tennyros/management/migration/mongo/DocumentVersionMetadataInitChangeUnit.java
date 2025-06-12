package com.github.tennyros.management.migration.mongo;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;

@ChangeLog(order = "001")
public class DocumentVersionMetadataInitChangeUnit {

    @ChangeSet(order = "001", id = "initCollection", author = "vadim")
    public void initCollection(MongockTemplate mongoTemplate) {
        if (!mongoTemplate.collectionExists("document_version_metadata")) {
            mongoTemplate.createCollection("document_version_metadata");
        }

        mongoTemplate.indexOps("document_version_metadata").ensureIndex(
                new Index().on("documentVersionId", Sort.Direction.ASC).unique()
        );
    }
}