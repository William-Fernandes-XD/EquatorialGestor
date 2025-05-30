package com.gestorcoi.hibernate;

public class VariaveisConexaoUtil {

	private static final String JAVA_COMP_ENV = "java:/comp/env/jdbc/datasource";
	
	public static String getJavaCompEnv() {
		return JAVA_COMP_ENV;
	}
}
