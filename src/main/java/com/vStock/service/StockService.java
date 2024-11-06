package com.vStock.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vStock.dao.MoneyAccountDao;
import com.vStock.dao.NormalUserDao;
import com.vStock.dao.StockHoldingDao;
import com.vStock.dao.StockHoldingDetailsDao;
import com.vStock.dao.StockTransactionDao;
import com.vStock.dao.Twt84uDao;
import com.vStock.model.MoneyAccount;
import com.vStock.model.NormalUser;
import com.vStock.model.StockHolding;
import com.vStock.model.StockHoldingDetails;
import com.vStock.model.StockTransaction;
import com.vStock.model.TWT84U;
import com.vStock.other.TransactionType;

@Service
public class StockService {
	
	private static final Logger logger = LogManager.getLogger(StockService.class);
	
	private RestTemplate restTemplate;
	
	@Autowired
	private NormalUserDao normalUserDao;
	
	@Autowired
	private StockTransactionDao stockTransactionDao;
	
	@Autowired
	private MoneyAccountDao moneyAccountDao;
	
	@Autowired
	private Twt84uDao twt84uDao;
	
	@Autowired
	private StockHoldingDao stockHoldingDao;
	
	@Autowired
	private StockHoldingDetailsDao stockHoldingDetailsDao;
	
	
	public StockService() {
		if (this.restTemplate == null) {
			logger.debug("建立RestTemplate");
			restTemplate = new RestTemplate();
		}
		this.saveApiDataToExcel();
	}
	
//	@Scheduled(fixedRate = 1000)
	public void testSchedule() {
		logger.debug("排程測試");
	}
	/*
	 * 取得台股證交所股票資訊(前一日收盤價)
	 * */
	public String getStockInfo() {
		String url = "https://openapi.twse.com.tw/v1/exchangeReport/TWT84U";
		return restTemplate.getForObject(url, String.class);
	}
	/*
	 * 將取得的股票api資料存入桌面excel以利測試時刪table也可留存
	 * */
	public void saveApiDataToExcel() {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	    //C:\Users\tiebi\桌面\證交所excel\證交所api記錄檔.xlsx
	    String path = System.getProperty("user.home")
	    		+System.getProperty("file.separator")
	    		+"桌面"
	    		+System.getProperty("file.separator")
	    		+"證交所excel"
	    		+System.getProperty("file.separator")
	    		+"證交所api記錄檔"+today+".xlsx";
		if (new File(path).exists()) {
			System.out.println("檔案: "+path+"已存在");
			return;
	    }
		ObjectMapper objectMapper = new ObjectMapper();
		List<TWT84U> table = null;
		try {
			table = objectMapper.readValue(this.getStockInfo(), new TypeReference<List<TWT84U>>() {});
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} 
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet(today);
	    int rowIndex = 0;
	    int i=0;
	    Row row = sheet.createRow(0);
	    Cell cell = row.createCell(0);
	    cell = row.createCell(i);
	    cell.setCellValue("Code");
	    cell = row.createCell(++i);
	    cell.setCellValue("Name");
	    cell = row.createCell(++i);
	    cell.setCellValue("TodayLimitUp");
	    cell = row.createCell(++i);
	    cell.setCellValue("TodayOpeningRefPrice");
	    cell = row.createCell(++i);
	    cell.setCellValue("TodayLimitDown");
	    cell = row.createCell(++i);
	    cell.setCellValue("PreviousDayOpeningRefPrice");
	    cell = row.createCell(++i);
	    cell.setCellValue("PreviousDayPrice");
	    cell = row.createCell(++i);
	    cell.setCellValue("PreviousDayLimitUp");
	    cell = row.createCell(++i);
	    cell.setCellValue("PreviousDayLimitDown");
	    cell = row.createCell(++i);
	    cell.setCellValue("LastTradingDay");
	    cell = row.createCell(++i);
	    cell.setCellValue("AllowOddLotTrade");
	    i=0;
	    for(TWT84U data:table) {
	    	row = sheet.createRow(++rowIndex);
	    	cell = row.createCell(i);
	    	cell.setCellValue(data.getCode());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getName());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getTodayLimitUp());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getTodayOpeningRefPrice());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getTodayLimitDown());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getPreviousDayOpeningRefPrice());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getPreviousDayPrice());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getPreviousDayLimitUp());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getPreviousDayLimitDown());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getLastTradingDay());
	    	cell = row.createCell(++i);
	    	cell.setCellValue(data.getAllowOddLotTrade());
	    	i=0;
	    }

	    try (FileOutputStream fileOut = new FileOutputStream(path)) {
	        workbook.write(fileOut);
	    }catch(Exception e){
	    	e.printStackTrace();
	    }finally{
	    	try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    logger.debug("證交所API檔案已輸出至路徑: "+path);
	}
	
	@Transactional
	public StockTransaction buyOrSellStock(TransactionType type
											,@NotEmpty int userId
					                        ,@NotEmpty int quantity
											,@NotEmpty String stockCode
											,@NotEmpty String date) {
		try {
			Optional<NormalUser> user = normalUserDao.findById(userId);
			if(user.isEmpty() || (!user.get().isEnabled()) ) {
				throw new RuntimeException("User not found or disabled");
			}
			double price =Double.valueOf(twt84uDao.findPrviousDayPriceByCodeAndDate(stockCode,date));
			
			Optional<MoneyAccount> account = moneyAccountDao.findByFkUserId(userId);
			if(account.isEmpty()) {
				throw new RuntimeException("Account not found");
			}
			if(account.get().isFrozen()) {
				throw new RuntimeException("This account is frozen");
			}
			BigDecimal cost = new BigDecimal(price * quantity);
			BigDecimal balance = account.get().getBalance();
			Optional<StockHolding> stockHoldingO = stockHoldingDao.findByFkUserIdAndStockCode(userId, stockCode);
			if(TransactionType.BUY==type) {//買的話就減帳戶的錢
				if (balance.compareTo(cost) < 0) {
					throw new RuntimeException("帳戶餘額不足");
				}
				moneyAccountDao.updateMoney(account.get().getFkUserId()
						, balance.subtract(cost));
			}else if(TransactionType.SELL==type) {//賣的話就加帳戶的錢
				//檢查使用者手上的持股是否夠他想賣出的數量
				if(stockHoldingO.isEmpty()) {
					throw new RuntimeException("未持有此檔股票: "+stockCode);
				}
				StockHolding stockHolding = stockHoldingO.get();
				if (stockHolding.getTotalQuantity() < quantity) {
					throw new RuntimeException("持有單位不足: " + stockHolding.getTotalQuantity());
				}
				if (cost.compareTo(balance) >=1) {
					throw new RuntimeException("賣出的金額有誤，餘額為: "+balance
							+" 賣出金額為: "+cost);
				}
				moneyAccountDao.updateMoney(account.get().getFkUserId()
						, balance.add(cost));			
			}else{
				throw new RuntimeException("不明的交易種類，請指定買或賣");
			}
			String stockName = twt84uDao.findNameByCode(stockCode);
			stockHoldingDetailsDao.save(StockHoldingDetails.builder()
					.setFkUserId(userId)
					.setStockCode(stockCode)
					.setStockName(stockName)
					.setQuantity(quantity)
					.setTransactionDate(new Date(System.currentTimeMillis()))
					.setTransactionType(type.toString())
					.setPrice(TransactionType.BUY==type? price : -price)//如果是賣出則價格轉為負數
					.setCost(TransactionType.BUY==type? cost : cost.negate())//如果是賣出則成本轉為負數)
					.build());
			//若是要買入且手上持股為空則新增持股資訊，賣出則不檢查因為上面已檢查過，其餘情況一率更新持股資訊
			if(TransactionType.BUY==type & stockHoldingO.isEmpty()) {
				stockHoldingDao.save(StockHolding.builder()
						.setFkUserId(userId)
						.setStockCode(stockCode)
						.setStockName(stockName)
						.setPriceAverage(price)
						.setTotalQuantity(quantity)
						.setTotalCost(cost)
						.build());
			}else {
				//更新持股資訊
				StockHolding stockHolding = stockHoldingO.get();
				List<StockHoldingDetails> stockHoldingDetails = stockHoldingDetailsDao.findByFkUserIdAndStockCode(userId, stockCode).get();
				int totalQuantity = stockHoldingDetails.stream().collect(Collectors.summingInt(StockHoldingDetails::getQuantity));
				stockHolding.setTotalQuantity(totalQuantity);
				BigDecimal totalCost = stockHoldingDetails.stream().map(StockHoldingDetails::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
				stockHolding.setTotalCost(totalCost);
				double priceAverage = totalCost.divide(new BigDecimal(totalQuantity)).doubleValue();
				stockHolding.setPriceAverage(priceAverage);
				stockHoldingDao.save(stockHolding);
			}
			return stockTransactionDao.save(StockTransaction.builder()
					.setFkUserId(userId)
					.setStockCode(stockCode)
					.setQuantity(quantity)
					.setPrice(price)
					.setTransactionDate(new Date(System.currentTimeMillis()))
					.setTransactionType(type.toString())
					.build());
		}catch(Exception e) {
			logger.error("交易失敗", e.getMessage());
			throw new RuntimeException("交易失敗: "+ e.getMessage());
		}
	}

}
