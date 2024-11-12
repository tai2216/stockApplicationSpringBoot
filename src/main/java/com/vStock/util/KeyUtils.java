package com.vStock.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyUtils {
	
	/*
	 * 生成指定長度的隨機字節數組，然後將其編碼為 Base64 
	 * */
	public static String generateKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
	}
	
	/*
	 * 直接指定最小與最大長度生成
	 * */
	public static String generateKey(int min, int max) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] key = new byte[(int) (Math.random() * (max - min) + min)];
		secureRandom.nextBytes(key);
		return Base64.getEncoder().encodeToString(key);
	}
	
	/*
	 * 生成指定範圍的隨機數字
	 * */
	public static int generateRandomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}
}
