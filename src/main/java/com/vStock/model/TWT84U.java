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
@Table(name = "TWT84U")
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
	@Column(name = "today_limit_up",columnDefinition = "varchar(10)")
	private String todayLimitUp;
	
	@JsonProperty("TodayOpeningRefPrice")
	@Column(name = "today_opening_ref_price",columnDefinition = "varchar(10)")
	private String todayOpeningRefPrice;
	
	@JsonProperty("TodayLimitDown")
	@Column(name = "today_limit_down",columnDefinition = "varchar(10)")
	private String todayLimitDown;
	
	@JsonProperty("PreviousDayOpeningRefPrice")
	@Column(name = "previous_day_opening_ref_price",columnDefinition = "varchar(10)")
	private String previousDayOpeningRefPrice;
	
	@JsonProperty("PreviousDayPrice")
	@Column(name = "previous_day_price",columnDefinition = "varchar(10)")
	private String previousDayPrice;
	
	@JsonProperty("PreviousDayLimitUp")
	@Column(name = "previous_day_limit_up",columnDefinition = "varchar(10)")
	private String previousDayLimitUp;
	
	@JsonProperty("PreviousDayLimitDown")
	@Column(name = "previous_day_limit_down",columnDefinition = "varchar(10)")
	private String previousDayLimitDown;
	
	@JsonProperty("LastTradingDay")
	@Column(name = "last_trading_day",columnDefinition = "varchar(7)")
	private String lastTradingDay;
	
	@JsonProperty("AllowOddLotTrade")
	@Column(name = "allow_odd_lot_trade",columnDefinition = "varchar(5)")
	private String allowOddLotTrade;
	

	
}
