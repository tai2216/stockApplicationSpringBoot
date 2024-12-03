package com.vStock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.vStock.*")
@EnableScheduling
@EnableEurekaClient
public class StockMarketSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketSimulationApplication.class,args);}
	
}
