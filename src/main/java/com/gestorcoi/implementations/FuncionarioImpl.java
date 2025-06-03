package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestorcoi.entities.Funcionarios;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class FuncionarioImpl implements AbstractMethods<Funcionarios>, Serializable{

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
	public void merge(Funcionarios obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}
	
	public Funcionarios merge2(Funcionarios obj) throws Exception {
		
		validateSessionFactory();
		Funcionarios funcionario = (Funcionarios) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return funcionario;
	}

	@Override
	public void remove(Funcionarios obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<Funcionarios> findAll(Class<Funcionarios> classe) throws Exception {
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(Funcionarios.class.getSimpleName())
		.append(" entity");
		
		List<Funcionarios> funcionarios = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		
		return funcionarios;
	}
	
	public Funcionarios findByName(String nome) throws Exception{
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(Funcionarios.class.getSimpleName())
		.append(" entity where nome = :nome");
		
		Funcionarios funcionario = (Funcionarios) sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("nome", nome).uniqueResult();
		
		return funcionario;
	}

	@Override
	public List<Funcionarios> findAllOnCondition(Class<Funcionarios> classe, String condition) throws Exception {
		return null;
	}

}
