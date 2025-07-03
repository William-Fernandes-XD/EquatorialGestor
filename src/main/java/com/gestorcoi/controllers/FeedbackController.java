package com.gestorcoi.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gestorcoi.email.EmailUtil;
import com.gestorcoi.entities.Feedback;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.FeedbackImpl;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "feedback")
@ViewScoped
public class FeedbackController {

	private FeedbackImpl feedbackImpl = new FeedbackImpl();
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private Funcionarios funcionarios;
	private Feedback feedback;
	
	private SupervisorImpl supervisorImpl = new SupervisorImpl();
	
	private List<Funcionarios> funcionariosListaBusca = new ArrayList<>();
	private List<Feedback> feedbacksListaBusca = new ArrayList<>();
	
	private String filtro = "";
	
	@PostConstruct
	public void init() {
		
		feedback = new Feedback();
		feedback.setFuncionario(new Funcionarios());
		funcionarios = new Funcionarios();
	}
	
	public void salvar() throws Exception{
		
		if(feedback.getFuncionario().getNome() != null || !feedback.getFuncionario().getNome().trim().equals("")
				|| feedback.getFuncionario().getNome().trim() != "" || !feedback.getFuncionario().getNome().isEmpty()) {
			
			String dataAtual = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			feedback.setData(new SimpleDateFormat("dd/MM/yyyy").parse(dataAtual));
			
			funcionarios = funcionarioImpl.findByName(funcionarios.getNome());
			
			if(funcionarios == null) {
				MensagensJSF.msgSeverityInfo("Não foi possível encontrar esse funcionário", "Dados ilegíveis");
			}else {
				
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				
				Supervisor avaliador = supervisorImpl.findByName(authentication.getName());
			
				feedback.setAvaliador(avaliador);
				feedback.setFuncionario(funcionarios);
			
				funcionarios.getFeedbacks().add(feedback);
			
				funcionarioImpl.merge2(funcionarios);
			
				funcionarios.getFeedbacks().remove(feedback);
				funcionarioImpl.merge2(funcionarios);
				
				EmailUtil.enviarFeedBack(feedback);
				
				limpar();
			
				MensagensJSF.msgSeverityInfo("Feedback realizado com sucesso!", "Salvo");
			
			}
		}else {
			MensagensJSF.msgSeverityInfo("Você deve inserir um funcionário antes!", "Dados Incompletos");
		}
	}
	
	public void limparNome() {
		funcionarios.setNome("");
	}
	
	public void limpar() {
		init();
	}
	
	public void remover(Feedback feedback) throws Exception{
		
		feedbackImpl.remove(feedback);
	}
	
	public List<String> findAllFuncionario(String query) throws Exception{
		
		List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		
		List<String> nomeFuncionarios = new ArrayList<>();
		
		for (Funcionarios obj : funcionarios) {
	        if (obj.getNome().toLowerCase().contains(query.toLowerCase())) {
	            nomeFuncionarios.add(obj.getNome());
	        }
	    }
		
		return nomeFuncionarios;
	}
	
	public List<String> retornarPositivoNegativo(){
		
		List<String> list = new ArrayList<>();
		list.add("Positivo");
		list.add("Negativo");
		
		return list;
	}
	
	public List<Funcionarios> getAllFuncionariosObjeto() throws Exception{
		
		List<Funcionarios> funcionariosObjeto = funcionarioImpl.findAll(Funcionarios.class);
		return funcionariosObjeto;
	}
	
	public List<Feedback> listarAll() throws Exception{
		
		List<Feedback> feedbacks = feedbackImpl.findAll(Feedback.class);	
		
		Collections.sort(feedbacks, new Comparator<Feedback>() {

			@Override
			public int compare(Feedback o1, Feedback o2) {
				return o2.getData().compareTo(o1.getData());
			}
		});
		
		if ("Positivo".equalsIgnoreCase(filtro)) {
		    feedbacks = feedbacks.stream()
		            .filter(f -> "Positivo".equalsIgnoreCase(f.getPositivoOrNegative()))
		            .collect(Collectors.toList());
		} else if ("Negativo".equalsIgnoreCase(filtro)) {
		    feedbacks = feedbacks.stream()
		            .filter(f -> "Negativo".equalsIgnoreCase(f.getPositivoOrNegative()))
		            .collect(Collectors.toList());
		}
		
		return feedbacks;
	}
	
	public void carregarFeedBacksByObject(Funcionarios funcionario) throws Exception{
		
		if(funcionario.getId() != null) {
			
			List<Feedback> feedbacks = feedbackImpl.findAllByNameFuncionario(funcionario.getId());
			
			Collections.sort(feedbacks, new Comparator<Feedback>() {

				@Override
				public int compare(Feedback o1, Feedback o2) {
					return o2.getData().compareTo(o1.getData());
				}
			});
			
			feedbacksListaBusca = feedbacks;
		}else {
			feedbacksListaBusca = new ArrayList<>();
		}
	}
	
	public void carregarFeedBacksByName() throws Exception{
	
		
		if(feedback.getFuncionario().getNome() != null || !feedback.getFuncionario().getNome().trim().equals("")) {
			
			List<Feedback> feedbacks = feedbackImpl.findAllByNameFuncionario2(feedback.getFuncionario().getNome());
			
			Collections.sort(feedbacks, new Comparator<Feedback>() {

				@Override
				public int compare(Feedback o1, Feedback o2) {
					return o2.getData().compareTo(o1.getData());
				}
			});
			
			feedbacksListaBusca = feedbacks;
			
		}else {
			
			feedbacksListaBusca = new ArrayList<>();
		}
	}
	
	public List<Feedback> getFeedbacksListaBusca() {
		return feedbacksListaBusca;
	}
	
	public void setFeedbacksListaBusca(List<Feedback> feedbacksListaBusca) {
		this.feedbacksListaBusca = feedbacksListaBusca;
	}
	
	public void filtrar(String filtrar){
		this.filtro = filtrar; 
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
	
	public List<Funcionarios> getFuncionariosListaBusca() {
		return funcionariosListaBusca;
	}
	
	public void setFuncionariosListaBusca(List<Funcionarios> funcionariosListaBusca) {
		this.funcionariosListaBusca = funcionariosListaBusca;
	}
	
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
}
