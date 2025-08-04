package com.gestorcoi.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.entities.RegistroAusencia;
import com.gestorcoi.implementations.FuncionarioImpl;
import com.gestorcoi.implementations.RegistrarAusenciaImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "ausenciaController")
@ViewScoped
public class RegistrarAusenciaController implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Funcionarios funcionarios;
	private RegistroAusencia ausencia;
	
	private RegistrarAusenciaImpl ausenciaImpl = new RegistrarAusenciaImpl();
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private List<Funcionarios> listaFuncionariosAusencia = new ArrayList<>();
	private List<RegistroAusencia> listaAusenciasFuncionario = new ArrayList<>();
	
	private List<RegistroAusencia> listaAusenciasGerais = new ArrayList<>();
	
	private List<String> funcionariosNomesAutoComplete = new ArrayList<>();
	
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
		
		if(ausencia.getFuncionarios() == null) {
			ausencia.setFuncionarios(new Funcionarios());
		}
	}
	
	public RegistrarAusenciaController(){
		
		try {
			
			if(funcionarios == null) {
				funcionarios = new Funcionarios();
				ausencia = new RegistroAusencia();
				ausencia.setFuncionarios(new Funcionarios());
				
				if(funcionarios.getAusencias() == null || funcionarios.getAusencias().isEmpty()) {
					funcionarios.setAusencias(new ArrayList<RegistroAusencia>());
				}
			}
			
			listAllAusencia();
		} catch (Exception e) {
			MensagensJSF.msgSeverityInfo("Não foi possível carregar a tabela de dados", "Um erro inesperado");
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Carrega para o autoComplete do Primefaces
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public List<String> buscarFuncionarios(String query) throws Exception{
		
		
		if(funcionariosNomesAutoComplete == null) {
			funcionariosNomesAutoComplete = new ArrayList<>();
		}
		
		if(funcionariosNomesAutoComplete.isEmpty()) {
			
			List<Funcionarios> funcionarios = funcionarioImpl.findAll(Funcionarios.class);
			
			for (Funcionarios funcionario : funcionarios) {
				funcionariosNomesAutoComplete.add(funcionario.getNome());
			}
		}
		
		List<String> nomesFiltrados = funcionariosNomesAutoComplete;

	    return nomesFiltrados.stream().filter(name -> name.toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
	}
	
	public void limpar() {
		funcionarios = new Funcionarios();
		ausencia = new RegistroAusencia();
		ausencia.setFuncionarios(new Funcionarios());
		funcionarios.setAusencias(new ArrayList<RegistroAusencia>());
	}
	
	public void salvarAusencia() throws Exception{
		
		try {
			
				if(ausencia.getFuncionarios().getNome() != null && !ausencia.getFuncionarios().getNome().trim().equals("")) {
			
					funcionarios = (Funcionarios) funcionarioImpl.findByName(ausencia.getFuncionarios().getNome());
				
					if(funcionarios.getId() != null) {
					
						funcionarios.getAusencias().add(ausencia);
						ausencia.setFuncionarios(funcionarios);
					
						ausencia = ausenciaImpl.merge2(ausencia);
					
						listaAusenciasGerais.add(ausencia);
					
						MensagensJSF.msgSeverityInfo("Ausência Registrada para " + funcionarios.getNome(), "Salvo");
					
						limpar();
					}else {
						MensagensJSF.msgSeverityError("Funcionário não encontrado");
					}
				}else {
					MensagensJSF.msgSeverityError("Funcionário não encontrado");
				}
			}catch(Exception e) {
				MensagensJSF.msgSeverityInfo("Não foi possível salvar nova ausência", "Um erro inesperado");
				e.printStackTrace();
			}
	}
	
	public void salvarAusenciaComFuncionario() throws Exception{
		
		try {
			
				if(funcionarios.getNome() != null && !funcionarios.getNome().trim().equals("")) {
			
					funcionarios = (Funcionarios) funcionarioImpl.findByName(funcionarios.getNome());
				
					if(funcionarios.getId() != null) {
					
						funcionarios.getAusencias().add(ausencia);
						ausencia.setFuncionarios(funcionarios);
					
						ausencia = ausenciaImpl.merge2(ausencia);
						
						listaAusenciasGerais.add(ausencia);
					
						MensagensJSF.msgSeverityInfo("Ausência Registrada para " + funcionarios.getNome(), "Salvo");
					
						limpar();
					}else {
						MensagensJSF.msgSeverityError("Funcionário não encontrado");
					}
				}else {
					MensagensJSF.msgSeverityError("Funcionário não encontrado");
				}
			}catch(Exception e) {
				MensagensJSF.msgSeverityInfo("Não foi possível salvar nova ausência", "Um erro inesperado");
				e.printStackTrace();
			}
	}
	
	public void removerAusenciaOptimusPrime(RegistroAusencia ausencia) {
		
		try {
			
			ausenciaImpl.remove(ausencia);
			listaAusenciasGerais.remove(ausencia);
			MensagensJSF.msgSeverityInfo("Registro removido com sucesso", "Removido");
			
		}catch(Exception e) {
			MensagensJSF.msgSeverityInfo("Impossível apagar os dados", "Um erro inesperado");
			e.printStackTrace();
		}
	}
	
	public void listAllAusencia() throws Exception{
		
		List<RegistroAusencia> ausencias = ausenciaImpl.findAll(RegistroAusencia.class);
		
		Collections.sort(ausencias, new Comparator<RegistroAusencia>() {

			@Override
			public int compare(RegistroAusencia o1, RegistroAusencia o2) {
				return o2.getData_ausencia().compareTo(o1.getData_ausencia());
			}
		});;
		
		listaAusenciasGerais = ausencias;
	}
	
	public void carregarAusenciasFuncionarioByObject(Funcionarios funcionario) throws Exception {
		
		List<RegistroAusencia> ausenciasRegistradas = ausenciaImpl.findAllByFuncionario(funcionario.getId());
		listaAusenciasFuncionario = ausenciasRegistradas;
	}
	
	public void carregarAusenciasFuncionarioByName(String name) throws Exception {
		
		if(!name.trim().equals("") || name != null) {
			
			List<RegistroAusencia> ausenciasRegistradas = ausenciaImpl.findAllByNameFuncionario(name);
			listaAusenciasFuncionario = ausenciasRegistradas;
		}
	}
	
	public Long contadorDeAusencias() {
		return (long) listaAusenciasGerais.size();
	}
	
	public Long contadorDeFuncionarios() {
		return funcionarioImpl.count();
	}
	
	public void carregarFuncionariosAusencias() throws Exception{
		
		listaFuncionariosAusencia = funcionarioImpl.findAll(Funcionarios.class);
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
	
	public List<Funcionarios> getListaFuncionariosAusencia() {
		return listaFuncionariosAusencia;
	}
	
	public void setListaFuncionariosAusencia(List<Funcionarios> listaFuncionariosAusencia) {
		this.listaFuncionariosAusencia = listaFuncionariosAusencia;
	}
	
	public List<RegistroAusencia> getListaAusenciasFuncionario() {
		return listaAusenciasFuncionario;
	}
	
	public List<RegistroAusencia> getListaAusenciasGerais() {
		return listaAusenciasGerais;
	}
	
	public void setListaAusenciasGerais(List<RegistroAusencia> listaAusenciasGerais) {
		this.listaAusenciasGerais = listaAusenciasGerais;
	}
	
	public void setListaAusenciasFuncionario(List<RegistroAusencia> listaAusenciasFuncionario) {
		this.listaAusenciasFuncionario = listaAusenciasFuncionario;
	}
}
