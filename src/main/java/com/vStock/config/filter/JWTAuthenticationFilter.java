package com.vStock.config.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;

import io.jsonwebtoken.Jwts;

public class JWTAuthenticationFilter extends BasicAuthenticationFilter{
	
	private static final Logger logger = LogManager.getLogger(JWTAuthentication.class);
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
		 
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request.getRequestURI() != null && "/login".equals(request.getRequestURI())) {
//			logger.info("request uri:"+request.getRequestURI());
			chain.doFilter(request, response);
			return;
		}
		String header = request.getHeader("Authorization");
		logger.info("JWTAuth Header: "+header);
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		logger.info("After authentication: " + authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		logger.info("After setAuthentication");
		logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		chain.doFilter(request, response);
		 
	}
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			logger.info("JWTAuthenticationFilter: Token != null : "+token);
			// parse the token.
			String user = Jwts.parser()
							.setSigningKey("MyJwtSecret")
							.parseClaimsJws(token.replace("Bearer ", ""))
							.getBody()
							.getSubject();
			if (user != null) {
				logger.info("user!= null"+user.toString());
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}else{
				logger.error("JWTAuthenticationFilter:未找到該使用者");
			}
		    return null;
		}
		
		return null;
	
	} 
	
}
