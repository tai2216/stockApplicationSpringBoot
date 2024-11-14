
package com.vStock.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
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

//    @Test
//    @Order(1)
//    void buyStockFailed() throws Exception {
//        mockMvc.perform(addJwtToken(post("/buyStock")
//                .param("userId", "999")
//                .param("quantity", "9")
//                .param("stockCode", "2330")
//                .andExpect(status().isInternalServerError())
//                .andExpect(jsonPath("$.message").value("交易失敗"))
//                .andExpect(jsonPath("$.error").value(new TestMatcher<String>("交易失敗: 未找到使用者: ")))
//                .andReturn()
//                .getResponse()
//                .getWriter().println();
//    }
//
//    @Test
//    @Order(2)
//    void buyStockSuccessed() throws Exception {
//        mockMvc.perform(addJwtToken(post("/buyStock")
//                .param("userId", "1")
//                .param("quantity", "10")
//                .param("stockCode", "2330")
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("交易成功"))
//                .andReturn()
//                .getResponse()
//                .getWriter().println();
//    }
//    
//    @Test
//    @Order(3)
//    void sellStockFailed1() throws Exception{
//    	mockMvc.perform(addJwtToken(post("/sellStock")
//    			.param("userId", "1")
//    			.param("quantity", "3001")
//    			.param("stockCode", "2330")
//    	        .andExpect(status().isInternalServerError())
//    	        .andExpect(jsonPath("$.error").value(new TestMatcher<String>("交易失敗: 持有單位不足: ")))
//                .andReturn()
//                .getResponse()
//                .getWriter().println();
//    }
//    
//    @Test
//    @Order(4)
//    void sellStockFailed2() throws Exception{
//    	mockMvc.perform(addJwtToken(post("/sellStock")
//    			.param("userId", "1")
//    			.param("quantity", "10")
//    			.param("stockCode", "2330")
//    	.andExpect(status().isInternalServerError())
//        .andReturn()
//        .getResponse()
//        .getWriter().println();
//    }
//    @Test
//    @Order(5)
//    void sellStockFailed3() throws Exception{
//    	mockMvc.perform(addJwtToken(post("/sellStock")
//    			.param("userId", "1")
//    			.param("quantity", "10")
//    			.param("stockCode", "2331")
//    	.andExpect(status().isInternalServerError())
//    	.andExpect(jsonPath("$.error").value(new TestMatcher<String>("交易失敗: 未持有此檔股票: ")))
//        .andReturn()
//        .getResponse()
//        .getWriter().println();
//    }
//    @Test
//    @Order(6)
//    void sellStockSuccessed() throws Exception{
//    	mockMvc.perform(addJwtToken(post("/sellStock")
//    			.param("userId", "1")
//    			.param("quantity", "10")
//    			.param("stockCode", "2330")
//        .andExpect(status().isOk())
//        .andExpect(jsonPath("$.message").value("交易成功"))
//    	.andReturn()
//    	.getResponse()
//    	.getWriter().println();
//    }
    

//    @Test
//    @Order(7)
//    void testQueryStockInfoFail1() throws UnsupportedEncodingException, Exception {
//    	mockMvc.perform(addJwtToken(get("/queryStockInfo")
//    			.param("page", "-1")
//    			.param("size", "30")
//    			.param("sort", "LastTradingDay")
//    			.param("order", "asc")))
//    	.andExpect(status().isInternalServerError())
//    	.andExpect(jsonPath("$.error").value("Page index must not be less than zero!"))
//    	.andReturn()
//    	.getResponse()
//    	.getWriter()
//    	.println();
//    }
//    @Test
//    @Order(8)
//    void testQueryStockInfoFail2() throws UnsupportedEncodingException, Exception {
//    	mockMvc.perform(addJwtToken(get("/queryStockInfo")
//    			.param("page", "1")
//    			.param("size", "-30")
//    			.param("sort", "LastTradingDay")
//    			.param("order", "asc")))
//    	.andExpect(status().isInternalServerError())
//    	.andExpect(jsonPath("$.error").value("Page size must not be less than one!"))
//    	.andReturn()
//    	.getResponse()
//    	.getWriter()
//    	.println();
//    }
//    
//    @Test
//    @Order(9)
//    void testQueryStockInfoSuccess() throws UnsupportedEncodingException, Exception {
//    	mockMvc.perform(addJwtToken(get("/queryStockInfo")
//    			.param("page", "1")
//    			.param("size", "30")
//    			.param("sort", "LastTradingDay")
//    			.param("order", "asc")))
//    			.andExpect(status().isOk())
//    			.andExpect(jsonPath("$.data").hasJsonPath())
//    			.andReturn()
//    			.getResponse()
//    			.getWriter()
//    			.println();
//    }
    
    @Test
    @Order(10)
    void testSearchStock() throws Exception{
    	String r  = mockMvc.perform(addJwtToken(get("/searchStock")
    			.param("keyWord", "50")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("查詢成功"))
    	.andReturn()
    	.getResponse().getContentAsString();
//    	System.out.println(r);
    }
    
}
