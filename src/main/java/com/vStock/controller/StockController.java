package com.vStock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vStock.model.GeneralResponse;
import com.vStock.model.StockTransaction;
import com.vStock.other.TransactionType;
import com.vStock.service.StockService;
import com.vStock.util.ResponseUtils;

@RestController
public class StockController {
	
	@Autowired
	private StockService stockService;
	
	@RequestMapping(method = RequestMethod.POST
					,value = "/buyStock"
					,produces = "application/json")
	public ResponseEntity<GeneralResponse> buyStock(@RequestParam(name = "userId") int userId,
										            @RequestParam(name = "quantity") int quantity,
										            @RequestParam(name = "stockCode") String stockCode) {
		try {
			StockTransaction transaction = stockService.buyOrSellStock(TransactionType.BUY, userId, quantity, stockCode);
			return ResponseUtils.success("success", "交易成功", transaction);
		}catch(Exception e) {
			return ResponseUtils.error("failed", "交易失敗", e);
		}
		
	}
	@RequestMapping(method = RequestMethod.POST
			,value = "/sellStock"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> sellStock(@RequestParam(name = "userId") int userId,
										            @RequestParam(name = "quantity") int quantity,
										            @RequestParam(name = "stockCode") String stockCode) {
		try {
			StockTransaction transaction = stockService.buyOrSellStock(TransactionType.SELL, userId, quantity, stockCode);
			return ResponseUtils.success("success", "交易成功", transaction);
		}catch(Exception e) {
			return ResponseUtils.error("failed", "交易失敗", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
            ,value = "/queryStockInfo"
            ,produces = "application/json")
	public ResponseEntity<GeneralResponse> queryStockInfo(@RequestParam(name = "page") int page,
                                                          @RequestParam(name = "size") int size,
                                                          @RequestParam(name = "sort") String sort,
                                                          @RequestParam(name = "order") String order) {
        try {
        	return ResponseUtils.success("success", "查詢成功", stockService.getStockByPageWithSort(page, size, sort, order));
        }catch(Exception e) {
        	return ResponseUtils.error("failed", "查詢失敗", e);
        }
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/searchStock"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> searchStock(@RequestParam(name = "keyWord")String keyWord) {
		try {
        	return ResponseUtils.success("success", "查詢成功", stockService.searchForStock(keyWord));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "查詢失敗", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/queryStockHolding"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> queryStockHolding(
			@RequestParam(name = "userId")int userId,
			@RequestParam(name = "page")int page) {
		try {
			return ResponseUtils.success("success", "查詢成功", stockService.queryStockHolding(page,userId));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "查詢持股錯誤", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/queryStockHoldingDetails"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> queryStockHoldingDetails(
			@RequestParam(name = "stockHoldingNo")int stockHoldingNo,
			@RequestParam(name = "page")int page) {
		try {
			return ResponseUtils.success("success", "查詢成功", stockService.queryStockHoldingDetails(page,stockHoldingNo));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "查詢持股明細錯誤", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/getTWIndexCurrentMonth"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> queryStockHoldingDetails() {
		try {
			return ResponseUtils.success("success", "查詢成功", stockService.getTwStockMonthData());
		}catch(Exception e) {
			return ResponseUtils.error("failed", "資料發生錯誤", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/getCurrentPrice"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> getCurrentPrice(@RequestParam(name = "stockCodes") String stockCodes) {
		try {
			String[] stockCodeArray = stockCodes.split(",");
			return ResponseUtils.success("success", "查詢成功", stockService.getCurrentPrice(stockCodeArray));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "資料發生錯誤", e);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET
			,value = "/getTransactionHistory"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> getTransactionHistory(
			@RequestParam(name = "userId") int userId,
			@RequestParam(name = "page") int page) {
		try {
			return ResponseUtils.success("success", "查詢成功", stockService.getStockTransactionHistory(userId,page));
		}catch(Exception e) {
			return ResponseUtils.error("failed", "資料發生錯誤", e);
		}
		
	}
	

	
	
}
