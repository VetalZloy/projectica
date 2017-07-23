package com.vetalzloy.projectica.configuration.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * This class adds cookie with name "authorized" and value "false";
 * and redirect to '/'
 * @author VetalZloy
 *
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

	private RedirectStrategy strategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
		response.addCookie(new Cookie("authorized", "false"));
		
		strategy.sendRedirect(request, response, "/");
		
	}

}
