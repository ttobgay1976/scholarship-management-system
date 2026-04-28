package com.sprms.system.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.core.services.BSAFundRequestService;
import com.sprms.system.master.dao.BSARegistrationRepository;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.frmbeans.BSAFundRequestFormBean;
import com.sprms.system.frmbeans.BSAFundRequestDTO;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/bsa-fund-request/form")
public class BSAFundRequestFormController {

    private static final Logger logger = LoggerFactory.getLogger(BSAFundRequestFormController.class);

    private final BSAFundRequestService bsaFundRequestService;
    private final UserRepository userRepository;
    private final BSARegistrationRepository bsaRepository;

    public BSAFundRequestFormController(BSAFundRequestService bsaFundRequestService,
                                        UserRepository userRepository,
                                        BSARegistrationRepository bsaRepository) {
        this.bsaFundRequestService = bsaFundRequestService;
        this.userRepository = userRepository;
        this.bsaRepository = bsaRepository;
    }

    // FR-BSA-06: Show fund request submission form
    @GetMapping("/submit")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public String showSubmitForm(Model model) {
        try {
            // Get available BSAs
            List<BSA> activeBSAs = bsaRepository.findAllActiveBSAs();
            model.addAttribute("activeBSAs", activeBSAs);

            // Create form bean
            BSAFundRequestFormBean formBean = new BSAFundRequestFormBean();
            model.addAttribute("bsaFundRequestForm", formBean);

            return "BSAFundRequestSubmissionFrm";
        } catch (Exception e) {
            logger.error("Error loading fund request submission form: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load form: " + e.getMessage());
            return "error";
        }
    }

    // FR-BSA-06: Handle fund request submission
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP')")
    public String submitFundRequest(BSAFundRequestFormBean formBean, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Get current logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate mandatory fields
            if (formBean.getActivityDescription() == null || formBean.getActivityDescription().trim().isEmpty()) {
                model.addAttribute("error", "Activity description is required");
                return showSubmitForm(model);
            }

            if (formBean.getJointAccountDetails() == null || formBean.getJointAccountDetails().trim().isEmpty()) {
                model.addAttribute("error", "Joint account details are required");
                return showSubmitForm(model);
            }

            if (formBean.getBankDetails() == null || formBean.getBankDetails().trim().isEmpty()) {
                model.addAttribute("error", "Bank details are required");
                return showSubmitForm(model);
            }

            if (formBean.getRequestedAmount() == null || formBean.getRequestedAmount().doubleValue() <= 0) {
                model.addAttribute("error", "Requested amount must be greater than 0");
                return showSubmitForm(model);
            }

            // Submit fund request
            BSAFundRequestDTO result = bsaFundRequestService.submitFundRequest(formBean, currentUser.getId());

            redirectAttributes.addFlashAttribute("success", "Fund request submitted successfully with ID: " + result.getFundRequestId());
            return "redirect:/bsa-fund-request/list";

        } catch (Exception e) {
            logger.error("Error submitting fund request: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to submit fund request: " + e.getMessage());
            return showSubmitForm(model);
        }
    }
}
