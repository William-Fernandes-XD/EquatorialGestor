package com.gestorcoi.excel;

import java.io.BufferedReader;
import java.io.FileReader;

import com.gestorcoi.utils.MensagensJSF;

public class LeitorDadosCardInterrupcao {

	private String caminho;
	private String cod_equipamento;
	
	public LeitorDadosCardInterrupcao() {
		this.caminho = "C:/Users/E21057649/eclipse-workspace/com.gestorcoi/src/main/java/com/gestorcoi/excel/DadosCardsEquipamentos.csv";
	}
	
	public DadosCardsAuxiliar carregarDados(String cod_equipamento) {
		
		this.cod_equipamento = cod_equipamento;
		
		try(BufferedReader reader = new BufferedReader(new FileReader(this.caminho))){
			
			String linha;
			
			DadosCardsAuxiliar dadosCardsAuxiliar = new DadosCardsAuxiliar();

			while ((linha = reader.readLine()) != null) {
			    if (cod_equipamento != null && !cod_equipamento.trim().isEmpty()) {
			    	
			    	String dados[] = linha.split(";");
			    	
			        if (dados[3].equalsIgnoreCase(cod_equipamento)) {
			            // Selecionando os campos para retornar ao metodo de cards
			            
			            dadosCardsAuxiliar.setDistribuidora(dados[0]);
			            dadosCardsAuxiliar.setTipo_ativo(dados[1]);
			            dadosCardsAuxiliar.setRegional(dados[2]);
			            dadosCardsAuxiliar.setCod_equipamento(dados[3]);
			            
			            dadosCardsAuxiliar.setQtd_uc(dados[4].contains(".") ? dados[4].replace(".", "").toString() : dados[4]);
			            dadosCardsAuxiliar.setLocais_atingidos(dados[5]);
			            dadosCardsAuxiliar.setSuperintendencia(dados[6]);
			            dadosCardsAuxiliar.setSubestacao(dados[7]);
			            dadosCardsAuxiliar.setAlimentador(dados[8]);
			            dadosCardsAuxiliar.setTensao(Integer.parseInt(dados[9]));
			            
			            break;
			        }
			    }
			}
			
			return dadosCardsAuxiliar;
			
		}catch(Exception e) {
			throw new RuntimeException("Um erro ao tentar ler o arquivo de dados");
		}
	}
}
