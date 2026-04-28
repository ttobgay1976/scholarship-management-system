package com.sprms.system.core.servicesdao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.BSAFundRequestParticipant;
import com.sprms.system.hbmbeans.BSAFundRequest;

@Repository
public interface BSAFundRequestParticipantRepository extends JpaRepository<BSAFundRequestParticipant, Long> {

    // Find participants by fund request
    List<BSAFundRequestParticipant> findByFundRequest(BSAFundRequest fundRequest);

    // Find participants by fund request ordered by name
    List<BSAFundRequestParticipant> findByFundRequestOrderByParticipantNameAsc(BSAFundRequest fundRequest);

    // Find participants by name (case-insensitive)
    List<BSAFundRequestParticipant> findByParticipantNameIgnoreCase(String participantName);

    // Find participants by CID
    List<BSAFundRequestParticipant> findByParticipantCid(String participantCid);

    // Find participants by contact number
    List<BSAFundRequestParticipant> findByContactNumber(String contactNumber);

    // Find participants by email address
    List<BSAFundRequestParticipant> findByEmailAddressIgnoreCase(String emailAddress);

    // Count participants by fund request
    Long countByFundRequest(BSAFundRequest fundRequest);

    // Check if participant exists by CID and fund request
    boolean existsByParticipantCidAndFundRequest(String participantCid, BSAFundRequest fundRequest);

    // Delete participants by fund request
    void deleteByFundRequest(BSAFundRequest fundRequest);

    // Find participants by multiple criteria
    @Query("SELECT p FROM BSAFundRequestParticipant p WHERE " +
           "(:fundRequest IS NULL OR p.fundRequest = :fundRequest) AND " +
           "(:participantName IS NULL OR LOWER(p.participantName) LIKE LOWER(CONCAT('%', :participantName, '%'))) AND " +
           "(:participantCid IS NULL OR p.participantCid = :participantCid) AND " +
           "(:contactNumber IS NULL OR p.contactNumber = :contactNumber) AND " +
           "(:emailAddress IS NULL OR LOWER(p.emailAddress) LIKE LOWER(CONCAT('%', :emailAddress, '%')))")
    List<BSAFundRequestParticipant> findByMultipleCriteria(
            @Param("fundRequest") BSAFundRequest fundRequest,
            @Param("participantName") String participantName,
            @Param("participantCid") String participantCid,
            @Param("contactNumber") String contactNumber,
            @Param("emailAddress") String emailAddress);

    // Custom query to get participant statistics by college/department
    @Query("SELECT p.collegeDepartment, COUNT(p) FROM BSAFundRequestParticipant p WHERE p.collegeDepartment IS NOT NULL GROUP BY p.collegeDepartment")
    List<Object[]> getParticipantStatisticsByDepartment();

    // Find participants with specific role designation
    List<BSAFundRequestParticipant> findByRoleDesignationIgnoreCase(String roleDesignation);

    // Find participants by college/department
    List<BSAFundRequestParticipant> findByCollegeDepartmentIgnoreCase(String collegeDepartment);
}
