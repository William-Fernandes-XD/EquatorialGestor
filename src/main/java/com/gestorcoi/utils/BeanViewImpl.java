package com.gestorcoi.utils;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public abstract class BeanViewImpl implements ActionViewPadrao, Serializable{

	private static final long serialVersionUID = 1L;
	
	public BeanViewImpl() {

	}

	@Override
	public void addMsg(String msg) throws Exception {
		MensagensJSF.msg(msg);
	}
	
	public void sucesso() throws Exception{
		statusOperation(EstatusPersistencia.SUCESSO);
	}
	
	public void erro() throws Exception{
		statusOperation(EstatusPersistencia.ERRO);
	}
	
	@Override
	public String ativar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void consultarEntidade() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String editar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void excluir() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void limparLista() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String novo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String redirecionarNewEntidade() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String save() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void saveEdit() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setarVariaveisNulas() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void statusOperation(EstatusPersistencia a) throws Exception {
		MensagensJSF.responseOperation(a);
	}
}
