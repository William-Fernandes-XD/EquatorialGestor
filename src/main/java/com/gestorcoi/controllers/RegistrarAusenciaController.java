package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.RegistroAusencia;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.implementations.RegistrarAusenciaImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "ausenciaController")
@ViewScoped
public class RegistrarAusenciaController {
	
	private Funcionarios funcionarios;
	private RegistroAusencia ausencia;
	
	private RegistrarAusenciaImpl ausenciaImpl = new RegistrarAusenciaImpl();
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	@PostConstruct
	public void init() {
		
		if(funcionarios == null) {
			funcionarios = new Funcionarios();
			ausencia = new RegistroAusencia();
			ausencia.setFuncionarios(new Funcionarios());
			
			if(funcionarios.getAusencias() == null || funcionarios.getAusencias().isEmpty()) {
				funcionarios.setAusencias(new ArrayList<RegistroAusencia>());
			}
		}
	}
	
	public List<String> buscarFuncionarios(String query) throws Exception{
		
	    List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
	    List<String> nomesFiltrados = new ArrayList<>();

	    for (Funcionarios obj : funcionarios) {
	        if (obj.getNome().toLowerCase().contains(query.toLowerCase())) {
	            nomesFiltrados.add(obj.getNome());
	        }
	    }

	    return nomesFiltrados;
	}
	
	public void limpar() {
		funcionarios = new Funcionarios();
		ausencia = new RegistroAusencia();
		ausencia.setFuncionarios(new Funcionarios());
		funcionarios.setAusencias(new ArrayList<RegistroAusencia>());
	}
	
	public void removerAusencia(RegistroAusencia ausencia) throws Exception{
		
		if(ausencia.getId() != null) {
			
			funcionarios = funcionarioImpl.findByName(ausencia.getFuncionarios().getNome());
			funcionarios.getAusencias().remove(ausencia);
			
			funcionarioImpl.merge2(funcionarios);
			
			MensagensJSF.msgSeverityInfo("Removido com sucesso");
		}else {
			MensagensJSF.msgSeverityError("Não foi possível identificar essa ausência");
		}
	}
	
	public void salvarAusencia() throws Exception{
		
		funcionarios = (Funcionarios) funcionarioImpl.findByName(ausencia.getFuncionarios().getNome());
		
		if(funcionarios.getId() != null) {
			
			funcionarios.getAusencias().add(ausencia);
			ausencia.setFuncionarios(funcionarios);
			
			ausencia = ausenciaImpl.merge2(ausencia);
			
			funcionarios.getAusencias().remove(ausencia);
			
			funcionarioImpl.merge2(funcionarios);
			
			limpar();
			
			MensagensJSF.msgSeverityInfo("Ausência Registrada para " + funcionarios.getNome());
		}else {
			MensagensJSF.msgSeverityError("Funcionário não encontrado");
		}
	}
	
	public List<Funcionarios> findAllFuncionarios() throws Exception{
		
		List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
		
		return funcionarios;
	}
	
	public List<RegistroAusencia> findAllAusenciasByName() throws Exception{
		
		List<RegistroAusencia> ausencias = new ArrayList<>();
		
		if(funcionarios.getId() != null) {
			ausencias = ausenciaImpl.findAllByFuncionario(funcionarios.getId());
		}else if(funcionarios.getNome() != null) {
			ausencias = ausenciaImpl.findAllByNameFuncionario(funcionarios.getNome());
		}
		return ausencias;
	}
	
	public List<RegistroAusencia> listAllAusencia() throws Exception{
		
		List<RegistroAusencia> ausencias = ausenciaImpl.findAll(RegistroAusencia.class);
		
		Collections.sort(ausencias, new Comparator<RegistroAusencia>() {

			@Override
			public int compare(RegistroAusencia o1, RegistroAusencia o2) {
				return o2.getData_ausencia().compareTo(o1.getData_ausencia());
			}
		});;
		
		return ausencias;
	}
	
	public RegistroAusencia getAusencia() {
		return ausencia;
	}
	
	public void setAusencia(RegistroAusencia ausencia) {
		this.ausencia = ausencia;
	}
	
	public void setFuncionarios(Funcionarios funcionarios) {
		this.funcionarios = funcionarios;
	}
	
	public Funcionarios getFuncionarios() {
		return funcionarios;
	}
}
