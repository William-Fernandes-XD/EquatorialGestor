package com.gestorcoi.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean(name = "userVerification")
@ViewScoped
public class UserVerification {

	public boolean isAdmin(){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"));
	}
}
