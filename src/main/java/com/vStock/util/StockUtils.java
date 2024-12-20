package com.vStock.util;

public class StockUtils {
	
	/*
	 * 計算台股交易手續費 0.1425% (0.001425)
	 * */
	public static long countServiceCharge(double price) {
		long calculateResult = Math.round(price * 0.001425);
		return (calculateResult < 20) ? 20 : calculateResult;
	}
	
	/*
	 * 計算台股證券交易稅 0.3% (0.003)
	 * */
	public static long countTax(double price) {
        long calculateResult = Math.round(price * 0.003);
        return calculateResult;
	}
	
}
