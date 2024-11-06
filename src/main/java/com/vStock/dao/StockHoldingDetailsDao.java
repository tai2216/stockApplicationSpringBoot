package com.vStock.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vStock.model.StockHoldingDetails;
import com.vStock.other.TransactionType;

@Repository
public interface StockHoldingDetailsDao extends JpaRepository<StockHoldingDetails, Integer>{
	
	//todo:需要考慮一下到底要不要把details整合為另一個table免得每次計算
	@Query(nativeQuery = true
			,value = "SELECT * FROM STOCK_HOLDING_DETAILS WHERE FK_USER_ID = :fkUserId")
	@Transactional(readOnly = true)
	public Optional<List<StockHoldingDetails>> findByFkUserId(int fkUserId);
	
	@Query(nativeQuery = true
			,value = "SELECT * FROM STOCK_HOLDING_DETAILS WHERE FK_USER_ID = :fkUserId AND STOCK_CODE = :stockCode")
	@Transactional(readOnly = true)
	public Optional<List<StockHoldingDetails>> findByFkUserIdAndStockCode(int fkUserId,String stockCode);
	
	@Query(nativeQuery = true
			,value = "SELECT * FROM STOCK_HOLDING_DETAILS WHERE FK_USER_ID = :fkUserId AND STOCK_CODE = :stockCode AND TRANSACTION_TYPE = :type")
	@Transactional(readOnly = true)
	public Optional<List<StockHoldingDetails>> findByFkUserIdAndStockCodeAndTransType(int fkUserId,String stockCode,TransactionType type);
	
}
