package com.sprms.system.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.BSAStatus;
import com.sprms.system.master.dao.BSARegistrationRepository;

@Controller
@RequestMapping("/vp-president-membership")
public class VPPresidentMembershipWebController {

    private final BSARegistrationRepository bsaRegistrationRepository;

    public VPPresidentMembershipWebController(BSARegistrationRepository bsaRegistrationRepository) {
        this.bsaRegistrationRepository = bsaRegistrationRepository;
    }

    // Display VP/President membership request page (public access)
    @GetMapping("/request-form")
    public String showMembershipRequestFormPage(Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Request");
        
        // Load active BSA list from database
        BSA[] activeBSAs = bsaRegistrationRepository.findByStatus(BSAStatus.ACTIVE).toArray(new BSA[0]);
        model.addAttribute("activeBSAs", activeBSAs);
        
        return "VPPresidentMembershipRequestFrm";
    }

    // Display VP/President membership request page (authenticated access)
    @GetMapping("/request")
    public String showMembershipRequestPage(Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Request");
        
        // Load active BSA list from database
        BSA[] activeBSAs = bsaRegistrationRepository.findByStatus(BSAStatus.ACTIVE).toArray(new BSA[0]);
        model.addAttribute("activeBSAs", activeBSAs);
        
        return "VPPresidentMembershipRequestFrm";
    }

    // Display membership request view page
    @GetMapping("/view")
    public String showMembershipViewPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Request Details");
        model.addAttribute("membershipId", membershipId);
        return "VPPresidentMembershipViewDetailsFrm";
    }

    // Display membership list page
    @GetMapping("/list")
    public String showMembershipListPage(Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Requests");
        return "VPPresidentMembershipListFrm";
    }

    // Display Focal Officer verification page
    @GetMapping("/focal-verification")
    public String showFocalVerificationPage(Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Focal Verification");
        return "VPPresidentMembershipFocalVerificationFrm";
    }

    // Display focal verification view page
    @GetMapping("/focal-view")
    public String showFocalViewPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "VP/President Membership Request Details");
        model.addAttribute("membershipId", membershipId);
        return "VPPresidentMembershipFocalViewDetailsFrm";
    }

    // Display focal verification approve page
    @GetMapping("/focal-verify")
    public String showFocalVerifyPage(@RequestParam("id") Long membershipId, Model model) {
        model.addAttribute("pageTitle", "Verify VP/President Membership Request");
        model.addAttribute("membershipId", membershipId);
        return "VPPresidentMembershipFocalVerifyFrm";
    }
}
