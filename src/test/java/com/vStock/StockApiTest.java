package com.vStock;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vStock.dao.StockHoldingDetailsDao;
import com.vStock.dao.Twt84uDao;
import com.vStock.service.StockService;

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation.class)
public class StockApiTest {

	@Autowired
	private StockService stockService;
	
	@Autowired
	private StockHoldingDetailsDao stockHoldingDetailsDao;
	
	@Autowired
	private Twt84uDao twt84uDao;
	
	private RestTemplate restTemplate = new RestTemplate();
	
//	@Test
//	@Order(1)
////	@Transactional
//	void getStockInfo() {
//		try {
//			System.out.println("呼叫api");
//			ObjectMapper mapper = new ObjectMapper();
//			String result = stockService.getStockInfo();
//			List<TWT84U> table = mapper.readValue(result, new TypeReference<List<TWT84U>>() {});
//			twt84uDao.saveAll(table);
//			twt84uDao.flush();
//			System.out.println("已存入資料庫");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	@Order(2)
//	void saveApiDataToExcel() {
//		stockService.saveApiDataToExcel();
//	}
	
//	@Test
//	@Order(3)
//	void buyOrSellStock() {
//		stockService.buyOrSellStock(TransactionType.BUY, 1, 1000, "0050", "1131104");
//	}
	
//	@Test
//	@Order(4)
//	void testQueryWithPageObject() throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
//		String json = objectMapper.writeValueAsString(stockService.getStockByPageWithSort(1, 30, "lasttradingday", "asc"));
//		System.out.println("json:");
//		System.out.println(json);
//		System.out.println();
//	}
	
//    @Test
//    @Order(5)
//    void testStockPage() throws JsonProcessingException {
//    	int page = 0;
//    	int sNo = 1;
//		List<StockHoldingDetails> result = stockHoldingDetailsDao.findByFkStockHoldingNoToPage(page ==0? 0:page+14,sNo)
//				.orElseThrow(()->new RuntimeException("查無此使用者持股明細"));
////		PagedListHolder<StockHoldingDetails> p = new PagedListHolder(result);
////		p.setPageSize(15);
////		p.setPage(page);
//		Pageable pageable = PageRequest.of(page, 15,Sort.by("SERIAL_NO").ascending());//stockHoldingDetailsDao.findCountByFkStockHoldingNo(stockHoldingNo)
//		PageImpl<StockHoldingDetails> p =  new PageImpl<>(result, pageable
//				,page !=0 ?
//				result.size()+1 : stockHoldingDetailsDao.findCountByFkStockHoldingNo(sNo));
//		ObjectMapper objectMapper = new ObjectMapper();
//		System.out.println(objectMapper.writeValueAsString(p));
//    }
	
	@Test
	@Order(6)
	void testApiTW() throws JsonProcessingException {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(today);
		StockModel model = restTemplate.getForObject("https://www.twse.com.tw/rwd/zh/afterTrading/FMTQIK?date="+formattedDate+"&response=json"
				, StockModel.class);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writeValueAsString(model));
		
	}
	
}
