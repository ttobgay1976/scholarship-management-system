package com.sprms.system.master.controller;

import com.sprms.system.core.services.BSACollegeRegistrationService;
import com.sprms.system.frmbeans.BSACollegeRegistrationDTO;
import com.sprms.system.frmbeans.BSACollegeRegistrationFormBean;
import com.sprms.system.hbmbeans.BSACollegeRegistration;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.hbmbeans.State;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.RegistrationStatus;
import com.sprms.system.master.dao.CountryRepository;
import com.sprms.system.master.dao.StateRepository;
import com.sprms.system.master.dao.CityRepository;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.core.servicesdao.BSACollegeRegistrationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bsa-college-registration")
public class BSACollegeRegistrationController {

    private final BSACollegeRegistrationService registrationService;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CollegeRegistrationRepository collegeRegistrationRepository;
    private final BSACollegeRegistrationRepository registrationRepository;

    public BSACollegeRegistrationController(
            BSACollegeRegistrationService registrationService,
            CountryRepository countryRepository,
            StateRepository stateRepository,
            CityRepository cityRepository,
            CollegeRegistrationRepository collegeRegistrationRepository,
            BSACollegeRegistrationRepository registrationRepository) {
        this.registrationService = registrationService;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.collegeRegistrationRepository = collegeRegistrationRepository;
        this.registrationRepository = registrationRepository;
    }

    // Create page - Show form to register new colleges with BSA selection
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        // Load BSAs for selection
        List<BSA> bsas = registrationService.getAllActiveBSAs();
        
        // Load countries for dropdown
        List<Country> countries = countryRepository.findAll();
        
        // Create empty form bean
        BSACollegeRegistrationFormBean formBean = new BSACollegeRegistrationFormBean();
        
        model.addAttribute("formBean", formBean);
        model.addAttribute("bsas", bsas);
        model.addAttribute("countries", countries);
        model.addAttribute("states", List.of());
        model.addAttribute("cities", List.of());
        model.addAttribute("availableColleges", List.of());
        
        return "BSACollegeRegistrationCreateFrm";
    }

    // General list page - Show all registered colleges
    @GetMapping("/list")
    public String listAllRegistrations(Model model) {
        List<BSACollegeRegistrationDTO> registrations = registrationService.getAllRegistrations();
        
        // Add overall statistics
        Long totalCount = registrationService.getTotalRegistrationCount();
        
        model.addAttribute("registrations", registrations);
        model.addAttribute("totalCount", totalCount);
        
        return "BSACollegeRegistrationListFrm";
    }

    // List page - Show all registered colleges for a BSA
    @GetMapping("/list/{bsaId}")
    public String listRegistrations(@PathVariable Long bsaId, Model model) {
        List<BSACollegeRegistrationDTO> registrations = registrationService.getRegistrationsByBSA(bsaId);
        
        // Add statistics
        Long totalCount = registrationService.getRegistrationCountByBSA(bsaId);
        
        model.addAttribute("registrations", registrations);
        model.addAttribute("bsaId", bsaId);
        model.addAttribute("totalCount", totalCount);
        
        return "BSACollegeRegistrationListFrm";
    }

    // Create page - Show form to register new colleges
    @GetMapping("/create/{bsaId}")
    public String showCreateFormWithBSA(@PathVariable Long bsaId, Model model) {
        BSACollegeRegistrationFormBean formBean = registrationService.prepareCreateFormBean(bsaId);
        
        // Load countries for dropdown
        List<Country> countries = countryRepository.findAll();
        
        model.addAttribute("formBean", formBean);
        model.addAttribute("countries", countries);
        model.addAttribute("states", List.of());
        model.addAttribute("cities", List.of());
        model.addAttribute("availableColleges", List.of());
        
        return "BSACollegeRegistrationCreateFrm";
    }

    // API endpoint to get states by country
    @GetMapping("/api/states/{countryId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getStatesByCountry(@PathVariable Long countryId) {
        List<State> states = stateRepository.findByCountryId(countryId);
        List<Map<String, Object>> stateList = states.stream()
                .map(state -> {
                    Map<String, Object> stateMap = new HashMap<>();
                    stateMap.put("stateId", state.getId());
                    stateMap.put("stateName", state.getStateName());
                    return stateMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(stateList);
    }

    // API endpoint to get cities by state
    @GetMapping("/api/cities/{stateId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getCitiesByState(@PathVariable Long stateId) {
        List<Cities> cities = cityRepository.findByStateId(stateId);
        List<Map<String, Object>> cityList = cities.stream()
                .map(city -> {
                    Map<String, Object> cityMap = new HashMap<>();
                    cityMap.put("cityId", city.getId());
                    cityMap.put("cityName", city.getCityName());
                    return cityMap;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(cityList);
    }

    // API endpoint to get colleges by location (from college table)
    @GetMapping("/api/colleges")
    @ResponseBody
    public ResponseEntity<List<BSACollegeRegistrationDTO.CollegeInfo>> getCollegesByLocation(
            @RequestParam(required = false) Long bsaId,
            @RequestParam Long countryId,
            @RequestParam Long stateId,
            @RequestParam Long cityId) {
        
        List<BSACollegeRegistrationDTO.CollegeInfo> colleges;
        if (bsaId != null) {
            // Get available colleges for this specific BSA (excluding already registered ones)
            colleges = registrationService.getAvailableCollegesForBSA(bsaId, countryId, stateId, cityId);
        } else {
            // Get all colleges for the location (for cases where BSA is not selected yet)
            colleges = registrationService.getCollegesByLocation(countryId, stateId, cityId);
        }
        return ResponseEntity.ok(colleges);
    }

    // GET handler for register endpoint - redirect to create
    @GetMapping("/register")
    public String handleRegisterGet() {
        return "redirect:/bsa-college-registration/create";
    }

    // Debug endpoint to test college loading
    @GetMapping("/api/debug/colleges")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> debugColleges() {
        List<College> allColleges = collegeRegistrationRepository.findAll();
        List<Map<String, Object>> result = allColleges.stream()
            .map(college -> {
                Map<String, Object> collegeMap = new HashMap<>();
                collegeMap.put("collegeId", college.getId());
                collegeMap.put("collegeName", college.getCollegeName());
                collegeMap.put("shortName", college.getShortName());
                collegeMap.put("countryId", college.getCountry() != null ? college.getCountry().getId() : null);
                collegeMap.put("stateId", college.getState() != null ? college.getState().getId() : null);
                collegeMap.put("cityId", college.getCity() != null ? college.getCity().getId() : null);
                return collegeMap;
            })
            .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    // Debug endpoint to test college count by location
    @GetMapping("/api/debug/college-count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> debugCollegeCount(
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) Long cityId) {
        
        Long count = registrationRepository.countCollegesByLocation(countryId, stateId, cityId);
        Map<String, Object> result = new HashMap<>();
        result.put("countryId", countryId);
        result.put("stateId", stateId);
        result.put("cityId", cityId);
        result.put("count", count);
        result.put("message", "Found " + count + " colleges matching this location");
        
        return ResponseEntity.ok(result);
    }

    // API endpoint to get BSA location data
    @GetMapping("/api/bsa/{bsaId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getBSALocation(@PathVariable Long bsaId) {
        java.util.Optional<BSA> bsaOpt = registrationService.getBSAById(bsaId);
        if (bsaOpt.isPresent()) {
            BSA bsa = bsaOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("countryId", bsa.getCountry() != null ? bsa.getCountry().getId() : null);
            response.put("stateId", bsa.getState() != null ? bsa.getState().getId() : null);
            response.put("cityId", bsa.getCity() != null ? bsa.getCity().getId() : null);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    // Process college registration
    @PostMapping("/register")
    public String registerColleges(@ModelAttribute BSACollegeRegistrationFormBean formBean,
                                   RedirectAttributes redirectAttributes,
                                   Authentication authentication) {
        
        try {
            // Validate form
            if (!registrationService.validateFormBean(formBean)) {
                redirectAttributes.addFlashAttribute("formBean", formBean);
                redirectAttributes.addFlashAttribute("errorMessage", formBean.getErrorMessage());
                return "redirect:/bsa-college-registration/create/" + formBean.getBsaId();
            }
            
            // Get username from authentication
            String username = authentication != null ? authentication.getName() : "system";
            
            // Register colleges
            List<BSACollegeRegistrationDTO> registeredColleges = 
                    registrationService.registerCollegesToBSA(formBean, username);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error registering colleges: " + e.getMessage());
        }
        
        return "redirect:/bsa-college-registration/list/" + formBean.getBsaId();
    }

    
    // Delete registration
    @PostMapping("/delete/{registrationId}")
    public String deleteRegistration(@PathVariable Long registrationId,
                                     @RequestParam Long bsaId,
                                     RedirectAttributes redirectAttributes) {
        
        try {
            registrationService.deleteRegistration(registrationId);
            redirectAttributes.addFlashAttribute("successMessage", "College registration deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting registration: " + e.getMessage());
        }
        
        return "redirect:/bsa-college-registration/list/" + bsaId;
    }

    // Activate registration
    @PostMapping("/activate/{registrationId}")
    public String activateRegistration(@PathVariable Long registrationId,
                                 @RequestParam Long bsaId,
                                 RedirectAttributes redirectAttributes) {
        try {
            registrationService.updateRegistrationStatus(registrationId, RegistrationStatus.ACTIVE, "system");
            redirectAttributes.addFlashAttribute("successMessage", "College registration activated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error activating registration: " + e.getMessage());
        }
        
        return "redirect:/bsa-college-registration/list/" + bsaId;
    }

    // Deactivate registration
    @PostMapping("/deactivate/{registrationId}")
    public String deactivateRegistration(@PathVariable Long registrationId,
                                   @RequestParam Long bsaId,
                                   RedirectAttributes redirectAttributes) {
        try {
            registrationService.updateRegistrationStatus(registrationId, RegistrationStatus.INACTIVE, "system");
            redirectAttributes.addFlashAttribute("successMessage", "College registration deactivated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deactivating registration: " + e.getMessage());
        }
        
        return "redirect:/bsa-college-registration/list/" + bsaId;
    }

    // API endpoint to check if college is already registered
    @GetMapping("/api/check-registration")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkCollegeRegistration(
            @RequestParam Long bsaId,
            @RequestParam Long collegeId) {
        
        Map<String, Object> response = new HashMap<>();
        boolean isRegistered = registrationService.isCollegeRegisteredWithBSA(bsaId, collegeId);
        
        response.put("isRegistered", isRegistered);
        response.put("message", isRegistered ? 
                "College is already registered with this BSA" : 
                "College is available for registration");
        
        return ResponseEntity.ok(response);
    }

    // Get registration details
    @GetMapping("/details/{registrationId}")
    @ResponseBody
    public ResponseEntity<BSACollegeRegistrationDTO> getRegistrationDetails(@PathVariable Long registrationId) {
        // This would need to be implemented in the service
        // For now, returning empty response
        return ResponseEntity.ok(new BSACollegeRegistrationDTO());
    }

    // Exception handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "An error occurred: " + e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
