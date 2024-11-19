package com.vStock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.StockTransaction;

@Repository
public interface StockTransactionDao extends JpaRepository<StockTransaction, Integer>{
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM STOCK_TRANSACTION WHERE FK_USER_ID = :userId "
					+ "ORDER BY SERIAL_NUMBER OFFSET :page ROWS FETCH NEXT 15 ROWS ONLY")
	@Transactional(readOnly = true)
	public Optional<List<StockTransaction>> findByUserId(int userId,int page);
	
	@Query(nativeQuery = true
			, value = "SELECT COUNT(*) FROM STOCK_TRANSACTION WHERE FK_USER_ID = :userId")
	@Transactional(readOnly = true)
	public int findCountByUserId(int userId);

}
