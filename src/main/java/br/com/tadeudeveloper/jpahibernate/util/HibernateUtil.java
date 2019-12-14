package br.com.tadeudeveloper.jpahibernate.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
	
	public static EntityManagerFactory factory = null;
	
	static {
		init();
	}
	
	private static void init() {
		try {
			if(factory == null) {
				factory = Persistence.createEntityManagerFactory("jpa-hibernate");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static EntityManager geEntityManager() {
		return factory.createEntityManager(); /* Provê persistência*/
	}
	
	public static Object getPrimaryKey(Object entity) { // Retorna a Primary Key
		return factory.getPersistenceUnitUtil().getIdentifier(entity);
	}

}
