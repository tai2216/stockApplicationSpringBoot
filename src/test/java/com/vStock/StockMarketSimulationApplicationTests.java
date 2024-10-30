package com.vStock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.vStock.dao.NormalUserDao;
import com.vStock.dao.UserDisabledDetailDao;
import com.vStock.model.NormalUser;
import com.vStock.model.UserDisabledDetail;

@SpringBootTest
@TestMethodOrder(value = OrderAnnotation.class)
class StockMarketSimulationApplicationTests {

	@Autowired
	private NormalUserDao normalUserdao;
	
	@Autowired
	private UserDisabledDetailDao userDisabledDetaildao;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	@Order(1)
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
			normalUserdao.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(2)
	void normalUserCanBeEdited() {
        System.out.println("Test Normal User Edit");
        Optional<NormalUser> user = normalUserdao.findById(1);
		if (user.isPresent()) {
			user.get().setUsername("editedUser");
			assertEquals("editedUser",normalUserdao.save(user.get()).getUsername());
			normalUserdao.flush();
		}else {
			System.out.println("User not found");
			throw new RuntimeException("User not found");
		}
	}
	
	@Test
	@Order(3)
	void UserDisabledDetailCanBeInserted() {
        System.out.println("Test UserDisabledDetail Insertion");
        List<UserDisabledDetail> list = new ArrayList<>();
        NormalUser fkUser = normalUserdao.findById(1).orElseThrow(() -> new RuntimeException("User not found"));
        for(int i=1; i<=3; i++) {
        	UserDisabledDetail detail = UserDisabledDetail.builder()
        			.setFkUserId(fkUser.getId())
        			.setDisabledReason("Illeagal Access"+i)
        			.setDisabledDate(new Date(System.currentTimeMillis()))
        			.setErrorLog("XXException:XXerror occurred"+i)
        			.setEnabledReason("電話認證為真人"+i)
        			.setEnabledDate(new Date(System.currentTimeMillis()))
        			.build();
        	list.add(detail);
        }
        System.out.println("有"+list.size()+"筆資料");
        try {
        	userDisabledDetaildao.saveAll(list);
        	userDisabledDetaildao.flush();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
//	@Test
//	@Order(4)
//	void normalUserCanBeDeleted() {
//		System.out.println("Test Normal User Delete");
//		try {
//			normalUserdao.deleteById(1);
//			normalUserdao.flush();
//	    } catch (Exception e) {
//	    	e.printStackTrace();
//	    }
//	}
	

                		
}
