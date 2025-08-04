package com.gestorcoi.controllers;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ManagedBean(name = "homeController")
@ViewScoped
public class HomeBeanController {

	public void init() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		
		if (Boolean.TRUE.equals(session.getAttribute("loginSucesso"))) {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Autenticado com sucesso",
							"Seja muito bem-vindo(a) ao sistema, " + auth.getName()));
			session.removeAttribute("loginSucesso");
		}
	}
}
