package com.vStock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.StockHolding;

@Repository
public interface StockHoldingDao extends JpaRepository<StockHolding, Integer>{
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM STOCK_HOLDING WHERE FK_USER_ID = :fkUserId")
	@Transactional(readOnly = true)
	public Optional<List<StockHolding>> findByFkUserId(int fkUserId);
	
	@Query(nativeQuery = true, value = "SELECT COUNT(*) FROM STOCK_HOLDING WHERE FK_USER_ID = :fkUserId ")
	@Transactional(readOnly = true)
	public int findCountByFkUserId(int fkUserId);
	
	@Query(nativeQuery = true
			, value = "SELECT * FROM STOCK_HOLDING WHERE FK_USER_ID = :fkUserId "
					+ "ORDER BY SERIAL_NO OFFSET :page ROWS FETCH NEXT 15 ROWS ONLY")
	@Transactional(readOnly = true)
	public Optional<List<StockHolding>> findByFkUserIdToPage(int page, int fkUserId);
	
	
	@Query(nativeQuery = true
            , value = "SELECT * FROM STOCK_HOLDING WHERE FK_USER_ID = :fkUserId AND STOCK_CODE = :stockCode")
	@Transactional(readOnly = true)
	public Optional<StockHolding> findByFkUserIdAndStockCode(int fkUserId,String stockCode);
}
