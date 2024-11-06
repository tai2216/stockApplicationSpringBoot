package com.vStock.little;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

public class Test {
	public static void main(String[] args) {
		System.out.println(generateRandomKey(new Random().nextInt(100,200)));
	}
	
    public static String generateRandomKey(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[length];
        secureRandom.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
