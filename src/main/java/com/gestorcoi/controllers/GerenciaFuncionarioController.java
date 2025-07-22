package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.Feedback;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.RegistroAusencia;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gerenciaFuncionarios")
@ViewScoped
public class GerenciaFuncionarioController {

	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private Funcionarios funcionarios;
	
	private SupervisorImpl supervisorImpl = new SupervisorImpl();
	
	private List<Funcionarios> funcionariosCarregadosTela = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		if(funcionarios == null) {
			funcionarios = new Funcionarios();
		}
		
		if (funcionarios.getAusencias() == null) {
		    funcionarios.setAusencias(new ArrayList<>());
		}
		
		if (funcionarios.getFeedbacks() == null) {
		    funcionarios.setFeedbacks(new ArrayList<>());
		}
	}
	
	public GerenciaFuncionarioController() throws Exception{
		if(funcionariosCarregadosTela.isEmpty()) {
			findAllFuncionariosObjeto();
		}
	}
	
	/**
	 * Usado para alterar as férias do funcionário
	 */
	public void mergeFuncionario() {
		
		try {
			
			if(funcionarios.getId() != null) {
				
				Date feriasInicio = funcionarios.getFeriasInicio();
				Date feriasFim = funcionarios.getFeriasFim();
				
				funcionarios = funcionarioImpl.findById(funcionarios.getId());
				
				funcionarios.setFeriasInicio(feriasInicio);
				funcionarios.setFeriasFim(feriasFim);
				
				funcionarios = funcionarioImpl.merge2(funcionarios);
				MensagensJSF.msgSeverityInfo("Usuário " + funcionarios.getNome() + ", atualizado com sucesso", "Atualizado");
			}else {
				MensagensJSF.msgSeverityInfo("Não foi possível atualizar o funcionário", "Atualização comprometida");
			}
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityInfo("Não foi possível atualizar o funcionário", "Atualização comprometida");
			e.printStackTrace();
		}
	}
	
	public void salvarFuncionario() throws Exception{
		
		try {
			
			funcionarioImpl.merge2(funcionarios);
			funcionariosCarregadosTela.add(funcionarios);
			limpar();
			
			MensagensJSF.msgSeverityInfo("Funcionário: " + funcionarios.getNome() + ", salvo com sucesso", "Salvo");
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Não foi possível salvar o funcionário");
			e.printStackTrace();
		}
	}
	
	public void limpar() {
		init();
	}
	
	public void remover(Funcionarios funcionarios) throws Exception{
		
		String nome = funcionarios.getNome();
		funcionarioImpl.remove(funcionarios);
		funcionariosCarregadosTela.remove(funcionarios);
		MensagensJSF.msgSeverityInfo("Funcionário: " + nome + ", removido com sucesso", "Removido com sucesso");
	}
	
	public void findAllFuncionariosObjeto() throws Exception{
		
		List<Funcionarios> funcionariosObjeto = funcionarioImpl.findAll(Funcionarios.class);
		funcionariosCarregadosTela = funcionariosObjeto;
	}
	
	public Funcionarios getFuncionarios() {
		return funcionarios;
	}
	
	public List<Funcionarios> getFuncionariosCarregadosTela() {
		return funcionariosCarregadosTela;
	}
	
	public void setFuncionariosCarregadosTela(List<Funcionarios> funcionariosCarregadosTela) {
		this.funcionariosCarregadosTela = funcionariosCarregadosTela;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	/**
	 * 
	 * Carregamento automático Autocompletes
	 * 
	 * 
	 */
	
	public List<String> retornaTipos(String query){
		
		List<String> retornoTipo = new ArrayList<>();
		
		retornoTipo.add("MT");
		retornoTipo.add("BT");
		
		return retornoTipo.stream().filter(texto -> texto.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> retornaAtividadeSuperintendencia(String query){
		
		List<String> retornoAtividades = new ArrayList<>();
		
		retornoAtividades.add("Emergencial");
		retornoAtividades.add("Comercial");
		retornoAtividades.add("Avaliação");
		retornoAtividades.add("Ilha de risco");
		retornoAtividades.add("Triagem");
		retornoAtividades.add("Impacto");
		retornoAtividades.add("Manobra");
		retornoAtividades.add("Supervisão");
		retornoAtividades.add("PTP");
		
		return retornoAtividades.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> retornaRegionais(String query){
		
		List<String> retornaRegionais = new ArrayList<>();
		
		retornaRegionais.add("Morrinhos");
		retornaRegionais.add("Rio Verde");
		retornaRegionais.add("Iporá");
		retornaRegionais.add("Montes Belos");
		retornaRegionais.add("Luziânia");
		retornaRegionais.add("Formosa");
		retornaRegionais.add("Anápolis");
		retornaRegionais.add("Uruaçu");
		retornaRegionais.add("Goiânia");
		retornaRegionais.add("Metropolitana");
		retornaRegionais.add("Metropolitana");
		
		retornaRegionais.add("Ilha de Risco");
		retornaRegionais.add("Supervisão");
		retornaRegionais.add("Avaliação");
		retornaRegionais.add("Impacto");
		retornaRegionais.add("Manobra");
		
		return retornaRegionais.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> retornaSecao(String query){
		
		List<String> retornaSecoes = new ArrayList<>();
		
		if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Emergencial")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
			retornaSecoes.add("3");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("PTP")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Comercial")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Avaliação")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
			retornaSecoes.add("3");
			retornaSecoes.add("4");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("ilha de risco")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
			retornaSecoes.add("3");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("triagem")) {
			retornaSecoes.add("1");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Impacto")
				|| funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Manobra")) {
			retornaSecoes.add("1");
			retornaSecoes.add("2");
			retornaSecoes.add("3");
			retornaSecoes.add("4");
			retornaSecoes.add("5");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Supervisão")) {
			retornaSecoes.add("1");
		}
		
		return retornaSecoes.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
	
	public List<String> retornaEscala(String query){
		
		List<String> retornaEscala = new ArrayList<>();
		
		if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Emergencial")) {
			retornaEscala.add("6666");
			retornaEscala.add("15151515");
			retornaEscala.add("21212121");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("PTP")) {
			retornaEscala.add("5-PTP");
			retornaEscala.add("4-PTP");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Comercial")) {
			retornaEscala.add("66666");
			retornaEscala.add("1515151515");
			retornaEscala.add("15151515");
			retornaEscala.add("1515151515");
			retornaEscala.add("1515151515");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Avaliação")) {
			retornaEscala.add("6666");
			retornaEscala.add("15151515");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("ilha de risco")) {
			retornaEscala.add("6666");
			retornaEscala.add("15151515");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("triagem")) {
			retornaEscala.add("88888");
			retornaEscala.add("66666");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Impacto")
				|| funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Manobra")) {
			retornaEscala.add("4321");
			retornaEscala.add("6666");
			retornaEscala.add("15151515");
		}else if(funcionarios.getAtividadeSuperintendencia().equalsIgnoreCase("Supervisão")) {
			retornaEscala.add("PlanilhaExcel");
		}
		
		return retornaEscala.stream().filter(s -> s.toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
	}
}
