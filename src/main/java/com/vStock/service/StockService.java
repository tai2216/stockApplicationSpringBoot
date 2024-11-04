package com.vStock.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {
	
	private static final Logger logger = LogManager.getLogger(StockService.class);
	
	private RestTemplate restTemplate;
	
	private StockService() {
		if (this.restTemplate == null) {
			logger.info("建立RestTemplate");
			restTemplate = new RestTemplate();
		}
	}
	
//	@Scheduled(fixedRate = 1000)
	public void testSchedule() {
		logger.info("排程測試");
	}
	
	public String getStockInfo() {
		String url = "https://openapi.twse.com.tw/v1/exchangeReport/TWT84U";
		return restTemplate.getForObject(url, String.class);
	}

}
