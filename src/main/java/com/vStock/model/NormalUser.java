package com.vStock.model;

import java.sql.Date;
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

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "normal_user")
@DynamicInsert
@DynamicUpdate
@Validated
public class NormalUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;
	
	@NotEmpty
	@Size(min=8, max=30)
	@Column(name = "username",unique = true,columnDefinition = "NVARCHAR(30)",nullable = false)
	private String username;
	
	@NotEmpty
	@Size(min=8, max=200)
	@Column(name = "password",columnDefinition = "NVARCHAR(200)",nullable = false)
	private String password;
	
	@Column(name = "user_role",columnDefinition = "NVARCHAR(30) DEFAULT 'NORMAL'")
//	@ColumnDefault(value = "'NORMAL'")
	private String userRole;
	
	@Column(name = "email",columnDefinition = "NVARCHAR(200)",nullable = false)
	@Size(min=8, max=200)
	private String email;
	
	@Column(name = "phone",columnDefinition = "NVARCHAR(50)")
	@ColumnDefault(value = "'0917648234'")
	private String phone;
	
	@Column(name = "enabled",columnDefinition = "BIT")
	@ColumnDefault(value = "0")
	private boolean enabled;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "last_login_date",columnDefinition = "DATE",nullable = true)
	private Date lastLoginDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "register_date",columnDefinition = "DATE")
	private Date registerDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "enabled_date",columnDefinition = "DATE",nullable = true)
	private Date enabledDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "disabled_date",columnDefinition = "DATE",nullable = true)
	private Date disabledDate;
	
	@Column(name = "remark",columnDefinition = "NVARCHAR(200)",nullable = true)
	private String remark;
	
	@OneToMany(fetch = FetchType.EAGER,
			mappedBy = "fkUserId",
			cascade = CascadeType.ALL)
	private List<UserDisabledDetail> userDisabledDetailList;
	
	
	
	public NormalUser(int id, @NotEmpty @Size(min = 8, max = 30) String username,
			@NotEmpty @Size(min = 8, max = 200) String password, String userRole,
			@NotEmpty @Size(min = 8, max = 200) String email, String phone, boolean enabled, Date lastLoginDate,
			Date registerDate, Date enabledDate, Date disabledDate, String remark,
			List<UserDisabledDetail> userDisabledDetailList) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.userRole = userRole;
		this.email = email;
		this.phone = phone;
		this.enabled = enabled;
		this.lastLoginDate = lastLoginDate;
		this.registerDate = registerDate;
		this.enabledDate = enabledDate;
		this.disabledDate = disabledDate;
		this.remark = remark;
		this.userDisabledDetailList = userDisabledDetailList;
	}



	public static class NormalUserBuilder{
		private int id;
        private String username;
        private String password;
        private String userRole;
        private String email;
        private String phone;
        private boolean enabled;
        private Date lastLoginDate;
        private Date registerDate;
        private Date enabledDate;
        private Date disabledDate;
        private String remark;
        private List<UserDisabledDetail> userDisabledDetailList = null;
        
		public NormalUserBuilder setId(int id) {
            this.id = id;
            return this;
        }
        
        public NormalUserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }
        
        public NormalUserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }
        
        public NormalUserBuilder setRole(String userRole) {
            this.userRole = userRole;
            return this;
        }
        
        public NormalUserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }
        
        public NormalUserBuilder setPhone(String phone) {
            this.phone = phone;
            return this;
        }
        
        public NormalUserBuilder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        public NormalUserBuilder setLastLoginDate(Date lastLoginDate) {
            this.lastLoginDate = lastLoginDate;
            return this;
        }
        
        public NormalUserBuilder setRegisterDate(Date registerDate) {
            this.registerDate = registerDate;
            return this;
        }
        
        public NormalUserBuilder setEnabledDate(Date enabledDate) {
            this.enabledDate = enabledDate;
            return this;
        }
        
        public NormalUserBuilder setDisabledDate(Date disabledDate) {
            this.disabledDate = disabledDate;
            return this;
        }
        
        public NormalUserBuilder setRemark(String remark) {
            this.remark = remark;
            return this;
        }
        
        public NormalUserBuilder setUserDisabledDetailList(List<UserDisabledDetail> userDisabledDetailList) {
            this.userDisabledDetailList = userDisabledDetailList;
            return this;
        }
        
        public NormalUser build() {
            return new NormalUser(this.id, this.username, this.password, this.userRole, this.email, this.phone, this.enabled, this.lastLoginDate, this.registerDate, this.enabledDate, this.disabledDate, this.remark, this.userDisabledDetailList);
        }
	}
	
	
}
