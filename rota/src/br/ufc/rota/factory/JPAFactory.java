package br.ufc.rota.factory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAFactory {
	
	private static EntityManagerFactory factory;
	
	private JPAFactory() {};
	
	public static EntityManager getEntityManager() {
		if(factory == null) {
			factory = Persistence.createEntityManagerFactory("rota");
		}
		return factory.createEntityManager();
	}	

}
