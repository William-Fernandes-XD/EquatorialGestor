package com.gestorcoi.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFramework implements Serializable{

	private static final long serialVersionUID = 1L;

	
	public static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();
	
	public synchronized static ThreadLocal<Long> geThreadLocal(){
		return threadLocal; 
	}
}
