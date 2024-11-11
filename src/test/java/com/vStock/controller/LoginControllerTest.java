package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest(classes={LoginController.class})
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
public class LoginControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
//    @Autowired
//    private LoginController loginController;

//    @MockBean
//    @Autowired
//    private NormalUserServiceImpl normalUserService;
    
//    @MockBean
//    @Autowired
//    private NormalUserDao normalUserDao;
    
//    @MockBean
//    @Autowired
//    private JavaMailService mailService;
    
//    @Autowired
//    private UsersDetailServiceImpl usersDetailServiceImpl;
    
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
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
//    	.content("{username:"+username+","+"password:"+password+"}"))
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
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        		  .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
//                .content("username="+username+"&"+"password="+password))
//    			.content("{username:"+username+","+"password:"+password+"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Log In Success"));
    }
    
    
    
    
}
