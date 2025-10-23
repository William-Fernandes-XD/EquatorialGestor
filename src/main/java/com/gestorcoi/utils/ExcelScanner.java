package com.gestorcoi.utils;

import java.io.FileInputStream;
import java.util.List;

import org.apache.commons.fileupload.FileUpload;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.UploadedFile;

import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.FuncionarioImpl;

public class ExcelScanner {
	
	private String caminho;
	
	public ExcelScanner(String caminho) {

		this.caminho = caminho;
	}
	
	public void scannerExcel() {
		
		try(FileInputStream fileInputStream = new FileInputStream(caminho)){
			
			
			FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
			
			List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
			
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			Sheet planilha = workbook.getSheetAt(0);
			
			
			
			for(Funcionarios funcionario : funcionarios) {
				
				for (Row row : planilha) {
						
					Cell cell = row.getCell(0);
					
					if(cell != null && cell.getStringCellValue() != "") {
						if(funcionario.getNome().equalsIgnoreCase(cell.getStringCellValue())) {
							
							for (Cell celulaPorFuncionario : row) {
								
								/**
								 * 
								 * Ler celula por celula
								 * 
								 */
							}
						}else {
							continue;
						}
					}
				}
			}
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Ocorreu um erro ao tentar realizar a leitura do arquivo excel");
			e.printStackTrace();
		}
	}
}
