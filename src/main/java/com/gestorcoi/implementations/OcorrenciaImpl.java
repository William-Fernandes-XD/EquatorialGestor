package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.gestorcoi.entities.Ocorrencia;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

/**
 * 
 * Classe responsável pelos metódos de consulta ao banco de dados de ocorrencias
 * @author e21057649
 *
 */
@SuppressWarnings("unchecked")
@Component
public class OcorrenciaImpl implements AbstractMethods<Ocorrencia>, Serializable{

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
	public void merge(Ocorrencia obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}

	@Override
	public void remove(Ocorrencia obj) throws Exception {
		
		validateSessionFactory();
	    sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().createQuery(query);
		executeFlush();
	}
	
	public List<Ocorrencia> findAllByGestor(Long gestor_id){
		
		StringBuilder query = new StringBuilder();
		query.append("select distinct entity from ");
		query.append(Ocorrencia.class.getSimpleName());
		query.append(" entity where entity.gestorEntity.id = :id");
		 
		List<Ocorrencia> Ocorrencias = sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("id", gestor_id).list();
		 
		return Ocorrencias;
	}

	@Override
	public List<Ocorrencia> findAll(Class<Ocorrencia> classe) throws Exception {
		
		StringBuilder query = new StringBuilder();
		query.append("select distinct(entity) from ");
		query.append(Ocorrencia.class.getSimpleName());
		query.append(" entity");
		 
		List<Ocorrencia> Ocorrencias = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		 
		return Ocorrencias;
	}

	@Override
	public List<Ocorrencia> findAllOnCondition(Class<Ocorrencia> classe, String condition) throws Exception {
		
		StringBuilder query = new StringBuilder();
		query.append("select distinct(entity) from ");
		query.append(Ocorrencia.class.getSimpleName());
		query.append(" entity where ").append(condition);
		 
		List<Ocorrencia> Ocorrencias = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		 
		return Ocorrencias;
	}

}
