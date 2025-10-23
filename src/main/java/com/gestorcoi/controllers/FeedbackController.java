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
	
	/**
	 * Listas mais pesadas de consulta, sendo as de principal view
	 */
	
	private	List<Feedback> listaAllFuncionariosMainPageViewAuxiliar = new ArrayList<>();
	private List<Feedback> listaAllFuncionariosMainPageView = new ArrayList<>();
	private List<Funcionarios> allFuncionariosObjectDialog = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		try {
			listarAll();
		} catch (Exception e) {
			MensagensJSF.msgSeverityInfo("Não foi possível carregar a tabela de feedbacks", "Um erro inesperado");
		}
		
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
			
				feedbackImpl.merge2(feedback);
				
				listaAllFuncionariosMainPageView.add(feedback);
				
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
		listaAllFuncionariosMainPageView.remove(feedback);
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
	
	public void getAllFuncionariosObjeto() throws Exception{
		
		allFuncionariosObjectDialog = funcionarioImpl.findAll(Funcionarios.class);
	}
	
	public void listarAll() throws Exception{
		
		listaAllFuncionariosMainPageViewAuxiliar = feedbackImpl.findAll(Feedback.class);	
		
		listaAllFuncionariosMainPageView = listaAllFuncionariosMainPageViewAuxiliar;
		
		Collections.sort(listaAllFuncionariosMainPageView, new Comparator<Feedback>() {

			@Override
			public int compare(Feedback o1, Feedback o2) {
				return o2.getData().compareTo(o1.getData());
			}
		});
	}
	
	public void filtros(String filtro) throws Exception{
		
		listaAllFuncionariosMainPageView = listaAllFuncionariosMainPageViewAuxiliar;
		
		if ("Positivo".equalsIgnoreCase(filtro)) {
			listaAllFuncionariosMainPageView = listaAllFuncionariosMainPageView.stream()
		            .filter(f -> "Positivo".equalsIgnoreCase(f.getPositivoOrNegative()))
		            .collect(Collectors.toList());
		} else if ("Negativo".equalsIgnoreCase(filtro)) {
			listaAllFuncionariosMainPageView = listaAllFuncionariosMainPageView.stream()
		            .filter(f -> "Negativo".equalsIgnoreCase(f.getPositivoOrNegative()))
		            .collect(Collectors.toList());
		}
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
	
	public List<String> feedbacksAutoComplete(String query){
		
		List<String> feedbacks = new ArrayList<>();
		
		if(feedback.getPositivoOrNegative() != null && feedback.getPositivoOrNegative().equalsIgnoreCase("Positivo")) {
			
			feedbacks.add("Bom desenvolvimento");
			feedbacks.add("Cumpriu prazos");
			feedbacks.add("Demonstrou um ótimo empenho");
		}else if(feedback.getPositivoOrNegative() != null && feedback.getPositivoOrNegative().equalsIgnoreCase("Negativo")) {
			
			feedbacks.add("Não está cumprindo prazos");
			feedbacks.add("Falta de empenho");
		}else {
			feedbacks.add("Bom desenvolvimento");
			feedbacks.add("Cumpriu prazos");
			feedbacks.add("Demonstrou um ótimo empenho");
			feedbacks.add("Não está cumprindo prazos");
			feedbacks.add("Falta de empenho");
		}
		
		return feedbacks.stream().filter(s -> s.toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public List<Feedback> getFeedbacksListaBusca() {
		return feedbacksListaBusca;
	}
	
	public void setFeedbacksListaBusca(List<Feedback> feedbacksListaBusca) {
		this.feedbacksListaBusca = feedbacksListaBusca;
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
	
	public List<Funcionarios> getAllFuncionariosObjectDialog() {
		return allFuncionariosObjectDialog;
	}
	
	public void setAllFuncionariosObjectDialog(List<Funcionarios> allFuncionariosObjectDialog) {
		this.allFuncionariosObjectDialog = allFuncionariosObjectDialog;
	}
	
	public List<Feedback> getListaAllFuncionariosMainPageView() {
		return listaAllFuncionariosMainPageView;
	}
	
	public void setListaAllFuncionariosMainPageView(List<Feedback> listaAllFuncionariosMainPageView) {
		this.listaAllFuncionariosMainPageView = listaAllFuncionariosMainPageView;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
}
