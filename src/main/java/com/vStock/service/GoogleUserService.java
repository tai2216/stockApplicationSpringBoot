package com.vStock.service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.vStock.dao.GoogleUserDao;
import com.vStock.dao.MoneyAccountDao;
import com.vStock.dao.NormalUserDao;
import com.vStock.model.GoogleCredential;
import com.vStock.model.GoogleUser;
import com.vStock.model.MoneyAccount;
import com.vStock.model.NormalUser;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class GoogleUserService {
	
	private static final Logger logger = LogManager.getLogger(GoogleUserService.class);
	
	@Autowired
	private GoogleUserDao googleUserDao;
	
	@Autowired
	private NormalUserDao normalUserDao;
	
	@Autowired
	private MoneyAccountDao moneyAccountDao;
	
	private String jwtSecretKey;
	
	public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
	}
	
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;
	
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;
	
	public void googleLoginFlow(String reqBody,HttpServletRequest request, HttpServletResponse response) {
		try {
//			logger.debug(reqBody);
			ObjectMapper objectMapper = new ObjectMapper();
			com.vStock.model.GoogleCredential credential = objectMapper.readValue(reqBody, GoogleCredential.class);
//			logger.debug("google credential: "+credential.getCredential());
			GoogleUser googleUser = verifyGoogleToken(credential.getCredential());
			if(googleUser!=null) {
				String token = Jwts.builder()
						.setIssuer("Sam")
						.setSubject(googleUser.getName())
						.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
						.signWith(SignatureAlgorithm.HS512, jwtSecretKey)
						.compact();
//				logger.debug("google login token: "+token);
				response.addHeader("loginUserName", googleUser.getUsername());
				response.addHeader("googleName", googleUser.getName());
				response.addHeader("googlePicture", googleUser.getPictureUrl());
				response.addHeader("Authorization", "Bearer " + token);
				response.addHeader("role", "GOOGLE_USER");
			}else {
				throw new RuntimeException("找不到該Google用戶");
			}
		}catch(Exception e) {
			e.printStackTrace();
			response.addHeader("GoogleLogInError", e.getMessage());
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			logger.error("google 驗證失敗");
			
		}
	}
	
	@Transactional
	public GoogleUser verifyGoogleToken(String googleCredential) {
		try {
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
					.Builder(new NetHttpTransport(),GsonFactory.getDefaultInstance())
					// Specify the CLIENT_ID of the app that accesses the backend:
					.setAudience(Collections.singletonList(clientId))
					// Or, if multiple clients access the backend:
					//.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
					.build();
			GoogleIdToken idToken = null;
			idToken = verifier.verify(googleCredential);
			if (idToken != null) {
				Payload payload = idToken.getPayload();
				GoogleUser googleUser = GoogleUser.builder()
							                    .setUsername(payload.getSubject()+payload.getEmail())
												.setEmail(payload.getEmail())
												.setEmailVerified(Boolean.valueOf(payload.getEmailVerified()))
												.setFamilyName((String) payload.get("family_name"))
												.setGivenName((String) payload.get("given_name"))
												.setLocale((String) payload.get("locale"))
												.setName((String) payload.get("name"))
												.setPictureUrl((String) payload.get("picture"))
												.build();
				if (googleUserDao.findByUsername(googleUser.getUsername()).isPresent()) {
					return normalUserDao.findByEmail(googleUser.getEmail()).isPresent()? googleUser : null;
				}else {
					googleUserDao.save(googleUser);
				}
				if(normalUserDao.findByEmail(googleUser.getEmail()).isPresent()) {
					throw new RuntimeException("此Google信箱已被註冊");
				}
				if (normalUserDao.findByUsername(googleUser.getUsername()).isPresent()) {
					throw new RuntimeException("此Google帳號已被註冊");
				}
				NormalUser user = NormalUser.builder()
						.setUsername(googleUser.getUsername())
						.setPassword(googleUser.getUsername())
						.setUserRole("GOOGLE_USER")
						.setEmail(googleUser.getEmail())
						.setRegisterDate(Timestamp.from(java.time.Instant.now()))
						.setEnabled(true)
						.setEnabledDate(Timestamp.from(java.time.Instant.now()))
						.setLastLoginDate(Timestamp.from(java.time.Instant.now()))
						.setRemark("Google User")
						.build();
				normalUserDao.save(user);
				//create money account
				moneyAccountDao.save(MoneyAccount.builder()
						.setFkUserId(user.getId())
						.setIsFrozen(false)
						.build());
				return googleUser;
			} else {
				throw new RuntimeException("Invalid Credential token");
			}
		}catch(Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}
