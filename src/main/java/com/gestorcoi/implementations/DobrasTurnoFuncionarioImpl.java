package com.gestorcoi.implementations;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;

import com.gestorcoi.entities.DobraTurnoFuncionario;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

public class DobrasTurnoFuncionarioImpl implements Serializable, AbstractMethods<DobraTurnoFuncionario>{

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
	public void merge(DobraTurnoFuncionario obj) throws Exception {
		
		validateSessionFactory();
		
	}
	
	public DobraTurnoFuncionario merge2(DobraTurnoFuncionario obj) throws Exception {
		
		validateSessionFactory();
		DobraTurnoFuncionario dobraTurnoFuncionario = (DobraTurnoFuncionario) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return dobraTurnoFuncionario;
	}

	@Override
	public void remove(DobraTurnoFuncionario obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unchecked")
	public List<DobraTurnoFuncionario> findAllGestoriaTurnos(LocalDate dias) throws Exception {
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(DobraTurnoFuncionario.class.getSimpleName())
		.append(" entity where datadobra >= :dataLimite");
		
		
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(java.sql.Date.valueOf(dias));
		calendar.add(Calendar.DAY_OF_YEAR, -10);
		
		
		return sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("dataLimite", calendar.getTime()).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DobraTurnoFuncionario> findAll(Class<DobraTurnoFuncionario> classe) throws Exception {
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(DobraTurnoFuncionario.class.getSimpleName())
		.append(" entity where datadobra >= :dataLimite");
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -45);
		
		
		return sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("dataLimite", calendar.getTime()).list();
	}

	@Override
	public List<DobraTurnoFuncionario> findAllOnCondition(Class<DobraTurnoFuncionario> classe, String condition)
			throws Exception {
		return null;
	}
	
	
}
