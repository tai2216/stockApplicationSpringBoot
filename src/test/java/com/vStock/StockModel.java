package com.vStock;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude
public class StockModel {
	@JsonProperty("data")
	private List<String[]> data;
}
