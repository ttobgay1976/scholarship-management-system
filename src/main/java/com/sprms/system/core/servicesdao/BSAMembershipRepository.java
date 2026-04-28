package com.sprms.system.core.servicesdao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.BSAMembership;
import com.sprms.system.hbmbeans.MembershipStatus;
import com.sprms.system.hbmbeans.User;

@Repository
public interface BSAMembershipRepository extends JpaRepository<BSAMembership, Long> {

    // Find membership by student and status
    List<BSAMembership> findByStudentAndMembershipStatus(User student, MembershipStatus status);
    
    // Find membership by student
    List<BSAMembership> findByStudent(User student);
    
    // Find membership by status
    Page<BSAMembership> findByMembershipStatus(MembershipStatus status, Pageable pageable);
    
    // Find membership by BSA
    List<BSAMembership> findByBsa_BsaId(Long bsaId);
    
    // Check if student already has membership with specific BSA
    boolean existsByStudentAndBsa(User student, com.sprms.system.hbmbeans.BSA bsa);
    
    // Find pending memberships for VP approval
    @Query("SELECT m FROM BSAMembership m WHERE m.membershipStatus = :status ORDER BY m.membershipRequestDate ASC")
    Page<BSAMembership> findByMembershipStatusOrderByRequestDate(@Param("status") MembershipStatus status, Pageable pageable);
    
    // Find approved memberships for focal officer verification
    @Query("SELECT m FROM BSAMembership m WHERE m.membershipStatus = :status ORDER BY m.vpApprovalDate ASC")
    Page<BSAMembership> findByMembershipStatusOrderByVpApprovalDate(@Param("status") MembershipStatus status, Pageable pageable);
    
    // Find membership by ID with student and BSA details
    @Query("SELECT m FROM BSAMembership m LEFT JOIN FETCH m.student LEFT JOIN FETCH m.bsa WHERE m.membershipId = :id")
    Optional<BSAMembership> findByIdWithDetails(@Param("id") Long id);
    
    // Count memberships by status
    long countByMembershipStatus(MembershipStatus status);
    
    // Find all memberships for a student with details
    @Query("SELECT m FROM BSAMembership m LEFT JOIN FETCH m.bsa WHERE m.student.id = :studentId ORDER BY m.membershipRequestDate DESC")
    List<BSAMembership> findByStudentIdWithBsaDetails(@Param("studentId") Long studentId);
    
    // Count active memberships by BSA
    @Query("SELECT COUNT(m) FROM BSAMembership m WHERE m.bsa.bsaId = :bsaId AND m.membershipStatus = :status")
    long countByBsaIdAndMembershipStatus(@Param("bsaId") Long bsaId, @Param("status") MembershipStatus status);
    
    // Find membership by reference number
    Optional<BSAMembership> findByReferenceNumber(String referenceNumber);
    
    // Debug method to count all memberships
    long count();
}
