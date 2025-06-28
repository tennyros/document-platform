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

    /**
     * Deletes all Document Versions associated with the specified Document.
     *
     * @param documentId the ID of the parent Document whose versions should be deleted
     */
    @Modifying
    @Query("DELETE FROM DocumentVersion dv WHERE dv.document.id = :documentId")
    void deleteAllByDocumentId(@Param("documentId") Long documentId);

    /**
     * Retrieves a list of version information (ID, storage key, and version number)
     * for all Document Versions associated with the specified Document.
     *
     * @param documentId the ID of the parent Document
     * @return a list of {@link DocumentVersionInfo}
     *         containing version details for the Document
     */
    @Query("SELECT new com.github.tennyros.management.dto.version.DocumentVersionInfo(" +
           "v.id, v.storageKey, v.versionNumber) " +
           "FROM DocumentVersion v WHERE v.document.id = :documentId")
    List<DocumentVersionInfo> findVersionsInfoByDocumentId(@Param("documentId") Long documentId);

    /**
     * Deletes a specific Document Version by its Document ID and Version number.
     *
     * @param documentId the ID of the parent Document
     * @param versionNumber the version number of the Document Version to delete
     * @return the number of deleted rows (0 if no such version exists)
     */
    @Modifying
    @Query("DELETE FROM DocumentVersion v WHERE v.document.id = :documentId AND v.versionNumber = :versionNumber")
    int deleteByDocumentIdAndVersionNumberDirect(@Param("documentId") Long documentId,
                                                 @Param("versionNumber") Long versionNumber);
}
