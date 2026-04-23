package com.sprms.system.master.controller;

import com.sprms.system.frmbeans.BSADTO;
import com.sprms.system.frmbeans.BSAFrmBean;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.hbmbeans.State;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.master.dao.CityRepository;
import com.sprms.system.master.dao.CountryRepository;
import com.sprms.system.master.dao.StateRepository;
import com.sprms.system.master.services.BSARegistrationServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/bsa")
public class BSARegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(BSARegistrationController.class);

    private final BSARegistrationServices bsaRegistrationServices;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CollegeRegistrationRepository collegeRepository;

    public BSARegistrationController(
            BSARegistrationServices bsaRegistrationServices,
            CountryRepository countryRepository,
            StateRepository stateRepository,
            CityRepository cityRepository,
            CollegeRegistrationRepository collegeRepository) {
        this.bsaRegistrationServices = bsaRegistrationServices;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.collegeRepository = collegeRepository;
    }

    /**
     * Display BSA Registration Form
     */
    @GetMapping("/registrationfrm")
    public String displayBSARegistrationForm(Model model) {
        try {
            logger.info("Displaying BSA Registration Form");
            List<Country> lstCountry = countryRepository.findAll();
            model.addAttribute("lstcountry", lstCountry);
            model.addAttribute("bsafrmbean", new BSAFrmBean());
            return "BSARegistrationFrm";
        } catch (Exception e) {
            logger.error("Error displaying BSA registration form: ", e);
            model.addAttribute("error", "Error loading form. Please try again.");
            return "redirect:/bsa/lstbsas";
        }
    }

    /**
     * Save BSA
     */
    @PostMapping("/register")
    public String saveBSA(
            @ModelAttribute BSAFrmBean bsafrmbean,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            logger.info("Saving BSA: {}", bsafrmbean.getBsaCode());

            String username = authentication != null ? authentication.getName() : "SYSTEM";
            BSADTO savedBSA = bsaRegistrationServices.saveBSA(bsafrmbean, username);

            if (bsafrmbean.getBsaId() != null && bsafrmbean.getBsaId() > 0) {
                redirectAttributes.addFlashAttribute("message", "BSA updated successfully");
                redirectAttributes.addFlashAttribute("alertType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "BSA registered successfully");
                redirectAttributes.addFlashAttribute("alertType", "success");
            }

            return "redirect:/bsa/lstbsas";
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error while saving BSA: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa/registrationfrm";
        } catch (Exception e) {
            logger.error("Error saving BSA: ", e);
            redirectAttributes.addFlashAttribute("error", "Error saving BSA. Please try again.");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa/registrationfrm";
        }
    }

    /**
     * Display Edit BSA Form
     */
    @GetMapping("/registration/edit/{id}")
    public String editBSAForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Editing BSA with ID: {}", id);
            BSADTO bsaDTO = bsaRegistrationServices.getBSAById(id);

            if (bsaDTO == null) {
                redirectAttributes.addFlashAttribute("error", "BSA not found");
                return "redirect:/bsa/lstbsas";
            }

            List<Country> lstCountry = countryRepository.findAll();
            List<State> lstState = stateRepository.findByCountryId(bsaDTO.getCountryId());
            List<Cities> lstCity = cityRepository.findByStateId(bsaDTO.getStateId());
            List<College> lstCollege = collegeRepository.findAll();

            model.addAttribute("lstcountry", lstCountry);
            model.addAttribute("lststate", lstState);
            model.addAttribute("lstcity", lstCity);
            model.addAttribute("lstcollege", lstCollege);
            model.addAttribute("bsafrmbean", convertToFormBean(bsaDTO));

            return "BSARegistrationFrm";
        } catch (Exception e) {
            logger.error("Error editing BSA: ", e);
            redirectAttributes.addFlashAttribute("error", "Error loading BSA for edit");
            return "redirect:/bsa/lstbsas";
        }
    }

    /**
     * List all BSAs
     */
    @GetMapping("/lstbsas")
    public String listBSAs(Model model) {
        try {
            logger.info("Fetching all BSAs");
            List<BSADTO> bsas = bsaRegistrationServices.getAllBSAs();
            model.addAttribute("bsas", bsas);
            return "BSAListFrm";
        } catch (Exception e) {
            logger.error("Error fetching BSAs: ", e);
            model.addAttribute("error", "Error loading BSA list");
            return "BSAListFrm";
        }
    }

    /**
     * Deactivate BSA
     */
    @GetMapping("/registration/deactivate/{id}")
    public String deactivateBSA(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            logger.info("Deactivating BSA with ID: {}", id);
            String username = authentication != null ? authentication.getName() : "SYSTEM";
            bsaRegistrationServices.deactivateBSA(id, username);
            redirectAttributes.addFlashAttribute("message", "BSA deactivated successfully");
            redirectAttributes.addFlashAttribute("alertType", "success");
            return "redirect:/bsa/lstbsas";
        } catch (Exception e) {
            logger.error("Error deactivating BSA: ", e);
            redirectAttributes.addFlashAttribute("error", "Error deactivating BSA");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa/lstbsas";
        }
    }

    /**
     * Delete BSA (hard delete)
     */
    @GetMapping("/registration/delete/{id}")
    public String deleteBSA(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            logger.info("Deleting BSA with ID: {}", id);
            bsaRegistrationServices.deleteBSA(id);
            redirectAttributes.addFlashAttribute("message", "BSA deleted successfully");
            redirectAttributes.addFlashAttribute("alertType", "success");
            return "redirect:/bsa/lstbsas";
        } catch (Exception e) {
            logger.error("Error deleting BSA: ", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting BSA");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa/lstbsas";
        }
    }

    // Helper Methods
    private BSAFrmBean convertToFormBean(BSADTO dto) {
        BSAFrmBean formBean = new BSAFrmBean();
        formBean.setBsaId(dto.getBsaId());
        formBean.setBsaCode(dto.getBsaCode());
        formBean.setBsaName(dto.getBsaName());
        formBean.setDescription(dto.getDescription());
        formBean.setCountryId(dto.getCountryId());
        formBean.setStateId(dto.getStateId());
        formBean.setCityId(dto.getCityId());
        formBean.setInstituteId(dto.getInstituteId());
        formBean.setStatus(dto.getStatus());
        return formBean;
    }
}
