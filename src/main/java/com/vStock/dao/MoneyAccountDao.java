package com.vStock.dao;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.MoneyAccount;

@Repository
public interface MoneyAccountDao extends JpaRepository<MoneyAccount, Integer> {

	@Query(nativeQuery = true
			,value="SELECT * FROM MONEY_ACCOUNT WHERE FK_USER_ID = :fkUserId")
	@Transactional(readOnly = true)
	public Optional<MoneyAccount> findByFkUserId(int fkUserId);
	
	@Query(nativeQuery = true
			, value = "UPDATE MONEY_ACCOUNT SET IS_FROZEN = 1 WHERE FK_USER_ID = :fkUserId")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void freezeAccount(int fkUserId);
	
	@Query(nativeQuery = true
			, value = "UPDATE MONEY_ACCOUNT SET IS_FROZEN = 0 WHERE FK_USER_ID = :fkUserId")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void unfreezeAccount(int fkUserId);
	
	@Query(nativeQuery = true
			,value="UPDATE MONEY_ACCOUNT SET BALANCE = :money WHERE FK_USER_ID = :fkUserId")
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	public void updateMoney(int fkUserId, BigDecimal money);
}
