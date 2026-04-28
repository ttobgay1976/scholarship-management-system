package com.sprms.system.master.dao;

import com.sprms.system.hbmbeans.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRegistrationRepository extends JpaRepository<College, Long> {

    // Find colleges by country, state, city, and status
    @Query("SELECT c FROM College c WHERE c.country.id = :countryId AND c.state.id = :stateId AND c.city.id = :cityId AND c.status = :status")
    List<College> findByCountryIdAndStateIdAndCityIdAndStatus(@Param("countryId") Long countryId, 
                                                              @Param("stateId") Long stateId, 
                                                              @Param("cityId") Long cityId, 
                                                              @Param("status") Boolean status);

    // Find colleges by country
    @Query("SELECT c FROM College c WHERE c.country.id = :countryId AND c.status = true")
    List<College> findByCountryIdAndStatus(@Param("countryId") Long countryId);

    // Find colleges by country and state
    @Query("SELECT c FROM College c WHERE c.country.id = :countryId AND c.state.id = :stateId AND c.status = true")
    List<College> findByCountryIdAndStateIdAndStatus(@Param("countryId") Long countryId, 
                                                     @Param("stateId") Long stateId);

    // Find active colleges
    List<College> findByStatus(Boolean status);

    // Find college by ID
    @Query("SELECT c FROM College c WHERE c.id = :collegeId AND c.status = true")
    College findActiveCollegeById(@Param("collegeId") Long collegeId);
}
