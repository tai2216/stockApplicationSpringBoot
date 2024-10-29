package com.vStock.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.model.NormalUser;

@RestController
public class TestController {
	@RequestMapping(value = "/home",
			method = {RequestMethod.POST, RequestMethod.GET},
			produces = "application/json")
	public ResponseEntity<Map<String,String>> testMessage(HttpServletResponse response,
			Authentication auth) throws IOException {
		Map<String,String> jsonResponse = new HashMap<String,String>();
		auth.getAuthorities().forEach(authority -> {
			jsonResponse.put("role", authority.getAuthority());
		});
		OAuth2User user = (OAuth2User) auth.getPrincipal();
		user.getAttributes().forEach((k, v) -> {
			jsonResponse.put(k, v.toString());
		});
		user.getAuthorities().forEach(authority -> {
			jsonResponse.put("role", authority.getAuthority());
		});
		jsonResponse.put("username", user.getName());
		jsonResponse.put("message", "Hello! welcome to stock market simulation");
		return ResponseEntity.ok(jsonResponse);
	}
	
	@RequestMapping(value = "/testLogin", 
			method = RequestMethod.GET, 
			produces = "application/json")
	public ResponseEntity<Map<String,String>> testLogin(){
		Map<String,String> jsonResponse = new HashMap<String,String>();
		jsonResponse.put("message", "log in api test response");
		return ResponseEntity.ok(jsonResponse);
	}

	@RequestMapping(value = "/testLogout",
			method = RequestMethod.GET,
			produces = "application/json")
	public ResponseEntity<Map<String,String>> testLogout(){
        Map<String,String> jsonResponse = new HashMap<String,String>();
        jsonResponse.put("message", "log out success api test response");
        return ResponseEntity.ok(jsonResponse);
    }

	
}
