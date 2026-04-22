package com.sprms.system.master.controller;

import com.sprms.system.frmbeans.BSADTO;
import com.sprms.system.master.services.BSARegistrationServices;
import com.sprms.system.wrapper.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bsa")
@CrossOrigin(origins = "*")
public class BSAApiController {

    private static final Logger logger = LoggerFactory.getLogger(BSAApiController.class);

    private final BSARegistrationServices bsaRegistrationServices;

    public BSAApiController(BSARegistrationServices bsaRegistrationServices) {
        this.bsaRegistrationServices = bsaRegistrationServices;
    }

    /**
     * Get all BSAs
     */
    @GetMapping("/all")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getAllBSAs() {
        try {
            logger.info("API: Fetching all BSAs");
            List<BSADTO> bsas = bsaRegistrationServices.getAllBSAs();
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("BSAs fetched successfully");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching all BSAs: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSAs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get active BSAs
     */
    @GetMapping("/active")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getActiveBSAs() {
        try {
            logger.info("API: Fetching active BSAs");
            List<BSADTO> bsas = bsaRegistrationServices.getAllActiveBSAs();
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("Active BSAs fetched successfully");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching active BSAs: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching active BSAs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get BSA by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse<BSADTO>> getBSAById(@PathVariable Long id) {
        try {
            logger.info("API: Fetching BSA with ID: {}", id);
            BSADTO bsa = bsaRegistrationServices.getBSAById(id);
            ServiceResponse<BSADTO> response = new ServiceResponse<>();

            if (bsa == null) {
                response.setSuccess(false);
                response.setMessage("BSA not found with ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response.setSuccess(true);
            response.setMessage("BSA fetched successfully");
            response.setData(bsa);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching BSA by ID: ", e);
            ServiceResponse<BSADTO> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSA: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get BSAs by country
     */
    @GetMapping("/country/{countryId}")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getBSAsByCountry(@PathVariable Long countryId) {
        try {
            logger.info("API: Fetching BSAs for country ID: {}", countryId);
            List<BSADTO> bsas = bsaRegistrationServices.getBSAsByCountry(countryId);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("BSAs fetched successfully for country");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching BSAs by country: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSAs by country: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get BSAs by country and state
     */
    @GetMapping("/country/{countryId}/state/{stateId}")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getBSAsByCountryAndState(
            @PathVariable Long countryId,
            @PathVariable Long stateId) {
        try {
            logger.info("API: Fetching BSAs for country ID: {} and state ID: {}", countryId, stateId);
            List<BSADTO> bsas = bsaRegistrationServices.getBSAsByCountryAndState(countryId, stateId);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("BSAs fetched successfully for country and state");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching BSAs by country and state: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSAs: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get BSAs by city
     */
    @GetMapping("/city/{cityId}")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getBSAsByCity(@PathVariable Long cityId) {
        try {
            logger.info("API: Fetching BSAs for city ID: {}", cityId);
            List<BSADTO> bsas = bsaRegistrationServices.getBSAsByCity(cityId);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("BSAs fetched successfully for city");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching BSAs by city: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSAs by city: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get BSAs by institute
     */
    @GetMapping("/institute/{instituteId}")
    public ResponseEntity<ServiceResponse<List<BSADTO>>> getBSAsByInstitute(@PathVariable Long instituteId) {
        try {
            logger.info("API: Fetching BSAs for institute ID: {}", instituteId);
            List<BSADTO> bsas = bsaRegistrationServices.getBSAsByInstitute(instituteId);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(true);
            response.setMessage("BSAs fetched successfully for institute");
            response.setData(bsas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("API: Error fetching BSAs by institute: ", e);
            ServiceResponse<List<BSADTO>> response = new ServiceResponse<>();
            response.setSuccess(false);
            response.setMessage("Error fetching BSAs by institute: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
