package com.gestorcoi.controllers;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.BancosTurno;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.configGeraEscala.GeradorEscalaEntity;
import com.gestorcoi.excels.ExcelScanner;
import com.gestorcoi.implementations.BancosTurnoImpl;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.implementations.GeradorEscalaEntityImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gestorTurnoFuncionarios")
@ViewScoped
public class GestorTurnoFuncionariosController implements Serializable{
	
	private static final long serialVersionUID = 1L;

	// Filtro da tabela que aparece na tela (pode ser nulo, e não funciona se nao tiver uma planilha feita)
	
	private String filtro = "";
	
	private String filtroGrupo = "";
	
	// set ferias
	
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private GeradorEscalaEntityImpl geradorEscalaEntityImpl = new GeradorEscalaEntityImpl();
	
	private GeradorEscalaEntity geradorEscalaEntity = new GeradorEscalaEntity();

	private List<LocalDate> dias;

	private List<Funcionarios> funcionarios = new ArrayList<>();
	private List<Funcionarios> funcionariosFiltrados = new ArrayList<>();
	
	private Map<String, Map<LocalDate, String>> tabelaDados;
	
	private BancosTurnoImpl bancosTurnoImpl = new BancosTurnoImpl();
	
	private List<GeradorEscalaEntity> listaDeEscalasSalvas = new ArrayList<>();
	
	public void carregarFuncionariosTabelaPrincipal() throws Exception {

		funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		
		funcionarios.sort(Comparator.comparing(Funcionarios::getAtividadeSuperintendencia, Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(Funcionarios::getSecao, Comparator.nullsLast(String::compareToIgnoreCase))
				.thenComparing(
						Funcionarios::getEscala,
						Comparator.nullsLast(String::compareToIgnoreCase).reversed())
				.thenComparing(Funcionarios::getNome, Comparator.nullsLast(String::compareToIgnoreCase))
				);
		
		funcionariosFiltrados = funcionarios;
	}
	
	public GestorTurnoFuncionariosController() throws Exception{
		carregarFuncionariosTabelaPrincipal();
	}
	
	/**
	 * Realiza o salvamento de escalas
	 */
	public void salvarEscala() {
		
		if(geradorEscalaEntity.getInicio() != null && geradorEscalaEntity.getFim() != null) {
			
			try {
				
				LocalDate inicioDetudo = geradorEscalaEntity.getInicio().toInstant()
						.atZone(ZoneId.systemDefault()).toLocalDate();
					
				LocalDate dataFimDeTudo = inicioDetudo.plusDays(35);
					
					geradorEscalaEntity.setFim(Date.from(dataFimDeTudo.atStartOfDay(ZoneId.systemDefault()).toInstant()));
				
				if (geradorEscalaEntity.getEmergencialFolga() == null) 
				    geradorEscalaEntity.setEmergencialFolga("0");

				if (geradorEscalaEntity.getPtpFolga() == null) 
				    geradorEscalaEntity.setPtpFolga("0");

				if (geradorEscalaEntity.getPtpFolga4x4() == null) 
				    geradorEscalaEntity.setPtpFolga4x4("0");

				if (geradorEscalaEntity.getComercialFolga() == null) 
				    geradorEscalaEntity.setComercialFolga("0");

				if (geradorEscalaEntity.getAvaliacaoFolga() == null) 
				    geradorEscalaEntity.setAvaliacaoFolga("0");

				if (geradorEscalaEntity.getIlhaDeRiscoFolga() == null) 
				    geradorEscalaEntity.setIlhaDeRiscoFolga("0");

				if (geradorEscalaEntity.getTriagemFolga() == null) 
				    geradorEscalaEntity.setTriagemFolga("0");

				if (geradorEscalaEntity.getImpactoFolga() == null) 
				    geradorEscalaEntity.setImpactoFolga("0");

				if (geradorEscalaEntity.getImpactoFolga2() == null) 
				    geradorEscalaEntity.setImpactoFolga2("0");

				if (geradorEscalaEntity.getImpactoFolga3() == null) 
				    geradorEscalaEntity.setImpactoFolga3("0");

				if (geradorEscalaEntity.getImpactoFolga4() == null) 
				    geradorEscalaEntity.setImpactoFolga4("0");

				if (geradorEscalaEntity.getImpactoFolga5() == null) 
				    geradorEscalaEntity.setImpactoFolga5("0");

				if (geradorEscalaEntity.getImpactoFolga6() == null) 
				    geradorEscalaEntity.setImpactoFolga6("0");
			    
				geradorEscalaEntityImpl.merge(geradorEscalaEntity);
				listaDeEscalasSalvas.add(geradorEscalaEntity);
				
				MensagensJSF.msgSeverityInfo("Nova escala salva com sucesso", "Salvo");
				
			}catch(Exception e) {
				MensagensJSF.msgSeverityInfo("Não foi possível salvar a escala", "Um erro inesperado");
			}
		}
	}
	
	public void removerEscala(GeradorEscalaEntity entity) {
		
		try{
			geradorEscalaEntityImpl.remove(entity);
			listaDeEscalasSalvas.remove(entity);
			MensagensJSF.msgSeverityInfo("Escala removida com sucesso", "Removido");
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityInfo("Não foi possível remover a escala", "Um erro inesperado");
		}
	}
	
	public void carregarEscalasSalvas() {
		
		try {
			
			listaDeEscalasSalvas = geradorEscalaEntityImpl.findAll(GeradorEscalaEntity.class);
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityInfo("Não foi possível realizar a consulta de escalas", "Um erro inesperado");
		}
	}
	
	public void gerarEscalaFiltrada(){
		
		funcionariosFiltrados = funcionarios;
		
		try {
			
			if(filtro != null && !filtro.trim().isEmpty()) {
				funcionariosFiltrados =  funcionariosFiltrados.stream().filter(s -> s.getAtividadeSuperintendencia().equalsIgnoreCase(filtro)).collect(Collectors.toList());
			}
			
			if(filtroGrupo != null && !filtroGrupo.trim().isEmpty()){
				funcionariosFiltrados = funcionariosFiltrados.stream().filter(s -> s.getSecao().equalsIgnoreCase(filtroGrupo)).collect(Collectors.toList());
			}
		}catch(Exception e) {
			
			MensagensJSF.msgSeverityInfo("Não foi possível realizar o filtro", "Um erro inesperado");
		}
		
		gerarEscala();
	}

	/**
	 * Gera a escala que aparece na tela
	 */
	public void gerarEscala() {

		if (geradorEscalaEntity.getInicio() != null) {
			
			/**
			 * 
			 * Organizando tabela
			 */
			
			dias = new ArrayList<>();
			
			/**
			 * Setamento de afastamento de 35 dias
			 */
			LocalDate inicioDetudo = geradorEscalaEntity.getInicio().toInstant()
					.atZone(ZoneId.systemDefault()).toLocalDate();
				
			LocalDate dataFimDeTudo = inicioDetudo.plusDays(35);
				
			geradorEscalaEntity.setFim(Date.from(dataFimDeTudo.atStartOfDay(ZoneId.systemDefault()).toInstant()));
	

			LocalDate inicio = new java.sql.Date(geradorEscalaEntity.getInicio().getTime()).toLocalDate();
			LocalDate fim = new java.sql.Date(geradorEscalaEntity.getFim().getTime()).toLocalDate();

			LocalDate atual = inicio;
			long diferencaDias = ChronoUnit.DAYS.between(inicio, fim);

			if (diferencaDias == 35) {
				while (!atual.isAfter(fim)) {
					dias.add(atual);
					atual = atual.plusDays(1);
				}

				tabelaDados = new HashMap<String, Map<LocalDate, String>>();

				for (Funcionarios funcionario : funcionariosFiltrados) {
					Map<LocalDate, String> mapadias = new HashMap<>();
					
					int offset = 0;
					
					// Ferias
					
					LocalDate feriasInicio;
					LocalDate feriasFim;
					
					try {
						feriasInicio = new java.sql.Date(funcionario.getFeriasInicio().getTime()).toLocalDate();
						feriasFim = new java.sql.Date(funcionario.getFeriasFim().getTime()).toLocalDate();
					}catch(Exception e) {
						feriasInicio = null;
						feriasFim = null;
					}
					
					// licença
					
					LocalDate licencaInicio;
					LocalDate licencaFim;
					
					try {
						licencaInicio = new java.sql.Date(funcionario.getLicencaInicio().getTime()).toLocalDate();
						licencaFim = new java.sql.Date(funcionario.getLicencaFim().getTime()).toLocalDate();
					}catch(Exception e) {
						licencaInicio = null;
						licencaFim = null;
					}
					
					// Bancos de horas
					
					List<BancosTurno> bancosTurnos;
					
					try {
						bancosTurnos = bancosTurnoImpl.findAllById(funcionario.getId());
					}catch(Exception e) {
						bancosTurnos = new ArrayList<>();
					}
					
					// Troca de Turno
					
					LocalDate trocaTurnoFim;
					LocalDate trocaTurnoDate;
					String trocaTurno;
					
					try {
						
						trocaTurnoDate = new java.sql.Date(funcionario.getTrocaTurnoData().getTime()).toLocalDate();
						trocaTurno = funcionario.getTrocaTurno();
						
						trocaTurnoFim = new java.sql.Date(funcionario.getTrocaTurnoFim().getTime()).toLocalDate();
						
					}catch(Exception e) {
						
						trocaTurnoDate = null;
						trocaTurnoFim = null;
						trocaTurno = "";
					}
					
					/**
					 * Pessoal da emergencial apenas
					 */
					if(funcionario.getAtividadeSuperintendencia().equalsIgnoreCase("emergencial")) {
						
						try {
							offset = Integer.parseInt(geradorEscalaEntity.getEmergencialFolga());
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
							
							/**
							 * Troca de turno do funcionário, caso ele queira trocar algum dia
							 */
							if (trocaTurnoDate != null && trocaTurnoFim != null &&
								    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
								     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
								
								if(trocaTurno.trim() != "") {
									if("6666".equals(funcionario.getEscala())) {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-21" : "X";
									}else if("15151515".equals(funcionario.getEscala())) {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-21" : "X";
									}else if("21212121".equals(funcionario.getEscala())) {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-21" : "X";
									}
								}
							}
							
							if(!bancosTurnos.isEmpty()) {
								for (BancosTurno bancoTurno : bancosTurnos) {
									LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
									valor = dia.isEqual(bancoData) ? "B" : valor;
								}
							}
							
							if (
								    feriasInicio != null && feriasFim != null &&
								    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
								) {
								    valor = "Férias";
								}
							
							if (
								    licencaInicio != null && licencaFim != null &&
								    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
								) {
								    valor = "Licença";
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
							
							if(funcionario.getEscala().equalsIgnoreCase("4-PTP")) {
								offset = Integer.parseInt(geradorEscalaEntity.getPtpFolga4x4());
							}else if(funcionario.getEscala().equalsIgnoreCase("5-PTP")){
								offset = Integer.parseInt(geradorEscalaEntity.getPtpFolga());
							}
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										valor = !valor.equalsIgnoreCase("X") ? "T-PTP" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("4-PTP".equalsIgnoreCase(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "PTP";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										valor = !valor.equalsIgnoreCase("X") ? "T-PTP" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
						
						/**
						 * 
						 * Comercial
						 */
					
					if("Comercial".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())) {
						
						try {
							offset = Integer.parseInt(geradorEscalaEntity.getComercialFolga());
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("66666".equals(funcionario.getEscala())) {
											if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
											if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
										}
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("1515151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "15";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}else if("15151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("Avaliação".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(geradorEscalaEntity.getAvaliacaoFolga());
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("15151515".equals(funcionario.getEscala())) {
							for(LocalDate dia : dias) {
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("ilha de risco".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(geradorEscalaEntity.getIlhaDeRiscoFolga());
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("15151515".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-15" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if("triagem".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())){
						
						try {
							offset = Integer.parseInt(geradorEscalaEntity.getTriagemFolga());
						}catch(Exception e) {
							offset = 0;
						}
						
						int index = 0;
						
						if("88888".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "8";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-8" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
						
						else if("66666".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (index - offset + 7) % 7;
								
								String valor = (ciclo >= 5) ? "X" : "6";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-6" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-8" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								index++;
							}
						}
					}
					
					if ("impacto".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())
							|| "manobra".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())) {

						try {
							offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga());
							
							if("1".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga());
								
							}else if("2".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga2());
								
							}else if("3".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga3());
							}
							else if("4".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga4());
								
							}else if("5".equals(funcionario.getSecao()) && "4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga5());
							}
							
							/**
							 * Escala normal fixa
							 */
							
							else if("1".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga6());
							}else if("2".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga6()) + 2;
							}else if("3".equals(funcionario.getSecao()) && !"4321".equals(funcionario.getEscala())) {
								offset = Integer.parseInt(geradorEscalaEntity.getImpactoFolga6()) + 4;
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-1" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-2" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-3" : "X";
										if("4".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-4" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
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
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-1" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-2" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-3" : "X";
										if("4".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-4" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								indexEscalaFixa++;
							}
						}
						
						if("15151515".equals(funcionario.getEscala())) {
							
							for(LocalDate dia : dias) {
								
								int ciclo = (indexEscalaFixa - offset + 6) % 6;
								
								String valor = (ciclo >= 4) ? "X" : "15";
								
								/**
								 * Troca de turno do funcionário, caso ele queira trocar algum dia
								 */
								if (trocaTurnoDate != null && trocaTurnoFim != null &&
									    (dia.isEqual(trocaTurnoDate) || dia.isEqual(trocaTurnoFim) ||
									     (dia.isAfter(trocaTurnoDate) && dia.isBefore(trocaTurnoFim)))) {
									if(trocaTurno.trim() != "") {
										if("1".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-1" : "X";
										if("2".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-2" : "X";
										if("3".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-3" : "X";
										if("4".equals(trocaTurno)) valor = !valor.equalsIgnoreCase("X") ? "T-4" : "X";
									}
								}
								
								if(!bancosTurnos.isEmpty()) {
									for (BancosTurno bancoTurno : bancosTurnos) {
										LocalDate bancoData = new java.sql.Date(bancoTurno.getDataBanco().getTime()).toLocalDate();
										valor = dia.isEqual(bancoData) ? "B" : valor;
									}
								}
								
								if (
									    feriasInicio != null && feriasFim != null &&
									    !dia.isBefore(feriasInicio) && !dia.isAfter(feriasFim)
									) {
									    valor = "Férias";
									}
								
								if (
									    licencaInicio != null && licencaFim != null &&
									    !dia.isBefore(licencaInicio) && !dia.isAfter(licencaFim)
									) {
									    valor = "Licença";
									}
								
								mapadias.put(dia, valor);
								indexEscalaFixa++;
							}
						}
					}
					
					if("supervisão".equalsIgnoreCase(funcionario.getAtividadeSuperintendencia())) {
						
						Map<String, Map<LocalDate, String>> supervisoresDados = new HashMap<>();
						
						if(supervisoresDados == null || supervisoresDados.isEmpty()) {
							supervisoresDados = ExcelScanner.carregarSupervisoresDadosTurno(inicio);
							
							if(supervisoresDados != null && !supervisoresDados.isEmpty()) {
								for (LocalDate dia : dias) {
									mapadias.put(dia, supervisoresDados.get(funcionario.getNome()).get(dia));
								}
							}else {
								for (LocalDate dia : dias) {
									mapadias.put(dia, "-");
								}
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
		
		if (valor != null && valor.contains("-")) {
		    String[] corte = valor.trim().split("-");
		    if (corte.length > 0 && corte[0].trim().equals("T")) {
		        return "fundo-amarelo";
		    }
		}
		
		switch(valor != null ? valor.toUpperCase() : "") {
		
			case "X":
				return "fundo-verde";
			case "PTP":
				return "fundo-azul";
			case "FÉRIAS":
				return "fundo-laranja";
			case "R":
				return "fundo-azul-claro";
			case "S":
				return "fundo-azul-claro";
			case "A":
				return "fundo-azul-claro";
			case "T":
				return "fundo-azul-claro";
			case "B":
				return "fundo-azul-claro";
			case "LICENÇA":
				return "fundo-cinza";
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
	
	/**
	 * Autocomplete
	 * 
	 */

	public List<String> filtrosAtividadesAutoComplete(String query){
		
		List<String> retorno = new ArrayList<>();
		
		retorno.add("Emergencial");
		retorno.add("Comercial");
		retorno.add("Supervisão");
		retorno.add("Impacto");
		retorno.add("Manobra");
		retorno.add("Avaliação");
		retorno.add("Ilha de Risco");
		retorno.add("PTP");
		retorno.add("TRIAGEM");
		
		return retorno.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> filtrosGrupoAutoComplete(){
		
		List<String> retorno = new ArrayList<>();
		
		retorno.add("1");
		retorno.add("2");
		retorno.add("3");
		retorno.add("4");
		retorno.add("5");
		
		return retorno;
	}
	
	public List<String> returnTipos() {

		List<String> tipos = new ArrayList<>();
		tipos.add("Emergencial");

		return tipos;
	}
	
	public List<LocalDate> getDias() {
		return dias;
	}

	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}
	
	public GeradorEscalaEntity getGeradorEscalaEntity() {
		return geradorEscalaEntity;
	}
	
	public String getFiltro() {
		return filtro;
	}
	
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	public void setGeradorEscalaEntity(GeradorEscalaEntity geradorEscalaEntity) {
		this.geradorEscalaEntity = geradorEscalaEntity;
	}
	
	public List<GeradorEscalaEntity> getListaDeEscalasSalvas() {
		return listaDeEscalasSalvas;
	}
	
	public List<Funcionarios> getFuncionariosFiltrados() {
		return funcionariosFiltrados;
	}
	
	public void setFuncionariosFiltrados(List<Funcionarios> funcionariosFiltrados) {
		this.funcionariosFiltrados = funcionariosFiltrados;
	}
	
	public String getFiltroGrupo() {
		return filtroGrupo;
	}
	
	public void setFiltroGrupo(String filtroTurno) {
		this.filtroGrupo = filtroTurno;
	}
	
	public void setListaDeEscalasSalvas(List<GeradorEscalaEntity> listaDeEscalasSalvas) {
		this.listaDeEscalasSalvas = listaDeEscalasSalvas;
	}
}
