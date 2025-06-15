package com.github.tennyros.management.repository.jpa;

import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.dto.version.DocumentVersionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.List;

@RepositoryRestResource(path = "document-versions", collectionResourceRel = "documentVersions")
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findTopByDocumentOrderByVersionNumberDesc(Document document);

    Optional<DocumentVersion> findByDocumentIdAndVersionNumber(Long documentId, Long versionNumber);

    @Modifying
    @Query("DELETE FROM DocumentVersion dv WHERE dv.document.id = :documentId")
    void deleteAllByDocumentId(@Param("documentId") Long documentId);

    @Query("SELECT new com.github.tennyros.management.dto.version.DocumentVersionInfo(v.id, v.storageKey, v.versionNumber) " +
           "FROM DocumentVersion v WHERE v.document.id = :documentId")
    List<DocumentVersionInfo> findVersionInfoByDocumentId(@Param("documentId") Long documentId);

    @Modifying
    @Query("DELETE FROM DocumentVersion v WHERE v.document.id = :documentId AND v.versionNumber = :versionNumber")
    int deleteByDocumentIdAndVersionNumberDirect(@Param("documentId") Long documentId,
                                                 @Param("versionNumber") Long versionNumber);

}
