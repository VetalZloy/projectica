package com.vetalzloy.projectica.configuration.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * This class adds cookie with name "authorized" and value "true";
 * and redirect to '/'
 * @author VetalZloy
 *
 */
public class LoginAuthorizeCookieSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy strategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		response.addCookie(new Cookie("authorized", "true"));
		
		strategy.sendRedirect(request, response, "/");
	}

}
