package com.sprms.system.core.servicesdao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.VPMembershipStatus;
import com.sprms.system.hbmbeans.VPPresidentMembership;
import com.sprms.system.hbmbeans.User;

@Repository
public interface VPPresidentMembershipRepository extends JpaRepository<VPPresidentMembership, Long> {

    // Find by requester
    List<VPPresidentMembership> findByRequester(User requester);

    // Find by status
    Page<VPPresidentMembership> findByMembershipStatusOrderByRequestDate(VPMembershipStatus status, Pageable pageable);

    // Find pending requests for focal officer verification
    @Query("SELECT vpm FROM VPPresidentMembership vpm WHERE vpm.membershipStatus = :status ORDER BY vpm.requestDate DESC")
    Page<VPPresidentMembership> findByMembershipStatusForVerification(@Param("status") VPMembershipStatus status, Pageable pageable);

    // Find by BSA
    List<VPPresidentMembership> findByBsaBsaId(Long bsaId);

    // Find by requester and status
    List<VPPresidentMembership> findByRequesterAndMembershipStatus(User requester, VPMembershipStatus status);

    // Count by status
    long countByMembershipStatus(VPMembershipStatus status);

    // Find requests within date range
    @Query("SELECT vpm FROM VPPresidentMembership vpm WHERE vpm.requestDate BETWEEN :startDate AND :endDate ORDER BY vpm.requestDate DESC")
    List<VPPresidentMembership> findByRequestDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Find by requested position
    List<VPPresidentMembership> findByRequestedPosition(com.sprms.system.hbmbeans.Position position);
}
