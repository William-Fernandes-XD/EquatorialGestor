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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.SelectEvent;

import com.gestorcoi.entities.GestorEntity;
import com.gestorcoi.entities.Ocorrencia;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.GestorEntityImpl;
import com.gestorcoi.implementations.OcorrenciaImpl;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;

@ManagedBean(name = "gestorTurnoController")
@ViewScoped
public class GestorTurnoController {
	
	//private List<Row> rowsOcorrenciasXlsx = ExcelScanner.retornarOcorrencia(); 

	private Ocorrencia ocorrencia = new Ocorrencia();
	private GestorEntity gestorEntity = new GestorEntity();
	
	private SupervisorImpl supervisorImpl = new SupervisorImpl();
	private OcorrenciaImpl ocorrenciaImpl = new OcorrenciaImpl();
	private GestorEntityImpl gestorEntityImpl = new GestorEntityImpl();
	
	private List<String> supervisoresName = new ArrayList<>();
	
	private List<String> ocorrenciasAutoComplete = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		if(gestorEntity.getSupervisorSaindo() == null || gestorEntity.getSupervisorEntrando() == null) {
			gestorEntity.setSupervisorEntrando(new Supervisor());
			gestorEntity.setSupervisorSaindo(new Supervisor());
			gestorEntity.setOcorrencias(new ArrayList<>());
		}
	}
	
	public void salvar() throws Exception{
		
		if(gestorEntity.getId() == null) {
			
			if(gestorEntity.getSupervisorEntrando() != null && gestorEntity.getSupervisorSaindo() != null &&
					!gestorEntity.getSupervisorEntrando().getName().trim().equals("")
					&& !gestorEntity.getSupervisorSaindo().getName().trim().equals("")) {
				
				Supervisor supervisorEntrando = supervisorImpl.findByName(gestorEntity.getSupervisorEntrando().getName());
				Supervisor supervisorSaindo = supervisorImpl.findByName(gestorEntity.getSupervisorSaindo().getName());
				
				if(supervisorEntrando != null && supervisorSaindo != null) {
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			        Date agora = new Date();

			        Date somenteData = formatter.parse(formatter.format(agora));
					
					gestorEntity.setDate(somenteData);
				
					gestorEntity.setSupervisorEntrando(supervisorEntrando);
					gestorEntity.setSupervisorSaindo(supervisorSaindo);
					
					ocorrencia.setSupervisorEntrandoOcorrencia(supervisorEntrando);
					
					gestorEntity.getOcorrencias().add(ocorrencia);
					ocorrencia.setGestorEntity(gestorEntity);
					
					gestorEntity = gestorEntityImpl.merge2(gestorEntity);
					
					gestorEntity.getOcorrencias().remove(ocorrencia);
					ocorrenciaImpl.remove(ocorrencia);
					
					gestorEntity = gestorEntityImpl.merge2(gestorEntity);
					
					 MensagensJSF.msgSeverityInfo("Gestão salva com sucesso!", "Salvo");
				}else {
					MensagensJSF.msgSeverityError("Esse usuário não pode ser encontrado ou não existe");
				}
			}else {
				MensagensJSF.msgSeverityInfo("Preencha os dados corretamente", "Dados Incompletos");
			}
		}else {
			MensagensJSF.msgSeverityInfo("Limpe a seleção primeiramente antes de salvar outra gestão", "Negado");
		}
	}
	
	public void limpar() {
		
		gestorEntity = new GestorEntity();
		ocorrencia = new Ocorrencia();
		gestorEntity.setSupervisorEntrando(new Supervisor());
		gestorEntity.setSupervisorSaindo(new Supervisor());
		gestorEntity.setOcorrencias(new ArrayList<>());
	}
	
	public List<GestorEntity> listAllGestor() throws Exception{
		
		List<GestorEntity> gestorEntities = gestorEntityImpl.findAll(GestorEntity.class);
		
		Collections.sort(gestorEntities, new Comparator<GestorEntity>() {

			@Override
			public int compare(GestorEntity o1, GestorEntity o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		
		return gestorEntities;
	}
	
	public List<String> findAllName(String query) throws Exception{
		
		
		if(supervisoresName == null || supervisoresName.isEmpty()) {
			
			List<Supervisor> supervisores = supervisorImpl.findAllSupervisores();
			
			for (Supervisor supervisor : supervisores) {
				supervisoresName.add(supervisor.getName());
			}
		}
		
		List<String> nomes = supervisoresName;
		
		return nomes.stream().filter(name -> name.toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public List<String> findAllName2(String query) throws Exception{
		
		if (supervisoresName == null) {
		    supervisoresName = new ArrayList<>();
		}

		if (supervisoresName.isEmpty()) {
		    List<Supervisor> supervisores = supervisorImpl.findAllSupervisores();

		    for (Supervisor supervisor : supervisores) {
		        supervisoresName.add(supervisor.getName());
		    }
		}
		
		List<String> nomes = supervisoresName;
		
		return nomes.stream().filter(name -> name.toLowerCase().contains(query.toLowerCase()))
				.collect(Collectors.toList());
	}
	
	public List<Ocorrencia> listAllOcorrencias() throws Exception{
		
		List<Ocorrencia> ocorrencias = ocorrenciaImpl.findAllByGestor(gestorEntity.getId());
		
		Collections.sort(ocorrencias, new Comparator<Ocorrencia>() {

			@Override
			public int compare(Ocorrencia o1, Ocorrencia o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		
		return ocorrencias;
	}
	
	public void removerOcorrencia(Ocorrencia ocorrencia) throws Exception{
		
		 if (gestorEntity.getId() != null) {
		        GestorEntity gestorEntity = gestorEntityImpl.findById(ocorrencia.getGestorEntity().getId());
		        
		        boolean removed = gestorEntity.getOcorrencias().remove(ocorrencia);
		        if(removed) {
		            ocorrencia.setGestorEntity(null);
		            
		            gestorEntityImpl.merge2(gestorEntity);
		            
		            MensagensJSF.msgSeverityInfo("Ocorrência removida com sucesso", "Removido");
		        } else {
		            MensagensJSF.msgSeverityError("Ocorrência não encontrada na gestão");
		        }
		        
		    } else {
		        MensagensJSF.msgSeverityInfo("Não foi possível identificar essa ocorrência, tente atualizar ou chamar a equipe TI", "Dados ilegíveis");
		    }
	}
	
	public void adicionarOcorrencia() throws Exception {

	    ocorrencia.setId(null);

	    if (gestorEntity.getId() != null) {

	        Supervisor supervisorEntrando = supervisorImpl.findByName(gestorEntity.getSupervisorEntrando().getName());

	        if (supervisorEntrando == null) {
	            MensagensJSF.msgSeverityError("Supervisor não encontrado");
	            return;
	        }

	        GestorEntity gestorDoBanco = gestorEntityImpl.findById(gestorEntity.getId());

	        ocorrencia.setSupervisorEntrandoOcorrencia(supervisorEntrando);
	        ocorrencia.setGestorEntity(gestorDoBanco); 

	        gestorDoBanco.getOcorrencias().add(ocorrencia); 

	        gestorEntity = gestorEntityImpl.merge2(gestorDoBanco);
	        
	        gestorEntity.getOcorrencias().remove(ocorrencia);
	        
	        gestorEntity = gestorEntityImpl.merge2(gestorEntity);
	        
	        MensagensJSF.msgSeverityInfo("Ocorrência adicionada com sucesso!", "Salvo");
	    } else {
	        MensagensJSF.msgSeverityInfo("Crie uma gestão primeiro", "Dados Incompletos");
	    }
	}
	
	public void remover(GestorEntity gestorEntity) throws Exception{
		
		if(gestorEntity.getId() != null) {
			gestorEntityImpl.remove(gestorEntity);
			MensagensJSF.msgSeverityInfo("Ocorrência removida com sucesso!", "Removido");
		}else {
			MensagensJSF.msgSeverityError("Não foi possível identificar essa gestão");
		}
	}
	
	public List<String> carregarTurnos(){
		
		List<String> retorno = new ArrayList<>();
		
		retorno.add("1");
		retorno.add("2");
		retorno.add("3");
		retorno.add("4");
		
		return retorno;
	}
	
	public GestorEntity getGestorEntity() {
		return gestorEntity;
	}
	
	public void setGestorEntity(GestorEntity gestorEntity) {
		this.gestorEntity = gestorEntity;
	}
	
	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}
	
	public List<String> getSupervisoresName() {
		return supervisoresName;
	}
	
	public void setSupervisoresName(List<String> supervisoresName) {
		this.supervisoresName = supervisoresName;
	}
	
	public List<String> getOcorrenciasAutoComplete() {
		return ocorrenciasAutoComplete;
	}
	
	public void setOcorrenciasAutoComplete(List<String> ocorrenciasAutoComplete) {
		this.ocorrenciasAutoComplete = ocorrenciasAutoComplete;
	}
	
	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}
}
