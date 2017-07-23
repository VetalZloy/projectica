package com.vetalzloy.projectica.configuration.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.vetalzloy.projectica.service.UserService;

@Configuration
public class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionRegistry registry;
	
	@Autowired
	private AuthenticationProvider provider;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	public void init(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(provider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
		repo.setDataSource(ds);
				
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		cef.setForceEncoding(false);
		
		http.authorizeRequests().anyRequest().permitAll()
			.and().formLogin()
				  .loginPage("/login")
				  .usernameParameter("username")
				  .passwordParameter("password")
				  .successHandler(new CustomAuthenticationSuccessHandler(userService))
				  //.defaultSuccessUrl("/") Either own successHandler or default, not both
				  .failureUrl("/?login-error")
			.and().sessionManagement()
				  .maximumSessions(-1)
				  .sessionRegistry(registry)
				  .and().invalidSessionUrl("/")
			.and().rememberMe()
				  .tokenRepository(repo)
				  .authenticationSuccessHandler(new CustomAuthenticationSuccessHandler(userService))
			.and().logout().permitAll()
				  .logoutUrl("/logout")
				  .logoutSuccessHandler(new CustomLogoutSuccessHandler())
				  //.logoutSuccessUrl("/") Either own successHandler or default, not both
			.and().exceptionHandling().accessDeniedPage("/")
			.and().csrf().disable()
			.addFilterBefore(cef, UsernamePasswordAuthenticationFilter.class);
		
		http.sessionManagement().maximumSessions(-1).sessionRegistry(registry);
	}
	
}
