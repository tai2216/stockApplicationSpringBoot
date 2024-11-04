package com.vStock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.model.LoginResponse;

@RestController
public class LoginController {

	@RequestMapping(value = "/login"
					,method = {RequestMethod.POST}
					,produces = "application/json")
	public ResponseEntity<LoginResponse> login(HttpServletRequest req
											  ,HttpServletResponse res){
		String authResult = res.getHeader("Log In Error");
		if (authResult != null) {
			return ResponseEntity.status(403).body(LoginResponse.builder().message(authResult).build());
		}
		ResponseEntity<LoginResponse> success = ResponseEntity.ok(
				LoginResponse.builder().username(res.getHeader("loginUserName"))
							       		 .role(res.getHeader("role"))
							       		 .token(res.getHeader("Authorization"))
							       		 .message("Log In Success")
							       		 .build());
        return success;
	}
	
	
	
	
}
