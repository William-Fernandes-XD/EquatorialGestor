package com.gestorcoi.exception;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.utils.EstatusPersistencia;
import com.gestorcoi.utils.MensagensJSF;

@Component
public class CustomExceptionHandler extends ExceptionHandlerWrapper implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ExceptionHandler wrapperd;
	private final FacesContext facesContext = FacesContext.getCurrentInstance();
	private final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
	private final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
	
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.wrapperd = exceptionHandler;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapperd;
	}
	
	@Override
	public void handle() throws FacesException {
		
		Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();
		
		while(iterator.hasNext()) {
			
			ExceptionQueuedEvent event = iterator.next();
			ExceptionQueuedEventContext context = event.getContext();
			
			Throwable exception = context.getException();
			
			try {
				
				requestMap.put("exceptionMessage", exception.getMessage());
				
				if(exception != null && exception.getMessage() != null && exception.getMessage()
						.indexOf("ConstraintViolationException") != -1) {
					MensagensJSF.responseOperation(EstatusPersistencia.OBJETO_REFERENCIADO);
				}else if(exception != null && exception.getMessage() != null && exception.getMessage()
						.indexOf("org.hibernate.StaleObject.StateException") != -1) {
					facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Esse registro já foi movido ou removido por outro usuário", ""));
				}else {
					facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, "O sistema se recuperou de um"
							+ " erro inesperado.", ""));
					
					facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, ""
							+ "Você pode continuar operando o sistema normalmente", ""));
					
					facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro causado por: " 
							+ exception.getMessage(), ""));
				}
			}catch(Exception e) {
				
				facesContext.addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, exception.getMessage(), ""));
				navigationHandler.handleNavigation(facesContext, null, "/error/error.jsf?faces-redirect=true&expired=true");
			}finally {
				SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
				if(sessionFactory.getCurrentSession().getTransaction().isActive()) {
					sessionFactory.getCurrentSession().getTransaction().rollback();
					exception.printStackTrace();
					iterator.remove();
				}
			}
			getWrapped().handle();
		}
	}
}
