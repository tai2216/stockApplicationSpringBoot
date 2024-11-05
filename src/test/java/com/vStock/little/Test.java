package com.vStock.little;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vStock.other.TransactionType;

public class Test {

	public static void main(String[] args) {
		System.out.println(new Date(System.currentTimeMillis()));
		printEnum(TransactionType.SELL);
	}
	public static void printEnum(TransactionType type) {
		System.out.println("種類為: "+type.toString());
//		if (type == TransactionType.BUY) {
//			System.out.println("BUY");
//        }else {
//        	System.out.println("SELL");
//        }
	}
}
