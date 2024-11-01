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

import com.vStock.dao.NormalUserDao;
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
			NormalUser user = new NormalUser.NormalUserBuilder()
					.setUsername(username)
					.setPassword(password)
					.setEmail(email)
					.setRegisterDate(new Date(System.currentTimeMillis()))
					.build();
			normalUserDao.save(user);
			normalUserDao.flush();
		    String[] receivers = {user.getEmail()};
		    String subject = "[註冊成功] 請點擊信件連結以啟用帳號";
		    String htmlContent = "<h1>您已成功註冊 Stock Market Simulation!</h1>"
		            + "<p>請點擊以下連結以啟用帳號:</p>"
		            + "<a href="+urlPrefix+"enableUser/?username="+username+"\">啟用帳號</a>";
			mailService.sendMail(Arrays.asList(receivers), subject, htmlContent);
		}catch(MessagingException me) {
			logger.error(me.getCause());
			throw new RuntimeException(me);
		}catch(Exception e) {
			logger.error(e.getCause());
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
			user.get().setEnabled(true);
			try {
				normalUserDao.save(user.get());
			}catch(Exception e) {
				logger.error(e.getCause());
			}
		}else {
			throw new UsernameNotFoundException("查無此使用者帳號");
		}
		
	}
}
