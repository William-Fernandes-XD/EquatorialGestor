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
	
	public void salvar() throws Exception {

	    if (funcionarios.getNome() != null && !funcionarios.getNome().trim().isEmpty()) {

	        if (funcionarios.getAusencias() == null) {
	            funcionarios.setAusencias(new ArrayList<>());
	        } else {
	            funcionarios.getAusencias().clear();
	        }

	        if (funcionarios.getFeedbacks() == null) {
	            funcionarios.setFeedbacks(new ArrayList<>());
	        } else {
	            funcionarios.getFeedbacks().clear();
	        }

	        RegistroAusencia registroAusencia = new RegistroAusencia();
	        registroAusencia.setFuncionarios(funcionarios);

	        Feedback feedback = new Feedback();
	        feedback.setFuncionario(funcionarios);

	        funcionarios.getAusencias().add(registroAusencia);
	        funcionarios.getFeedbacks().add(feedback);

	        funcionarioImpl.merge2(funcionarios); 

	        limpar();

	        MensagensJSF.msgSeverityInfo("Funcionário criado com sucesso", "Salvo");
	    } else {
	        MensagensJSF.msgSeverityInfo("Você deve inserir um funcionário antes", "Dados Incompletos");
	    }
	}
	
	public void salvarFuncionario() throws Exception{
		
		try {
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
			
			limpar();
			
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
		return funcionariosObjeto;
	}
	
	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
	
}
