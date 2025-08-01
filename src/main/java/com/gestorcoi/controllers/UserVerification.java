package com.gestorcoi.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.gestorcoi.entities.Roles;
import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.implementations.SupervisorImpl;

@ManagedBean(name = "userVerification")
@ViewScoped
public class UserVerification {
	
	private Supervisor supervisorLogado;

	public boolean isAdmin(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
	}
	
	public boolean linkFuncionarios(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR")
		|| authority.getAuthority().equals("REGISTRADORA"));
	}
	
	public boolean linkSupervisorTurno(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR"));
	}
	
	public boolean linkFeedbacks(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR")
		|| authority.getAuthority().equals("Avaliador"));
	}
	
	public boolean linkCards(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR")
		|| authority.getAuthority().equals("AVALIADOR"));
	}
	
	public boolean linkGestorTurnoColaboradores(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR")
		|| authority.getAuthority().equals("REGISTRADORA"));
	}
	
	public boolean linkRegistroAusencias(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().stream().anyMatch(authority -> 
		
		authority.getAuthority().equals("ADMIN") 
		|| authority.getAuthority().equals("SUPERVISOR")
		|| authority.getAuthority().equals("REGISTRADORA"));
	}
	
	public boolean isRegistradora(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("REGISTRADORA"));
	}
	
	@PostConstruct
	private void carregarSupervisorLogado() throws Exception{
		
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String nomeSupervisor = auth.getName();
			
			SupervisorImpl supervisorImpl = new SupervisorImpl();
			
			this.supervisorLogado = supervisorImpl.findByName(nomeSupervisor);
			
		}catch(Exception e) {
			
			FacesContext.getCurrentInstance().getExternalContext()
			.redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/");
			
			e.printStackTrace();
		}
	}
	
	public List<String> papeisUsuario() throws Exception{
		
		if(this.supervisorLogado == null) {
			carregarSupervisorLogado();
		}
		
		List<String> rolesCarregadosString = new ArrayList<>();
		
		for (Roles role : this.supervisorLogado.getRoles()) {
			rolesCarregadosString.add(role.getRole());
		}
		
		Collections.sort(rolesCarregadosString, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});
		
		return rolesCarregadosString;
	}
	
	public Supervisor getSupervisorLogado() throws Exception{
		
		if(this.supervisorLogado == null) {
			carregarSupervisorLogado();
		}
		
		return supervisorLogado;
	}
}
