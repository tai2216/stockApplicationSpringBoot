package com.vStock.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import com.vStock.model.StockModel;
import com.vStock.model.StockModel2;
import com.vStock.model.StockTransaction;
import com.vStock.model.TWT84U;
import com.vStock.other.TransactionType;
import com.vStock.util.StockUtils;

@Service
public class StockService {
	
	private static final Logger logger = LogManager.getLogger(StockService.class);
	
	private RestTemplate restTemplate;
	
	/*
	 * 首頁圖表資料一天內為固定因此設為快取
	 * */
	private static List<StockModel2> twStockMonthData;
	
	/*
	 * 使用者的股票列表第一頁基本上為固定因此在此設為快取
	 * */
	private static Page<TWT84U> firstPageData;
	
	private static String formattedDate;
	
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
	
	@Value("${spring.mail.username}")
	private String developerEmail;
	
	@Value("${getStockDataOnInit}")
	private String getStockDataOnInit;
	
	private String[] columnNameArray = {
			"Code", "Name", "TodayLimitUp", "TodayOpeningRefPrice", "TodayLimitDown", "PreviousDayOpeningRefPrice",
			"PreviousDayPrice", "PreviousDayLimitUp", "PreviousDayLimitDown", "LastTradingDay",
			"AllowOddLotTrade"
	};
	
	public StockService() throws JsonMappingException, JsonProcessingException {
		if (this.restTemplate == null) {
			logger.debug("建立RestTemplate");
			this.restTemplate = new RestTemplate();
		}
		this.saveApiDataToExcel();//保存歷史資料到excel,目前還無使用規劃但先保存
//		this.testSchedule();
//		todo:要加入每日排程去察看當日註冊的使用者帳號沒有啟用的應全部刪除以避免過多無效帳號
	}
	
	@Scheduled(fixedRate = 8,timeUnit = TimeUnit.HOURS,zone = "Asia/Taipei")
	public void clearCache() {
		logger.debug("清除股票資料相關快取");
		twStockMonthData = null;
		firstPageData=null;
	}
	
	@Scheduled(fixedRate = 8,timeUnit = TimeUnit.HOURS,zone = "Asia/Taipei")
	@Transactional(rollbackFor = Exception.class)
	public void testSchedule() {
		if("false".equals(getStockDataOnInit)) {//測試開發時使用，不須每次啟動都執行
			logger.debug("排程測試今日已執行");
			return;
		}
		try {
			logger.debug("取得最新證交所API資料...請稍後");
			ObjectMapper mapper = new ObjectMapper();
			String result = this.getStockInfo();
			List<TWT84U> table = mapper.readValue(result, new TypeReference<List<TWT84U>>() {});
			table = table.stream().filter(twt-> (!twt.getPreviousDayPrice().equals("0")) & StringUtils.hasText(twt.getPreviousDayPrice())).collect(Collectors.toList());
			logger.debug("執行清空資料庫股票資訊(Table名稱:TWT84U)...請稍後");
			twt84uDao.deleteAll();
			twt84uDao.flush();
			logger.debug("已清空股票資訊(Table名稱:TWT84U)...請稍後");
			twt84uDao.saveAll(table);
			twt84uDao.flush();
			logger.debug("已完成更新證交所api最新股票資料(Table名稱:TWT84U)!");
		}catch(Exception e) {
			logger.error("證交所api最新股票資料更新至資料庫失敗(Table名稱:TWT84U): "+e.getMessage());
		}
	}
	/*
	 * 取得台股證交所股票資訊(前一日收盤價)
	 * */
	public String getStockInfo() {
		String url = "https://openapi.twse.com.tw/v1/exchangeReport/TWT84U";
		return restTemplate.getForObject(url, String.class);
	}
	
	public boolean checkColumnName(String columnName) {
		boolean columnNameExist = false;
		for(String s : this.columnNameArray) {
			if(columnName.equals(s)) {
				columnNameExist = true;
				break;
			}
		}
		return columnNameExist;
	}
	
	public Page<TWT84U> getStockByPage(int page, int size){
		PageRequest of = PageRequest.of(page,size);
		return twt84uDao.findAll(of);
		
	}
	
	public Page<TWT84U> getFirstPageData(){
		if(firstPageData == null) {
            firstPageData = this.getStockByPage(0, 15);
        }
        return firstPageData;
	}
	
	public Page<TWT84U> getStockByPageWithSort(int page, int size, @NotEmpty String columnName, String ascOrDesc){
		if (page == 0 & size == 15 & columnName.equals("Code")&ascOrDesc.equals("asc")) {
//			logger.debug("使用股票列表首頁快取");
			return this.getFirstPageData();
		}
		if(!StringUtils.hasText(ascOrDesc)) {
			ascOrDesc = "asc";//若這個參數有問題則預設給asc排序
		}
		if(!checkColumnName(columnName)) {
			throw new RuntimeException("錯誤的排序欄位名稱: "+columnName+", 必須為: "+Arrays.toString(this.columnNameArray)+"的其中一種且大小寫須完全符合");
		}
		PageRequest of = null;
		if(ascOrDesc.toLowerCase().equals("desc")) {
			of = PageRequest.of(page,size, Sort.by(columnName).descending());
		}else {
			of = PageRequest.of(page,size, Sort.by(columnName).ascending());
		}
		return twt84uDao.findAll(of);
	}
	
	public Page<TWT84U> searchForStock(String keyWord){
		if (!StringUtils.hasText(keyWord)) {
			throw new RuntimeException("搜尋關鍵字不可為空");
		}
		List<TWT84U> result = twt84uDao.findByCodeOrName(keyWord);
		Pageable pageable = PageRequest.of(0, 15,Sort.by("Code").ascending());
		return new PageImpl<>(result,pageable,result.size());
	}
	
	public Page<StockHolding> queryStockHolding(int page, int userId){
		List<StockHolding> result = stockHoldingDao.findByFkUserIdToPage(countPage(page),userId)
				.orElseThrow(()->new RuntimeException("查無此使用者持股"));
		Pageable pageable = PageRequest.of(page, 15,Sort.by("SERIAL_NO").ascending());
		return new PageImpl<>(result,pageable,stockHoldingDao.findCountByFkUserId(userId));
	}
	
	public Page<StockHoldingDetails> queryStockHoldingDetails(int page, int stockHoldingNo){
		List<StockHoldingDetails> result = stockHoldingDetailsDao.findByFkStockHoldingNoToPage(countPage(page),stockHoldingNo)
				.orElseThrow(()->new RuntimeException("查無此使用者持股明細"));
		Pageable pageable = PageRequest.of(page, 15,Sort.by("SERIAL_NO").ascending());//stockHoldingDetailsDao.findCountByFkStockHoldingNo(stockHoldingNo)
		return new PageImpl<>(result, pageable,stockHoldingDetailsDao.findCountByFkStockHoldingNo(stockHoldingNo));
	}
	
	/*
	 * 將取得的股票api資料存入桌面excel以利測試時使用drop table，也仍有留存資料供日後使用
	 * */
	public void saveApiDataToExcel() {
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String seperator = System.getProperty("file.separator");
	    //C:\Users\tiebi\桌面\證交所excel\證交所api記錄檔.xlsx
	    String path = System.getProperty("user.home")
	    		+seperator
	    		+"桌面"
	    		+seperator
	    		+"證交所excel"
	    		+seperator
	    		+"證交所api記錄檔"+today+".xlsx";
		if (new File(path).exists()) {
			logger.debug("檔案: "+path+"已存在");
			return;
	    }
		ObjectMapper objectMapper = new ObjectMapper();
		List<TWT84U> table = null;
		try {
			table = objectMapper.readValue(this.getStockInfo(), new TypeReference<List<TWT84U>>() {});
		} catch (Exception e) {
			logger.debug("證交所API資料轉換失敗: "+e.getMessage());
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
//	    	e.printStackTrace();
	    	logger.debug(e.getMessage());
	    }finally{
	    	try {
				workbook.close();
			} catch (IOException e) {
//				e.printStackTrace();
				logger.debug(e.getMessage());
			}
	    }
	    logger.debug("證交所API檔案已輸出至路徑: "+path);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public StockTransaction buyOrSellStock(TransactionType type
											,@NotEmpty int userId
					                        ,@NotEmpty int quantity
											,@NotEmpty String stockCode) {
		try {
			Optional<NormalUser> user = normalUserDao.findById(userId);
			if(user.isEmpty()) {
				throw new RuntimeException("未找到使用者: "+userId);
			}
			if((!user.get().isEnabled())) {
				throw new RuntimeException("此帳號狀態目前為停用");
			}
			Optional<String> p = twt84uDao.findPrviousDayPriceByCode(stockCode);
			if (p.isEmpty()) {
				throw new RuntimeException("未找到此檔股票: " + stockCode);
			}
			double price =Double.valueOf(p.get());
			
			Optional<MoneyAccount> account = moneyAccountDao.findByFkUserId(userId);
			if(account.isEmpty()) {
				throw new RuntimeException("未找到使用者存款帳戶");
			}
			if(account.get().isFrozen()) {
				throw new RuntimeException("此使用者存款帳戶已被凍結");
			}
			if(quantity<=0) {
				throw new RuntimeException("交易數量不得小於等於零");
			}
			long serviceCharge = StockUtils.countServiceCharge(price*quantity);
			long tax = 0;
			BigDecimal cost = null;
			if (TransactionType.SELL == type) {//只有賣出時才收證交稅0.3%其餘時間為0
				tax = StockUtils.countTax(price * quantity);
				cost = new BigDecimal((price * quantity)-serviceCharge-tax);
			}else if(TransactionType.BUY == type) {
				cost = new BigDecimal((price * quantity)+serviceCharge);
			}
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
					throw new RuntimeException("持有數量不足");
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
			}
			//只有在非賣出的情況下才去新增持股明細
			if(TransactionType.SELL!=type) {
				stockHoldingDetailsDao.save(StockHoldingDetails.builder()
						.setFkUserId(userId)
						.setFkStockHoldingNo(stockHoldingDao.findByFkUserIdAndStockCode(userId,stockCode)
								.orElseThrow(()->new RuntimeException("找不到此持股")).getSerialNo())
						.setStockCode(stockCode)
						.setStockName(stockName)
						.setQuantity(quantity)
						.setTransactionDate(new Date(System.currentTimeMillis()))
						.setTransactionType(type.toString())
						.setPrice(price)
						.setCost(cost)
						.build());
			}
			//更新持股資訊
			StockHolding stockHolding = stockHoldingDao.findByFkUserIdAndStockCode(userId, stockCode)
														.orElseThrow(()->new RuntimeException("找不到此持股"));
			List<StockHoldingDetails> stockHoldingDetails = stockHoldingDetailsDao.findByFkUserIdAndStockCode(userId, stockCode)
																					.orElseThrow(()->new RuntimeException("找不到此持股細節"));
			//賣股邏輯:從明細裡面依照交易序號serail_no排序由最早交易買入的開始賣出
			if(TransactionType.SELL==type) {
//				logger.debug("賣出前的stockHoldingDetails: "+stockHoldingDetails.size());
				int tmpQuantity = quantity;
				List<StockHoldingDetails> toRemove = new ArrayList<>();
				for(StockHoldingDetails details:stockHoldingDetails) {
					tmpQuantity -= details.getQuantity();
					if (tmpQuantity== 0) {
						toRemove.add(details);
						break;
					}else if(tmpQuantity>0){
						toRemove.add(details);
					} if(tmpQuantity<0) {//持股細節大於要賣出的股數
						int absQuantity = Math.abs(tmpQuantity);
						details.setQuantity(absQuantity);
						details.setCost(new BigDecimal(absQuantity*details.getPrice()));
						stockHoldingDetailsDao.saveAndFlush(details);
						break;
					}
				}
//				logger.debug("要刪除的持股明細: "+toRemove.size());
				stockHoldingDetails.removeAll(toRemove);
				stockHoldingDetailsDao.deleteAll(toRemove);
//				logger.debug("賣出後的stockHoldingDetails: "+stockHoldingDetails.size());
			}
			
			
			
			int totalQuantity = stockHoldingDetails.stream().collect(Collectors.summingInt(StockHoldingDetails::getQuantity));
//			logger.debug("totalQuantity: "+totalQuantity);
			BigDecimal totalCost = stockHoldingDetails.stream().map(StockHoldingDetails::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
//			logger.debug("totalCost: "+totalCost);
			if(checkStockHoldingAllZero(totalQuantity, totalCost)) {
//				此時關聯到的持股明細不會更新(可能是因為交易尚未被成立)，因此需要自己去做更新，這樣才會在後續的刪除操作自動關聯相關的持股明細且一起跟著持股被刪除
//				stockHoldingDao.findById(stockHolding.getSerialNo())
//								.get().setStockHoldingDetailsList(stockHoldingDetailsDao.findByFkUserIdAndStockCode(userId,stockCode).get());
				//fk關聯在這個交易中不會實時更新，所以乾脆自己刪比較快
				stockHoldingDetailsDao.deleteAll(stockHoldingDetailsDao.findByFkUserIdAndStockCode(userId,stockCode)
																		.orElseThrow(()-> new RuntimeException("刪除持股明細失敗")));
				//如果賣出後持股數為0以及其他各項數字皆為0則可以直接刪除此筆資料
				stockHoldingDao.deleteById(stockHolding.getSerialNo());
			}else {
				double priceAverage = totalCost.divide(new BigDecimal(totalQuantity),2, RoundingMode.HALF_UP).doubleValue();
				stockHolding.setTotalQuantity(totalQuantity);
				stockHolding.setTotalCost(totalCost);
				stockHolding.setPriceAverage(priceAverage);
				stockHoldingDao.save(stockHolding);
			}
			return stockTransactionDao.save(StockTransaction.builder()
					.setFkUserId(userId)
					.setStockCode(stockCode)
					.setQuantity(TransactionType.BUY==type? quantity: -quantity)
					.setPrice(TransactionType.BUY==type? price : -price)
					.setServiceCharge(serviceCharge)
					.setTax(tax)
					.setTransactionDate(new Date(System.currentTimeMillis()))
					.setTransactionType(type.toString())
					.build());
		}catch(Exception e) {
//			e.printStackTrace();
			logger.error("交易失敗", e.getMessage());
			if(StringUtils.hasText(e.getMessage())) {
				throw e;
			}else {
				throw new RuntimeException("交易失敗，請洽詢網站管理者或來信至"+developerEmail);
			}
		}
	}
	
	public boolean checkStockHoldingAllZero(int totalQuantity, BigDecimal totalCost) {
		int totalCostCompareResult = totalCost.compareTo(BigDecimal.ZERO);
		if(totalQuantity==0&totalCostCompareResult==0) {
			return true;
		}
		if(totalQuantity==0 & (totalCostCompareResult>0 || totalCostCompareResult<0) ){
			throw new RuntimeException("計算金額(賣股後持有數量等於0但持有成本大於0或小於0)有誤");
		}else if (totalCostCompareResult == 0 & totalQuantity != 0) {
			throw new RuntimeException("計算金額(賣股後持有成本等於0但持有數量不為0)有誤");
		}
		return false;
	}
	
	/*
	 * 取得現價用來計算損益
	 * */
	public Map<String,String> getCurrentPrice(String...stockCodes){
		try {
			for(String s : stockCodes) {
				if(s==null|| s.isEmpty() || s.isBlank()) {
					throw new RuntimeException("股票代碼不可為空");
				}
			}
			List<String[]> findPriceByStockCodes = twt84uDao.findPriceByStockCodes(stockCodes);
			Map<String,String> map = new HashMap<>();
			for (String[] obj : findPriceByStockCodes) {
				map.put(obj[0], obj[1]);
			}
			return map;
		}catch(Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	/*
	 * 取得台股近一個月大盤的收盤價資料提供給首頁的圖表使用
	 * */
	public List<StockModel2> getTwStockMonthData() {
		java.util.Date today = new java.util.Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		if (formattedDate != null && formattedDate.equals(dateFormat.format(today))) {
//			logger.debug("今日已取得證交所API台股近月收盤價資料");
			return twStockMonthData;
        }
        formattedDate = dateFormat.format(today);
		StockModel stockModel = this.restTemplate
				.getForObject("https://www.twse.com.tw/rwd/zh/afterTrading/FMTQIK?date="+formattedDate+"&response=json"
				, StockModel.class);
		List<StockModel2> list = new ArrayList<>();
		for (String[] s : stockModel.getData()) {
			list.add(StockModel2.builder()
					.date(s[0])
					.price(s[4])
					.build());
		}
		twStockMonthData = list;
		logger.debug("取得證交所API台股近月收盤價資料成功");
		return twStockMonthData;
	}
	
	public Page<StockTransaction> getStockTransactionHistory(int userId, int page){
		try {
			List<StockTransaction> result = stockTransactionDao.findByUserId(userId,countPage(page))
					.orElseThrow(()->new RuntimeException("查無此使用者交易紀錄"));
			Pageable pageable = PageRequest.of(page, 15,Sort.by("SERIAL_NUMBER").ascending());//stockHoldingDetailsDao.findCountByFkStockHoldingNo(stockHoldingNo)
			return new PageImpl<>(result, pageable,stockTransactionDao.findCountByUserId(userId));
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private int countPage(int page) {
		if(page==0) {
			return 0;
		}
		return page*15;
	}
	

}
