package com.vetalzloy.projectica.configuration.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.vetalzloy.projectica.service.UserService;
import com.vetalzloy.projectica.service.exception.UserNotFoundException;
import com.vetalzloy.projectica.util.SecurityUtil;

/**
 * This class adds cookie with name "authorized" and value "true";
 * and redirect to '/'
 * @author VetalZloy
 *
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	private RedirectStrategy strategy = new DefaultRedirectStrategy();
	
	private UserService userService;
	
	public CustomAuthenticationSuccessHandler(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		try {
			String currentUsername = SecurityUtil.getCurrentUsername();
			long id = userService.getByUsername(currentUsername).getUserId();
			request.getSession().setAttribute("id", "|BEGIN|" + id);
		} catch (UserNotFoundException e) {
			logger.warn("Error happened during login.", e);
		}
		
		//request.getSession().setAttribute("username", "|BEGIN|" + SecurityUtil.getCurrentUsername());
		response.addCookie(new Cookie("authorized", "true"));
		
		strategy.sendRedirect(request, response, "/");
	}

}
