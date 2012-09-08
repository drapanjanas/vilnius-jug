package lt.jug.vilnius.osgi.example.app.dao;

import java.io.Serializable;

public interface BaseDao<T, K extends Serializable> {
	T get(K id);

	T saveOrUpdate(T entity);

	void delete(T entity);
}
