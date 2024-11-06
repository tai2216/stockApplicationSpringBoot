
package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.vStock.dao.JwtSecretKeyDao;
import com.vStock.util.TestMatcher;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = OrderAnnotation.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private JwtSecretKeyDao jwtSecretKeyDao;
	
    private String generateJwtToken() {
//		String jwtSecretkey = (String) ((Map<String,Object>)env.getPropertySources().get("jwtProperties").getSource()).get("jwtSecretKey");
		String jwtSecretkey = jwtSecretKeyDao.findAll().get(0).getJwtKey();
		return Jwts.builder()
				.setIssuer("Sam")
				.setSubject("testuser")
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtSecretkey)
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
                .andExpect(jsonPath("$.message").value("交易失敗"))
                .andReturn()
                .getResponse()
                .getWriter().println();
    }

//    @Test
//    @Order(2)
//    void buyStockSuccessed() throws Exception {
//        mockMvc.perform(addJwtToken(post("/buyStock")
//                .param("userId", "1")
//                .param("quantity", "3000")
//                .param("stockCode", "2330")
//                .param("date", "1131104")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("交易成功"))
//                .andReturn()
//                .getResponse()
//                .getWriter().println();
//        
//    }
    
    @Test
    @Order(3)
    void sellStockFailed1() throws Exception{
    	mockMvc.perform(addJwtToken(post("/sellStock")
    			.param("userId", "1")
    			.param("quantity", "3001")
    			.param("stockCode", "2330")
    			.param("date", "1131104")))
    	        .andExpect(status().isInternalServerError())
    	        .andExpect(jsonPath("$.error").value("交易失敗: 未持有此檔股票: 2330"))
                .andReturn()
                .getResponse()
                .getWriter().println();
    }
    
    @Test
    @Order(4)
    void sellStockFailed2() throws Exception{
    	mockMvc.perform(addJwtToken(post("/sellStock")
    			.param("userId", "1")
    			.param("quantity", "3001")
    			.param("stockCode", "2330")
    			.param("date", "11311045")))
    	.andExpect(status().isInternalServerError())
        .andReturn()
        .getResponse()
        .getWriter().println();
    }
    @Test
    @Order(5)
    void sellStockFailed3() throws Exception{
    	mockMvc.perform(addJwtToken(post("/sellStock")
    			.param("userId", "1")
    			.param("quantity", "3000")
    			.param("stockCode", "2331")
    			.param("date", "1131104")))
    	.andExpect(status().isInternalServerError())
    	.andExpect(jsonPath("$.error").value(new TestMatcher<String>("交易失敗: 未持有此檔股票: ")))
        .andReturn()
        .getResponse()
        .getWriter().println();
    }
    
    
}
