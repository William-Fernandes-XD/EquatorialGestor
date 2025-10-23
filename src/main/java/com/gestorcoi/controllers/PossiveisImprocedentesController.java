package com.gestorcoi.controllers;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.gestorcoi.entities.ImprocedentesEntidade;
import com.gestorcoi.implementations.CarregarImprocedentes;
import com.gestorcoi.utils.MensagensJSF;

@ApplicationScoped
@ManagedBean(name = "improcedentes")
public class PossiveisImprocedentesController {

	private List<ImprocedentesEntidade> improcedentesEntidade = new ArrayList<>();
	private CarregarImprocedentes carregarImprocedentes = new CarregarImprocedentes();
	private String ultimaAtualizacao;
	
	public PossiveisImprocedentesController() {
		
		this.improcedentesEntidade = carregarImprocedentes.iniciarSqlImprocedentes();
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		this.ultimaAtualizacao = LocalDateTime.now().format(formatter);
		
		MensagensJSF.msgSeverityInfo("Tabela de improcedentes Atualizada, recarregue a página", "Pressione F5");
		
		atualizarDados();
	}
	
	@PostConstruct
	public void atualizarDados() {
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(() -> {
			
			try{
				
				this.improcedentesEntidade = carregarImprocedentes.iniciarSqlImprocedentes();
				
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
				this.ultimaAtualizacao = LocalDateTime.now().format(formatter);
				
				MensagensJSF.msgSeverityInfo("Tabela de improcedentes Atualizada, recarregue a página", "Pressione F5");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}, 0, 10, TimeUnit.MINUTES);
	}
	
	public List<ImprocedentesEntidade> getImprocedentesEntidade() {
		return improcedentesEntidade;
	}
	
	public void setImprocedentesEntidade(List<ImprocedentesEntidade> improcedentesEntidade) {
		this.improcedentesEntidade = improcedentesEntidade;
	}

	public String getUltimaAtualizacao() {
		return ultimaAtualizacao;
	}

	public void setUltimaAtualizacao(String ultimaAtualizacao) {
		this.ultimaAtualizacao = ultimaAtualizacao;
	}
}
