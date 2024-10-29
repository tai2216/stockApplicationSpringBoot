package com.vStock;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vStock.dao.NormalUserDao;
import com.vStock.dao.UserDisabledDetailDao;
import com.vStock.model.NormalUser;

@SpringBootTest
class StockMarketSimulationApplicationTests {

	@Autowired
	private NormalUserDao normalUserdao;
	
	@Autowired
	private UserDisabledDetailDao userDisabledDetaildao;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void normalUserCanBeInserted() {
		System.out.println("Test Normal User Insertion");
		NormalUser user = new NormalUser.NormalUserBuilder()
							.setUsername("testUser")
							.setPassword("testPassword")
							.setEmail("testEmail@gamil.com")
							.setRegisterDate(new Date(System.currentTimeMillis()))
							.build();
		try {
			normalUserdao.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		normalUserdao.flush();
	}

}
