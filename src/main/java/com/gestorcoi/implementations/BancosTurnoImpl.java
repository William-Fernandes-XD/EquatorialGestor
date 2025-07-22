package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.gestorcoi.entities.BancosTurno;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

public class BancosTurnoImpl implements AbstractMethods<BancosTurno>, Serializable{

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
	
	public void save(BancosTurno obj) throws Exception{
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}

	@Override
	public void merge(BancosTurno obj) throws Exception {
		
	}

	@Override
	public void remove(BancosTurno obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public List<BancosTurno> findAllById(Long id) throws Exception{
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
				
		query.append("select distinct(entity) from ").append(BancosTurno.class.getSimpleName())
		.append(" entity where funcionario_id = :id");
		
		@SuppressWarnings("unchecked")
		List<BancosTurno> retorno = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("id", id).list();
		
		return retorno;
	}

	@Override
	public List<BancosTurno> findAll(Class<BancosTurno> classe) throws Exception {
		
		return null;
	}

	@Override
	public List<BancosTurno> findAllOnCondition(Class<BancosTurno> classe, String condition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
