package com.gestorcoi.utils;

public enum EstatusPersistencia {

	ERRO("Erro"), SUCESSO("Sucesso"), OBJETO_REFERENCIADO("Esse objeto não pode ser apagado pois"
			+ " possui referências");
	
	private String name;
	
	private EstatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString() {
		return name.toString();
	}
}
