package com.gestorcoi.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.Feedback;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.FeedbackImpl;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "feedback")
@ViewScoped
public class FeedbackController {

	private FeedbackImpl feedbackImpl = new FeedbackImpl();
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private Funcionarios funcionarios;
	private Feedback feedback;
	
	@PostConstruct
	public void init() {
		
		feedback = new Feedback();
		feedback.setFuncionario(new Funcionarios());
	}
	
	public void salvar() throws Exception{
		
		if(feedback.getFuncionario().getNome() != null || !feedback.getFuncionario().getNome().trim().isBlank()
				|| feedback.getFuncionario().getNome().trim() != "" || !feedback.getFuncionario().getNome().isEmpty()) {
			
			String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			feedback.setData(new SimpleDateFormat("dd/MM/yyyy").parse(dataAtual));
			
			funcionarios = funcionarioImpl.findByName(feedback.getFuncionario().getNome());
			feedback.setFuncionario(funcionarios);
			
			funcionarios.getFeedbacks().add(feedback);
			
			funcionarioImpl.merge2(funcionarios);
			
			funcionarios.getFeedbacks().remove(feedback);
			funcionarioImpl.merge2(funcionarios);
			limpar();
			
			MensagensJSF.msgSeverityInfo("Feedback realizado com sucesso");
		}else {
			MensagensJSF.msgSeverityInfo("Você deve inserir um funcionário antes");
		}
	}
	
	public void limpar() {
		init();
	}
	
	public void remover(Feedback feedback) throws Exception{
		
		feedbackImpl.remove(feedback);
	}
	
	public List<String> findAllFuncionario() throws Exception{
		
		List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		
		List<String> nomeFuncionarios = new ArrayList<>();
		
		for (Funcionarios obj : funcionarios) {
			nomeFuncionarios.add(obj.getNome());
		}
		
		return nomeFuncionarios;
	}
	
	public List<Funcionarios> getAllFuncionariosObjeto() throws Exception{
		
		List<Funcionarios> funcionariosObjeto = funcionarioImpl.findAll(Funcionarios.class);
		return funcionariosObjeto;
	}
	
	public List<Feedback> findAllByName() throws Exception{
		
		if(funcionarios != null || funcionarios.getId() != null) {
			
			return feedbackImpl.findAllByNameFuncionario(funcionarios.getId());
		}
		
		
		return new ArrayList<>();
	}
	
	public List<Feedback> listarAll() throws Exception{
		return feedbackImpl.findAll(Feedback.class);
	}

	public Funcionarios getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	public Feedback getFeedback() {
		return feedback;
	}
	
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
}
