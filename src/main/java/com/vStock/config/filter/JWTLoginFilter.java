package com.vStock.config.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
		System.out.println("req content type: "+request.getContentType());
		System.out.println("req encoding: "+request.getCharacterEncoding());
		System.out.println("req content length: "+request.getContentLength());
		System.out.println("User Name: "+request.getParameter("username"));
		System.out.println("Password: "+request.getParameter("password"));
		//showRequestBody(request);
		try {	
		    Authentication authenticate = authenticationManager.authenticate(
										    new UsernamePasswordAuthenticationToken(
										    request.getParameter("username"),
										    request.getParameter("password"),
										       new ArrayList<>())
			);
		   System.out.println("getName: "+authenticate.getName());
		   System.out.println("isAuthenticated: "+authenticate.isAuthenticated());
		   System.out.println("toString: "+authenticate.toString());
		   return authenticate;
	    }catch(AuthenticationException ae) {
		  System.out.println("驗證失敗");
		  //ae.printStackTrace();
	    }
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("Response Header: "+response.getHeader("Access-Control-Allow-Origin"));
		System.out.println("successful");
		System.out.println("JWT END-------------------");
		String token = Jwts.builder()
				.setIssuer("Sam")
			    .setSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
			    .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
			    .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
			    .compact();
		System.out.println(token);
		System.out.println("Remember Me: "+request.getParameter("rememberMe"));
		
		response.addHeader("loginUserName", authResult.getName());
		response.addHeader("Authorization", "Bearer " + token);
		chain.doFilter(request, response);
			  
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		System.out.println("Unsuccessful, 驗證失敗");
		
		super.unsuccessfulAuthentication(request, response, failed);
	}
	
	//Debug用 看當前的request Body 內容
	public static void showRequestBody(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		try {
			ServletInputStream inputStream = request.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		
			String line;
		    while ((line = reader.readLine()) != null) {
		       sb.append(line);
		    }
		    reader.close();
		    inputStream.close();
		    // Process the request body
		    String requestBodyString = sb.toString();
		    System.out.println("requestBodyString: "+requestBodyString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	
	
	
	
	
	

}
