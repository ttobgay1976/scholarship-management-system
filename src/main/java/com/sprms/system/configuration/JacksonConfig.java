package com.sprms.system.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

@Configurable
public class JacksonConfig {

	@Bean
	public Module hibernateModule() {
		Hibernate5Module module = new Hibernate5Module();
		module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false);
		return module;
	}
}
