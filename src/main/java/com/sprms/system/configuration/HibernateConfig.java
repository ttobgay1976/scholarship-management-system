package com.sprms.system.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	// Properties of database

	@Value("${spring.datasource.driver-class-name}")
	private String driver;

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	// Properties of hibernate

	@Value("${spring.jpa.properties.hibernate.dialect}")
	private String dialect;

	@Value("${spring.jpa.show-sql}")
	private String show_sql;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddl_update;

	@Value("${packagesToScan}")
	private String packagesToScan;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

//	@Bean(name = "entityManagerFactory")
//	public LocalSessionFactoryBean sessionFactoryBean() {
//		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
//
//		// set datasource
//		sessionFactoryBean.setDataSource(dataSource());
//
//		// set properties
//		Properties properties = new Properties();
//		properties.put("org.hibernate.dialect.MySQLDialect", dialect);
//		properties.put("hibernate.show_sql", show_sql);
//		//properties.put("hibernate.hbm2ddl.auto", ddl_update);
//
//		sessionFactoryBean.setHibernateProperties(properties);
//
//		// packageToScan
//		sessionFactoryBean.setPackagesToScan(packagesToScan);
//		return sessionFactoryBean;
//	}
//
//	@Bean(name = "transactionManager")
//	public HibernateTransactionManager hibernateTransactionManager() {
//		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
//		transactionManager.setSessionFactory(sessionFactoryBean().getObject());
//		return transactionManager;
//	}
//
//	@Bean
//	public MessageSource messageSource() {
//		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
//		source.setBasename("messages");
//		return source;
//	}


	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}


}
