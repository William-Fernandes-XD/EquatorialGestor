package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;
import com.gestorcoi.utils.MensagensJSF;

/**
 * Classe responsável pelos metódos padrões de consulta ao banco de dados para supervisores
 * @author e21057649
 *
 */
@SuppressWarnings("unchecked")
@Component
public class SupervisorImpl implements AbstractMethods<Supervisor>, Serializable{
	
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
	
	public void salvar(Supervisor obj) {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}
	
	public Supervisor findById(Long id) {
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ");
		
		query.append(Supervisor.class.getSimpleName()).append(" entity where entity.id = :id");
		Supervisor supervisor = (Supervisor) sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("id", id).uniqueResult();
		
		return supervisor;
	}
	
	
	public Supervisor findByName(String name) {
		
		validateSessionFactory();
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ");
		
		query.append(Supervisor.class.getSimpleName()).append(" entity where entity.name = :name");
		List<Supervisor> supervisor = sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("name", name).list();
		
		if(supervisor.get(0) != null || !supervisor.isEmpty()) {
			return supervisor.get(0);
		}else {
			MensagensJSF.msgSeverityError("Esse supervisor não existe");
			return new Supervisor();
		}
	}

	@Override
	public void merge(Supervisor obj) throws Exception {
		
		validateSessionFactory();
        sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
	}
	
	public Supervisor merge2(Supervisor obj) throws Exception {
		
		validateSessionFactory();
        Supervisor supervisor = (Supervisor) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return supervisor;
	}

	@Override
	public void remove(Supervisor obj) throws Exception {
		
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
	
	@Override
	public List<Supervisor> findAll(Class<Supervisor> classe) throws Exception {
		
		 validateSessionFactory();
		
		 StringBuilder query = new StringBuilder();
		 query.append("select distinct(entity) from ");
		 query.append(Supervisor.class.getSimpleName());
		 query.append(" entity");
		 
		 List<Supervisor> supervisores = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		 
		 return supervisores;
	}

	@Override
	public List<Supervisor> findAllOnCondition(Class<Supervisor> classe, String condition) throws Exception {
		
		 StringBuilder query = new StringBuilder();
		 query.append("select distinct(entity) from ");
		 query.append(Supervisor.class.getSimpleName());
		 query.append(" entity where ").append(condition);
		 
  		 List<Supervisor> supervisores = sessionFactory.getCurrentSession().createQuery(query.toString()).list();
		 
		 return supervisores;
	}
	
	public boolean autentico(String login, String password) throws Exception{
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("Select count(1) as autentica from ")
		.append(Supervisor.class.getSimpleName())
		.append(" where name = :name and password = :password");
		
		Long count = (Long) sessionFactory.getCurrentSession().createQuery(builder.toString())
				.setParameter("name", login).setParameter("password", password)
				.uniqueResult();
		
		return count != null && count > 0;
	}
}
