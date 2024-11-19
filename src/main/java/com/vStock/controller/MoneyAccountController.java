package com.vStock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.dao.MoneyAccountDao;
import com.vStock.model.GeneralResponse;
import com.vStock.util.ResponseUtils;

@RestController
public class MoneyAccountController {
	
	@Autowired
	private MoneyAccountDao moneyAccountdao;
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/getAccountBalance"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> getAccountBalance(@RequestParam(name="userId")int userId){
		try {
			return ResponseUtils.success("success", "查詢成功"
					,moneyAccountdao.findByFkUserId(userId).orElseThrow(()->new RuntimeException("查無此帳戶餘額")));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "查詢帳戶餘額失敗", e);
		}
	}
}
