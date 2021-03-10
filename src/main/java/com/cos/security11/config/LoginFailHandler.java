package com.cos.security11.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFailHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String errormsg = exception.getMessage();
				
		log.info("onAuthenticationFailure ::::  {}", errormsg);
		log.info("onAuthenticationFailure ::::  {}", exception);
		
//		new DefaultRedirectStrategy().sendRedirect(request, response, "/login");
		new DefaultRedirectStrategy().sendRedirect(request, response, "/error");
	}

}
