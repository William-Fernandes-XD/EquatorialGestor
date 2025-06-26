package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestorcoi.entities.RegistroAusencia;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@Repository
@SuppressWarnings("unchecked")
public class RegistrarAusenciaImpl implements AbstractMethods<RegistroAusencia>, Serializable{

	
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
	public void merge(RegistroAusencia obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}
	
	public RegistroAusencia merge2(RegistroAusencia obj) throws Exception {
		
		validateSessionFactory();
		RegistroAusencia registroAusencia = (RegistroAusencia) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return registroAusencia;
	}

	@Override
	public void remove(RegistroAusencia obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}
	
	public List<RegistroAusencia> findAllByFuncionario(Long funcionario_id) throws Exception{
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(RegistroAusencia.class.getSimpleName())
		.append(" entity where entity.funcionarios.id = :id");
		
		List<RegistroAusencia> ausencias = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("id", funcionario_id).list();
		
		return ausencias;
	}
	
	public List<RegistroAusencia> findAllByNameFuncionario(String funcionario_name) throws Exception{
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(RegistroAusencia.class.getSimpleName())
		.append(" entity where entity.funcionarios.nome = :nome");
		
		List<RegistroAusencia> ausencias = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("nome", funcionario_name).list();
		
		return ausencias;
	}

	@Override
	public List<RegistroAusencia> findAll(Class<RegistroAusencia> classe) throws Exception {
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(RegistroAusencia.class.getSimpleName())
		.append(" entity where entity.data_ausencia >= :datalimite");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -90);
		
		List<RegistroAusencia> ausencias = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("datalimite", cal.getTime()).setMaxResults(40).list();
		
		return ausencias;
	}

	@Override
	public List<RegistroAusencia> findAllOnCondition(Class<RegistroAusencia> classe, String condition)
			throws Exception {
		return null;
	}

}
