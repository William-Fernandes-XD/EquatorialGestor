package com.gestorcoi.implementations;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestorcoi.entities.Feedback;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class FeedbackImpl implements AbstractMethods<Feedback>, Serializable{

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
	public void merge(Feedback obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}
	
	public Feedback merge2(Feedback obj) throws Exception {
		
		validateSessionFactory();
		Feedback feedback = (Feedback) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return feedback;
	}

	@Override
	public void remove(Feedback obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public List<Feedback> findAllByNameFuncionario(Long funcionario_id) throws Exception{
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(Feedback.class.getSimpleName())
		.append(" entity where entity.funcionario.id = :id");
		
		List<Feedback> feedbacksById = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("id", funcionario_id).list();
		
		return feedbacksById;
	}
	
	public List<Feedback> findAllByNameFuncionario2(String nome_funcionario) throws Exception{
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(Feedback.class.getSimpleName())
		.append(" entity where entity.funcionario.nome = :nome");
		
		List<Feedback> feedbacksById = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("nome", nome_funcionario).list();
		
		return feedbacksById;
	}
	
	@Override
	public List<Feedback> findAll(Class<Feedback> classe) throws Exception {
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ").append(Feedback.class.getSimpleName())
		.append(" entity where entity.data >= :data");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -30);
		
		List<Feedback> feedbacks = sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("data", cal.getTime()).setMaxResults(20).list();
		
		return feedbacks;
	}

	@Override
	public List<Feedback> findAllOnCondition(Class<Feedback> classe, String condition) throws Exception {
		return null;
	}

}
