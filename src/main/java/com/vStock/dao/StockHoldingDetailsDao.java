package com.vStock.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vStock.model.StockHoldingDetails;

@Repository
public interface StockHoldingDetailsDao extends JpaRepository<StockHoldingDetails, Integer>{
	
	//todo:需要考慮一下到底要不要把details整合為另一個table免得每次計算
	public Optional<StockHoldingDetails> findByFkUserId();
}
