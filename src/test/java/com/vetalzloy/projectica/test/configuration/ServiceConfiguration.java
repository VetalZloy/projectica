package com.vetalzloy.projectica.test.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vetalzloy.projectica.service.MessagingService;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.service.dao.UserDAOImpl;
import com.vetalzloy.projectica.test.stub.MessagingServiceImplStub;

@Configuration
@ComponentScan(
				basePackageClasses=RootTestConfiguration.class,
				basePackages="com.vetalzloy.projectica.service")
@EnableTransactionManagement
public class ServiceConfiguration {
	
	@Bean
	public UserDAO userDAO(){
		return new UserDAOImpl();
	}
	
	@Bean
	public MessagingService messagingService(){
		return new MessagingServiceImplStub();
	}
	
	@Bean
	public DataSource dataSource(){
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:test");
		
		/*dataSource.setUrl("jdbc:h2:~/projectica;mv_store=false");
		dataSource.setUsername("sa");*/
		
		
		/*dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost/aaa");
		dataSource.setUsername("root");
		dataSource.setPassword("ope147qy");*/
		
		return dataSource;
	}
	
	@Bean
	@Autowired
	public LocalSessionFactoryBean sessionFactory(DataSource ds){
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		
		Properties p = new Properties();
		p.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		//p.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		p.setProperty("hibernate.hbm2ddl.auto", "create");
		p.setProperty("hibernate.jdbc.fetch_size", "50");
		
		
		p.setProperty("hibernate.show_sql", "true");
		p.setProperty("log4j.logger.org.hibernate", "INFO");
		/*p.setProperty("hibernate.format_sql", "true");
		p.setProperty("hibernate.use_sql_comments", "true");*/
		//p.setProperty("hibernate.enable_lazy_load_no_trans", "true");
		
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
