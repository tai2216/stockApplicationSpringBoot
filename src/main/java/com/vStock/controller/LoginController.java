package com.vStock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.dao.NormalUserDao;
import com.vStock.model.LoginResponse;
import com.vStock.service.GoogleUserService;
import com.vStock.service.NormalUserService;

@RestController
public class LoginController {

	@Autowired
	private NormalUserService normalUserService;
	
	@Autowired
	private NormalUserDao normalUserDao;
	
	@Autowired
	private GoogleUserService googleUserService;
	
	@RequestMapping(value = "/login"
					,method = {RequestMethod.POST}
					,produces = "application/json")
	public ResponseEntity<LoginResponse> login(HttpServletRequest req
											  ,HttpServletResponse res){
		String authResult = res.getHeader("Log In Error");
		if (authResult != null) {
			return ResponseEntity.status(403).body(LoginResponse.builder().message(authResult).build());
		}
		normalUserService.updateLoginDate(res.getHeader("loginUserName"));
        return ResponseEntity.ok(
				LoginResponse.builder()
				.userId(normalUserDao.findByUsername(res.getHeader("loginUserName")).get().getId())
				.username(res.getHeader("loginUserName"))
				.role(res.getHeader("role"))
				.token(res.getHeader("Authorization"))
				.message("Log In Success")
				.build());
	}
	
	@RequestMapping(value = "/googleLogin"
					,method = {RequestMethod.POST}
					,produces = "application/json")
	public ResponseEntity<LoginResponse> googleLogin(@RequestBody String body
													,HttpServletRequest req
													,HttpServletResponse res){
		googleUserService.googleLoginFlow(body,req, res);
		String authResult = res.getHeader("Google Log In Error");
		if (authResult != null) {
			return ResponseEntity.status(403).body(LoginResponse.builder().message(authResult).build());
		}
		normalUserService.updateLoginDate(res.getHeader("loginUserName"));
		return ResponseEntity.ok(
				LoginResponse.builder()
				.userId(normalUserDao.findByUsername(res.getHeader("loginUserName")).get().getId())
//				.username(res.getHeader("loginUserName"))
				.username(res.getHeader("googleName"))
				.role(res.getHeader("role"))
				.token(res.getHeader("Authorization"))
				.googlePictureUrl(res.getHeader("googlePicture"))
				.message("Log In Success")
				.build());
	}
	
//	@RequestMapping(value = "/testGoogleApi"
//			,method = {RequestMethod.POST}
//			,produces = "application/json")
//	public ResponseEntity<LoginResponse> testGoogleApi(@RequestBody String body,HttpServletRequest req,HttpServletResponse res){
//		try {
//			System.out.println("requestBody: "+body);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	
	
	
}
