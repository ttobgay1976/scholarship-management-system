package com.sprms.system.core.servicesdao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.BSAFundRequestDocument;
import com.sprms.system.hbmbeans.BSAFundRequest;
import com.sprms.system.hbmbeans.DocumentType;

@Repository
public interface BSAFundRequestDocumentRepository extends JpaRepository<BSAFundRequestDocument, Long> {

    // Find documents by fund request
    List<BSAFundRequestDocument> findByFundRequest(BSAFundRequest fundRequest);

    // Find documents by fund request and document type
    List<BSAFundRequestDocument> findByFundRequestAndDocumentType(BSAFundRequest fundRequest, DocumentType documentType);

    // Find documents by document type
    List<BSAFundRequestDocument> findByDocumentType(DocumentType documentType);

    // Count documents by fund request
    Long countByFundRequest(BSAFundRequest fundRequest);

    // Count documents by fund request and document type
    Long countByFundRequestAndDocumentType(BSAFundRequest fundRequest, DocumentType documentType);

    // Find document by fund request and document name
    Optional<BSAFundRequestDocument> findByFundRequestAndDocumentName(BSAFundRequest fundRequest, String documentName);

    // Delete documents by fund request
    void deleteByFundRequest(BSAFundRequest fundRequest);

    // Custom query to get document statistics
    @Query("SELECT d.documentType, COUNT(d) FROM BSAFundRequestDocument d GROUP BY d.documentType")
    List<Object[]> getDocumentStatistics();

    // Find documents by uploaded by user
    @Query("SELECT d FROM BSAFundRequestDocument d WHERE d.uploadedBy = :uploadedBy")
    List<BSAFundRequestDocument> findByUploadedBy(@Param("uploadedBy") Long uploadedBy);
}
