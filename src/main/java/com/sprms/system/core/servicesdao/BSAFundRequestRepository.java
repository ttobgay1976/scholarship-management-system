package com.sprms.system.core.servicesdao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.BSAFundRequest;
import com.sprms.system.hbmbeans.FundRequestStatus;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.BSA;

@Repository
public interface BSAFundRequestRepository extends JpaRepository<BSAFundRequest, Long> {

    // Find fund requests by BSA
    List<BSAFundRequest> findByBsa(BSA bsa);

    // Find fund requests by requested by user
    List<BSAFundRequest> findByRequestedBy(User requestedBy);

    // Find fund requests by status
    List<BSAFundRequest> findByFundRequestStatus(FundRequestStatus fundRequestStatus);

    // Find fund requests by status with pagination
    Page<BSAFundRequest> findByFundRequestStatus(FundRequestStatus fundRequestStatus, Pageable pageable);

    // Find pending fund requests for focal officer verification
    @Query("SELECT fr FROM BSAFundRequest fr WHERE fr.fundRequestStatus = :status ORDER BY fr.requestDate DESC")
    Page<BSAFundRequest> findByFundRequestStatusOrderByRequestDateDesc(@Param("status") FundRequestStatus status, Pageable pageable);

    // Find verified fund requests for chief approval
    @Query("SELECT fr FROM BSAFundRequest fr WHERE fr.fundRequestStatus = :status ORDER BY fr.focalVerificationDate DESC")
    Page<BSAFundRequest> findVerifiedForChiefApproval(@Param("status") FundRequestStatus status, Pageable pageable);

    // Find chief approved fund requests for accountant approval
    @Query("SELECT fr FROM BSAFundRequest fr WHERE fr.fundRequestStatus = :status ORDER BY fr.chiefApprovalDate DESC")
    Page<BSAFundRequest> findChiefApprovedForAccountant(@Param("status") FundRequestStatus status, Pageable pageable);

    // Find fund requests by BSA and status
    List<BSAFundRequest> findByBsaAndFundRequestStatus(BSA bsa, FundRequestStatus fundRequestStatus);

    // Count fund requests by status
    Long countByFundRequestStatus(FundRequestStatus fundRequestStatus);

    // Count fund requests by BSA and status
    Long countByBsaAndFundRequestStatus(BSA bsa, FundRequestStatus fundRequestStatus);

    // Find fund requests requested by a specific user with pagination
    Page<BSAFundRequest> findByRequestedBy(User requestedBy, Pageable pageable);

    // Find fund requests by multiple criteria
    @Query("SELECT fr FROM BSAFundRequest fr WHERE " +
           "(:bsa IS NULL OR fr.bsa = :bsa) AND " +
           "(:status IS NULL OR fr.fundRequestStatus = :status) AND " +
           "(:requestedBy IS NULL OR fr.requestedBy = :requestedBy) AND " +
           "(:startDate IS NULL OR fr.requestDate >= :startDate) AND " +
           "(:endDate IS NULL OR fr.requestDate <= :endDate)")
    Page<BSAFundRequest> findByMultipleCriteria(
            @Param("bsa") BSA bsa,
            @Param("status") FundRequestStatus status,
            @Param("requestedBy") User requestedBy,
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate,
            Pageable pageable);

    // Find fund requests with their documents and participants
    @Query("SELECT DISTINCT fr FROM BSAFundRequest fr " +
           "LEFT JOIN FETCH fr.documents " +
           "LEFT JOIN FETCH fr.participants " +
           "WHERE fr.fundRequestId = :fundRequestId")
    Optional<BSAFundRequest> findByIdWithDocumentsAndParticipants(@Param("fundRequestId") Long fundRequestId);

    // Custom query to get fund request statistics
    @Query("SELECT fr.fundRequestStatus, COUNT(fr) FROM BSAFundRequest fr GROUP BY fr.fundRequestStatus")
    List<Object[]> getFundRequestStatistics();

    // Find fund requests by date range
    @Query("SELECT fr FROM BSAFundRequest fr WHERE fr.requestDate BETWEEN :startDate AND :endDate ORDER BY fr.requestDate DESC")
    List<BSAFundRequest> findByRequestDateBetween(
            @Param("startDate") java.time.LocalDateTime startDate,
            @Param("endDate") java.time.LocalDateTime endDate);

    // Find fund request by reference number
    Optional<BSAFundRequest> findByReferenceNumber(String referenceNumber);
}
