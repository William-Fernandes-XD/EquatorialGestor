package com.gestorcoi.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gestorTurnoFuncionarios")
@ViewScoped
public class GestorTurnoFuncionariosController {

	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();

	// Registro de escala

	private String tipo;
	private String folgaEmergencialDia;
	private String folgaPtpDia;
	private String comercialFolga;
	private String folgaAvaliacao;
	private String ilhaRiscoFolga;
	private String triagemFolga;
	private String impactoFolga;
	private String impactoFolga2;
	private String impactoFolga3;
	private String impactoFolga4;
	private String impactoFolga5;
	private String impactoFolga6;

	private Date datainicio;
	private Date datafinal;

	private List<LocalDate> dias;

	private List<Funcionarios> funcionarios = new ArrayList<>();

	private Map<String, Map<LocalDate, String>> tabelaDados;

	public List<Funcionarios> carregarFuncionariosTabelaPrincipal() throws Exception {

		funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		
		funcionarios.sort(Comparator.comparing(Funcionarios::getAtividadeSuperintendencia, Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(Funcionarios::getSecao, Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(
						Funcionarios::getEscala,
						Comparator.nullsLast(String::compareToIgnoreCase).reversed())
				.thenComparing(Funcionarios::getNome, Comparator.nullsLast(String::compareToIgnoreCase))
				);
		
		return funcionarios;
	}

	public void gerarEscala() {

		if (datainicio != null && datafinal != null) {
			
			/**
			 * 
			 * Organizando tabela
			 */
			
			dias = new ArrayList<>();

			LocalDate inicio = new java.sql.Date(datainicio.getTime()).toLocalDate();
			LocalDate fim = new java.sql.Date(datafinal.getTime()).toLocalDate();

			LocalDate atual = inicio;
			long diferencaDias = ChronoUnit.DAYS.between(inicio, fim);

			if (diferencaDias == 35) {
				while (!atual.isAfter(fim)) {
					dias.add(atual);
					atual = atual.plusDays(1);
				}

				tabelaDados = new HashMap<String, Map<LocalDate, String>>();

				for (Funcionarios funcionario : funcionarios) {
					Map<LocalDate, String> mapadias = new HashMap<>();
					
					int offset = 0;
					
					/**
					 * Pessoal da emergencial apenas
					 */
					if(funcionario.getAtividadeSuperintendencia().equalsIgnoreCase("emergencial")) {
						
						try {
							offset = Integer.parseInt(folgaEmergencialDia);
						} catch (Exception e) {
							offset = 0;
						}
						
						// Definir offset conforme a seção
						if ("2".equals(funcionario.getSecao())) {
							offset += 2;
						} else if ("3".equals(funcionario.getSecao())) {
							offset += 4;
						}

						int index = 0;

						for (LocalDate dia : dias) {
							int ciclo = (index - offset + 6) % 6; // Mantém dentro de [0,5]

							String valor = "1"; // padrão

							if ("6666".equals(funcionario.getEscala())) {
								if (ciclo >= 4) {
									valor = "X"; // folga
								} else {
									valor = "6"; // trabalho
								}
							} else if ("15151515".equals(funcionario.getEscala())) {
								valor = (ciclo >= 4) ? "X" : "15";
							} else if ("21212121".equals(funcionario.getEscala())) {
								valor = (ciclo >= 4) ? "X" : "21";
							}

							mapadias.put(dia, valor);
							index++;
						}
					}

					/**
					 * Pessoal do PTP apenas
					 */
					
					if(funcionario.getAtividadeSuperintendencia().equalsIgnoreCase("ptp")) {
						
						try{
							offset = Integer.parseInt(folgaPtpDia);
						}catch(Exception e) {
							offset = 0;
						}
						
						if("2".equals(funcionario.getSecao())) {
							offset+= 2;
						}
						
						int index = 0;
						
						if("5-PTP".equalsIgnoreCase(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "PTP";
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("4-PTP".equalsIgnoreCase(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "PTP";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("Comercial".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())) {
						
						try {
							offset = Integer.parseInt(comercialFolga);
						}catch(Exception e) {
							offset = 0;
						}
						
						if("2".equals(funcionario.getSecao())) {
							offset+=2;
						}
						
						int index = 0;
						
						if("66666".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "6";
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("1515151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "15";
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("15151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("Avaliação".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(folgaAvaliacao);
						}catch(Exception e) {
							offset = 0;
						}
						
						if("2".equalsIgnoreCase(funcionario.getSecao())) {
							offset += 3;
						}
						
						if("3".equals(funcionario.getSecao())) {
							offset -= 1; 
						}
						
						if("4".equals(funcionario.getSecao())) {
							offset += 1;
						}
						
						int index = 0;
						
						if("6666".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "6";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("15151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("ilha de risco".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(ilhaRiscoFolga);
						}catch(Exception e) {
							offset = 0;
						}
						
						if("2".equals(funcionario.getSecao())) {
							offset += 2;
						}
						
						if("3".equals(funcionario.getSecao())) {
							offset += 4;
						}
						
						int index = 0;
						
						if("6666".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "6";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("15151515".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("triagem".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(triagemFolga);
						}catch(Exception e) {
							offset = 0;
						}
						
						int index = 0;
						
						if("88888".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "8";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("66666".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "6";
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if ("impacto".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())
							|| "manobra".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())) {

						try {
							offset = Integer.parseInt(impactoFolga);
							
							if("1".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga);
								
							}else if("2".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga2);
								
							}else if("3".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga3);
							}
							else if("4".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga4);
								
							}else if("5".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga5);
							}
							
							/**
							 * Escala normal fixa
							 */
							
							else if("1".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga6);
							}else if("2".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga6) + 2;
							}else if("3".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(impactoFolga6) + 4;
							}
						} catch (Exception e) {
							offset = 0;
						}

						int secao = 0;
						try {
							secao = Integer.parseInt(funcionario.getSecao());
						} catch (Exception e) {
							secao = 0;
						}

						// Seção 1: offset = 0, Seção 2: offset = 2, Seção 3: offset = 4, ...
						offset += (secao > 1) ? (secao - 1) * 2 : 0;

						List<String> sequenciaEscala = Arrays.asList(
							"4", "3", "2", "1", "X", "X",
							"4", "3", "2", "1", "X",
							"4", "4", "3", "2", "1", "X",
							"4", "3", "3", "2", "1", "X",
							"4", "3", "2", "2", "1", "X",
							"4", "3", "2", "1", "1", "X"
						); 

						if ("4321".equals(funcionario.getEscala())) {
							int totalDiasCiclo = sequenciaEscala.size();
							int index = 0;

							for (LocalDate dia : dias) {
								int posicaoLista = (index - offset + totalDiasCiclo) % totalDiasCiclo;
								String valor = sequenciaEscala.get(posicaoLista);

								mapadias.put(dia, valor);
								index++;
							}
						}
						
						/**
						 * Impacto de escala fixa
						 */
						
						int indexEscalaFixa = 0;
						
						if("6666".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (indexEscalaFixa - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "6";
								
								mapadias.put(dia, valor);
								indexEscalaFixa++;
							}
						}
						
						if("15151515".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (indexEscalaFixa - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								mapadias.put(dia, valor);
								indexEscalaFixa++;
							}
						}
					}
					
					tabelaDados.put(funcionario.getNome(), mapadias);
				}
			} else {
				MensagensJSF.msgSeverityInfo("O limite máximo entre as datas não pode ser diferente de 35 dias",
						"Operação não realizada");
			}
		}
	}
	
	public String getClasseCelula(String nome, LocalDate dia) {
		
		String valor = getValor(nome, dia);
		
		switch(valor != null ? valor.toUpperCase() : "") {
		
			case "X":
				return "fundo-verde";
			case "PTP":
				return "fundo-azul";
			case "FERIAS":
				return "fundo-laranja";
			default:
				return "";
		}
	}

	public String formatarDataCabecalho(LocalDate dia) {

		return dia.format(DateTimeFormatter.ofPattern("dd/MM"));
	}

	public String getValor(String nome, LocalDate dia) {
		return tabelaDados.get(nome).get(dia);
	}

	public List<String> returnTipos() {

		List<String> tipos = new ArrayList<>();
		tipos.add("Emergencial");

		return tipos;
	}
	
	public String getFolgaAvaliacao() {
		return folgaAvaliacao;
	}
	
	public void setFolgaAvaliacao(String folgaAvaliacao) {
		this.folgaAvaliacao = folgaAvaliacao;
	}

	public Date getDatafinal() {
		return datafinal;
	}

	public Date getDatainicio() {
		return datainicio;
	}
	
	public String getComercialFolga() {
		return comercialFolga;
	}
	
	public void setComercialFolga(String comercialFolga) {
		this.comercialFolga = comercialFolga;
	}

	public String getFolgaEmergencialDia() {
		return folgaEmergencialDia;
	}

	public void setFolgaEmergencialDia(String folgaEmergencialDia) {
		this.folgaEmergencialDia = folgaEmergencialDia;
	}

	public void setDatafinal(Date datafinal) {
		this.datafinal = datafinal;
	}

	public void setDatainicio(Date datainicio) {
		this.datainicio = datainicio;
	}

	public String getTipo() {
		return tipo;
	}

	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}
	
	public String getIlhaRiscoFolga() {
		return ilhaRiscoFolga;
	}
	
	public void setIlhaRiscoFolga(String ilhaRiscoFolga) {
		this.ilhaRiscoFolga = ilhaRiscoFolga;
	}
	
	public String getFolgaPtpDia() {
		return folgaPtpDia;
	}
	public void setFolgaPtpDia(String folgaPtpDia) {
		this.folgaPtpDia = folgaPtpDia;
	}

	public String getTriagemFolga() {
		return triagemFolga;
	}
	
	public void setTriagemFolga(String triagemFolga) {
		this.triagemFolga = triagemFolga;
	}
	
	public String getImpactoFolga() {
		return impactoFolga;
	}
	
	public String getImpactoFolga2() {
		return impactoFolga2;
	}
	
	public void setImpactoFolga2(String impactoFolga2) {
		this.impactoFolga2 = impactoFolga2;
	}
	
	public String getImpactoFolga3() {
		return impactoFolga3;
	}
	
	public void setImpactoFolga3(String impactoFolga3) {
		this.impactoFolga3 = impactoFolga3;
	}
	
	public String getImpactoFolga4() {
		return impactoFolga4;
	}
	
	public void setImpactoFolga4(String impactoFolga4) {
		this.impactoFolga4 = impactoFolga4;
	}
	
	public String getImpactoFolga5() {
		return impactoFolga5;
	}
	
	public void setImpactoFolga5(String impactoFolga5) {
		this.impactoFolga5 = impactoFolga5;
	}
	
	public void setImpactoFolga(String impactoFolga) {
		this.impactoFolga = impactoFolga;
	}
	
	public String getImpactoFolga6() {
		return impactoFolga6;
	}
	
	public void setImpactoFolga6(String impactoFolga6) {
		this.impactoFolga6 = impactoFolga6;
	}
	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
