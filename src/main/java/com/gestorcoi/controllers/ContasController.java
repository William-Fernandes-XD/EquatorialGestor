package com.gestorcoi.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gestorcoi.entities.Roles;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.RoleImpl;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;

@Controller
@Scope(value="request")
@javax.faces.bean.ManagedBean(name = "contaController")
public class ContasController {

	private SupervisorImpl metodo = new SupervisorImpl();
	
	private RoleImpl roleMetodos = new RoleImpl();
	
	private Supervisor supervisor = new Supervisor();
	
	private String role;
	
	public ContasController() {

	}
	
	public List<Supervisor> listAllSupervisores() throws Exception{
		
		List<Supervisor> supervisores = metodo.findAll(Supervisor.class);
		return supervisores;
	}
	
	public void salvar() throws Exception{
		
		if(supervisor.getName() != null && supervisor.getName().trim() != ""){
			
			Roles roles = new Roles(role, supervisor);
			
			metodo.salvar(supervisor);
			roleMetodos.save(roles);
			
			List<Roles> roleslist = new ArrayList<>();
			roleslist.add(roles);
			
			supervisor.setRoles(roleslist);
			
			MensagensJSF.msgSeverityInfo("Salvo com sucesso");
		}
	}
	
	
	
	public void merge() throws Exception{
		
		if(supervisor.getId() != null) {
			
			Supervisor supervisorBuscado = metodo.findById(supervisor.getId());
			Roles role = new Roles(this.role, supervisorBuscado);
			
			supervisorBuscado.getRoles().add(role);
			
			supervisor = metodo.merge2(supervisorBuscado);
			
			supervisor.getRoles().remove(role);
			
			supervisor = metodo.merge2(supervisor);
			
			MensagensJSF.msgSeverityInfo("Salvo ou atualizado com sucesso");
		}else {
			salvar();
		}
	}
	
	public void remover(Supervisor obj) throws Exception{
		
		if(obj.getId() != null) {
			metodo.remove(obj);
			listAllSupervisores();
		}
	}
	
	public List<String> retornarPapeis() {
		List<String> roles = new ArrayList<>();
		roles.add("ADMIN");
		roles.add("SUPERVISOR");
		roles.add("AVALIADOR");
		roles.add("REGISTRADORA");
		
		return roles;
	}
	
	public Supervisor getSupervisor() {
		return supervisor;
	}
	
	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}
	
	public String getRole() {
		
		return this.role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
