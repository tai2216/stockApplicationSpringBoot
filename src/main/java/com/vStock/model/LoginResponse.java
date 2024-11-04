package com.vStock.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
	private String username;
	private String role;
	private String message;
	private String token;
}
