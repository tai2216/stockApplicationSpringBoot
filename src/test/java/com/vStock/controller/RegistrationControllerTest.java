package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.vStock.dao.NormalUserDao;
import com.vStock.model.NormalUser;

//@SpringBootTest(classes={RegistrationController.class})
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
//    @Autowired
//    private RegistrationController registrationController;

//    @MockBean
//    @Autowired
//    private NormalUserServiceImpl normalUserService;
    
//    @MockBean
    @Autowired
    private NormalUserDao normalUserDao;
    
//    @MockBean
//    @Autowired
//    private JavaMailService mailService;

//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registrationController)
//                .setControllerAdvice(new Exception())
//                .build();
////        MockitoAnnotations.openMocks(this);
//    }
	String username = "";
	String password = "";
	String email = "";
    @Test
    @Order(1)
    public void testRegisterUser_Failure() throws Exception {
    	System.out.println("執行註冊使用者失敗案例");
    	username = "testuser";
    	password = "QQ123";
    	email = "1234";
        mockMvc.perform(post("/register")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\", \"email\":\"" + email + "\"}"))
//                .param("username", "testuser2")
//                .param("password", "password2")
//                .param("email", "1234"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("註冊失敗"))
                .andExpect(jsonPath("$.status").value("error"));
    }
    

    @Test
    @Order(2)
    public void testRegisterUser_Success() throws Exception {
    	System.out.println("執行註冊使用者成功案例");
    	username = "testuser";
    	password = "password";
    	email = "sam.tian@frog-jump.com";
    	registerMock(username,password,email);
    	username = "testuser2";
    	password = "password";
    	email = "abc123@frog-jump.com";
    	registerMock(username,password,email);
    	username = "testuser3";
    	password = "password";
    	email = "abc1234@frog-jump.com";
    	registerMock(username,password,email);
    	
    	
    }
    
    public void registerMock(String username, String password, String email) throws Exception {
        mockMvc.perform(post("/register")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\", \"email\":\"" + email + "\"}"))
//                .param("username", "testuser")
//                .param("password", "password")
//                .param("email", "sam.tian@frog-jump.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("註冊成功，請於今日結束之前至信箱查看確認信件以啟用帳號"))
                .andExpect(jsonPath("$.status").value("success"));
    }
    
//    @Test
//    @Order(3)
//	public void testEnableUser_Failure() throws Exception {
//		System.out.println("執行啟用使用者帳號失敗案例");
//
//		mockMvc.perform(get("/enableUser").param("username", "testuser2")).andExpect(status().is(500))
//				.andExpect(jsonPath("$.message").value("非正常的啟用請求")).andExpect(jsonPath("$.status").value("error"));
//	}
    
//    @Test
//    @Order(4)
//	public void testEnableUser_Success() throws Exception {
//		System.out.println("執行啟用使用者帳號成功案例");
//
//		mockMvc.perform(get("/enableUser")
//				.param("username", "testuser")
//				.param("remark","13"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.message")
//						.value("啟用成功，請使用此帳號登入"))
//				.andExpect(jsonPath("$.status").value("success"));
//	}
    
    @Test
    @Order(5)
    public void deleteUnvalidatedUser() {
    	System.out.println("執行刪除未驗證使用者案例");
    	List<NormalUser> findByRemarkStartWith = normalUserDao.findByRemarkStartWith("VERIFY_EMAIL_");
    	System.out.println("印出未驗證帳號");
    	findByRemarkStartWith.forEach(System.out::println);
    }
    
    
}
