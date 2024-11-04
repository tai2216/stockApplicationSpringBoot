package com.vStock.config.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager authenticationManager;
	
	private static final Logger logger = LogManager.getLogger(JWTLoginFilter.class);
	
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		logger.info("req content type: "+request.getContentType());
		logger.info("req encoding: "+request.getCharacterEncoding());
		logger.info("req content length: "+request.getContentLength());
		logger.info("User Name: "+request.getParameter("username"));
		logger.info("Password: "+request.getParameter("password"));

		try {	
		    Authentication authenticate = authenticationManager.authenticate(
										    new UsernamePasswordAuthenticationToken(
										    request.getParameter("username"),
										    request.getParameter("password"),
										       new ArrayList<>())
			);
		    logger.info("getName: "+authenticate.getName());
		    logger.info("isAuthenticated: "+authenticate.isAuthenticated());
		    logger.info("toString: "+authenticate.toString());
		    return authenticate;
	    }catch(AuthenticationException ae) {
			response.addHeader("Log In Error", ae.getMessage());
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		    logger.error("JWTLoginFilter: 使用者"+request.getParameter("username")+"驗證失敗: "+ae.getMessage());
		    throw ae;
	    }
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetails user = (UserDetails)authResult.getPrincipal();
		logger.info("user: "+new ObjectMapper().writeValueAsString(user));
		if(!user.isEnabled()) {
			response.addHeader("Log In Error", "This account is not enabled");
			response.sendError(403, "This account is not enabled");
		}else {
			logger.info("Response Header: "+response.getHeader("Access-Control-Allow-Origin"));
			logger.info("successful");
			logger.info("JWT END-------------------");
			String token = Jwts.builder()
					.setIssuer("Sam")
					.setSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
					.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
					.signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
					.compact();
			logger.info(token);
			response.addHeader("loginUserName", authResult.getName());
			response.addHeader("Authorization", "Bearer " + token);
			response.addHeader("role", user.getAuthorities().toString());
//			response.addHeader("authResult", new ObjectMapper().writeValueAsString(authResult));
		}
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		logger.error("Unsuccessful, 驗證失敗: "+failed.getMessage());
		super.unsuccessfulAuthentication(request, response, failed);
	}
	



	
	
	
	
	
	
	

}
