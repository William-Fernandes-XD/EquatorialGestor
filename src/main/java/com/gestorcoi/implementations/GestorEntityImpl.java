package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestorcoi.entities.GestorEntity;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class GestorEntityImpl implements AbstractMethods<GestorEntity>, Serializable{

	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
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
	public void merge(GestorEntity obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
	}


	public GestorEntity merge2(GestorEntity obj) throws Exception {
		
		validateSessionFactory();
		GestorEntity gestorEntity = (GestorEntity) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return gestorEntity;
	}

	@Override
	public void remove(GestorEntity obj) throws Exception {
	
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<GestorEntity> findAll(Class<GestorEntity> classe) throws Exception {
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(GestorEntity.class.getSimpleName())
		.append(" entity");
		
		List<GestorEntity> gestorEntities = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return gestorEntities;
	}
	
	public GestorEntity findById(Long id) {
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(GestorEntity.class.getSimpleName())
		.append(" entity where entity.id = :id");
		
		GestorEntity gestorEntity = (GestorEntity) sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("id", id).uniqueResult();
		
		return gestorEntity;
	}

	@Override
	public List<GestorEntity> findAllOnCondition(Class<GestorEntity> classe, String condition) throws Exception {
		return null;
	}
}
