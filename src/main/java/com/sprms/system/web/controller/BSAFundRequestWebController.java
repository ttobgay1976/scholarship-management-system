package com.sprms.system.web.controller;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.core.services.BSAFundRequestService;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.frmbeans.BSAFundRequestDTO;
import com.sprms.system.frmbeans.FundRequestVerificationDTO;
import com.sprms.system.hbmbeans.User;

@Controller
@RequestMapping("/bsa-fund-request")
public class BSAFundRequestWebController {

    private static final Logger logger = LoggerFactory.getLogger(BSAFundRequestWebController.class);

    private final BSAFundRequestService bsaFundRequestService;
    private final UserRepository userRepository;
    private final BSARegistrationRepository bsaRepository;

    public BSAFundRequestWebController(BSAFundRequestService bsaFundRequestService,
                                       UserRepository userRepository,
                                       BSARegistrationRepository bsaRepository) {
        this.bsaFundRequestService = bsaFundRequestService;
        this.userRepository = userRepository;
        this.bsaRepository = bsaRepository;
    }

    
    
    // Show fund request details
    @GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP', 'FOCAL_OFFICER', 'CHIEF', 'ACCOUNTANT', 'ADMIN')")
    public String showFundRequestDetails(@PathVariable Long id, Model model) {
        try {
            BSAFundRequestDTO fundRequest = bsaFundRequestService.getFundRequestById(id);
            model.addAttribute("fundRequest", fundRequest);

            // Get current user for role-based actions
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            model.addAttribute("currentUserRole", auth.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .collect(Collectors.toList()));

            return "BSAFundRequestViewFrm";
        } catch (Exception e) {
            logger.error("Error loading fund request details: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load fund request details: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-07: Show focal officer verification form with pending requests
    @GetMapping("/verify")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public String showVerificationForm(Model model) {
        try {
            // Get all pending fund requests for verification
            org.springframework.data.domain.Page<BSAFundRequestDTO> pendingRequests = 
                bsaFundRequestService.getPendingFundRequestsForVerification(
                    org.springframework.data.domain.PageRequest.of(0, 50));
            
            model.addAttribute("pendingRequests", pendingRequests.getContent());
            model.addAttribute("verificationForm", new FundRequestVerificationDTO());

            return "BSAFundRequestVerificationFrm";
        } catch (Exception e) {
            logger.error("Error loading verification form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load verification form: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/verify/{id}")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public String showVerificationFormForSpecificRequest(@PathVariable Long id, Model model) {
        try {
            BSAFundRequestDTO fundRequest = bsaFundRequestService.getFundRequestById(id);
            
            // Check if fund request is in PENDING status
            if (!"PENDING".equals(fundRequest.getFundRequestStatus())) {
                model.addAttribute("error", "This fund request cannot be verified. Current status: " + fundRequest.getFundRequestStatus());
                return "redirect:/bsa-fund-request/list";
            }

            model.addAttribute("fundRequest", fundRequest);
            model.addAttribute("verificationForm", new FundRequestVerificationDTO());

            return "BSAFundRequestVerificationFrm";
        } catch (Exception e) {
            logger.error("Error loading verification form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load verification form: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-07: Process focal officer verification
    @PostMapping("/verify/{id}")
    @PreAuthorize("hasRole('FOCAL_OFFICER')")
    public String processVerification(@PathVariable Long id, FundRequestVerificationDTO verificationDTO, 
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set fund request ID
            verificationDTO.setFundRequestId(id);

            // Validate verification data
            if (verificationDTO.getVerifierStatus() == null || verificationDTO.getVerifierStatus().trim().isEmpty()) {
                model.addAttribute("error", "Please select a verification decision (Verify or Reject)");
                return showVerificationFormForSpecificRequest(id, model);
            }
            
            if ("VERIFIED".equals(verificationDTO.getVerifierStatus()) && 
                (verificationDTO.getApprovedAmount() == null || verificationDTO.getApprovedAmount().doubleValue() <= 0)) {
                model.addAttribute("error", "Approved amount is required when verifying a fund request");
                return showVerificationFormForSpecificRequest(id, model);
            }
            
            if (verificationDTO.getVerifierRemarks() == null || verificationDTO.getVerifierRemarks().trim().isEmpty()) {
                model.addAttribute("error", "Verification remarks are required");
                return showVerificationFormForSpecificRequest(id, model);
            }

            // Process verification
            BSAFundRequestDTO result = bsaFundRequestService.processFocalVerification(verificationDTO, currentUser.getId());

            redirectAttributes.addFlashAttribute("success", "Fund request verification processed successfully");
            return "redirect:/bsa-fund-request/list";

        } catch (Exception e) {
            logger.error("Error processing verification: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to process verification: " + e.getMessage());
            return showVerificationFormForSpecificRequest(id, model);
        }
    }

    // FR-BSA-08: Show chief approval form
    @GetMapping("/chief-approve/{id}")
    @PreAuthorize("hasRole('CHIEF')")
    public String showChiefApprovalForm(@PathVariable Long id, Model model) {
        try {
            BSAFundRequestDTO fundRequest = bsaFundRequestService.getFundRequestById(id);
            
            // Check if fund request is in FOCAL_VERIFIED status
            if (!"FOCAL_VERIFIED".equals(fundRequest.getFundRequestStatus())) {
                model.addAttribute("error", "This fund request cannot be approved. Current status: " + fundRequest.getFundRequestStatus());
                return "redirect:/bsa-fund-request/list";
            }

            model.addAttribute("fundRequest", fundRequest);
            model.addAttribute("approvalForm", new FundRequestVerificationDTO());

            return "BSAFundRequestChiefApprovalFrm";
        } catch (Exception e) {
            logger.error("Error loading chief approval form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load approval form: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-08: Process chief approval
    @PostMapping("/chief-approve/{id}")
    @PreAuthorize("hasRole('CHIEF')")
    public String processChiefApproval(@PathVariable Long id, FundRequestVerificationDTO approvalDTO, 
                                     Model model, RedirectAttributes redirectAttributes) {
        try {
            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set fund request ID
            approvalDTO.setFundRequestId(id);

            // Process approval
            BSAFundRequestDTO result = bsaFundRequestService.processChiefApproval(approvalDTO, currentUser.getId());

            redirectAttributes.addFlashAttribute("success", "Fund request chief approval processed successfully");
            return "redirect:/bsa-fund-request/list";

        } catch (Exception e) {
            logger.error("Error processing chief approval: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to process chief approval: " + e.getMessage());
            return showChiefApprovalForm(id, model);
        }
    }

    // FR-BSA-08: Show accountant approval form
    @GetMapping("/accountant-approve/{id}")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public String showAccountantApprovalForm(@PathVariable Long id, Model model) {
        try {
            BSAFundRequestDTO fundRequest = bsaFundRequestService.getFundRequestById(id);
            
            // Check if fund request is in CHIEF_APPROVED status
            if (!"CHIEF_APPROVED".equals(fundRequest.getFundRequestStatus())) {
                model.addAttribute("error", "This fund request cannot be approved. Current status: " + fundRequest.getFundRequestStatus());
                return "redirect:/bsa-fund-request/list";
            }

            model.addAttribute("fundRequest", fundRequest);
            model.addAttribute("approvalForm", new FundRequestVerificationDTO());

            return "BSAFundRequestAccountantApprovalFrm";
        } catch (Exception e) {
            logger.error("Error loading accountant approval form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load approval form: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-08: Process accountant approval
    @PostMapping("/accountant-approve/{id}")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public String processAccountantApproval(@PathVariable Long id, FundRequestVerificationDTO approvalDTO, 
                                          Model model, RedirectAttributes redirectAttributes) {
        try {
            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Set fund request ID
            approvalDTO.setFundRequestId(id);

            // Process approval
            BSAFundRequestDTO result = bsaFundRequestService.processAccountantApproval(approvalDTO, currentUser.getId());

            redirectAttributes.addFlashAttribute("success", "Fund request accountant approval processed successfully");
            return "redirect:/bsa-fund-request/list";

        } catch (Exception e) {
            logger.error("Error processing accountant approval: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to process accountant approval: " + e.getMessage());
            return showAccountantApprovalForm(id, model);
        }
    }

    
    // FR-BSA-08: Show chief approval form with verified requests
    @GetMapping("/chief-approve")
    @PreAuthorize("hasRole('CHIEF')")
    public String showChiefApprovalList(Model model) {
        try {
            // Get all verified fund requests for chief approval
            org.springframework.data.domain.Page<BSAFundRequestDTO> verifiedRequests = 
                bsaFundRequestService.getVerifiedFundRequestsForChiefApproval(
                    org.springframework.data.domain.PageRequest.of(0, 50));
            
            model.addAttribute("verifiedRequests", verifiedRequests.getContent());
            model.addAttribute("approvalForm", new FundRequestVerificationDTO());

            return "BSAFundRequestChiefApprovalFrm";
        } catch (Exception e) {
            logger.error("Error loading chief approval form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load chief approval form: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-08: Show accountant approval form with chief approved requests
    @GetMapping("/accountant-approve")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public String showAccountantApprovalList(Model model) {
        try {
            // Get all chief approved fund requests for accountant approval
            org.springframework.data.domain.Page<BSAFundRequestDTO> chiefApprovedRequests = 
                bsaFundRequestService.getChiefApprovedFundRequestsForAccountant(
                    org.springframework.data.domain.PageRequest.of(0, 50));
            
            model.addAttribute("chiefApprovedRequests", chiefApprovedRequests.getContent());
            model.addAttribute("approvalForm", new FundRequestVerificationDTO());

            return "BSAFundRequestAccountantApprovalFrm";
        } catch (Exception e) {
            logger.error("Error loading accountant approval form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load accountant approval form: " + e.getMessage());
            return "error";
        }
    }
}
