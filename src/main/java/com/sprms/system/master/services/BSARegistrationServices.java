package com.sprms.system.master.services;

import com.sprms.system.frmbeans.BSADTO;
import com.sprms.system.frmbeans.BSAFrmBean;
import com.sprms.system.hbmbeans.*;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.master.dao.CountryRepository;
import com.sprms.system.master.dao.StateRepository;
import com.sprms.system.master.dao.CityRepository;
import com.sprms.system.modelMapper.BSADTOMapper;
import com.sprms.system.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BSARegistrationServices {

    private static final Logger logger = LoggerFactory.getLogger(BSARegistrationServices.class);

    private final BSARegistrationRepository bsaRepository;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CollegeRegistrationRepository collegeRepository;

    public BSARegistrationServices(
            BSARegistrationRepository bsaRepository,
            CountryRepository countryRepository,
            StateRepository stateRepository,
            CityRepository cityRepository,
            CollegeRegistrationRepository collegeRepository) {
        this.bsaRepository = bsaRepository;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.collegeRepository = collegeRepository;
    }

    /**
     * Save a new BSA or update existing BSA
     */
    public BSADTO saveBSA(BSAFrmBean formBean, String username) {
        try {
            logger.info("Saving BSA with code: {}", formBean.getBsaCode());

            // Validate required fields
            if (formBean.getBsaCode() == null || formBean.getBsaCode().trim().isEmpty()) {
                throw new IllegalArgumentException("BSA Code is required");
            }
            if (formBean.getBsaName() == null || formBean.getBsaName().trim().isEmpty()) {
                throw new IllegalArgumentException("BSA Name is required");
            }
            if (formBean.getCountryId() == null) {
                throw new IllegalArgumentException("Country is required");
            }
            if (formBean.getStateId() == null) {
                throw new IllegalArgumentException("State is required");
            }
            if (formBean.getCityId() == null) {
                throw new IllegalArgumentException("City is required");
            }

            BSA bsa;
            if (formBean.getBsaId() != null && formBean.getBsaId() > 0) {
                // Update existing BSA
                Optional<BSA> existing = bsaRepository.findById(formBean.getBsaId());
                if (existing.isEmpty()) {
                    throw new IllegalArgumentException("BSA not found with ID: " + formBean.getBsaId());
                }
                bsa = existing.get();
                BSADTOMapper.updateEntityFromFormBean(bsa, formBean);
                bsa.setUpdatedDate(DateUtil.getCurrentDateTime());
                bsa.setUpdatedBy(username);
                logger.info("BSA updated successfully with ID: {}", bsa.getBsaId());
            } else {
                // Create new BSA
                bsa = BSADTOMapper.toEntity(formBean);
                bsa.setBsaId(Long.parseLong(DateUtil.getUniqueID()));
                bsa.setCreatedDate(DateUtil.getCurrentDateTime());
                bsa.setCreatedBy(username);
                logger.info("New BSA created with ID: {}", bsa.getBsaId());
            }

            // Set relationships
            Country country = countryRepository.findById(formBean.getCountryId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Country ID"));
            State state = stateRepository.findById(formBean.getStateId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid State ID"));
            Cities city = cityRepository.findById(formBean.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid City ID"));

            bsa.setCountry(country);
            bsa.setState(state);
            bsa.setCity(city);

            // Optional: Set institute if provided
            if (formBean.getInstituteId() != null && formBean.getInstituteId() > 0) {
                College institute = collegeRepository.findById(formBean.getInstituteId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid Institute ID"));
                bsa.setInstitute(institute);
            }

            // Save to database
            BSA savedBSA = bsaRepository.save(bsa);
            logger.info("BSA saved successfully: {}", savedBSA.getBsaCode());

            return BSADTOMapper.toDTO(savedBSA);
        } catch (Exception e) {
            logger.error("Error saving BSA: ", e);
            throw e;
        }
    }

    /**
     * Get all BSAs
     */
    public List<BSADTO> getAllBSAs() {
        try {
            logger.info("Fetching all BSAs");
            List<BSA> bsas = bsaRepository.findAll();
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching all BSAs: ", e);
            throw e;
        }
    }

    /**
     * Get BSA by ID
     */
    public BSADTO getBSAById(Long bsaId) {
        try {
            logger.info("Fetching BSA with ID: {}", bsaId);
            Optional<BSA> bsa = bsaRepository.findById(bsaId);
            if (bsa.isEmpty()) {
                logger.warn("BSA not found with ID: {}", bsaId);
                return null;
            }
            return BSADTOMapper.toDTO(bsa.get());
        } catch (Exception e) {
            logger.error("Error fetching BSA by ID: ", e);
            throw e;
        }
    }

    /**
     * Get all active BSAs
     */
    public List<BSADTO> getAllActiveBSAs() {
        try {
            logger.info("Fetching all active BSAs");
            List<BSA> bsas = bsaRepository.findByStatus(BSAStatus.ACTIVE);
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching active BSAs: ", e);
            throw e;
        }
    }

    /**
     * Get BSAs by country
     */
    public List<BSADTO> getBSAsByCountry(Long countryId) {
        try {
            logger.info("Fetching BSAs for country ID: {}", countryId);
            List<BSA> bsas = bsaRepository.findByCountryId(countryId);
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching BSAs by country: ", e);
            throw e;
        }
    }

    /**
     * Get BSAs by country and state
     */
    public List<BSADTO> getBSAsByCountryAndState(Long countryId, Long stateId) {
        try {
            logger.info("Fetching BSAs for country ID: {} and state ID: {}", countryId, stateId);
            List<BSA> bsas = bsaRepository.findByCountryIdAndStateId(countryId, stateId);
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching BSAs by country and state: ", e);
            throw e;
        }
    }

    /**
     * Get BSAs by city
     */
    public List<BSADTO> getBSAsByCity(Long cityId) {
        try {
            logger.info("Fetching BSAs for city ID: {}", cityId);
            List<BSA> bsas = bsaRepository.findByCityId(cityId);
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching BSAs by city: ", e);
            throw e;
        }
    }

    /**
     * Get BSAs by institute
     */
    public List<BSADTO> getBSAsByInstitute(Long instituteId) {
        try {
            logger.info("Fetching BSAs for institute ID: {}", instituteId);
            List<BSA> bsas = bsaRepository.findByInstituteId(instituteId);
            return bsas.stream()
                    .map(BSADTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching BSAs by institute: ", e);
            throw e;
        }
    }

    /**
     * Delete BSA (deactivate)
     */
    public void deactivateBSA(Long bsaId, String username) {
        try {
            logger.info("Deactivating BSA with ID: {}", bsaId);
            Optional<BSA> bsa = bsaRepository.findById(bsaId);
            if (bsa.isEmpty()) {
                throw new IllegalArgumentException("BSA not found with ID: " + bsaId);
            }
            BSA bsaEntity = bsa.get();
            bsaEntity.setStatus(BSAStatus.INACTIVE);
            bsaEntity.setUpdatedDate(DateUtil.getCurrentDateTime());
            bsaEntity.setUpdatedBy(username);
            bsaRepository.save(bsaEntity);
            logger.info("BSA deactivated successfully with ID: {}", bsaId);
        } catch (Exception e) {
            logger.error("Error deactivating BSA: ", e);
            throw e;
        }
    }

    /**
     * Hard delete BSA
     */
    public void deleteBSA(Long bsaId) {
        try {
            logger.info("Deleting BSA with ID: {}", bsaId);
            if (!bsaRepository.existsById(bsaId)) {
                throw new IllegalArgumentException("BSA not found with ID: " + bsaId);
            }
            bsaRepository.deleteById(bsaId);
            logger.info("BSA deleted successfully with ID: {}", bsaId);
        } catch (Exception e) {
            logger.error("Error deleting BSA: ", e);
            throw e;
        }
    }

    /**
     * Check if BSA code exists (excluding current ID during update)
     */
    public boolean isBSACodeExists(String bsaCode, Long excludeId) {
        try {
            if (excludeId == null || excludeId == 0) {
                Optional<BSA> bsa = bsaRepository.findByBsaCode(bsaCode);
                return bsa.isPresent();
            }
            return bsaRepository.existsByBsaCodeExcludingId(bsaCode, excludeId);
        } catch (Exception e) {
            logger.error("Error checking BSA code existence: ", e);
            throw e;
        }
    }
}
