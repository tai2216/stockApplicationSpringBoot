package com.vStock.config;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
 * 設定將CORS的執行順序移植到Spring Security filter chain的更高優先度
 * 2023/07/19
 * */

@Configuration
public class CorsConfig {
	
	private static final Logger logger = LogManager.getLogger(CorsConfig.class);
	
	@Bean
	public FilterRegistrationBean<CorsFilter>  corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
		corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
		corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
		//corsConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		CorsFilter corsFilter = new CorsFilter(source);
		FilterRegistrationBean<CorsFilter> filterRegistrationBean = new FilterRegistrationBean<>(corsFilter);
		filterRegistrationBean.setOrder(-101);// 小於 SpringSecurity Filter的 Order(-100)
		logger.info("經過CORS CONFIG");
		return filterRegistrationBean ;
	}	
		
}
