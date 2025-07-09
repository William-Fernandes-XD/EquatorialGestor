package com.gestorcoi.excels;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ExcelScanner {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("arquivo.csv"))){
			
			String linha;
			
			while((linha = bufferedReader.readLine()) != null) {
				
				String[] colunas = linha.split(",");
				
				for (String coluna : colunas) {
					System.out.print(coluna + ", ");
				}
				
				System.out.println();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Não foi possível carregar o arquivo de ocorrências, procure o TI");
		}
	}
}
