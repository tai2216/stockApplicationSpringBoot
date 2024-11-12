package com.vStock.service.impl;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import com.vStock.dao.MoneyAccountDao;
import com.vStock.dao.NormalUserDao;
import com.vStock.json.LoginJson;
import com.vStock.model.MoneyAccount;
import com.vStock.model.NormalUser;
import com.vStock.service.JavaMailService;
import com.vStock.service.NormalUserService;
import com.vStock.util.KeyUtils;

@Service
public class NormalUserServiceImpl implements NormalUserService{
	
	private final static Logger logger = LogManager.getLogger(NormalUserServiceImpl.class);
	
    @Value("${urlPrefix}")
    private String urlPrefix;
	
	@Autowired
	private NormalUserDao normalUserDao;
	
	@Autowired
	private MoneyAccountDao moneyAccountDao;
	
	@Autowired
	private JavaMailService mailService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Scheduled(cron = "0 0 0 * * ?")
	@Transactional
	public void cleanUnvalidUsers() {
		logger.info("開始清理信箱未驗證使用者帳號");
		normalUserDao.deleteAll(normalUserDao.findByRemarkStartWith("VERIFY_EMAIL_"));
		logger.info("清理信箱未驗證使用者帳號完成!");
	}

	@Transactional
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
		            + "<a href="+urlPrefix+"enableUser/?username="+username+"&remark="+remark+">啟用帳號</a>";
			mailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
		}catch(MessagingException me) {
			logger.error("啟用信件寄件錯誤",me.getMessage());
			throw new RuntimeException("啟用信件寄件錯誤");
		}catch(Exception e) {
			logger.error("註冊失敗",e.getMessage());
			throw new RuntimeException("註冊失敗");
		}
	}

	@Transactional
	public void enableUser(HttpServletRequest req, HttpServletResponse res) {
		Optional<NormalUser> user = null;
		String username = req.getParameter("username");
		String remark = req.getParameter("remark");
		System.out.println("username: "+username+" remark: "+remark);
		if(!StringUtils.hasText(username) | !StringUtils.hasText(remark)) {
			throw new RuntimeException("非正常的啟用請求");
		}
		if ((user = normalUserDao.findByUsernameAndRemark(username,remark)).isPresent()) {
			if (user.get().isEnabled()) {
				throw new RuntimeException("此帳號已啟用");
			}
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
