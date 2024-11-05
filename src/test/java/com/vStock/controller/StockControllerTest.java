
package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String generateJwtToken() {
		return Jwts.builder()
				.setIssuer("Sam")
				.setSubject("testuser")
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
				.signWith(SignatureAlgorithm.HS512, "MyJwtSecret")
				.compact();
    }

    private MockHttpServletRequestBuilder addJwtToken(MockHttpServletRequestBuilder requestBuilder) {
        return requestBuilder.header("Authorization", "Bearer " + generateJwtToken());
    }

    @Test
    @Order(1)
    void buyStockFailed() throws Exception {
        mockMvc.perform(addJwtToken(post("/buyStock")
                .param("userId", "999")
                .param("quantity", "9")
                .param("stockCode", "2330")
                .param("date", "1131104")))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("交易失敗"));
    }

    @Test
    @Order(2)
    void buyStockSuccessed() throws Exception {
        mockMvc.perform(addJwtToken(post("/buyStock")
                .param("userId", "1")
                .param("quantity", "3000")
                .param("stockCode", "2330")
                .param("date", "1131104")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("交易成功"));
    }
}
