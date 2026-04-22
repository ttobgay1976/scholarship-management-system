package com.sprms.system.master.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.BSARequest;
import com.sprms.system.hbmbeans.RequestStatus;

@Repository
public interface BSARequestRepository extends JpaRepository<BSARequest, Long> {
    
    // Find all requests by status
    List<BSARequest> findByRequestStatus(RequestStatus requestStatus);
    
    // Find requests submitted by specific user
    List<BSARequest> findBySubmittedBy(String submittedBy);
    
    // Find pending requests for approval
    @Query("SELECT r FROM BSARequest r WHERE r.requestStatus = :status ORDER BY r.submittedDate DESC")
    List<BSARequest> findPendingRequests(@Param("status") RequestStatus status);
    
    // Count requests by status
    long countByRequestStatus(RequestStatus requestStatus);
    
    // Find requests by BSA name (case-insensitive)
    @Query("SELECT r FROM BSARequest r WHERE LOWER(r.bsaName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<BSARequest> findByBsaNameContaining(@Param("name") String name);
}
