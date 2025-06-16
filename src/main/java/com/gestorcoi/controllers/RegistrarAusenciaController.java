package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
public class RegistrarAusenciaController {
	
	private Funcionarios funcionarios;
	private RegistroAusencia ausencia;
	
	private RegistrarAusenciaImpl ausenciaImpl = new RegistrarAusenciaImpl();
	private FuncionarioImpl funcionarioImpl = new FuncionarioImpl();
	
	private List<Funcionarios> listaFuncionariosAusencia = new ArrayList<>();
	private List<RegistroAusencia> listaAusenciasFuncionario = new ArrayList<>();
	
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
	
	/**
	 * 
	 * Carrega para o autoComplete do Primefaces
	 * @param query
	 * @return
	 * @throws Exception
	 */
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
		
		if(funcionarios.getNome() != null || !funcionarios.getNome().trim().equals("")) {
			
			funcionarios = (Funcionarios) funcionarioImpl.findByName(ausencia.getFuncionarios().getNome());
			
			if(funcionarios.getId() != null) {
				
				funcionarios.getAusencias().add(ausencia);
				ausencia.setFuncionarios(funcionarios);
				
				ausencia = ausenciaImpl.merge2(ausencia);
				
				funcionarios.getAusencias().remove(ausencia);
				
				funcionarioImpl.merge2(funcionarios);
				
				MensagensJSF.msgSeverityInfo("Ausência Registrada para " + funcionarios.getNome());
				
				limpar();
		}
		
		}else {
			MensagensJSF.msgSeverityError("Funcionário não encontrado");
		}
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
	
	public void carregarFuncionariosAusencias() throws Exception{
		
		listaFuncionariosAusencia = funcionarioImpl.findAll(Funcionarios.class);
		PrimeFaces.current().executeScript("PF('registroAusencia').show(); PF('funcionariosDialog').show();");
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
	
	public void setListaAusenciasFuncionario(List<RegistroAusencia> listaAusenciasFuncionario) {
		this.listaAusenciasFuncionario = listaAusenciasFuncionario;
	}
}
