package com.vStock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vStock.dao.Twt84uDao;
import com.vStock.model.TWT84U;
import com.vStock.other.TransactionType;
import com.vStock.service.StockService;

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation.class)
public class StockApiTest {

	@Autowired
	private StockService stockService;
	
	@Autowired
	private Twt84uDao twt84uDao;
	
	@Test
	@Order(1)
//	@Transactional
	void getStockInfo() {
		try {
			System.out.println("呼叫api");
			ObjectMapper mapper = new ObjectMapper();
			String result = stockService.getStockInfo();
			List<TWT84U> table = mapper.readValue(result, new TypeReference<List<TWT84U>>() {});
			twt84uDao.saveAll(table);
			twt84uDao.flush();
			System.out.println("已存入資料庫");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(2)
	void saveApiDataToExcel() {
		stockService.saveApiDataToExcel();
	}
	
//	@Test
//	@Order(3)
//	void buyOrSellStock() {
//		stockService.buyOrSellStock(TransactionType.BUY, 1, 1000, "0050", "1131104");
//	}
	
}
