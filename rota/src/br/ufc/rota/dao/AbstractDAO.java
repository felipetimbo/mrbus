package br.ufc.rota.dao;

public interface AbstractDAO {

	Object findById(Class<?> clazz, Long id);
	
}
