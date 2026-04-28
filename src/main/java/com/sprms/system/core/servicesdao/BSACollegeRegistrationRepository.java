package com.sprms.system.core.servicesdao;

import com.sprms.system.hbmbeans.BSACollegeRegistration;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BSACollegeRegistrationRepository extends JpaRepository<BSACollegeRegistration, Long> {

    // Find all registrations by BSA
    List<BSACollegeRegistration> findByBsa(BSA bsa);

    // Find all registrations by BSA ID
    List<BSACollegeRegistration> findByBsaBsaId(Long bsaId);

    // Find all registrations by college
    List<BSACollegeRegistration> findByCollege(College college);

    // Find all registrations by college ID
    List<BSACollegeRegistration> findByCollegeId(Long collegeId);

    // Find registration by BSA and College (unique combination)
    Optional<BSACollegeRegistration> findByBsaAndCollege(BSA bsa, College college);

    // Find registration by BSA ID and College ID
    Optional<BSACollegeRegistration> findByBsaBsaIdAndCollegeId(Long bsaId, Long collegeId);

    
    // Find registrations by country, state, and city
    @Query("SELECT r FROM BSACollegeRegistration r WHERE r.country.id = :countryId AND r.state.id = :stateId AND r.city.id = :cityId")
    List<BSACollegeRegistration> findByLocation(@Param("countryId") Long countryId, 
                                               @Param("stateId") Long stateId, 
                                               @Param("cityId") Long cityId);

    // Find registrations by BSA and location
    @Query("SELECT r FROM BSACollegeRegistration r WHERE r.bsa.bsaId = :bsaId AND r.country.id = :countryId AND r.state.id = :stateId AND r.city.id = :cityId")
    List<BSACollegeRegistration> findByBsaAndLocation(@Param("bsaId") Long bsaId,
                                                     @Param("countryId") Long countryId,
                                                     @Param("stateId") Long stateId,
                                                     @Param("cityId") Long cityId);

    // Find all colleges by location (country, state, city) from college table
    @Query("SELECT c FROM College c WHERE c.country.id = :countryId AND c.state.id = :stateId AND c.city.id = :cityId AND (c.status = true OR c.status IS NULL)")
    List<College> findCollegesByLocation(@Param("countryId") Long countryId,
                                          @Param("stateId") Long stateId,
                                          @Param("cityId") Long cityId);

    // Find college by ID
    @Query("SELECT c FROM College c WHERE c.id = :collegeId AND (c.status = true OR c.status IS NULL)")
    Optional<College> findCollegeById(@Param("collegeId") Long collegeId);

    // Debug method to check college data
    @Query("SELECT COUNT(c) FROM College c WHERE c.country.id = :countryId AND c.state.id = :stateId AND c.city.id = :cityId AND (c.status = true OR c.status IS NULL)")
    Long countCollegesByLocation(@Param("countryId") Long countryId,
                                   @Param("stateId") Long stateId,
                                   @Param("cityId") Long cityId);

    // Find colleges available for registration by BSA in a specific location (excluding already registered)
    @Query("SELECT c FROM College c WHERE c.country.id = :countryId AND c.state.id = :stateId AND c.city.id = :cityId " +
           "AND (c.status = true OR c.status IS NULL) " +
           "AND NOT EXISTS (SELECT r FROM BSACollegeRegistration r WHERE r.bsa.bsaId = :bsaId AND r.college.id = c.id)")
    List<College> findAvailableCollegesForBSAByLocation(@Param("bsaId") Long bsaId,
                                                        @Param("countryId") Long countryId,
                                                        @Param("stateId") Long stateId,
                                                        @Param("cityId") Long cityId);

    // Count registrations by BSA
    @Query("SELECT COUNT(r) FROM BSACollegeRegistration r WHERE r.bsa.bsaId = :bsaId")
    Long countByBsa(@Param("bsaId") Long bsaId);

    
    // Find registrations by status
    List<BSACollegeRegistration> findByStatus(RegistrationStatus status);

    // Find registrations by BSA and status
    List<BSACollegeRegistration> findByBsaAndStatus(BSA bsa, RegistrationStatus status);

    // Find registrations by BSA ID and status
    List<BSACollegeRegistration> findByBsaBsaIdAndStatus(Long bsaId, RegistrationStatus status);

    // Find registrations created within a date range
    @Query("SELECT r FROM BSACollegeRegistration r WHERE r.createdDate BETWEEN :startDate AND :endDate")
    List<BSACollegeRegistration> findByCreatedDateBetween(@Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    // Find registrations by BSA created within a date range
    @Query("SELECT r FROM BSACollegeRegistration r WHERE r.bsa.bsaId = :bsaId AND r.createdDate BETWEEN :startDate AND :endDate")
    List<BSACollegeRegistration> findByBsaAndCreatedDateBetween(@Param("bsaId") Long bsaId,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    // Check if college is already registered with BSA
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM BSACollegeRegistration r WHERE r.bsa.bsaId = :bsaId AND r.college.id = :collegeId")
    boolean existsByBsaAndCollege(@Param("bsaId") Long bsaId, @Param("collegeId") Long collegeId);

    // Delete registrations by BSA and college IDs
    void deleteByBsaBsaIdAndCollegeId(Long bsaId, Long collegeId);
}
