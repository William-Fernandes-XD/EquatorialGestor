package com.gestorcoi.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "supervisor")
@RequestScoped
public class Supervisor {

	public String getMessage() {
		return "hello world";
	}
}
