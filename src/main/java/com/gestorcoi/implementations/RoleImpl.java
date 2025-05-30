package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;

import com.gestorcoi.entities.Roles;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

public class RoleImpl implements AbstractMethods<Roles>, Serializable{

	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	
	private void validateSessionFactory() {
		
		if(sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}
	
	private void executeFlush() {
		sessionFactory.getCurrentSession().flush();
	}
		
	public void save(Roles role) {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(role);
		executeFlush();
	}

	public Roles merge2(Roles obj) throws Exception {
		
		validateSessionFactory();
		Roles roles = (Roles) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		return roles;
	}
	

	@Override
	public void remove(Roles obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Roles> findAll(Class<Roles> classe) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Roles> findAllOnCondition(Class<Roles> classe, String condition) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void merge(Roles obj) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
