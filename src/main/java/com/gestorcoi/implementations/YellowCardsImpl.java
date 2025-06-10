package com.gestorcoi.implementations;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.gestor.entityCards.YellowCard;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.interfaces.AbstractMethods;

@SuppressWarnings("unchecked")
@Repository
public class YellowCardsImpl implements AbstractMethods<YellowCard>{
	
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
	public void merge(YellowCard obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().merge(obj);
		executeFlush();
	}
	
	public List<YellowCard> findByRedCardId(Long redcard_id){
		
		validateSessionFactory();
		
		StringBuilder query = new StringBuilder();
		
		query.append("Select distinct(entity) from ")
		.append(YellowCard.class.getSimpleName())
		.append(" entity where entity.redCard.id = :id");
		
		List<YellowCard> yellowCards = sessionFactory.getCurrentSession()
				.createQuery(query.toString()).setParameter("id", redcard_id)
				.list();
		
		return yellowCards;
	}

	@Override
	public void remove(YellowCard obj) throws Exception {
		
		validateSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlush();
	}

	@Override
	public void executeQuery(String query) throws Exception {
		
	}

	@Override
	public List<YellowCard> findAll(Class<YellowCard> classe) throws Exception {
		
		validateSessionFactory();
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("select distinct(entity) from ").append(YellowCard.class.getSimpleName())
		.append(" entity");
		
		List<YellowCard> redCards = sessionFactory.getCurrentSession().createQuery(builder.toString()).list();
		
		return redCards;
	}

	@Override
	public List<YellowCard> findAllOnCondition(Class<YellowCard> classe, String condition) throws Exception {
		return null;
	}
}
