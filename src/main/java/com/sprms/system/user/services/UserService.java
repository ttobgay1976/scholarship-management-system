package com.sprms.system.user.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprms.system.frmbeans.CustomUserDetails;
import com.sprms.system.hbmbeans.Role;
import com.sprms.system.hbmbeans.User;
import com.sprms.system.hbmbeans.UserRoles;
import com.sprms.system.status.ServiceStaus;
import com.sprms.system.user.dao.UserRepository;
import com.sprms.system.utils.DateUtil;

@Service
public class UserService implements UserDetailsService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	private final UserRepository _userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this._userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = _userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
				.password(user.getPassword()) // this is encoded now
				.roles("USER") // assign at least one role
				.build();

	}

	/**
	 * Optional helper to load user by ID directly (useful for menus, etc.)
	 */
	public CustomUserDetails loadUserById(Long userId) {
		User user = _userRepository.findById(userId)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

		List<GrantedAuthority> authorities = user.getUserRoles().stream()
				.map(ur -> new SimpleGrantedAuthority("ROLE_" + ur.getRole().getRoleName()))
				.collect(Collectors.toList());

		return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
	}

	// Adding new user to system
	@Transactional
	public Optional<User> registerNewUser(User userEntity) {

		logger.info("@@@Calling the user registration method------------");

		ServiceStaus status = new ServiceStaus();

//			checking the duplicate user
		Optional<User> userExist = _userRepository.findByUsername(userEntity.getUsername());
		if (userExist.isEmpty()) {

			logger.info("@@@No duplicate user found......................");
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			userEntity.setStatus(1);
			userEntity.setId(Long.parseLong(DateUtil.getUniqueID()));
			userEntity.setCreatedat(DateUtil.getCurrentDateTime());

			logger.info("@@@Checking the Value foe ID :" + userEntity.getId());

			return Optional.of(_userRepository.save(userEntity));
		} else {

			status.setSuccess(false);
			status.setMessage("Userid/username already exist in system, try other userId/username");
			System.out.println("@@@User already exist with same UserNam");
			return Optional.empty();
		}
	}

//		list the users
	@Transactional
	public List<User> listUsers() {

		logger.info("@@@Calling the service implementation procedure---------------");

		return _userRepository.findAll();
	}

	public Optional<User> findByUsername(String username) {

		logger.info("@@@Calling the findByUsername proc...............");
		System.out.println("@@@UserName passed as value :" + username);

		return _userRepository.findByUsername(username);
	}

	// get the role by taking the user name /userID
	// prepare on date 30/03/2026
	// place : YK Office
	// Get all roles assigned to a user
	public Set<Role> getRolesByUsername(String username) {
		User user = _userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

		return user.getUserRoles().stream().map(UserRoles::getRole).collect(Collectors.toSet());
	}


}
