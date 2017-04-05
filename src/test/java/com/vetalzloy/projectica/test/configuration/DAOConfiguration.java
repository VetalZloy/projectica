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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.vetalzloy.projectica.service.dao.ChatMessageDAO;
import com.vetalzloy.projectica.service.dao.ChatMessageDAOImpl;
import com.vetalzloy.projectica.service.dao.ChatRoomDAO;
import com.vetalzloy.projectica.service.dao.ChatRoomDAOImpl;
import com.vetalzloy.projectica.service.dao.DialogMessageDAO;
import com.vetalzloy.projectica.service.dao.DialogMessageDAOImpl;
import com.vetalzloy.projectica.service.dao.PositionDAO;
import com.vetalzloy.projectica.service.dao.PositionDAOImpl;
import com.vetalzloy.projectica.service.dao.ProjectDAO;
import com.vetalzloy.projectica.service.dao.ProjectDAOImpl;
import com.vetalzloy.projectica.service.dao.RequestDAO;
import com.vetalzloy.projectica.service.dao.RequestDAOImpl;
import com.vetalzloy.projectica.service.dao.TagDAO;
import com.vetalzloy.projectica.service.dao.TagDAOImpl;
import com.vetalzloy.projectica.service.dao.UserDAO;
import com.vetalzloy.projectica.service.dao.UserDAOImpl;
import com.vetalzloy.projectica.test.helper.proxy.ChatMessageDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.ChatRoomDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.DialogMessageDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.PositionDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.ProjectDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.RequestDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.TagDAOProxy;
import com.vetalzloy.projectica.test.helper.proxy.UserDAOProxy;

@Configuration
@ComponentScan(basePackages = {
								"com.vetalzloy.projectica.test.dao",
								"com.vetalzloy.projectica.test.helper"
								})
@EnableTransactionManagement
public class DAOConfiguration {
	
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
		p.setProperty("hibernate.format_sql", "true");
		p.setProperty("hibernate.use_sql_comments", "true");
		p.setProperty("hibernate.hbm2ddl.auto", "create");
		p.setProperty("hibernate.jdbc.fetch_size", "50");
		//p.setProperty("hibernate.enable_lazy_load_no_trans", "true");
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
	@Qualifier("userDAOImpl")
	public UserDAO userDAOImpl(){
		return new UserDAOImpl();
	}	 
	 
	@Bean
	@Qualifier("userDAOProxy")
	public UserDAO userDAOProxy(){
		return new UserDAOProxy();
	}
	
	@Bean
	@Qualifier("projectDAOProxy")
	public ProjectDAO projectDAOProxy(){
		return new ProjectDAOProxy();
	}
	
	@Bean
	@Qualifier("positionDAOImpl")
	public ProjectDAO projectDAOImpl(){
		return new ProjectDAOImpl();
	}
	
	@Bean
	@Qualifier("positionDAOProxy")
	public PositionDAO positionDAOProxy(){
		return new PositionDAOProxy();
	}
	
	@Bean
	@Qualifier("projectDAOImpl")
	public PositionDAO positionDAOImpl(){
		return new PositionDAOImpl();
	}
	
	@Bean
	@Qualifier("requestDAOProxy")
	public RequestDAO requestDAOProxy(){
		return new RequestDAOProxy();
	}
	
	@Bean
	@Qualifier("requestDAOImpl")
	public RequestDAO requestDAOImpl(){
		return new RequestDAOImpl();
	}
	
	@Bean
	@Qualifier("tagDAOProxy")
	public TagDAO tagDAOProxy(){
		return new TagDAOProxy();
	}
	
	@Bean
	@Qualifier("tagDAOImpl")
	public TagDAO tagDAOImpl(){
		return new TagDAOImpl();
	}
	
	@Bean
	@Qualifier("dialogMessageDAOProxy")
	public DialogMessageDAO dialogDAOProxy(){
		return new DialogMessageDAOProxy();
	}
	
	@Bean
	@Qualifier("dialogMessageDAOImpl")
	public DialogMessageDAO dialogDAOImpl(){
		return new DialogMessageDAOImpl();
	}
	
	@Bean
	@Qualifier("chatRoomDAOProxy")
	public ChatRoomDAO chatRoomDAOProxy(){
		return new ChatRoomDAOProxy();
	}
	
	@Bean
	@Qualifier("chatRoomDAOImpl")
	public ChatRoomDAO chatRoomDAOImpl(){
		return new ChatRoomDAOImpl();
	}
	
	@Bean
	@Qualifier("chatMessageDAOProxy")
	public ChatMessageDAO chatMessageDAOProxy(){
		return new ChatMessageDAOProxy();
	}
	
	@Bean
	@Qualifier("chatMessageDAOImpl")
	public ChatMessageDAO chatMessageDAOImpl(){
		return new ChatMessageDAOImpl();
	}
}