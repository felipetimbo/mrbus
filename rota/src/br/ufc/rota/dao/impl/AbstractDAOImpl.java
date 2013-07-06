package br.ufc.rota.dao.impl;

import javax.persistence.EntityManager;

import br.com.caelum.vraptor.ioc.Component;
import br.ufc.rota.dao.AbstractDAO;
import br.ufc.rota.factory.JPAFactory;

@Component
public class AbstractDAOImpl implements AbstractDAO{

	protected EntityManager em;
	
	public AbstractDAOImpl() {
		em = JPAFactory.getEntityManager();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object findById(Class clazz, Long id) {
		return em.find(clazz, id);
	}
}
