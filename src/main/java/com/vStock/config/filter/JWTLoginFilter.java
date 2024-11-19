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

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vStock.json.LoginJson;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager authenticationManager;
	
	private String jwtSecretKey;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public JWTLoginFilter (AuthenticationManager authenticationManager, String jwtSecretKey) {
		super(authenticationManager);
		this.authenticationManager = authenticationManager;
		this.jwtSecretKey = jwtSecretKey;
		
	}
	private static final Logger logger = LogManager.getLogger(JWTLoginFilter.class);
	
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		logger.debug("req content type: "+request.getContentType());
		logger.debug("req encoding: "+request.getCharacterEncoding());
		logger.debug("req content length: "+request.getContentLength());
		logger.debug("User Name: "+request.getParameter("username"));
		logger.debug("Password: "+request.getParameter("password"));
		LoginJson loginJson = null;
		try {
//			loginJson = objectMapper.readValue(request.getReader().lines().reduce(new String(),String::concat), LoginJson.class);
			loginJson = objectMapper.readValue(request.getReader(), LoginJson.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("登入資訊有誤: "+e.getMessage());
		}
		if(loginJson==null) {
			throw new RuntimeException("Log in Json parse error");
		}
		try {	
		    Authentication authenticate = authenticationManager.authenticate(
										    new UsernamePasswordAuthenticationToken(
										    loginJson.getUsername(),
										    loginJson.getPassword(),
										       new ArrayList<>())
			);
		    logger.debug("getName: "+authenticate.getName());
		    logger.debug("isAuthenticated: "+authenticate.isAuthenticated());
		    logger.debug("toString: "+authenticate.toString());
		    return authenticate;
	    }catch(AuthenticationException ae) {
			response.addHeader("Log In Error", ae.getMessage());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		    logger.error("JWTLoginFilter: 使用者"+request.getParameter("username")+"驗證失敗: "+ae.getMessage());
//		    throw ae;
		    return null;
	    }
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetails user = (UserDetails)authResult.getPrincipal();
		logger.debug("user: "+new ObjectMapper().writeValueAsString(user));
		if(!user.isEnabled()) {
			response.addHeader("Log In Error", "This account is not enabled");
			response.sendError(403, "This account is not enabled");
		}else {
			logger.debug("Response Header: "+response.getHeader("Access-Control-Allow-Origin"));
			logger.debug("successful");
			logger.debug("JWT END-------------------");
			logger.debug("JWT secret key: "+jwtSecretKey);
			String token = Jwts.builder()
					.setIssuer("Sam")
					.setSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
					.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
					.signWith(SignatureAlgorithm.HS512, jwtSecretKey)
					.compact();
			logger.debug(token);
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
