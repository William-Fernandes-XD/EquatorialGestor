package com.gestorcoi.project.filter;

import java.io.IOException;

import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.gestorcoi.entities.Supervisor;
import com.gestorcoi.hibernate.HibernateUtil;
import com.gestorcoi.utils.UtilFramework;

@WebFilter(filterName="conexaoFilter")
public class FilterOpenSessionInView implements Serializable, Filter{

	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		sessionFactory = HibernateUtil.getSessionFactory();
	}	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		DataSource springDataSource = null;
		try {
		    InitialContext ctx = new InitialContext();
		    springDataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource");
		} catch (NamingException e) {
		    e.printStackTrace();
		    throw new ServletException("Datasource JNDI lookup failed", e);
		}
		
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
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}