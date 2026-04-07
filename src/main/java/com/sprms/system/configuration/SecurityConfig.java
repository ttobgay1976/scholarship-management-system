package com.sprms.system.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.sprms.system.user.services.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
//         .csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/user/openregistrationfrm", "/user/saveuser", "/css/**", "/others",
								"/images/**", "/js/**", "/api/**")
						.permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login") // custom login page
						.loginProcessingUrl("/validateUser") // POST URL to process login
						.defaultSuccessUrl("/dashboard", true) // redirect after success
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
						.invalidateHttpSession(true) // clear session
						.clearAuthentication(true) // clear authentication
						.deleteCookies("JSESSIONID"));

		return http.build();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService); // ✅ constructor injection

		authProvider.setPasswordEncoder(passwordEncoder);

		return authProvider;
	}

	/*
	 * @Bean public AuthenticationManager authManager(HttpSecurity http) throws
	 * Exception { return
	 * http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(
	 * _userService) .passwordEncoder(passwordEncoder()).and().build(); }
	 */

	/*
	 * @Bean public AuthenticationManager authManager(HttpSecurity http) throws
	 * Exception { return http.getSharedObject(AuthenticationManagerBuilder.class)
	 * .userDetailsService(_userService) .passwordEncoder(passwordEncoder()) .and()
	 * .build(); }
	 */

}
