package com.sprms.system.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.BSAStatus;
import com.sprms.system.master.dao.BSARegistrationRepository;

@Controller
@RequestMapping("/bsa-membership")
public class BSAMembershipWebController {

    private final BSARegistrationRepository bsaRegistrationRepository;

    public BSAMembershipWebController(BSARegistrationRepository bsaRegistrationRepository) {
        this.bsaRegistrationRepository = bsaRegistrationRepository;
    }

    // Display membership registration page
    @GetMapping("/request")
    public String showMembershipRequestPage(Model model) {
        // Add any model attributes needed for the page
        model.addAttribute("pageTitle", "BSA Membership Request");
        
        // Load active BSA list from database
        List<BSA> activeBSAs = bsaRegistrationRepository.findByStatus(BSAStatus.ACTIVE);
        model.addAttribute("activeBSAs", activeBSAs);
        
        return "BSAMembershipRequestFrm";
    }

    // Display membership request view page
    @GetMapping("/view")
    public String showMembershipViewPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "BSA Membership Request Details");
        model.addAttribute("membershipId", membershipId);
        return "BSAMembershipViewDetailsFrm";
    }

    // Display membership request approve page
    @GetMapping("/approve")
    public String showMembershipApprovePage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "Approve BSA Membership Request");
        model.addAttribute("membershipId", membershipId);
        return "BSAMembershipApproveFrm";
    }

    // Display membership list page
    @GetMapping("/list")
    public String showMembershipListPage(Model model) {
        model.addAttribute("pageTitle", "My BSA Membership Requests");
        return "BSAMembershipListFrm";
    }

    // Display VP approval page
    @GetMapping("/vp-approval")
    public String showVpApprovalPage(Model model) {
        model.addAttribute("pageTitle", "BSA Membership VP Approval");
        return "BSAMembershipVpApprovalFrm";
    }

    // Display Focal Officer verification page
    @GetMapping("/focal-verification")
    public String showFocalVerificationPage(Model model) {
        model.addAttribute("pageTitle", "BSA Membership Focal Verification");
        return "BSAMembershipFocalVerificationFrm";
    }

    // Display focal verification view page
    @GetMapping("/focal-view")
    public String showFocalViewPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "BSA Membership Request Details");
        model.addAttribute("membershipId", membershipId);
        return "BSAMembershipFocalViewDetailsFrm";
    }

    // Display focal verification approve page
    @GetMapping("/focal-verify")
    public String showFocalVerifyPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "Verify BSA Membership Request");
        model.addAttribute("membershipId", membershipId);
        return "BSAMembershipFocalVerifyFrm";
    }
}
