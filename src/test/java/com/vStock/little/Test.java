package com.vStock.little;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Random;

public class Test {
	public static void main(String[] args) {
		
//		System.out.println(new BigDecimal(1745).divide(new BigDecimal(9), 2, RoundingMode.HALF_UP));
		System.out.println(10-(-9));
	}
	
	public static BigDecimal toBig(int num) {
		BigDecimal dec = new BigDecimal(num);
		dec.setScale(0,RoundingMode.HALF_UP);
		return dec;
	}
	
    public static String generateRandomKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
    
	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
    /*
     * Generate a random alphanumeric key of the specified length
     */
    public static String generateAlphanumericKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder key = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(ALPHANUMERIC_CHARACTERS.length());
            key.append(ALPHANUMERIC_CHARACTERS.charAt(index));
        }
        return key.toString();
    } 
}
