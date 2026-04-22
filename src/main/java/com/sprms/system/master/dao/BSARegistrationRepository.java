package com.sprms.system.master.dao;

import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.BSAStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BSARegistrationRepository extends JpaRepository<BSA, Long> {

    // Find BSA by code
    Optional<BSA> findByBsaCode(String bsaCode);

    // Find all active BSAs
    List<BSA> findByStatus(BSAStatus status);

    // Find BSAs by country
    List<BSA> findByCountryId(Long countryId);

    // Find BSAs by country and state
    List<BSA> findByCountryIdAndStateId(Long countryId, Long stateId);

    // Find BSAs by country, state, and city
    List<BSA> findByCountryIdAndStateIdAndCityId(Long countryId, Long stateId, Long cityId);

    // Find BSAs by institute (college)
    List<BSA> findByInstituteId(Long instituteId);

    // Find BSAs by state
    List<BSA> findByStateId(Long stateId);

    // Find BSAs by city
    List<BSA> findByCityId(Long cityId);

    // Custom query to get BSAs with specific criteria
    @Query("SELECT b FROM BSA b WHERE b.status = :status AND b.country.id = :countryId")
    List<BSA> findActiveBSAsByCountry(@Param("status") BSAStatus status, @Param("countryId") Long countryId);

    // Custom query to check duplicate BSA code
    @Query("SELECT COUNT(b) > 0 FROM BSA b WHERE LOWER(b.bsaCode) = LOWER(:bsaCode) AND b.bsaId != :bsaId")
    boolean existsByBsaCodeExcludingId(@Param("bsaCode") String bsaCode, @Param("bsaId") Long bsaId);

    // Get all BSAs with active status
    @Query("SELECT b FROM BSA b WHERE b.status = 'ACTIVE' ORDER BY b.bsaName ASC")
    List<BSA> findAllActiveBSAs();
}
