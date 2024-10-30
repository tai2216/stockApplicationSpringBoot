package com.vStock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
public class GeneralResponse {
	 private String message;
	 private String status;
	 private Object data;
	 private Object error;
}
