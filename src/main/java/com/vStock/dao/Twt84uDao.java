package com.vStock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.TWT84U;

@Repository
public interface Twt84uDao extends JpaRepository<TWT84U, Integer>, PagingAndSortingRepository<TWT84U, Integer>{
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM TWT84U WHERE CODE = :stockCode")
	@Transactional(readOnly = true)
	public Optional<TWT84U> findByStockCode(String stockCode);
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM TWT84U WHERE CODE LIKE CONCAT('%',:keyWord,'%') OR NAME LIKE CONCAT('%',:keyWord,'%')")
	@Transactional(readOnly = true)
	public List<TWT84U> findByCodeOrName(String keyWord);
	
	@Query(nativeQuery = true
			, value = "SELECT DISTINCT NAME FROM TWT84U WHERE CODE = :code")
	@Transactional(readOnly = true)
	public String findNameByCode(String code);
	
	@Query(nativeQuery = true
			, value = "SELECT DISTINCT PREVIOUS_DAY_PRICE FROM TWT84U WHERE CODE = :code")
	@Transactional(readOnly = true)
	public Optional<String> findPrviousDayPriceByCode(String code);
	
	@Query(nativeQuery = true
			, value = "SELECT DISTINCT PREVIOUS_DAY_PRICE FROM TWT84U WHERE CODE = :code AND LAST_TRADING_DAY = :date")
	@Transactional(readOnly = true)
	public Optional<String> findPrviousDayPriceByCodeAndDate(String code, String date);
}
