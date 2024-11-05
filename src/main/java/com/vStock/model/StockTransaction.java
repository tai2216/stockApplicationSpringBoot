package com.vStock.model;

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
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@Entity
@Table(name = "STOCK_TRANSACTION")
@DynamicInsert
@DynamicUpdate
public class StockTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERIAL_NUMBER"
			,nullable = false
			,unique = true)
	private int serialNumber;
	
	@Column(name = "FK_USER_ID")
	private int fkUserId;
	
	@Column(name = "STOCK_CODE",columnDefinition = "NVARCHAR(10)")
	private String stockCode;
	
	@Column(name = "PRICE")
	private double price;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "TRANSACTION_DATE")
	private Date transactionDate;
	
	@Column(name = "TRANSACTION_TYPE")
	private String transactionType;	
	
	@Column(name = "REMARK")
	private String remark;
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "FK_USER_ID")
	private int getNormalUserId() {return this.fkUserId;};

}
