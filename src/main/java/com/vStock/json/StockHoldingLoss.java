package com.vStock.json;

import java.util.List;
import java.util.Map;

import com.vStock.model.StockHolding;

import lombok.Data;

@Data
public class StockHoldingLoss {
	
	private Map<String,String> stockNamesAndPrices;
	

	private List<StockHolding> holdingList;
	
}
