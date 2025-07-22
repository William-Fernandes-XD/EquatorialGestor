package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.gestorcoi.entities.configGeraEscala.GeradorEscalaEntity;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

public class GeradorEscalaEntityImpl implements AbstractMethods<GeradorEscalaEntity>, Serializable{

	private static final long serialVersionUID = 1L;

	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	private void validateSessionFactory() {
		
		if(sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().getTransaction().begin();
		}
	}
	
	private void executeFlush() {
		sessionFactory.getCurrentSession().flush();
	}
	
	@Override
	public void merge(GeradorEscalaEntity obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
	}

	@Override
	public void remove(GeradorEscalaEntity obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<GeradorEscalaEntity> findAll(Class<GeradorEscalaEntity> classe) throws Exception {
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("Select distinct(entity) from ").append(GeradorEscalaEntity.class.getSimpleName());
		query.append(" entity");
		
		@SuppressWarnings("unchecked")
		List<GeradorEscalaEntity> retorno = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return retorno;
	}

	@Override
	public List<GeradorEscalaEntity> findAllOnCondition(Class<GeradorEscalaEntity> classe, String condition)
			throws Exception {
		return null;
	}

	
}
