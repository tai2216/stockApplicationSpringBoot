package com.vStock.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@NoArgsConstructor
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@Entity
@Table(name = "STOCK_HOLDING_DETAILS")
@DynamicInsert
@DynamicUpdate
public class StockHoldingDetails {//使用者持股明細TABLE
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERIAL_NO"
			,nullable = false
			,unique = true)
	private int serialNo;
	
	@Column(name = "FK_STOCK_HOLDING_NO")
	private int fkStockHoldingNo;
	
	@Column(name = "FK_USER_ID")
	private int fkUserId;
	
	@Column(name = "STOCK_CODE")
	private String stockCode;
	
	@Column(name = "STOCK_NAME")
	private String stockName;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name = "PRICE")
	private double price;
	
	@Column(name = "COST")
	private BigDecimal cost;
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "FK_USER_ID")
	private int getNormalUserId() {return this.fkUserId;};
	
	@ManyToOne(targetEntity = StockHolding.class
			,optional = true
			,fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_STOCK_HOLDING_NO")
	private int getfkStockHoldingNo() {return this.fkStockHoldingNo;};

	
	
}
