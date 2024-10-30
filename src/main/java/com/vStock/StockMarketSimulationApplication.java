package com.vStock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.vStock.*")
public class StockMarketSimulationApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketSimulationApplication.class, args);
	}

}
