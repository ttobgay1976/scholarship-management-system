package com.sprms.system.master.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sprms.system.frmbeans.BankDTO;
import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.frmbeans.ScholarshipProgramDTO;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.FundingAgency;
import com.sprms.system.master.dao.BankRepository;
import com.sprms.system.master.services.BankServices;
import com.sprms.system.master.services.CollegeRegistrationServices;
import com.sprms.system.master.services.FundingAgencyService;
import com.sprms.system.master.services.ScholarshipProgramService;

@Controller
@RequestMapping("/master")
public class CommonMastersController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CommonMastersController.class);

	// setting the static variable for the form call
	private static String DISPLAY_FUNDING_AGENCY_REGISTRATION_FRM = "FundingAgencyFrm";
	private static String DISPLAY_FUNDING_AGENCY_LIST_FRM = "FundingAgencyLstFrm";

	// scholarship Program Registration Form
	private static String DISPLAY_SCHLOARSHIP_PROGRAM_REG_FRM = "ScholarshipProgramFrm";
	private static String DISPLAY_SCHLOARSHIP_PROGRAM_REG_LIST_FRM = "ScholarshipProgramLstFrm";

	// bank regitration
	private static String DISPLAY_BANK_REGISTRATION_FRM = "BankRegistrationFrm";
	private static String DISPLAY_BANK_REGISTRATION_LIST_FRM = "BankRegistrationLstFrm";

	// call repository
	private final FundingAgencyService _fundingAgencyService;
	private final ScholarshipProgramService _scholarshipProgramService;
	private final CollegeRegistrationServices _collegeRegistrationServices;
	private final BankServices _bankServices;

	// constructor
	public CommonMastersController(FundingAgencyService fundingAgencyService,
			ScholarshipProgramService scholarshipProgramService,
			CollegeRegistrationServices collegeRegistrationServices, BankServices bankServices) {
		this._fundingAgencyService = fundingAgencyService;
		this._scholarshipProgramService = scholarshipProgramService;
		this._collegeRegistrationServices = collegeRegistrationServices;
		this._bankServices = bankServices;

	}

	// get the Funding Agency Entry form
	@GetMapping("/fundingagencyfrm")
	public String getFundingAgencyRegistrationfrm(Model model) {

		logger.info("@@@Calling this getFundingAgencyRegistrationfrm proc................");

		model.addAttribute("fundingaagencydto", new FundingAgencyDTO());

		return DISPLAY_FUNDING_AGENCY_REGISTRATION_FRM;
	}

	// save the Funding Agency Name
	@PostMapping("/addfundingagency")
	public String saveFundingAgency(@ModelAttribute("fundingaagencydto") FundingAgencyDTO fundingAgencyDTO,
			RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the saveFundingAgency proc...............");

		FundingAgencyDTO saved = _fundingAgencyService.saveFundingAgency(fundingAgencyDTO);

		// check the save status
		if (saved != null && saved.getId() != null) {
			redirectAttributes.addFlashAttribute("message", "Information saved succesfully");
		} else {
			redirectAttributes.addFlashAttribute("message", "Failed to save the Information, try again");
		}

		return "redirect:/master/fundingagencyfrm";
	}

	// get the agency list
	@GetMapping("/fundingagencylst")
	public String getAllFundingAgencies(Model model) {

		logger.info("@@@Calling the getAllFundingAgencies proc.............. ");

		List<FundingAgencyDTO> fundingagencylist = _fundingAgencyService.getAllFundingAgencies();
		model.addAttribute("fundingagencylist", fundingagencylist);

		return DISPLAY_FUNDING_AGENCY_LIST_FRM;
	}

	// edit the FundingAgency data by calling the fundingAgency Id
	/// date 07/04/2026

	@GetMapping("/fundingagency/edit/{id}")
	public String editFundingRegistration(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the editFundingRegistration proc....................");

		FundingAgencyDTO fundingAgencyDTO = _fundingAgencyService.getFundingAgencyById(id);

		System.out.println("@@@Value fetch :" + fundingAgencyDTO.getFundingAgencyName());

		model.addAttribute("fundingaagencydto", fundingAgencyDTO);

		return DISPLAY_FUNDING_AGENCY_REGISTRATION_FRM;
	}

	// delete the Agency registration
	// date : 07/04*/2026
	@GetMapping("/fundingagency/delete/{id}")
	public String deleteFundingAgency(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		_fundingAgencyService.deleteFundingAgency(id);

		redirectAttributes.addFlashAttribute("message", "Deleted successfully");

		return "redirect:/master/fundingagencylst";
	}

	
	// Scholarship Program Details
	// created on dt /05/04/2026
	// place : at home
	
	// get the Scholarship Program Registration form
	@GetMapping("/scholarshipprogramregistrationfrm")
	public String getScholarshipProgramRegistrationFrm(Model model) {

		logger.info("@@@Calling the Scholarship Program Registration frm.................");

		model.addAttribute("scholarshipProgramdto", new ScholarshipProgramDTO());

		// get the list of funding agency name
		List<FundingAgencyDTO> fundingagencylst = _fundingAgencyService.getAllFundingAgencies();
		model.addAttribute("fundingagencylst", fundingagencylst);

		return DISPLAY_SCHLOARSHIP_PROGRAM_REG_FRM;
	}

	// save the Scholarship Program Registration data
	@PostMapping("/savescholarshipprogramregistration")
	public String saveScholarshipProgramregistration(@ModelAttribute("") ScholarshipProgramDTO scholarshipProgramDTO,
			RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the saveScholarshipProgramregistration proc....................");

		try {
			// save the value passed
			_scholarshipProgramService.saveScholarshipProgram(scholarshipProgramDTO);

			// if save successful then give this mag
			redirectAttributes.addFlashAttribute("message", "Scholarship Program saved successfully!");

		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", "Failed to save:" + e.getMessage());
		}

		return "redirect:/master/scholarshipprogramregistrationfrm";
	}

	// get the Scholarship Program Registration lists
	@GetMapping("/scholarshipprogramregistrationlst")
	public String getAllScholarshipprogramregistration(Model model) {

		logger.info("@@@Calling this getAllScholarshipprogramregistration proc..............");

		model.addAttribute("scholarshipprogramlst", _scholarshipProgramService.getAll());

		return DISPLAY_SCHLOARSHIP_PROGRAM_REG_LIST_FRM;
	}
	
	// edit the scholarshipprogramregistration data by calling the registration Id
	// date 07/04/2026
	@GetMapping("/scholarshipprogramregistration/edit/{id}")
	public String editScholarshipprogramregistration(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the editFundingRegistration proc....................");

		ScholarshipProgramDTO scholarshipProgramDTO = _scholarshipProgramService.getById(id);
		model.addAttribute("scholarshipProgramdto", scholarshipProgramDTO);
		
		List<FundingAgencyDTO> fundingagencylst = _fundingAgencyService.getAllFundingAgencies();
		model.addAttribute("fundingagencylst", fundingagencylst);

		return DISPLAY_SCHLOARSHIP_PROGRAM_REG_FRM;
	}
	
	// delete the Scholarship registration
	// date : 07/04*/2026
	@GetMapping("/scholarshipprogramregistration/delete/{id}")
	public String deleteScholarshipRegistrationProgram(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the deleteScholarshipRegistrationProgram proc...........");
		
		_scholarshipProgramService.deleteScholarshipProgram(id);

		redirectAttributes.addFlashAttribute("message", "Deleted successfully");

		return "redirect:/master/scholarshipprogramregistrationlst";
	}
	

	// Bank Registration Form
	// This form will map the College and it banks details
	@GetMapping("/bankregistrationfrm")
	public String getBankRegistrationfrm(Model model) {

		logger.info("@@@Calling the getBankRegistrationfrm........................ ");

		model.addAttribute("bank", new BankDTO());

	    //colleges details to dropdown
		List<College> colleges = _collegeRegistrationServices.getAllColleges();
		model.addAttribute("colleges", colleges);

		return DISPLAY_BANK_REGISTRATION_FRM;
	}

	// save the College Bank Registration
	@PostMapping("/addcollegebank")
	public String saveCollegeBankDetails(@ModelAttribute("bank") BankDTO bankDTO,
			RedirectAttributes redirectAttributes) {

		logger.info("@@@Calling the saveCollegeBankDetails proc...................");

		try {
			// check and save the information
			_bankServices.saveCollegeBank(bankDTO);
			redirectAttributes.addFlashAttribute("message", "Information save successfully.");

		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("message", "information could not be saved "+e.getMessage());
		}

		return "redirect:/master/bankregistrationfrm";
	}

	// list the Registered bank with colleges
	@GetMapping("/bankregistrationlstfrm")
	public String getRegisteredBankDetails(Model model) {

		logger.info("@@@Callin the getRegisteredBankDetails proc...............");

		model.addAttribute("banks", _bankServices.getAllRegisteredBanks());

		return DISPLAY_BANK_REGISTRATION_LIST_FRM;
	}
	
	//bank Edit option
	//created on 07/04/2026
	@GetMapping("/bankregistration/edit/{id}")
	public String editBankRegistration(@PathVariable Long id, Model model) {

	    logger.info("@@@Calling editBankRegistration................");

	    BankDTO dto = _bankServices.getByID(id);

	    model.addAttribute("bank", dto);
	    
	    //colleges details to dropdown
		List<College> colleges = _collegeRegistrationServices.getAllColleges();
		model.addAttribute("colleges", colleges);
		

	    return DISPLAY_BANK_REGISTRATION_FRM;
	}
	
	//delete the Bank Registration
	@GetMapping("/bankregistration/delete/{id}")
	public String deleteBank(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		try {
			_bankServices.deleteBankById(id);
			redirectAttributes.addFlashAttribute("message", "Deleted successfully");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "Delete failed: " + e.getMessage());
		}

		return "redirect:/master/bankregistrationlstfrm";
	}
}
