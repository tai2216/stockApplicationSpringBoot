package com.vStock.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vStock.model.StockTransaction;

@Repository
public interface StockTransactionDao extends JpaRepository<StockTransaction, Integer>{

}
