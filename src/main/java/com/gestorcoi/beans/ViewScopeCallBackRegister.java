package com.gestorcoi.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapListener;

/**
 * Toda vez que um bean for destruído ou registrado, deverá passar por essa classe
 * @author e21057649
 *
 */
public class ViewScopeCallBackRegister implements Serializable, ViewMapListener{

	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		
		if(event instanceof PostConstructViewMapEvent) {
			
			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;
			UIViewRoot uiViewRoot = (UIViewRoot) viewMapEvent.getComponent();
			uiViewRoot.getViewMap().put(ViewScope.VIEW_SCOPE_CALLBACKS, new HashMap<String, Runnable>());
			
		}else if(event instanceof PreDestroyViewMapEvent) {
			
			PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) event;
			UIViewRoot uiViewRoot= (UIViewRoot) viewMapEvent.getComponent();
			Map<String, Runnable> callbacks = (Map<String, Runnable>) uiViewRoot.getViewMap().get(ViewScope.VIEW_SCOPE_CALLBACKS);
			
			if(callbacks != null) {
				for(Runnable c : callbacks.values()) {
					c.run();
				}
				callbacks.clear();
			}
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}

	
}
