package com.gestorcoi.excels;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.examples.CellTypes;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.gestorcoi.utils.MensagensJSF;

public class ExcelScanner {
	
	/**
	 * Busca pelos parâmetros de turnos para os supervisores
	 * Existe essa consulta pois não é possível automatizar os turnos de supervisores
	 * @param inicio
	 * @return
	 */
	public static Map<String, Map<LocalDate, String>> carregarSupervisoresDadosTurno(LocalDate inicio){
		
		Map<String, Map<LocalDate, String>> mapaDados = new HashMap<String, Map<LocalDate,String>>();
		
		String caminho = "C:/Users/E21057649/OneDrive - GRUPO EQUATORIAL ENERGIA/supervisores-escala.xlsx";
		
		try(FileInputStream stream = new FileInputStream(caminho)){
			
			Workbook workbook = WorkbookFactory.create(stream);
			
			Sheet sheet = workbook.getSheetAt(0);
			
			for(Row row : sheet) {
				
				Map<LocalDate, String> mapaDiasTrabalho = new HashMap<>();
				LocalDate atual = inicio;
				
				for (Cell celula : row) {
					
					if(celula.getColumnIndex() >= 4) {
						
						String valorCelula = "";
						
						if(celula.getCellType() == Cell.CELL_TYPE_STRING) {
							valorCelula = celula.getStringCellValue();
						}else if(celula.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							valorCelula = String.valueOf(celula.getNumericCellValue());
						}
						
						mapaDiasTrabalho.put(atual, valorCelula);
						atual = atual.plusDays(1);
					}
				}
				
				mapaDados.put(row.getCell(0).getStringCellValue(), mapaDiasTrabalho);
				
			}
			
			return mapaDados;
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Não há dados de supervisores para ler, arquivo inválido");
			e.printStackTrace();
			return mapaDados = new HashMap<>();
		}
	}

	public static List<Row> retornarOcorrencia() {
		
		String caminho = "C:/Users/E21057649/OneDrive - GRUPO EQUATORIAL ENERGIA/arquivo.xlsx";
		
		List<Row> ocorrencias = new ArrayList<>();
		
		try(FileInputStream stream = new FileInputStream(caminho)){
			
			Workbook workbook = WorkbookFactory.create(stream);
			
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
