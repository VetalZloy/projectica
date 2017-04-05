package com.vetalzloy.projectica.test.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vetalzloy.projectica.service.PositionService;
import com.vetalzloy.projectica.service.PositionServiceImpl;
import com.vetalzloy.projectica.service.ProjectService;
import com.vetalzloy.projectica.service.ProjectServiceImpl;
import com.vetalzloy.projectica.service.RequestService;
import com.vetalzloy.projectica.service.RequestServiceImpl;
import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.UserServiceImpl;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.dao.PositionDAOImpl;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAOImpl;
import com.vetalzloy.projectica.service.dao.RequestDAO;
import com.vetalzloy.projectica.service.dao.RequestDAOImpl;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.service.dao.UserDAOImpl;

@Configuration
@ComponentScan(basePackageClasses = UtilConfiguration.class)
@EnableTransactionManagement
public class ServiceConfiguration {
	
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
		p.setProperty("hibernate.show_sql", "true");
		p.setProperty("hibernate.hbm2ddl.auto", "create");
		p.setProperty("log4j.logger.org.hibernate", "INFO");
		
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
	
	@Bean
	public UserService userService(){
		return new UserServiceImpl();
	}	
	
	@Bean
	public ProjectService projectService() {
		return new ProjectServiceImpl();
	}
	
	@Bean
	@Qualifier("userDAOImpl")
	public UserDAO userDAO(){
		return new UserDAOImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PositionService positionService(){
		return new PositionServiceImpl();
	}
	
	@Bean
	public PositionDAO positionDAO(){
		return new PositionDAOImpl();
	}
	
	@Bean
	public ProjectDAO projectDAO(){
		return new ProjectDAOImpl();
	}
	
	@Bean
	public RequestService requestService(){
		return new RequestServiceImpl();
	}
	
	@Bean
	public RequestDAO requestDAO(){
		return new RequestDAOImpl();
	}
	
}