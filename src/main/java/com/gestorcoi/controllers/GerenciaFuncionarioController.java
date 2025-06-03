package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.Feedback;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.RegistroAusencia;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gerenciaFuncionarios")
@ViewScoped
public class GerenciaFuncionarioController {

	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private Funcionarios funcionarios;
	
	@PostConstruct
	public void init() {
		
		if(funcionarios == null) {
			funcionarios = new Funcionarios();
		}
		
		funcionarios.setAusencias(new ArrayList<>());
		funcionarios.setFeedbacks(new ArrayList<>());
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

	        MensagensJSF.msgSeverityInfo("Funcionário criado com sucesso");
	    } else {
	        MensagensJSF.msgSeverityInfo("Você deve inserir um funcionário antes");
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
