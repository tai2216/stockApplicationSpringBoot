package com.vStock.json;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@Validated
public class LoginJson {
	
	@JsonProperty("username")
	@NotEmpty
	@Size(min = 8, max = 30)
	private String username;
	
	@JsonProperty("password")
	@NotEmpty
	@Size(min=8, max=200)
	private String password;
	
	@JsonProperty("email")
	@NotEmpty
	@Size(min=8, max=200)
	private String email;

}
