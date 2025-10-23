package com.gestorcoi.implementations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gestorcoi.entities.ImprocedentesEntidade;
import com.gestorcoi.hibernate.OperConnection;

public class CarregarImprocedentes {

	public OperConnection database = new OperConnection();
	
	public List<ImprocedentesEntidade> iniciarSqlImprocedentes() {
		
		try(Connection connection = database.getConnection()){
			
			StringBuilder query = new StringBuilder();
			
			try(BufferedReader bufferedReader = new BufferedReader(
					new FileReader("C:/Users/E21057649/eclipse-workspace/com.gestorcoi/src/main/"
							+ "java/com/gestorcoi/implementations/improcedentesSQL"))){
				
				String line;
				
				while((line = bufferedReader.readLine()) != null) {
					
					query.append(line).append("\n");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			List<ImprocedentesEntidade> improcedentes = new ArrayList<>();
			
			try(Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query.toString())){
				
				while(resultSet.next()) {
					
					ImprocedentesEntidade improcedente = new ImprocedentesEntidade();
					
					improcedente.setMotivoSolicitacao(resultSet.getString(1));
					improcedente.setOcorrencia(resultSet.getString(2));
					improcedente.setEndereco(resultSet.getString(3));
					improcedente.setInformacoesAdicionais(resultSet.getString(4));
					
					improcedentes.add(improcedente);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			return improcedentes;
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Um erro ocorreu ao tentar analisar o banco de dados");
		}
	}
}
