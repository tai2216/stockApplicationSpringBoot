package com.vStock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.ForgotPasswordVerifyCode;

@Repository
public interface ForgotPasswordVerifyCodeDao extends JpaRepository<ForgotPasswordVerifyCode, String> {
	
	@Query(nativeQuery = true
			,value="SELECT * FROM FORGOT_PASSWORD_VERIFY_CODE WHERE EMAIL = :email")
	@Transactional(readOnly = true)
	public Optional<ForgotPasswordVerifyCode> findByEmail(String email);
	
	@Query(nativeQuery = true
			, value = "UPDATE FORGOT_PASSWORD_VERIFY_CODE SET VERIFY_CODE = :verifyCode WHERE EMAIL = :email")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updateVerifyCode(String email, String verifyCode);
	
	@Query(nativeQuery = true
			, value = "UPDATE FORGOT_PASSWORD_VERIFY_CODE SET RETRY_COUNT = RETRY_COUNT+1 WHERE EMAIL = :email")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updateRetryCountByEmail(String email);
	
	@Query(nativeQuery = true
			,value="SELECT RETRY_COUNT FROM FORGOT_PASSWORD_VERIFY_CODE WHERE EMAIL = :email")
	@Transactional(readOnly = true)
	public int findRetryCountByEmail(String email);

}
