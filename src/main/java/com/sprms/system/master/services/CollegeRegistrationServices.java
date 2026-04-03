package com.sprms.system.master.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.CollegeFrmBean;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.master.dao.CollegeRegistrationRepository;
import com.sprms.system.modelMapper.CollegeFrmBeanMapper;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

@Service
public class CollegeRegistrationServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CollegeRegistrationServices.class);

	// call the repository
	private final CollegeRegistrationRepository _collegeRegistrationRepository;

	// constructor
	public CollegeRegistrationServices(CollegeRegistrationRepository collegeRegistrationRepository ) {
		this._collegeRegistrationRepository = collegeRegistrationRepository;

	}

	// Save the new colleges
	public ServiceResponse<College> saveCollege(College college) {
		ServiceResponse<College> response = new ServiceResponse<>();
		
		logger.info("@@@Calling the college save proc-------------");
		
		try {
			
			//assign the ID and created Date manually to ID setter
			college.setId(Long.parseLong(DateUtil.getUniqueID()));
			college.setCreatedDate(DateUtil.getCurrentDateTime());
			
			//check the value and save the new information to table/database
			College saveCollege =  _collegeRegistrationRepository.save(college);
			return new ServiceResponse<College>(true, "Information Saved Successfully", saveCollege);
			
		} catch (Exception e) {
			// TODO: handle exception
			return new ServiceResponse<College>(false, "Failed to save information", null);
		}
	}

	//  List all the colleges  
	public List<College> getAllColleges() {
		
		logger.info("@@@calling list colleges proc------------");
		//get the colleges and map to frmbean and then return
		List<College> colleges =_collegeRegistrationRepository.findAll();
		
		//map to frmBean and then return
		//List<CollegeFrmBean> collegeFrmBean = _collegeFrmBeanMapper.toFrmBean(colleges);
				
		return _collegeRegistrationRepository.findAll();
	}

	// get Colleges by its ID
	public College getCollegeById(Long id) {
		
		logger.info("@@@caliing the get college by id proc----------------");
		return _collegeRegistrationRepository.findById(id).orElse(null);
	}

	// delete college by its Id
	public void deleteCollegeById(Long id) {
		
		logger.info("@@@caliing the delete college proc---------------");
		_collegeRegistrationRepository.deleteById(id);
	}
}
