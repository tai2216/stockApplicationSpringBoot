package com.vStock.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "twt84u")
@DynamicInsert
@DynamicUpdate
public class TWT84U {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Nullable
	@Column(name = "id")
	@JsonIgnore
	private int id;
	
	@JsonProperty("Code")
	@Column(name = "code",columnDefinition = "varchar(10)")
	private String code;
	
	@JsonProperty("Name")
	@Column(name = "name",columnDefinition = "varchar(50)")
	private String name;
	
	@JsonProperty("TodayLimitUp")
	@Column(name = "todayLimitUp",columnDefinition = "varchar(10)")
	private String todayLimitUp;
	
	@JsonProperty("TodayOpeningRefPrice")
	@Column(name = "todayOpeningRefPrice",columnDefinition = "varchar(10)")
	private String todayOpeningRefPrice;
	
	@JsonProperty("TodayLimitDown")
	@Column(name = "todayLimitDown",columnDefinition = "varchar(10)")
	private String todayLimitDown;
	
	@JsonProperty("PreviousDayOpeningRefPrice")
	@Column(name = "previousDayOpeningRefPrice",columnDefinition = "varchar(10)")
	private String previousDayOpeningRefPrice;
	
	@JsonProperty("PreviousDayPrice")
	@Column(name = "previousDayPrice",columnDefinition = "varchar(10)")
	private String previousDayPrice;
	
	@JsonProperty("PreviousDayLimitUp")
	@Column(name = "previousDayLimitUp",columnDefinition = "varchar(10)")
	private String previousDayLimitUp;
	
	@JsonProperty("PreviousDayLimitDown")
	@Column(name = "previousDayLimitDown",columnDefinition = "varchar(10)")
	private String previousDayLimitDown;
	
	@JsonProperty("LastTradingDay")
	@Column(name = "lastTradingDay",columnDefinition = "varchar(7)")
	private String lastTradingDay;
	
	@JsonProperty("AllowOddLotTrade")
	@Column(name = "allowOddLotTrade",columnDefinition = "varchar(5)")
	private String allowOddLotTrade;
	

	
}
