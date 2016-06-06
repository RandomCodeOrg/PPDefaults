package com.github.randomcodeorg.ppplugin.ppdefaults.testing;

import java.util.Random;

public class BaseTest {

	private static final Random random = new Random();
	
	public BaseTest() {
		
	}
	
	public static final char[] FULL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöüß01234567890!\"§$%&/()=?´`^°+#-.,;:_'*<>|\\ []{}@".toCharArray();
	public static final char[] ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜabcdefghijklmnopqrstuvwxyzäöüß0123456789".toCharArray();
	
	
	public static String getRandomString(char[] legalCharacters, int length){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<length; i++){
			sb.append(legalCharacters[random.nextInt(legalCharacters.length)]);
		}
		return sb.toString();
	}
	
	protected String randomString(char[] legalCharacters, int length){
		return getRandomString(legalCharacters, length);
	}
	
	
	
	public static String getRandomString(int length){
		return getRandomString(FULL_CHARS, length);
	}
	
	protected String randomString(int length){
		return getRandomString(length);
	}
	
	
	
	public static String getRandomString(char[] legalCharacters){
		return getRandomString(legalCharacters, 5 + random.nextInt(16));
	}
	
	protected String randomString(char[] legalCharacters){
		return getRandomString(legalCharacters);
	}
	
	
	public static String getRandomString(){
		int length = 5 + random.nextInt(16);
		return getRandomString(length);
	}
	
	protected String randomString(){
		return getRandomString();
	}
	
	public static int getRandomInteger(){
		return random.nextInt();
	}
	
	protected int randomInteger(){
		return getRandomInteger();
	}
	
	protected Random random(){
		return random;
	}

}
