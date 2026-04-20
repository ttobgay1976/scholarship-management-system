package com.sprms.system.core.serviceController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sprms.system.applicationEnums.ApplicationStatus;
import com.sprms.system.core.services.EmailServices;
import com.sprms.system.core.services.ScholarshipRegistrationServices;
import com.sprms.system.core.services.SupportingFilesServices;
import com.sprms.system.frmbeans.ApproverActionDTO;
import com.sprms.system.frmbeans.ScholarshipRegistrationDTO;
import com.sprms.system.frmbeans.SupportingFilesDTO;
import com.sprms.system.frmbeans.VerifierActionDTO;

//this controller is created to created centralised SORMS Core Service
//this service will handle all the application process like Verification, Approval, rejection 
//created 13/04/2026
//Place : YK office

@Controller
@RequestMapping("/service")
public class CoreServiceController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CoreServiceController.class);

	// call the service
	private final ScholarshipRegistrationServices _scholarshipRegistrationServices;
	private final SupportingFilesServices _supportingFilesServices;
	
	//mail testing 
	private final EmailServices _emailServices;

	// constructor
	public CoreServiceController(ScholarshipRegistrationServices scholarshipRegistrationServices,
			SupportingFilesServices supportingFilesServices,EmailServices emailServices) {
		this._scholarshipRegistrationServices = scholarshipRegistrationServices;
		this._supportingFilesServices = supportingFilesServices;
		this._emailServices=emailServices;
	}

	// setting the static variable for the form call
	private static String DISPLAY_SCHOLARSHIP_REGISTRATION_APPLICATION_LIST_FRM = "ScholarshipRegistrationApplicationLstFrm";
	private static String DISPLAY_SCHOLARSHIP_REGISTRATION_APPLICATION_VERIFY_FRM = "ScholarshipRegistrationApplicationVerifyFrm";
	private static String DISPLAY_APPLICATION_FORWARDEDBY_VERIFIER_FRM = "ScholarshipRegistrationApplicationPendingApprovalLstFrm";
	private static String DISPLAY_APPLICATION_APPROVAL_FRM = "ScholarshipRegistrationApplicationApprovalFrm";
	private static String DISPLAY_SCHOLARSHIP_APPLICATION_APPROVED_LIST_FRM = "ScholarshipRegistrationApplicationApprovedLstFrm";

	// This page is use for Search purpose
	// created 16/04/2026
	private static String DISPLAY_SEARCH_FRM = "ApplicationSearchFrm";

	// --------THIS SECTION BELOW CODE ARE USED FOR
	// APPLICATION VERIFICATION PURPOSE------------//
	// --------CREATED ON
	// 14/04/2026----------------------------------//

	// get the Application list by taking the status "SUBMITTED" for the
	// verification Focal Dashboard
	@GetMapping("/scholarshipregistrationapplst")
	public String getScholarshipRegistrationApplication(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("@@@Calling the getScholarshipRegistrationApplication proc .................");
		try {

			// check the for error
			Page<ScholarshipRegistrationDTO> applicaitonlst = _scholarshipRegistrationServices
					.getApplicationByStatus(ApplicationStatus.SUBMITTED, page, size);

			model.addAttribute("schapplsts", applicaitonlst);

		} catch (Exception e) {
			e.getMessage();
		}

		return DISPLAY_SCHOLARSHIP_REGISTRATION_APPLICATION_LIST_FRM;
	}

	// Get the Application verification Form
	@GetMapping("/scholarshipregistration/verification/{id}")
	public String getScholarshipApplicationVerificationFrm(@PathVariable long id, Model model) {

		logger.info("@@@Calling the getScholarshipApplicationVerificationFrm proc...............");

		try {

			// get the information by ID
			ScholarshipRegistrationDTO scholarshipRegistrationDTO = _scholarshipRegistrationServices.getById(id);
			model.addAttribute("schapplication", scholarshipRegistrationDTO);

			// get the supporting documents along with application search
			List<SupportingFilesDTO> supportingFilesDTOs = _supportingFilesServices.getFilesByApplicationId(id);
			model.addAttribute("files", supportingFilesDTOs);

			// get the enums for the verifier and send to form
			List<ApplicationStatus> verifierStatuses = List.of(ApplicationStatus.VERIFIED, ApplicationStatus.REJECTED,
					ApplicationStatus.SENT_BACK_TO_APPLICANT);

			model.addAttribute("statuses", verifierStatuses);

			// this for the remaks to save
			VerifierActionDTO dto = new VerifierActionDTO();
			dto.setApplicationId(scholarshipRegistrationDTO.getId());
			model.addAttribute("verifierActionDTO", dto);

		} catch (Exception e) {

			e.getMessage();
		}

		return DISPLAY_SCHOLARSHIP_REGISTRATION_APPLICATION_VERIFY_FRM;
	}

	// save the verifier comment and status for the application
	// created 14/04/2026
	@PostMapping("/saveapplicationverifierremark")
	public String saveScholarshipApplicationVerifierRemarks(@ModelAttribute VerifierActionDTO dto, Model model) {

		logger.info("@@@Calling the saveScholarshipApplicationVerifierRemarks proc ............");

		try {

			// check the application and save the verifier remarks
			_scholarshipRegistrationServices.updateVerifierAction(dto);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "redirect:/service/scholarshipregistrationapplst";
	}

	// -------THIS SECTION BELOW CODE ARE USED FOR
	// APPLICATION APPROVAL PURPOSE------------//
	// -------CREATED ON
	// 15/04/2026------------------------------//

	// get the application list forwarded by verifier the approval dashboard
	@GetMapping("/applicationlstforwardedbyverifier")
	public String getApplicationlstforwardedbyverifier(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("@@@Calling the getApplicationlstforwardedbyverifier proc................");

		try {

			// check the for error
			Page<ScholarshipRegistrationDTO> applicaitonlst = _scholarshipRegistrationServices
					.getApplicationByStatus(ApplicationStatus.VERIFIED, page, size);

			model.addAttribute("schapplsts", applicaitonlst);

		} catch (Exception e) {

			e.getMessage();
		}
		return DISPLAY_APPLICATION_FORWARDEDBY_VERIFIER_FRM;
	}

	// Get the Application verification Form
	@GetMapping("/scholarshipregistration/approver/{id}")
	public String getScholarshipApplicationApprovalFrm(@PathVariable long id, Model model) {

		logger.info("@@@Calling the getScholarshipApplicationApprovalFrm proc...............");

		try {

			// get the information by ID
			ScholarshipRegistrationDTO scholarshipRegistrationDTO = _scholarshipRegistrationServices.getById(id);
			model.addAttribute("schapplication", scholarshipRegistrationDTO);

			// get the supporting documents along with application search
			List<SupportingFilesDTO> supportingFilesDTOs = _supportingFilesServices.getFilesByApplicationId(id);
			model.addAttribute("files", supportingFilesDTOs);

			// get the enums for the verifier and send to form
			List<ApplicationStatus> verifierStatuses = List.of(ApplicationStatus.APPROVED,
					ApplicationStatus.SENT_BACK_TO_VERIFIER);

			model.addAttribute("statuses", verifierStatuses);

			// this for the remaks to save
			ApproverActionDTO dto = new ApproverActionDTO();
			dto.setApplicationId(scholarshipRegistrationDTO.getId());
			model.addAttribute("approverActionDTO", dto);

		} catch (Exception e) {

			e.getMessage();
		}

		return DISPLAY_APPLICATION_APPROVAL_FRM;
	}

	// save the Approver comment and status for the application
	// created 15/04/2026
	@PostMapping("/saveapplicationapproverremark")
	public String saveScholarshipApplicationApproverRemarks(@ModelAttribute ApproverActionDTO dto, Model model) {

		logger.info("@@@Calling the saveScholarshipApplicationApproverRemarks proc ............");

		try {

			// check the application and save the verifier remarks
			_scholarshipRegistrationServices.updateApproverAction(dto);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "redirect:/service/applicationlstforwardedbyverifier";
	}

	// get all the approved application for reporting purpose
	// created 15/04/2026
	@GetMapping("/scholarshipapplicationapprovedlst")
	public String getAllScholarshipapplicationapprovedlst(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		logger.info("@@@Calling the getAllScholarshipapplicationapprovedlst proc...............");

		try {

			// check the for error
			Page<ScholarshipRegistrationDTO> applicaitonlst = _scholarshipRegistrationServices
					.getApplicationByStatus(ApplicationStatus.APPROVED, page, size);

			model.addAttribute("schapplsts", applicaitonlst);

		} catch (Exception e) {

			e.getMessage();
		}

		return DISPLAY_SCHOLARSHIP_APPLICATION_APPROVED_LIST_FRM;
	}

	// search/Track controller for the Application status
	@GetMapping("/trackapplicationstatus")
	public String trackApplicationByCid(Model model) {

		logger.info("@@@Calling the  trackApplicationByCid proc..................");

		return DISPLAY_SEARCH_FRM;
	}

	@GetMapping("/getpplicationstatus")
	public String getApplicationStatusByCid(@RequestParam("cid") String citizenId, Model model) {

		logger.info("@@@Calling the  getApplicationStatusByCid proc..................");

		System.out.println("CID received: " + citizenId);

		List<ScholarshipRegistrationDTO> applications = _scholarshipRegistrationServices.getByCid(citizenId);

		//check for the null or empty list return
		if (applications == null || applications.isEmpty()) {

			model.addAttribute("message", "No application found for this CID, try entering correct CID No.");

		} else {
			model.addAttribute("applications", applications);
		}

		return DISPLAY_SEARCH_FRM;
	}
	
	
    @GetMapping("/test")
    public String testMail() {

    	logger.info("@@@Calling the testMail proc................");
    	
        _emailServices.sendTestEmail("ttobgay@tech.gov.bt");

        return "Email sent successfully!";
    }
}
