package com.vStock.json;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Validated
public class FogotPasswordJson {
	
	@JsonProperty("email")
	@NotEmpty
	private String email;
	
	@JsonProperty("verifyCode")
	@NotEmpty
	private String verifyCode;
	
	@Size(min = 8, max = 100,message = "密碼長度至少為8~100")
	@JsonProperty("newPassword")
	@NotEmpty
	private String newPassword;
	
}
