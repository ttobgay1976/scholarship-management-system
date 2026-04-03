package com.sprms.system.user.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.system.hbmbeans.Menu;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.user.dao.MenuRepository;
import com.sprms.system.utils.DateUtil;
import com.sprms.system.wrapper.ServiceResponse;

@Service
public class MenuServices_Old {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(MenuServices_Old.class);
	
	//call the repo
	private final MenuRepository _menuRepository;
	
	//constructor
	public MenuServices_Old(MenuRepository menuRepository) {
		this._menuRepository=menuRepository;
	}
	
	//saving the new menu
	public ServiceResponse<Menu> saveRole(Menu menu) {

		logger.info("@@@Calling the role service save proc----------------");

		try {
			//set the value to ID
			menu.setId(Long.parseLong(DateUtil.getUniqueID()));
			
			//call the save proc
			Menu saveMenu =_menuRepository.saveAndFlush(menu);
			
			return new ServiceResponse<Menu>(true, "New Mneu Saved", saveMenu);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ServiceResponse<Menu>(false, "Failed to save information",null);
		}
	}
}
