package com.vetalzloy.projectica.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * This class configures DAO layer. All DAO classes are annotated as @Repository and 
 * placed in com.vetalzloy.projectica.dao package.
 * @author VetalZloy
 *
 */
@Configuration
@EnableTransactionManagement
@PropertySource(
		ignoreResourceNotFound = true,
		value = {
				 "classpath:dao.properties",
				 "classpath:dao-private.properties"
				})
public class DAOConfiguration {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource(){
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(env.getProperty("db.driver"));
		dataSource.setUrl(env.getProperty("db.url"));
		dataSource.setUsername(env.getProperty("db.username"));
		dataSource.setPassword(env.getProperty("db.password"));
		
		return dataSource;
	}
	
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(DataSource ds){
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		
		Properties p = new Properties();
		p.setProperty("log4j.logger.org.hibernate", 
					  env.getProperty("log4j.logger.org.hibernate"));
		
		p.setProperty("hibernate.dialect", 
					  env.getProperty("hibernate.dialect"));
		
		p.setProperty("hibernate.hbm2ddl.auto", 
					  env.getProperty("hibernate.hbm2ddl.auto"));
		
		p.setProperty("hibernate.show_sql", 
					  env.getProperty("hibernate.show_sql"));
		
		p.setProperty("hibernate.format_sql", 
					  env.getProperty("hibernate.format_sql"));
		
		p.setProperty("hibernate.use_sql_comments", 
					  env.getProperty("hibernate.use_sql_comments"));
		
		p.setProperty("hibernate.jdbc.fetch_size", 
					  env.getProperty("hibernate.jdbc.fetch_size"));
		
		p.setProperty("hibernate.enable_lazy_load_no_trans", 
					  env.getProperty("hibernate.enable_lazy_load_no_trans"));
		
		lsfb.setDataSource(ds);
		lsfb.setPackagesToScan("com.vetalzloy.projectica.model");
		lsfb.setHibernateProperties(p);
		
		return lsfb;
	}
	
	 @Autowired
	 @Bean(name = "transactionManager")
	 public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		 HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		 transactionManager.setSessionFactory(sessionFactory);
	     return transactionManager;
	 }
}
