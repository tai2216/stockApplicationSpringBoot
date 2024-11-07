package com.vStock.service.impl;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.dao.MoneyAccountDao;
import com.vStock.dao.NormalUserDao;
import com.vStock.model.MoneyAccount;
import com.vStock.model.NormalUser;
import com.vStock.service.JavaMailService;
import com.vStock.service.NormalUserService;

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

	@Transactional
	public void registerUser(HttpServletRequest req, HttpServletResponse res) {
		try {
			String username = req.getParameter("username");
			String password = new BCryptPasswordEncoder().encode(req.getParameter("password"));
			String email = req.getParameter("email");
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
					.setRegisterDate(new Date(System.currentTimeMillis()))
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
		            + "<a href="+urlPrefix+"enableUser/?username="+username+">啟用帳號</a>";
			mailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
		}catch(MessagingException me) {
			logger.error("啟用信件寄件錯誤",me.getMessage());
			throw new RuntimeException(me);
		}catch(Exception e) {
			logger.error("註冊失敗",e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public void enableUser(String username, HttpServletRequest req, HttpServletResponse res) {
		Optional<NormalUser> user = null;
		if ((user = normalUserDao.findByUsername(username)).isPresent()) {
			if (user.get().isEnabled()) {
				throw new RuntimeException("此帳號已啟用");
			}
			int id = user.get().getId();
			try {
				normalUserDao.enableUser(id, new Date(System.currentTimeMillis()));
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
		normalUserDao.updateLoginDate(user.getId(), new Date(System.currentTimeMillis()));
	}
}
