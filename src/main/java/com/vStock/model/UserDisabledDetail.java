package com.vStock.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
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
@Table(name = "user_disabled_detail")
public class UserDisabledDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_disabled_detail_id"
			,nullable = false
			,unique = false)
	private int id;
	
	@Column(name = "fk_user_id")
	private int fkUserId;
	
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
	
	@ManyToOne(targetEntity = NormalUser.class
			,optional = true)
	@JoinColumn(name = "fk_user_id")
	private int getNormalUserId() {return this.fkUserId;};
}
