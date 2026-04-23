package com.sprms.system.master.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.BSARequestDTO;
import com.sprms.system.frmbeans.BSARequestFormBean;
import com.sprms.system.hbmbeans.RequestStatus;
import com.sprms.system.master.dao.CityRepository;
import com.sprms.system.master.dao.CountryRepository;
import com.sprms.system.master.dao.StateRepository;
import com.sprms.system.master.services.BSARequestService;
import com.sprms.system.master.services.BSARegistrationServices;

@Controller
@RequestMapping("/bsa-request")
public class BSARequestController {

    private static final Logger logger = LoggerFactory.getLogger(BSARequestController.class);

    private final BSARequestService bsaRequestService;
    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final BSARegistrationServices bsaRegistrationServices;

    public BSARequestController(
            BSARequestService bsaRequestService,
            CountryRepository countryRepository,
            StateRepository stateRepository,
            CityRepository cityRepository,
            BSARegistrationServices bsaRegistrationServices) {
        this.bsaRequestService = bsaRequestService;
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.cityRepository = cityRepository;
        this.bsaRegistrationServices = bsaRegistrationServices;
    }

    @GetMapping("/registrationfrm")
    public String displayBSARequestForm(Model model) {
        try {
            logger.info("Displaying BSA Request Form");
            model.addAttribute("countries", countryRepository.findAll());
            model.addAttribute("activeBSAs", bsaRegistrationServices.getAllBSAs());
            model.addAttribute("bsaRequestForm", new BSARequestFormBean());
            return "BSARequestFrm";
        } catch (Exception e) {
            logger.error("Error displaying BSA request form: ", e);
            model.addAttribute("error", "Error loading form. Please try again.");
            return "redirect:/bsa-request/list";
        }
    }

    @PostMapping("/submit")
    public String submitBSARequest(
            @ModelAttribute BSARequestFormBean bsaRequestForm,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            logger.info("Submitting BSA request: {}", bsaRequestForm.getBsaName());

            String username = authentication != null ? authentication.getName() : "ANONYMOUS";
            bsaRequestService.submitRequest(bsaRequestForm, username);

            redirectAttributes.addFlashAttribute("message", "BSA registration request submitted successfully");
            redirectAttributes.addFlashAttribute("alertType", "success");
            return "redirect:/bsa-request/list";

        } catch (IllegalArgumentException e) {
            logger.warn("Validation error while submitting BSA request: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa-request/registrationfrm";
        } catch (Exception e) {
            logger.error("Error submitting BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error submitting request. Please try again.");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa-request/registrationfrm";
        }
    }

    @GetMapping("/list")
    public String listBSARequests(Model model, 
                               @RequestParam(defaultValue = "ALL") String status) {
        try {
            logger.info("Fetching BSA requests with status filter: {}", status);
            
            if ("PENDING".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getPendingRequests());
            } else if ("APPROVED".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getRequestsByStatus(RequestStatus.APPROVED));
            } else if ("REJECTED".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getRequestsByStatus(RequestStatus.REJECTED));
            } else {
                model.addAttribute("requests", bsaRequestService.getAllRequests());
            }
            
            model.addAttribute("currentStatus", status);
            model.addAttribute("stats", bsaRequestService.getRequestStats());
            
            return "BSARequestListFrm";
        } catch (Exception e) {
            logger.error("Error fetching BSA requests: ", e);
            model.addAttribute("error", "Error loading requests list");
            return "BSARequestListFrm";
        }
    }

    @GetMapping("/approval")
    public String listBSARequestsApproval(Model model, 
                               @RequestParam(defaultValue = "PENDING") String status) {
        try {
            logger.info("Fetching BSA requests with status filter: {}", status);
            
            if ("PENDING".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getPendingRequests());
            } else if ("APPROVED".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getRequestsByStatus(RequestStatus.APPROVED));
            } else if ("REJECTED".equals(status)) {
                model.addAttribute("requests", bsaRequestService.getRequestsByStatus(RequestStatus.REJECTED));
            } else {
                model.addAttribute("requests", bsaRequestService.getAllRequests());
            }
            
            model.addAttribute("currentStatus", status);
            model.addAttribute("stats", bsaRequestService.getRequestStats());
            
            return "BSARequestApprovalFrm";
        } catch (Exception e) {
            logger.error("Error fetching BSA requests: ", e);
            model.addAttribute("error", "Error loading requests list");
            return "BSARequestApprovalFrm";
        }
    }
    
    @GetMapping("/view/{id}")
    public String viewBSARequest(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Viewing BSA request with ID: {}", id);
            BSARequestDTO request = bsaRequestService.getRequestById(id);

            if (request == null) {
                redirectAttributes.addFlashAttribute("error", "Request not found");
                return "redirect:/bsa-request/list";
            }

            model.addAttribute("request", request);
            return "BSARequestViewFrm";
        } catch (Exception e) {
            logger.error("Error viewing BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error loading request details");
            return "redirect:/bsa-request/list";
        }
    }

    /**
     * BSA Request Approval Page
     */
    @GetMapping("/review/{id}")
    public String approveBSARequestPage(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Loading BSA request approval page for ID: {}", id);
            BSARequestDTO request = bsaRequestService.getRequestById(id);

            if (request == null) {
                redirectAttributes.addFlashAttribute("error", "Request not found");
                return "redirect:/bsa-request/list";
            }

            model.addAttribute("request", request);
            return "BSARequestApprovalFrm";
        } catch (Exception e) {
            logger.error("Error loading BSA request approval page: ", e);
            redirectAttributes.addFlashAttribute("error", "Error loading approval page");
            return "redirect:/bsa-request/list";
        }
    }

    @GetMapping("/approve/{id}")
    public String approveBSARequest(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Viewing BSA request with ID: {}", id);
            BSARequestDTO request = bsaRequestService.getRequestById(id);

            if (request == null) {
                redirectAttributes.addFlashAttribute("error", "Request not found");
                return "redirect:/bsa-request/list";
            }

            model.addAttribute("request", request);
            return "BSARequestApprovalViewFrm";
        } catch (Exception e) {
            logger.error("Error viewing BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error loading request details");
            return "redirect:/bsa-request/list";
        }
    }

    @PostMapping("/process-approve/{id}")
    public String processBSARequestApproval(
            @PathVariable Long id,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            logger.info("Processing BSA request approval for ID: {}", id);
            
            String username = authentication != null ? authentication.getName() : "SYSTEM";
            
            if ("APPROVE".equals(action)) {
                bsaRequestService.approveRequest(id, username, remarks, RequestStatus.APPROVED, remarks);
                redirectAttributes.addFlashAttribute("message", "BSA request approved successfully");
                redirectAttributes.addFlashAttribute("alertType", "success");
            } else if ("REJECT".equals(action)) {
                bsaRequestService.approveRequest(id, username, remarks, RequestStatus.REJECTED, remarks);
                redirectAttributes.addFlashAttribute("message", "BSA request rejected successfully");
                redirectAttributes.addFlashAttribute("alertType", "success");
            } else {
                redirectAttributes.addFlashAttribute("error", "Invalid action specified");
                redirectAttributes.addFlashAttribute("alertType", "danger");
            }
            
            return "redirect:/bsa-request/approval";
        } catch (Exception e) {
            logger.error("Error approving BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error approving request");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa-request/approval";
        }
    }

    // @PostMapping("/approve/{id}")
    // public String approveBSARequest(
    //         @PathVariable Long id,
    //         @RequestParam(required = false) String remarks,
    //         @RequestParam(required = false) String action,
    //         RedirectAttributes redirectAttributes,
    //         Authentication authentication) {
    //     try {
    //         logger.info("Processing BSA request approval for ID: {}", id);
            
    //         // If action is not specified, redirect to approval page
    //         if (action == null) {
    //             return "redirect:/bsa-request/review/" + id;
    //         }
            
    //         // Process the approval
    //         String username = authentication != null ? authentication.getName() : "SYSTEM";
    //         bsaRequestService.approveRequest(id, username, remarks);
            
    //         redirectAttributes.addFlashAttribute("message", "BSA request approved successfully");
    //         redirectAttributes.addFlashAttribute("alertType", "success");
    //         return "redirect:/bsa-request/list";
    //     } catch (Exception e) {
    //         logger.error("Error approving BSA request: ", e);
    //         redirectAttributes.addFlashAttribute("error", "Error approving request");
    //         redirectAttributes.addFlashAttribute("alertType", "danger");
    //         return "redirect:/bsa-request/list";
    //     }
    // }

    /**
     * Reject BSA Request
     */
    @PostMapping("/reject/{id}")
    public String rejectBSARequest(
            @PathVariable Long id,
            @RequestParam(required = false) String remarks,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            logger.info("Rejecting BSA request with ID: {}", id);
            
            String username = authentication != null ? authentication.getName() : "SYSTEM";
            bsaRequestService.rejectRequest(id, username, remarks);
            
            redirectAttributes.addFlashAttribute("message", "BSA request rejected successfully");
            redirectAttributes.addFlashAttribute("alertType", "warning");
            return "redirect:/bsa-request/list";
        } catch (Exception e) {
            logger.error("Error rejecting BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error rejecting request");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa-request/list";
        }
    }

    /**
     * Delete BSA Request
     */
    @GetMapping("/delete/{id}")
    public String deleteBSARequest(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        try {
            logger.info("Deleting BSA request with ID: {}", id);
            bsaRequestService.deleteRequest(id);
            
            redirectAttributes.addFlashAttribute("message", "BSA request deleted successfully");
            redirectAttributes.addFlashAttribute("alertType", "success");
            return "redirect:/bsa-request/list";
        } catch (Exception e) {
            logger.error("Error deleting BSA request: ", e);
            redirectAttributes.addFlashAttribute("error", "Error deleting request");
            redirectAttributes.addFlashAttribute("alertType", "danger");
            return "redirect:/bsa-request/list";
        }
    }

    /**
     * API endpoint to get states by country (for cascading dropdown)
     */
    @GetMapping("/api/states")
    @ResponseBody
    public String getStatesByCountry(@RequestParam Long countryId) {
        try {
            return stateRepository.findByCountryId(countryId).stream()
                    .map(state -> "{\"id\":" + state.getId() + ",\"name\":\"" + state.getStateName() + "\"}")
                    .reduce("[", (acc, item) -> acc + "," + item) + "]";
        } catch (Exception e) {
            logger.error("Error fetching states", e);
            return "[]";
        }
    }

    /**
     * API endpoint to get cities by state (for cascading dropdown)
     */
    @GetMapping("/api/cities")
    @ResponseBody
    public String getCitiesByState(@RequestParam Long stateId) {
        try {
            return cityRepository.findByStateId(stateId).stream()
                    .map(city -> "{\"id\":" + city.getId() + ",\"name\":\"" + city.getCityName() + "\"}")
                    .reduce("[", (acc, item) -> acc + "," + item) + "]";
        } catch (Exception e) {
            logger.error("Error fetching cities", e);
            return "[]";
        }
    }
}
