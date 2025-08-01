package com.gestorcoi.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.project.listeners.ContextLoaderListenerGestorcoiUtils;
import com.gestorcoi.utils.UtilFramework;

@WebFilter(filterName="conexaoFilter")
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory;
	
	@Override
	protected void initFilterBean() throws ServletException {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		BasicDataSource springDataSource = (BasicDataSource)
				ContextLoaderListenerGestorcoiUtils.getBean("springDataSource");
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springDataSource);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			
			if(!sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.openSession();
			}
			
			request.setCharacterEncoding("UTF-8");
			
			HttpServletRequest request2 = (HttpServletRequest) request;
			HttpSession session = request2.getSession();
			Supervisor userLogadoSessao = (Supervisor) session.getAttribute("userLogadoSessao");
			
			if(userLogadoSessao != null) {
				UtilFramework.geThreadLocal().set(userLogadoSessao.getId());
			}
			
			sessionFactory.getCurrentSession().beginTransaction();
			filterChain.doFilter(request, response);
			
			transactionManager.commit(status);
			
			if(sessionFactory.getCurrentSession().getTransaction().isActive()) {
				sessionFactory.getCurrentSession().flush();
				sessionFactory.getCurrentSession().getTransaction().commit();
			}
			
			if(sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.getCurrentSession().close();
			}
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			
		}catch(Exception e) {
			
			transactionManager.rollback(status);
			e.printStackTrace();
			
			if(sessionFactory.getCurrentSession().getTransaction().isActive()) {
				sessionFactory.getCurrentSession().getTransaction().rollback();
			}
			
			if(sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.getCurrentSession().close();
			}
			
		}finally {
			if(sessionFactory.getCurrentSession().isOpen()) {
				if(sessionFactory.getCurrentSession().beginTransaction().isActive()) {
					sessionFactory.getCurrentSession().flush();
					sessionFactory.getCurrentSession().clear();
				}
			}
			
			if(sessionFactory.getCurrentSession().isOpen()) {
				sessionFactory.getCurrentSession().close();
			}
		}
	}
}