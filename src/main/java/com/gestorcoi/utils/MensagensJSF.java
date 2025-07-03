package com.gestorcoi.utils;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.springframework.stereotype.Component;

/**
 * Faz as mensagens padrão que aparecem no JSF ou melhor dizendo, quadro de mensagens do primefaces
 * @author e21057649
 *
 */
@Component
public abstract class MensagensJSF extends FacesContext implements Serializable{

	private static final long serialVersionUID = 1L;

	public MensagensJSF() {

	}
	
	public static void responseOperation(EstatusPersistencia estatusPersistencia) {
		if(estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {
			sucesso();
		}else if(estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.ERRO)) {
			erroNaOperacao();
		}else if(estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {
			msgSeverityError(EstatusPersistencia.OBJETO_REFERENCIADO.toString());
		}else {
			erroNaOperacao();
		}
	}
	
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	private static boolean facesContextValido() {
		return getFacesContext() != null;
	}
	
	public static void msgSeverityWarn(String msg) {
		
		if(facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
		}
	}
	
	public static void msgSeverityError(String msg) {
		
		if(facesContextValido()) {
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
		}
	}

	public static void msgSeverityInfo(String msg, String summary) {
	
		if(facesContextValido()) {
		getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, summary, msg));
		}
	}
	
	public static void erroNaOperacao() {
		if(facesContextValido()) {
			msgSeverityError(ConstanteMensagem.ERRO_NA_OPERACAO);
		}
	}
	
	public static void sucesso() {
		if(facesContextValido()) {
			msgSeverityInfo(ConstanteMensagem.SUCESSO, "Sucesso");
		}
	}
	
	public static void msg(String msg) {
		if(facesContextValido()) {
			msgSeverityInfo(msg, msg);
		}
	}
}
