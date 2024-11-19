package com.vStock.util;

import org.springframework.http.ResponseEntity;

import com.vStock.model.GeneralResponse;


public class ResponseUtils {
	
	public static ResponseEntity<GeneralResponse> error(String status,String message,Exception e){
		return ResponseEntity.internalServerError()
				.body(GeneralResponse.builder()
						.setStatus(status)
						.setMessage(message)
						.setError(e.getMessage())
						.build());
	}
	
	public static ResponseEntity<GeneralResponse> success(String status,String message,Object e){
		return ResponseEntity.ok(GeneralResponse.builder()
				.setStatus(status)
				.setMessage(message)
				.setData(e)
				.build());
	}

	
}
