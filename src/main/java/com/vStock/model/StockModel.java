package com.vStock.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude
@Data
public class StockModel {
	@JsonProperty("data")
	private List<String[]> data;
}
