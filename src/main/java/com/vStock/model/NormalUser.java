package com.vStock.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Entity
@Table(name = "NORMAL_USER")
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@DynamicInsert
@DynamicUpdate
@Validated
public class NormalUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private int id;
	
	@NotEmpty
	@Size(min=8, max=30)
	@Column(name = "USERNAME",unique = true,columnDefinition = "NVARCHAR(30)",nullable = false)
	private String username;
	
	@NotEmpty
	@Size(min=8, max=200)
	@Column(name = "PASSWORD",columnDefinition = "NVARCHAR(200)",nullable = false)
	private String password;
	
	@Column(name = "USER_ROLE",columnDefinition = "NVARCHAR(30) DEFAULT 'NORMAL'")
//	@ColumnDefault(value = "'NORMAL'")
	private String userRole;
	
	@Column(name = "EMAIL",columnDefinition = "NVARCHAR(200)",nullable = false)
	@Size(min=8, max=200)
	private String email;
	
	@Column(name = "PHONE",columnDefinition = "NVARCHAR(50)")
	@ColumnDefault(value = "'0917648234'")
	private String phone;//還不確定需不需要這個欄位目前暫時保留
	
	@Column(name = "ENABLED",columnDefinition = "BIT")
	@ColumnDefault(value = "0")
	private boolean enabled;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "LAST_LOGIN_DATE",columnDefinition = "SMALLDATETIME",nullable = true)
	private Timestamp lastLoginDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "REGISTER_DATE",columnDefinition = "SMALLDATETIME")
	private Timestamp registerDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "ENABLED_DATE",columnDefinition = "SMALLDATETIME",nullable = true)
	private Timestamp enabledDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DISABLED_DATE",columnDefinition = "SMALLDATETIME",nullable = true)
	private Timestamp disabledDate;
	
	@Column(name = "REMARK",columnDefinition = "NVARCHAR(200)",nullable = true)
	private String remark;
	
	@OneToMany(fetch = FetchType.EAGER,
			mappedBy = "fkUserId",
			cascade = CascadeType.ALL)
	private List<UserDisabledDetail> userDisabledDetailList;
	

	
	
}
