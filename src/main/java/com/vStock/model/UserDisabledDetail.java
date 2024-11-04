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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@Entity
@Table(name = "USER_DISABLED_DETAIL")
public class UserDisabledDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_DISABLED_DETAIL_ID"
			,nullable = false
			,unique = false)
	private int id;
	
	@Column(name = "FK_USER_ID")
	private int fkUserId;
	
	@Column(name = "DISABLED_REASON")
	private String disabledReason;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DISABLED_DATE")
	private Date disabledDate;
	
	@Column(name = "ERROR_LOG")
	private String errorLog;
	
	@Column(name = "ENABLED_REASON")
	private String enabledReason;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "ENABLED_DATE")
	private Date enabledDate;
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "FK_USER_ID")
	private int getNormalUserId() {return this.fkUserId;};
}
