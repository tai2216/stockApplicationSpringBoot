package com.vStock.model;

import java.math.BigDecimal;

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
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@Entity
@Table(name = "MONEY_ACCOUNT")
@DynamicInsert
@DynamicUpdate
public class MoneyAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ACCOUNT_NUMBER"
			,nullable = false
			,unique = true)
	private int accountNumber;
	
	@Column(name = "FK_USER_ID")
	private int fkUserId;
	
	@Column(name = "BALANCE", columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
	private BigDecimal balance;
	
	@Column(name = "CURRENCY", columnDefinition = "NVARCHAR(3) DEFAULT 'TWD'")
	private String currency;
	
	@Column(name = "IS_FROZEN",columnDefinition = "BIT DEFAULT 0")
	private boolean isFrozen;
	
	@Column(name = "REMARK")
	private String remark;
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "FK_USER_ID")
	private int getNormalUserId() {return this.fkUserId;};
	
}
