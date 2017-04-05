package com.vetalzloy.projectica.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.vetalzloy.projectica.configuration.security.SecurityConfiguration;
import com.vetalzloy.projectica.configuration.security.SecurityConfigurerAdapter;

/**
 * Main configuration file
 * @author VetalZloy
 *
 */
@Configuration
@Import({WebAppConfiguration.class, 
		SecurityConfiguration.class, 
		SecurityConfigurerAdapter.class,
		DAOConfiguration.class, 
		ServiceConfiguration.class,
		UtilConfiguration.class
		})
public class RootConfiguration {

}
