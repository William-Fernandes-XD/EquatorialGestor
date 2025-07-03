package com.gestorcoi.controllers;


import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;


import com.gestorcoi.entities.Roles;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.RoleImpl;
import com.gestorcoi.implementations.SupervisorImpl;
import com.gestorcoi.utils.MensagensJSF;

@ViewScoped
@javax.faces.bean.ManagedBean(name = "contaController")
public class ContasController {

	private SupervisorImpl metodo = new SupervisorImpl();
	
	private RoleImpl roleMetodos = new RoleImpl();
	
	private Supervisor supervisor = new Supervisor();
	
	private List<Roles> rolesSupervisor = new ArrayList<>();
	
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
			
			MensagensJSF.msgSeverityInfo("Conta salva com sucesso!", "Salvo");
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
			
			MensagensJSF.msgSeverityInfo("Salvo ou atualizado com sucesso", "Atualizado");
		}else {
			salvar();
		}
	}
	
	public void carregarSupervisorRoles(Supervisor obj) {
		setRolesSupervisor(obj.getRoles());
	}
	
	public void remover(Supervisor obj) throws Exception{
		
		if(obj.getId() != null) {
			metodo.remove(obj);
			MensagensJSF.msgSeverityInfo("Acesso removido com sucesso!", "Removido");
		}
	}
	
	public void removerRole(Roles role) throws Exception{
		
		if(role.getId() != null) {
			
			Supervisor supervisor = metodo.findById(role.getSupervisor().getId());
			
			int quant = supervisor.getRoles().size();
			
			if(quant <= 1){
				MensagensJSF.msgSeverityInfo("O usuário não pode ficar sem permissões", "Negado");
			}else {
				
				for (Roles rol : supervisor.getRoles()) {
					if(role.getId().equals(rol.getId())) {
						role = rol;
					}
				}
				
				supervisor.getRoles().remove(role);
				
				metodo.merge2(supervisor);
				
				MensagensJSF.msgSeverityInfo("Permissão removida com sucesso!", "Removido");
			}
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
	
	public List<Roles> getRolesSupervisor() {
		return rolesSupervisor;
	}
	
	public void setRolesSupervisor(List<Roles> rolesSupervisor) {
		this.rolesSupervisor = rolesSupervisor;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
}
