package com.gestorcoi.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

	private Date datainicio;
	private Date datafinal;

	private List<LocalDate> dias;

	private List<Funcionarios> funcionarios = new ArrayList<>();

	private Map<String, Map<LocalDate, String>> tabelaDados;

	public List<Funcionarios> carregarFuncionariosTabelaPrincipal() throws Exception {

		funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		return funcionarios;
	}

	public void gerarEscala() {

		if (datainicio != null && datafinal != null) {
			
			/**
			 * 
			 * Organizando tabela
			 */
			
			funcionarios.sort(Comparator.comparing(Funcionarios::getAtividadeSuperintendencia, Comparator.nullsLast(String::compareToIgnoreCase))
					.thenComparing(Funcionarios::getNome, Comparator.nullsLast(String::compareToIgnoreCase)));

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
	
	public String getFolgaPtpDia() {
		return folgaPtpDia;
	}
	public void setFolgaPtpDia(String folgaPtpDia) {
		this.folgaPtpDia = folgaPtpDia;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
