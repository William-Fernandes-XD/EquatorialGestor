package com.gestorcoi.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class DataUtil implements Serializable{

	private static final long serialVersionUID = 1L;

	public static String getDataAtualString() {
		
		String dateFormat = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		return dateFormat.toString();
	}
	
	/**
	 * Data em formato dd-MM-yyyy
	 * @return
	 */
	public static LocalDate dateToDataCenter(String dateString) {
		
		try {
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate date = LocalDate.parse(dateString, formatter);
			
			return date;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao tentar traduzir data string para datasql");
		}
	}
}
