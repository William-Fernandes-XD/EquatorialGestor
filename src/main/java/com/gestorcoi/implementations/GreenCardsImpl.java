package com.gestorcoi.implementations;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestor.entityCards.GreenCard;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class GreenCardsImpl implements AbstractMethods<GreenCard>{
	
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
	public void merge(GreenCard obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
	}

	@Override
	public void remove(GreenCard obj) throws Exception {
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("UPDATE greencard SET redcard_id = null")
		.append(" where id = :id");
		
		sessionFactory.getCurrentSession().createSQLQuery(query.toString())
		.setParameter("id", obj.getId()).executeUpdate();
		
		query = new StringBuilder();
		
		query.append("DELETE FROM greencard where id = :id");
		
		sessionFactory.getCurrentSession().createSQLQuery(query.toString())
		.setParameter("id", obj.getId()).executeUpdate();
		
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<GreenCard> findAll(Class<GreenCard> classe) throws Exception {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("select distinct(entity) from ").append(GreenCard.class.getSimpleName())
		.append(" entity");
		
		List<GreenCard> redCards = sessionFactory.getCurrentSession().createQuery(builder.toString()).list();
		
		return redCards;
	}

	@Override
	public List<GreenCard> findAllOnCondition(Class<GreenCard> classe, String condition) throws Exception {
		return null;
	}
}
