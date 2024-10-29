package com.vStock.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "user_disabled_detail")
public class UserDisabledDetail {

	@Id
	@Column(name = "user_disabled_detail_id",nullable = false)
	private int id;
	
	@Column(name = "disabled_reason")
	private String disabledReason;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "disabled_date")
	private Date disabledDate;
	
	@Column(name = "error_log")
	private String errorLog;
	
	@Column(name = "enabled_reason")
	private String enabledReason;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "enabled_date")
	private Date enabledDate;
	
	@ManyToOne(targetEntity = NormalUser.class,optional = true)
	@JoinColumn(name = "user_id")
	private NormalUser normalUser;
}
