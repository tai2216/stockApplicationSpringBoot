package com.vStock.model;

import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "STOCK_HOLDING")
@DynamicInsert
@DynamicUpdate
public class StockHolding {//使用者持股TABLE
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERIAL_NO"
			,nullable = false
			,unique = true)
	private int serialNo;
	
	@Column(name = "FK_USER_ID")
	private int fkUserId;
	
	@Column(name="STOCK_CODE")
	private String stockCode;
	
	@Column(name="STOCK_NAME")
	private String stockName;
	
	@Column(name="PRICE_AVERAGE")
	private double priceAverage;
	
	@Column(name="TOTAL_QUANTITY")
	private int totalQuantity;
	
	@Column(name="TOTAL_COST")
	private BigDecimal totalCost;
	
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "FK_USER_ID")
	private int getNormalUserId() {return this.fkUserId;};
	
}
