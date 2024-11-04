package com.vStock.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.vStock.dao.NormalUserDao;
import com.vStock.service.JavaMailService;
import com.vStock.service.impl.NormalUserServiceImpl;

@SpringBootTest(classes={RegistrationController.class})
//@WebMvcTest(RegistrationController.class)
@TestMethodOrder(value = OrderAnnotation.class)
public class RegistrationControllerTest {

//    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private RegistrationController registrationController;

    @MockBean
    private NormalUserServiceImpl normalUserService;
    
    @MockBean
    private NormalUserDao normalUserDao;
    
    @MockBean
    private JavaMailService mailService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.registrationController)
                .setControllerAdvice(new Exception())
                .build();
//        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    public void testRegisterUser_Failure() throws Exception {
    	System.out.println("執行失敗案例");
        doThrow(new RuntimeException("Simulated error")).when(normalUserService).registerUser(any(HttpServletRequest.class), any(HttpServletResponse.class));

        mockMvc.perform(post("/register")
                .param("username", "testuser2")
                .param("password", "password2")
                .param("email", "sam.tian@frog-jump.com"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("註冊失敗，請稍後再試"))
                .andExpect(jsonPath("$.status").value("error"));
    }
    

    @Test
    @Order(2)
    public void testRegisterUser_Success() throws Exception {
    	System.out.println("執行成功案例");
        doNothing().when(normalUserService).registerUser(any(HttpServletRequest.class), any(HttpServletResponse.class));
        
        mockMvc.perform(post("/register")
                .param("username", "testuser")
                .param("password", "password")
                .param("email", "sam.tian@frog-jump.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("註冊成功，請至信箱查看確認信件以啟用帳號"))
                .andExpect(jsonPath("$.status").value("success"));
    }
    
    @Test
    @Order(3)
	public void testEnableUser_Failure() throws Exception {
		System.out.println("執行失敗案例");
		doThrow(new RuntimeException("Simulated error")).when(normalUserService).enableUser(anyString(),
				any(HttpServletRequest.class), any(HttpServletResponse.class));

		mockMvc.perform(get("/enableUser").param("username", "testuser2")).andExpect(status().is(500))
				.andExpect(jsonPath("$.message").value("啟用失敗，請稍後再試")).andExpect(jsonPath("$.status").value("error"));
	}
    
    @Test
    @Order(4)
	public void testEnableUser_Success() throws Exception {
		System.out.println("執行成功案例");
		doNothing().when(normalUserService).enableUser(anyString(), any(HttpServletRequest.class),
				any(HttpServletResponse.class));

		mockMvc.perform(get("/enableUser").param("username", "testuser")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("啟用成功，請使用此帳號登入")).andExpect(jsonPath("$.status").value("success"));
	}
    
    
}
