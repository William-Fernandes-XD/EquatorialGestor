package com.gestorcoi.implementations;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestor.entityCards.RedCard;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class RedCardImpl implements AbstractMethods<RedCard>{
	
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
	
	public RedCard findRedCardById(Long id) throws Exception{
		
		StringBuilder query = new StringBuilder();
		
		query.append("select distinct(entity) from ")
		.append(RedCard.class.getSimpleName()).append(" entity")
		.append(" where id = :id");
		
		RedCard redCard = (RedCard) sessionFactory.getCurrentSession().createQuery(query.toString())
				.setParameter("id", id).uniqueResult();
		
		return redCard;
	}
	
	@Override
	public void merge(RedCard obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlush();
	}
	
	public RedCard merge2(RedCard obj) throws Exception {
		
		validateSessionFactory();
		RedCard redCard = (RedCard) sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
		
		return redCard;
	}

	@Override
	public void remove(RedCard obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<RedCard> findAll(Class<RedCard> classe) throws Exception {
		
		validateSessionFactory();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("select distinct(entity) from ").append(RedCard.class.getSimpleName())
		.append(" entity");
		
		List<RedCard> redCards = sessionFactory.getCurrentSession().createQuery(builder.toString()).list();
		
		return redCards;
	}

	@Override
	public List<RedCard> findAllOnCondition(Class<RedCard> classe, String condition) throws Exception {
		return null;
	}
}
