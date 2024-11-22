package com.vStock.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vStock.dao.ForgotPasswordVerifyCodeDao;
import com.vStock.dao.MoneyAccountDao;
import com.vStock.dao.NormalUserDao;
import com.vStock.json.FogotPasswordJson;
import com.vStock.json.LoginJson;
import com.vStock.model.ForgotPasswordVerifyCode;
import com.vStock.model.MoneyAccount;
import com.vStock.model.NormalUser;
import com.vStock.util.KeyUtils;

@Service
public class NormalUserService{
	
	private final static Logger logger = LogManager.getLogger(NormalUserService.class);
	
    @Value("${urlPrefix}")
    private String urlPrefix;
    
	@Value("${frontEndUrlPrefix}")
	private String frontEndUrlPrefix;
	
	@Autowired
	private NormalUserDao normalUserDao;
	
	@Autowired
	private MoneyAccountDao moneyAccountDao;
	
	@Autowired
	private JavaMailService mailService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ForgotPasswordVerifyCodeDao forgotPasswordVerifyCodeDao;
	
	/*
	 * 每日凌晨12點的排程
	 * */
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void dailySchedule() {
		logger.info("開始清理信箱未驗證使用者帳號");
		normalUserDao.deleteAll(normalUserDao.findByRemarkStartWith("VERIFY_EMAIL_"));
		logger.info("清理信箱未驗證使用者帳號完成!");
		logger.info("開始清理信箱未完成驗證的忘記密碼驗證碼");
		forgotPasswordVerifyCodeDao.deleteAll();
		logger.info("清理信箱未完成驗證的忘記密碼驗證碼完成!");
	}
	
	@Transactional
	public void resetPassword(FogotPasswordJson fogotPasswordJson) {
		try {
			Optional<ForgotPasswordVerifyCode> obj = forgotPasswordVerifyCodeDao.findByEmail(fogotPasswordJson.getEmail());
			if (obj.isPresent()) {
				if(forgotPasswordVerifyCodeDao.findRetryCountByEmail(fogotPasswordJson.getEmail())>=5) {
					throw new RuntimeException("今日重置密碼次數已達5次上限，請明日再試");
				}
				if (obj.get().getVerifyCode().equals(fogotPasswordJson.getVerifyCode())) {
					NormalUser user = normalUserDao.findByEmail(fogotPasswordJson.getEmail())
							.orElseThrow(() -> new RuntimeException("查無此使用者帳號"));
					String newPassword = new BCryptPasswordEncoder().encode(fogotPasswordJson.getNewPassword());
					normalUserDao.updatePassword(user.getId(), newPassword);
					forgotPasswordVerifyCodeDao.delete(obj.get());
				}else {
					throw new RuntimeException("驗證碼錯誤，重置密碼次數若超過5次則需等到明日再試");
				}
			}else {
				throw new RuntimeException("查無此信箱");
			}
		}catch(Exception e) {
//			e.printStackTrace();
			logger.debug(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	//每次執行後就會更新重試次數，檢查若超過5次重試就需等到明日再試
	@Transactional(noRollbackForClassName = "Exception")
	public void updateRetryCount(String email) {
		forgotPasswordVerifyCodeDao.updateRetryCountByEmail(email);
		forgotPasswordVerifyCodeDao.flush();
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void sendForgotPasswordEmail(String email) {
		Optional<NormalUser> user = normalUserDao.findByEmail(email);
		try {
			if (user.isPresent()) {
				String verifyCode = KeyUtils.generateAlphanumericKey(5,15);
				if(forgotPasswordVerifyCodeDao.findByEmail(email).isPresent()) {
					//如果當天驗證碼已存在就採用update的方式換發驗證碼，避免將重試次數的紀錄歸零
                    forgotPasswordVerifyCodeDao.updateVerifyCode(email, verifyCode);
				}else {
					forgotPasswordVerifyCodeDao
					.save(ForgotPasswordVerifyCode.builder().setEmail(email)
							.setVerifyCode(verifyCode)
							.build());
				}
				String[] receivers = {user.get().getEmail()};
				String subject = "[重置密碼] 請於忘記密碼頁面輸入驗證碼以重置您的密碼";
				String htmlContent = "<h1>Stock Market Simulation</h1>"
						+ "<p>您的驗證碼為: </p>"
						+ verifyCode;
				
				mailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
			}else {
				throw new RuntimeException("請確認此信箱是否正確");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} catch(Exception e) {
//			e.printStackTrace();
			logger.debug(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void registerUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			LoginJson loginJson = null;
			try {
				loginJson = objectMapper.readValue(req.getReader(), LoginJson.class);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("註冊資訊有誤: "+e.getMessage());
				throw new RuntimeException("註冊資訊有誤");
			}
			String username = loginJson.getUsername();
			if(loginJson.getPassword().length()<8) {
				throw new RuntimeException("密碼長度需大於8碼");
			}
			String password = new BCryptPasswordEncoder().encode(loginJson.getPassword());
			String email = loginJson.getEmail();
			//用來啟用帳號時驗證用，避免使用者可直接繞過信箱驗證直接啟用帳號
			String remark = "VERIFY_EMAIL_"+KeyUtils.generateAlphanumericKey(10,30);
			if (normalUserDao.findByUsername(username).isPresent()) {
				throw new RuntimeException("此帳號已被註冊");
			}
			if (normalUserDao.findByEmail(email).isPresent()) {
				throw new RuntimeException("此信箱已被註冊");
			}
			NormalUser user = NormalUser.builder()
					.setUsername(username)
					.setPassword(password)
					.setEmail(email)
					.setRegisterDate(Timestamp.from(java.time.Instant.now()))
					.setRemark(remark)
					.build();
			normalUserDao.save(user);
			
			//create money account
			moneyAccountDao.save(MoneyAccount.builder()
					.setFkUserId(user.getId())
					.setIsFrozen(true)
					.build());
		    String[] receivers = {user.getEmail()};
		    String subject = "[註冊成功] 請點擊信件連結以啟用帳號";
		    String htmlContent = "<h1>您已成功註冊 Stock Market Simulation!</h1>"
		            + "<p>請點擊以下連結以啟用帳號:</p>"
		            + "<a href="+frontEndUrlPrefix+"enableUser/"+username+"/"+remark+">啟用帳號</a>";
			mailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
		}catch(MessagingException me) {
			logger.error("啟用信件寄件錯誤",me.getMessage());
			throw new RuntimeException("啟用信件寄件錯誤");
		}catch(Exception e) {
			logger.error("註冊失敗",e.getMessage());
			throw new RuntimeException("註冊失敗");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	public void enableUser(HttpServletRequest req, HttpServletResponse res) {
		Optional<NormalUser> user = null;
		String username = req.getParameter("username");
		String remark = req.getParameter("remark");
		logger.debug("username: "+username+" remark: "+remark);
		if(!StringUtils.hasText(username) | !StringUtils.hasText(remark)) {
			throw new RuntimeException("非正常的啟用請求");
		}
		user = normalUserDao.findByUsername(username);
		if (user.isPresent() && user.get().isEnabled()) {
			throw new RuntimeException("此帳號已啟用");
		}
		if ((user = normalUserDao.findByUsernameAndRemark(username,remark)).isPresent()) {
			int id = user.get().getId();
			try {
				normalUserDao.enableUser(id, Timestamp.from(java.time.Instant.now()));
				normalUserDao.updateRemarkByUsername(username, "");
				moneyAccountDao.unfreezeAccount(id);
			}catch(Exception e) {
				logger.error("使用者啟用帳號失敗或帳戶解凍失敗",e.getMessage());
				throw new RuntimeException("使用者啟用帳號失敗或帳戶解凍失敗");
			}
		}else {
			throw new UsernameNotFoundException("查無此使用者帳號");
		}
		
	}

	@Transactional
	public void updateLoginDate(String username) {
		NormalUser user = normalUserDao.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("查無此使用者帳號"));
		if(!user.isEnabled()) {
			throw new RuntimeException("此帳號狀態目前為停用");
		}
		normalUserDao.updateLoginDate(user.getId(), Timestamp.from(java.time.Instant.now()));
	}
}
