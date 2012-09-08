package lt.jug.vilnius.osgi.example.app.dao.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lt.jug.vilnius.osgi.example.app.dao.BaseDao;

public abstract class BaseDaoImpl<T, K extends Serializable> implements BaseDao<T, K> {

	@PersistenceContext
	protected EntityManager em;

	protected abstract Class<T> getEntityClass();
	
	@Override
	public T get(Serializable id) {
		return em.find(getEntityClass(), id);
	}

	@Override
	public T saveOrUpdate(T entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(T entity) {
		em.remove(entity);
	}
}
