package com.vStock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.model.GeneralResponse;
import com.vStock.service.impl.NormalUserServiceImpl;

@RestController
public class RegistrationController {
	
	@Autowired
	private NormalUserServiceImpl normalUserService;
	
	@RequestMapping(value = "/register"
			,method = { RequestMethod.POST}
			, produces = "application/json")
	public ResponseEntity<GeneralResponse> registerUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			normalUserService.registerUser(req, res);
		}catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(GeneralResponse.builder()
							.setMessage("註冊失敗，請稍後再試")
							.setStatus("error")
							.setError(e.getMessage())
							.build());
		}
		return ResponseEntity.ok(GeneralResponse.builder()
				.setMessage("註冊成功，請至信箱查看確認信件以啟用帳號")
				.setStatus("success")
				.build());
	}
	
	@RequestMapping(value = "/enableUser"
			, method = { RequestMethod.GET }
			, produces = "application/json")
	public ResponseEntity<GeneralResponse> enableUser(@RequestParam(name = "username") String username
													, HttpServletRequest req
													, HttpServletResponse res) {
		try {
			normalUserService.enableUser(username, req, res);
		} catch (Exception e) {
			return ResponseEntity.internalServerError()
					.body(GeneralResponse.builder()
							.setMessage("啟用失敗，請稍後再試")
							.setStatus("error")
							.setError(e.getMessage())
							.build());
		}
		return ResponseEntity.ok(GeneralResponse.builder()
				.setMessage("啟用成功，請使用此帳號登入")
				.setStatus("success")
				.build());
	}

}
