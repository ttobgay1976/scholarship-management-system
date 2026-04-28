package com.sprms.system.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.core.services.BSAMembershipService;
import com.sprms.system.core.services.BSAFundRequestService;
import com.sprms.system.hbmbeans.BSAFundRequest;

@Controller
public class StatusTrackingWebController {

    private final BSAMembershipService bsaMembershipService;
    private final BSAFundRequestService bsaFundRequestService;

    public StatusTrackingWebController(BSAMembershipService bsaMembershipService, BSAFundRequestService bsaFundRequestService) {
        this.bsaMembershipService = bsaMembershipService;
        this.bsaFundRequestService = bsaFundRequestService;
    }

    // Display status tracking dashboard page
    @GetMapping("/status-tracking")
    public String showStatusTrackingPage(Model model) {
        model.addAttribute("pageTitle", "Status Tracking Dashboard");
        return "StatusTrackingFrm";
    }

    // Display membership tracking search page
    @GetMapping("/membership-tracking/search")
    public String showMembershipTrackingSearchPage(Model model) {
        model.addAttribute("pageTitle", "Membership Application Tracking");
        return "MembershipTrackingSearchFrm";
    }

    // Search membership by reference number
    @PostMapping("/membership-tracking/search")
    public String searchMembershipByReferenceNumber(@RequestParam String referenceNumber, 
                                                   RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Searching for membership with reference number: " + referenceNumber);
        try {
            var membership = bsaMembershipService.getMembershipByReferenceNumber(referenceNumber);
            System.out.println("DEBUG: Found membership: " + (membership != null ? membership.getMembershipId() : "null"));
            redirectAttributes.addFlashAttribute("membership", membership);
            redirectAttributes.addFlashAttribute("success", "Membership found successfully");
            return "redirect:/membership-tracking/results";
        } catch (Exception e) {
            System.out.println("DEBUG: Error searching for membership: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Membership not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("referenceNumber", referenceNumber);
            return "redirect:/membership-tracking/search";
        }
    }

    // Display membership tracking results page
    @GetMapping("/membership-tracking/results")
    public String showMembershipTrackingResultsPage(Model model) {
        System.out.println("DEBUG: Loading results page");
        
        model.addAttribute("pageTitle", "Membership Application Search Results");
        return "MembershipTrackingResultsFrm";
    }

    // Display fund request tracking search page
    @GetMapping("/fund-request-tracking/search")
    public String showFundRequestTrackingSearchPage(Model model) {
        model.addAttribute("pageTitle", "Fund Request Tracking");
        return "FundRequestTrackingSearchFrm";
    }

    // Search fund request by reference number
    @PostMapping("/fund-request-tracking/search")
    public String searchFundRequestByReferenceNumber(@RequestParam String referenceNumber, 
                                                   RedirectAttributes redirectAttributes) {
        System.out.println("DEBUG: Searching for fund request with reference number: " + referenceNumber);
        try {
            BSAFundRequest fundRequest = bsaFundRequestService.findByReferenceNumber(referenceNumber);
            System.out.println("DEBUG: Found fund request: " + (fundRequest != null ? fundRequest.getFundRequestId() : "null"));
            redirectAttributes.addFlashAttribute("fundRequest", fundRequest);
            redirectAttributes.addFlashAttribute("success", "Fund request found successfully");
            return "redirect:/fund-request-tracking/results";
        } catch (Exception e) {
            System.out.println("DEBUG: Error searching for fund request: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Fund request not found: " + e.getMessage());
            redirectAttributes.addFlashAttribute("referenceNumber", referenceNumber);
            return "redirect:/fund-request-tracking/search";
        }
    }

    // Display fund request tracking results page
    @GetMapping("/fund-request-tracking/results")
    public String showFundRequestTrackingResultsPage(Model model) {
        model.addAttribute("pageTitle", "Fund Request Search Results");
        return "FundRequestTrackingResultsFrm";
    }
}
