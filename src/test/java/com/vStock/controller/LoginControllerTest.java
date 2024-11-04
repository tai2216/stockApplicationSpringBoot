package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vStock.dao.NormalUserDao;
import com.vStock.service.JavaMailService;
import com.vStock.service.impl.NormalUserServiceImpl;

@SpringBootTest(classes={LoginController.class})
@TestMethodOrder(value = OrderAnnotation.class)
public class LoginControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private LoginController loginController;

    @MockBean
    private NormalUserServiceImpl normalUserService;
    
    @MockBean
    private NormalUserDao normalUserDao;
    
    @MockBean
    private JavaMailService mailService;
    
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.loginController)
                .setControllerAdvice(new Exception())
                .build();
//        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @Order(1)
    public void testLoginControllerFail() throws Exception {
    	String username = "testuser";
    	String password = "password";
    	
    	// Perform the request and verify the response
    	mockMvc.perform(post("/login")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.status").value("error"))
    	.andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
    
    
    @Test
    @Order(2)
    public void testLoginControllerSuccess() throws Exception {
        String username = "testuser";
        String password = "password";

        // Perform the request and verify the response
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }
    
    
    
    
}
