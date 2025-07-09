package com.gestorcoi.excels;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelScanner {

	public static List<Row> retornarOcorrencia() {
		
		String caminho = "C:/Users/E21057649/OneDrive - GRUPO EQUATORIAL ENERGIA/arquivo.xlsx";
		
		List<Row> ocorrencias = new ArrayList<>();
		
		try(FileInputStream stream = new FileInputStream(caminho)){
			
			Workbook workbook = WorkbookFactory.create(stream);
			
			System.out.println(workbook.getNumberOfSheets());
			
			Sheet sheet = workbook.getSheetAt(0);
			
			for (Row row : sheet) {
				
				ocorrencias.add(row);
			}
			
			return ocorrencias;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Não foi possível carregar as tabelas .xlsx");
		}
	}
}
