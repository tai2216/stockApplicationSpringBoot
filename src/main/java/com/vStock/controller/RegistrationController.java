package com.vStock.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.model.GeneralResponse;
import com.vStock.service.NormalUserService;
import com.vStock.util.ResponseUtils;

@RestController
public class RegistrationController {
	
	@Autowired
	private NormalUserService normalUserService;
	
	@RequestMapping(value = "/register"
			,method = { RequestMethod.POST}
			, consumes = "application/json"
			, produces = "application/json")
	public ResponseEntity<GeneralResponse> registerUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			normalUserService.registerUser(req, res);
		}catch(Exception e) {
			return ResponseUtils.error("failed", e.getMessage(), e);
		}
		return ResponseUtils.success("success", "註冊成功，請於今日結束之前至信箱查看確認信件以啟用帳號", null);
	}
	
	@RequestMapping(value = "/enableUser"
			, method = { RequestMethod.GET }
			, produces = "application/json")
	public ResponseEntity<GeneralResponse> enableUser(HttpServletRequest req
													, HttpServletResponse res) {
		try {
			normalUserService.enableUser(req, res);
		} catch (Exception e) {
			return ResponseUtils.error("failed", e.getMessage(), e);
		}
		return ResponseUtils.success("success", "啟用成功，請使用此帳號登入", null);
	}

}
