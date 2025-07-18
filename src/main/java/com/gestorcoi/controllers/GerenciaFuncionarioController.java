package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	public void mergeFuncionario() {
		
		try {
			
			if(funcionarios.getId() != null) {
				
				Date feriasInicio = funcionarios.getFeriasInicio();
				Date feriasFim = funcionarios.getFeriasFim();
				
				funcionarios = funcionarioImpl.findById(funcionarios.getId());
				
				funcionarios.setFeriasInicio(feriasInicio);
				funcionarios.setFeriasFim(feriasFim);
				
				funcionarios = funcionarioImpl.merge2(funcionarios);
				
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
			
			/**
			 * Tentar apagar isso com urgência
			 * 
			RegistroAusencia registroAusencia = new RegistroAusencia();
			registroAusencia.setFuncionarios(funcionarios);
			
			registroAusencia.setData_ausencia(new Date());
			registroAusencia.setJustificativa("POR FAVOR APAGAR");
			registroAusencia.setTurno("POR FAVOR APAGAR");
			
			funcionarios.getAusencias().add(registroAusencia);
			
			Feedback feedback = new Feedback();
			feedback.setFuncionario(funcionarios);
			
			feedback.setAvaliador(supervisorImpl.findByName("admin"));
			feedback.setData(new Date());
			feedback.setFeedback("POR FAVOR APAGAR");
			feedback.setPositivoOrNegative("Positivo");
			
			funcionarios.getFeedbacks().add(feedback);
			funcionarioImpl.merge(funcionarios);
			
			MensagensJSF.msgSeverityInfo("Colaborador salvo com sucesso!", "Salvo");
			
			limpar();**/
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityError("Não foi possível salvar o funcionário");
			e.printStackTrace();
		}
	}
	
	public void limpar() {
		init();
	}
	
	public void remover(Funcionarios funcionarios) throws Exception{
		
		funcionarioImpl.remove(funcionarios);
	}
	
	public List<Funcionarios> findAllFuncionariosObjeto() throws Exception{
		
		List<Funcionarios> funcionariosObjeto = funcionarioImpl.findAll(Funcionarios.class);
		funcionariosCarregadosTela = funcionariosObjeto;
		
		return funcionariosObjeto;
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
	
}
