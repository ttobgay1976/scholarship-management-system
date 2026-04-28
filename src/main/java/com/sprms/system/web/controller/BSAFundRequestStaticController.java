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
@RequestMapping("/bsa-fund-request")
public class BSAFundRequestStaticController {

    private static final Logger logger = LoggerFactory.getLogger(BSAFundRequestStaticController.class);

    private final BSAFundRequestService bsaFundRequestService;
    private final UserRepository userRepository;
    private final BSARegistrationRepository bsaRepository;

    public BSAFundRequestStaticController(BSAFundRequestService bsaFundRequestService,
                                         UserRepository userRepository,
                                         BSARegistrationRepository bsaRepository) {
        this.bsaFundRequestService = bsaFundRequestService;
        this.userRepository = userRepository;
        this.bsaRepository = bsaRepository;
    }

    // Show fund request list
    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('PRESIDENT', 'VP', 'FOCAL_OFFICER', 'CHIEF', 'ACCOUNTANT', 'ADMIN')")
    public String showFundRequestList(org.springframework.data.domain.Pageable pageable, Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User currentUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            org.springframework.data.domain.Page<BSAFundRequestDTO> fundRequests;

            // Different views based on user role
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FOCAL_OFFICER"))) {
                fundRequests = bsaFundRequestService.getPendingFundRequestsForVerification(pageable);
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHIEF"))) {
                fundRequests = bsaFundRequestService.getVerifiedFundRequestsForChiefApproval(pageable);
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ACCOUNTANT"))) {
                fundRequests = bsaFundRequestService.getChiefApprovedFundRequestsForAccountant(pageable);
            } else {
                // For PRESIDENT, VP, ADMIN - show their own requests
                fundRequests = bsaFundRequestService.getFundRequestsByUser(currentUser.getId(), pageable);
            }

            model.addAttribute("fundRequests", fundRequests);
            model.addAttribute("currentPage", pageable.getPageNumber());
            model.addAttribute("pageSize", pageable.getPageSize());
            model.addAttribute("totalPages", fundRequests.getTotalPages());

            return "BSAFundRequestListFrm";
        } catch (Exception e) {
            logger.error("Error loading fund request list: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load fund requests: " + e.getMessage());
            return "error";
        }
    }

    // Show fund request statistics
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'FOCAL_OFFICER', 'CHIEF', 'ACCOUNTANT')")
    public String showStatistics(Model model) {
        try {
            List<Object[]> statistics = bsaFundRequestService.getFundRequestStatistics();
            
            java.util.Map<String, Long> statsMap = new java.util.HashMap<>();
            for (Object[] stat : statistics) {
                String status = stat[0].toString();
                Long count = Long.valueOf(stat[1].toString());
                statsMap.put(status.toLowerCase(), count);
            }
            
            model.addAttribute("statistics", statsMap);
            return "BSAFundRequestStatisticsFrm";
        } catch (Exception e) {
            logger.error("Error loading statistics: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load statistics: " + e.getMessage());
            return "error";
        }
    }
}
