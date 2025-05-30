package com.gestorcoi.interfaces;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface AbstractMethods<T> {

	void merge(T obj) throws Exception;
	void remove(T obj) throws Exception;
	void executeQuery(String query) throws Exception;
	List<T> findAll(Class<T> classe) throws Exception;
	List<T> findAllOnCondition(Class<T> classe, String condition) throws Exception;
}
