package com.vetalzloy.projectica.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This class configures service layer. All service classes are annotated as <b>@Service</b>
 * and placed in <b>com.vetalzloy.projectica.service</b> package
 * @author VetalZloy
 *
 */
@Configuration
@ComponentScan({ 
	"com.vetalzloy.projectica.service"
	})
public class ServiceConfiguration {
}
