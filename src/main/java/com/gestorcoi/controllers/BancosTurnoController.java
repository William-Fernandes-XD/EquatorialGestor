package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.BancosTurno;
import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.implementations.BancosTurnoImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "bancosTurnoController")
@ViewScoped
public class BancosTurnoController {
	
	private BancosTurno bancosTurno;
	private Funcionarios funcionario;
	
	private List<BancosTurno> bancosTurnos = new ArrayList<>();
	
	private BancosTurnoImpl bancosTurnoImpl = new BancosTurnoImpl();
	
	@PostConstruct
	public void init() {
		
		if(bancosTurno == null) {
			bancosTurno = new BancosTurno();
		}
		
		if(funcionario == null) {
			funcionario = new Funcionarios();
		}
	}

	public void salvar() throws Exception{
		
		bancosTurno.setFuncionario(funcionario);
		bancosTurnoImpl.save(bancosTurno);
		
		MensagensJSF.msgSeverityInfo("Banco de horas adicionado com sucesso", "Salvo");
	}
	
	public void remover(BancosTurno bancosTurno) throws Exception{
		
		bancosTurnoImpl.remove(bancosTurno);
		bancosTurnos.remove(bancosTurno);
		MensagensJSF.msgSeverityInfo("Banco de horas removido com sucesso", "Removido");
	}
	
	public void carregarTodos() throws Exception{
		
		if(funcionario.getId() != null) {
			bancosTurnos = bancosTurnoImpl.findAllById(funcionario.getId());
		}else {
			bancosTurnos = new ArrayList<>();
		}
	}
	
	public Funcionarios getFuncionario() {
		return funcionario;
	}
	
	public void setFuncionario(Funcionarios funcionario) {
		this.funcionario = funcionario;
	}
	
	public BancosTurno getBancosTurno() {
		return bancosTurno;
	}
	
	public void setBancosTurno(BancosTurno bancosTurno) {
		this.bancosTurno = bancosTurno;
	}
	
	public List<BancosTurno> getBancosTurnos() {
		return bancosTurnos;
	}
	
	public void setBancosTurnos(List<BancosTurno> bancosTurnos) {
		this.bancosTurnos = bancosTurnos;
	}
}
