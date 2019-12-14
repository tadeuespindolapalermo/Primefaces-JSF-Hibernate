package br.com.tadeudeveloper.jpahibernate.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.tadeudeveloper.jpahibernate.util.HibernateUtil;

public class GenericDAO<T> {

	private EntityManager entityManager = HibernateUtil.geEntityManager();
	
	public void salvar(T entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();
	}
	
	public T updateMerge(T entidade) { // salva ou atualiza
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		T entidadeSalva = entityManager.merge(entidade);
		transaction.commit();
		return entidadeSalva;
	}
	
	public T pesquisar(T entity) {
		Object id = HibernateUtil.getPrimaryKey(entity);
		@SuppressWarnings("unchecked")
		T t = (T) entityManager.find(entity.getClass(), id);
		return t;
	}
	
	public T pesquisar(Long id, Class<T> entity) {
		entityManager.clear();
		T t = (T) entityManager.find(entity, id);
		//T t = (T) entityManager.createQuery("from " + entity.getSimpleName() + " where id = " + id).getSingleResult();
		return t;
	}
	
	public void deletarPorId(T entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.createNativeQuery(
				"delete from " + entidade.getClass().getSimpleName().toLowerCase() + 
				" where id = " + id).executeUpdate(); // delete
		transaction.commit(); // grava a alteração (exclusão) no banco
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listar(Class<T> entidade) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();		
		List<T> lista = entityManager.createQuery("from " + entidade.getName()).getResultList();
		transaction.commit();
		return lista;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
}
