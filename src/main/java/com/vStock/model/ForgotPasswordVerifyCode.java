package com.vStock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@Entity
@Table(name = "FORGOT_PASSWORD_VERIFY_CODE")
public class ForgotPasswordVerifyCode {

	
	@Id
	@Column(name = "EMAIL", unique=true ,columnDefinition = "NVARCHAR(100)",nullable = false)
	private String email;
	
	@Column(name = "VERIFY_CODE",columnDefinition = "NVARCHAR(20)",nullable = false)
	private String verifyCode;
	
	@Column(name = "RETRY_COUNT",columnDefinition = "INT DEFAULT 0")
	private int retryCount;

}
