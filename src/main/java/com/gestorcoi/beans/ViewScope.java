package com.gestorcoi.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;


/**
 * 
 * Scopo de View do spring
 * @author e21057649
 *
 */
@SuppressWarnings("unchecked")
public class ViewScope implements Serializable, Scope{

	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callBacks";

	@Override
	public Object get(String name, ObjectFactory<?> object) {
		
		Object instance = getViewScope().get(name);
		
		if(instance == null) {
			instance = object.getObject();
			getViewScope().put(name, instance);
		}
		return instance;
	}

	@Override
	public String getConversationId() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		
		System.out.println(facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId());
		
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
	}

	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {

		Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewScope().get(VIEW_SCOPE_CALLBACKS);
		
		if(callbacks != null) {
			callbacks.put(VIEW_SCOPE_CALLBACKS, runnable);
		}
	}

	@Override
	public Object remove(String name) {
		
		Object instance = getViewScope().remove(name);
		
		if(instance != null) {
			Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewScope().get(VIEW_SCOPE_CALLBACKS); 
			if(callbacks != null) {
				callbacks.remove(name);
			}
		}
		return instance;
	}

	@Override
	public Object resolveContextualObject(String name) {
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(context);
		
		return facesRequestAttributes.resolveReference(name);
	}

	public static Map<String, Object> getViewScope() {
		return FacesContext.getCurrentInstance() != null ? FacesContext.getCurrentInstance().getViewRoot().getViewMap() :
			new HashMap<String, Object>();
	}
}
