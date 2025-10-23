package com.gestorcoi.constantes;

public class OperDatabase {
	
	private static final String URL = "jdbc:oracle:thin:@10.204.28.120:1521:OG2MDE";
	private static final String USER = "U21023701"; 
	private static final String PASSWORD = "h_!JnWt13yM9";
	
	public static String getPASSWORD() {
		return PASSWORD;
	}
	
	public static String getURL() {
		return URL;
	}
	
	public static String getUSER() {
		return USER;
	}
}
