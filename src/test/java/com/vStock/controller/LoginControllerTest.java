package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vStock.dao.NormalUserDao;
import com.vStock.service.JavaMailService;
import com.vStock.service.impl.NormalUserServiceImpl;
import com.vStock.service.impl.UsersDetailServiceImpl;

//@SpringBootTest(classes={LoginController.class})
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
public class LoginControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
    @Autowired
    private LoginController loginController;

//    @MockBean
    @Autowired
    private NormalUserServiceImpl normalUserService;
    
//    @MockBean
    @Autowired
    private NormalUserDao normalUserDao;
    
//    @MockBean
    @Autowired
    private JavaMailService mailService;
    
    @Autowired
    private UsersDetailServiceImpl usersDetailServiceImpl;
    
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(this.loginController)
//                .setControllerAdvice(new Exception())
//                .build();
////        MockitoAnnotations.openMocks(this);
//    }
    
    @Test
    @Order(1)
    public void testLoginControllerFail() throws Exception {
    	String username = "testuser";
    	String password = "QQ123";
    	
    	// Perform the request and verify the response
    	mockMvc.perform(post("/login")
    			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//    			.content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
    	.content("username="+username+"&"+"password="+password))
    	.andExpect(status().is(403))
//    	.andExpect(jsonPath("$.status").value("error"))
    	.andExpect(header().string("Log In Error", "Bad credentials"));
    }
    
    
    @Test
    @Order(2)
    public void testLoginControllerSuccess() throws Exception {
        String username = "testuser";
        String password = "password";

        // Perform the request and verify the response
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .content("username="+username+"&"+"password="+password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Log In Success"));
    }
    
    
    
    
}
