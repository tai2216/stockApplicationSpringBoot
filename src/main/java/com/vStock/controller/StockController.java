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

@RestController
public class StockController {
	
	@Autowired
	private StockService stockService;
	
	@RequestMapping(method = RequestMethod.POST
					,value = "/buyStock"
					,produces = "application/json")
	public ResponseEntity<GeneralResponse> buyStock(@RequestParam(name = "userId") int userId,
										            @RequestParam(name = "quantity") int quantity,
										            @RequestParam(name = "stockCode") String stockCode,
										            @RequestParam(name = "date") String date) {
		try {
			StockTransaction transaction = stockService.buyOrSellStock(TransactionType.BUY, userId, quantity, stockCode, date);
			return ResponseEntity.ok(GeneralResponse.builder()
													.setStatus("success")
													.setMessage("交易成功")
													.setData(transaction)
													.build());
		}catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(GeneralResponse.builder()
										.setError(e.getMessage())
										.setStatus("failed")
										.setMessage("交易失敗")
										.build());
		}
		
	}
	@RequestMapping(method = RequestMethod.POST
			,value = "/sellStock"
			,produces = "application/json")
	public ResponseEntity<GeneralResponse> sellStock(@RequestParam(name = "userId") int userId,
										            @RequestParam(name = "quantity") int quantity,
										            @RequestParam(name = "stockCode") String stockCode,
										            @RequestParam(name = "date") String date) {
		try {
			StockTransaction transaction = stockService.buyOrSellStock(TransactionType.SELL, userId, quantity, stockCode, date);
			return ResponseEntity.ok(GeneralResponse.builder()
					.setStatus("success")
					.setMessage("交易成功")
					.setData(transaction)
					.build());
		}catch(Exception e) {
			return ResponseEntity.internalServerError()
					.body(GeneralResponse.builder()
							.setError(e.getMessage())
							.setStatus("failed")
							.setMessage("交易失敗")
							.build());
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
            return ResponseEntity.ok(GeneralResponse.builder()
                    .setStatus("success")
                    .setMessage("查詢成功")
                    .setData(stockService.getStockByPageWithSort(page, size, sort, order))
                    .build());
        }catch(Exception e) {
            return ResponseEntity.internalServerError()
                    .body(GeneralResponse.builder()
                            .setError(e.getMessage())
                            .setStatus("failed")
                            .setMessage("查詢失敗")
                            .build());
        }
		
	}
	
	
}
