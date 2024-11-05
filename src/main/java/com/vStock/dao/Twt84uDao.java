package com.vStock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.TWT84U;

@Repository
public interface Twt84uDao extends JpaRepository<TWT84U, Integer>{
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM TWT84U WHERE CODE = :stockCode")
	@Transactional(readOnly = true)
	public Optional<TWT84U> findByStockCode(String stockCode);
	
	@Query(nativeQuery = true
			, value = "SELECT DISTINCT NAME FROM TWT84U WHERE CODE = :code")
	@Transactional(readOnly = true)
	public String findNameByCode(String code);
	
	@Query(nativeQuery = true
			, value = "SELECT DISTINCT PREVIOUS_DAY_PRICE FROM TWT84U WHERE CODE = :code AND LAST_TRADING_DAY = :date")
	@Transactional(readOnly = true)
	public String findPrviousDayPriceByCodeAndDate(String code, String date);
}
