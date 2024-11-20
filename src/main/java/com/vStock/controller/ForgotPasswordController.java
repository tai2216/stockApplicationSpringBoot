package com.vStock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.json.FogotPasswordJson;
import com.vStock.model.GeneralResponse;
import com.vStock.service.NormalUserService;
import com.vStock.util.ResponseUtils;

@RestController
public class ForgotPasswordController {

	@Autowired
	private NormalUserService normalUserService;
	
	@RequestMapping(value="/forgotPassword"
            ,method = { RequestMethod.GET }
            ,produces = "application/json")
	public ResponseEntity<GeneralResponse> forgotPassword(@RequestParam("email")String email){
		try {
			normalUserService.sendForgotPasswordEmail(email);
			return ResponseUtils.success("success", "請至信箱查看驗證信件", null);
		}catch(Exception e) {
			return ResponseUtils.error("failed", e.getMessage(), e);
		}
	}
	
	@RequestMapping(value="/resetPassword"
			,method = { RequestMethod.POST }
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> resetPassword(@RequestBody @Validated FogotPasswordJson fogotPasswordJson){
		try {
			normalUserService.resetPassword(fogotPasswordJson);
			return ResponseUtils.success("success", "請使用新密碼登入", null);
		}catch(Exception e) {
			normalUserService.updateRetryCount(fogotPasswordJson.getEmail());
			return ResponseUtils.error("failed", e.getMessage(), e);
		}
	}
	
}
