package com.vStock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = lombok.AccessLevel.PUBLIC)
@Entity
@Table(name = "GOOGLE_USER")
@Builder(access = lombok.AccessLevel.PUBLIC,setterPrefix = "set")
@DynamicInsert
@DynamicUpdate
public class GoogleUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private int id;
	
	@NotEmpty
	@Column(name = "USERNAME",unique = true,columnDefinition = "NVARCHAR(200)",nullable = false)
	//預計此欄位會組合subject跟email避免跟normalUser的username重複
	private String username;
	
	@Column(name = "SUBJECT",columnDefinition = "NVARCHAR(50)")
	private String subject;//等同於userID
	
	@Column(name = "EMAIL",columnDefinition = "NVARCHAR(100)",nullable = false)
	private String email;
	
	@Column(name = "EMAIL_VERIFIED",columnDefinition = "BIT")
	private boolean emailVerified;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "PICTURE_URL")
	private String pictureUrl;
	
	@Column(name = "LOCALE")
	private String locale;
	
	@Column(name = "FAMILY_NAME")
	private String familyName;
	
	@Column(name = "GIVEN_NAME")
	private String givenName;
}
