package com.vetalzloy.projectica.configuration;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitilizator extends AbstractAnnotationConfigDispatcherServletInitializer{
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{RootConfiguration.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}	
	
	/**
	 * Filtering for normal Cyrillic render
	 */
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("CP1251");
		cef.setForceEncoding(false);
		return new Filter[]{cef};
	}
	
	/*
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setLoadOnStartup(1);
		
		HttpConstraintElement forceHttpsConstraint = new HttpConstraintElement(ServletSecurity.TransportGuarantee.CONFIDENTIAL);
		ServletSecurityElement securityElement = new ServletSecurityElement(forceHttpsConstraint);
		registration.setServletSecurity(securityElement);
	}*/
	
}
