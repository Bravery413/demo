package dao;

import core.dbutils.DBException;
import core.dbutils.DBManager;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;

public class BaseDao<T> {
	public T find(Long id, Integer isLock) throws DBException {
		Class<T> clz = getTClass();
		return DBManager.findById(clz, id , isLock);
	}

	public T find(Long id) throws DBException {
		Class<T> clz = getTClass();
		return DBManager.findById(clz, id);
	}

	public Long save(Connection conn, T obj) throws DBException {
		return DBManager.insert(conn, obj);
	}

	public Long save(T obj) throws DBException {
		return DBManager.insert(obj);
	}

	public int update(Connection conn, T obj) throws DBException {
		return DBManager.update(conn, obj);
	}

	public int update(T obj) throws DBException {
		return DBManager.update(obj);
	}

	public int delete(Long id) throws DBException {
		Class<T> clz = getTClass();
		return DBManager.delete(clz, id);
	}

    public int delete(Connection conn, Long id) throws DBException {
        Class<T> clz = getTClass();
        return DBManager.delete(conn, clz, id);
    }

	public int deleteLogical(Long id) throws DBException {
		Class<T> clz = getTClass();
		return DBManager.deleteLogical(clz, id);
	}

    public int deleteLogical(Connection conn, Long id) throws DBException {
        Class<T> clz = getTClass();
        return DBManager.deleteLogical(conn, clz, id);
    }

	private Class<T> getTClass() {
		return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
