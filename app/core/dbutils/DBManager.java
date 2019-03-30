package core.dbutils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mysql.jdbc.MysqlErrorNumbers;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import play.Logger;
import play.Play;
import utils.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Apache DBUtils + C3P0 + Custom Field Mapping with Annotations
public class DBManager {
	private static class ColumnData {
		public String columnName;
		public Object value;
	}

	private static ComboPooledDataSource cpds = new ComboPooledDataSource();
	private static QueryRunner run;

	static {
		String dbUrl = Play.configuration.getProperty("dbman.url", "");
		String dbDriver = Play.configuration.getProperty("dbman.driver", "");
		String dbUser = Play.configuration.getProperty("dbman.user", "");
		String dbPass = Play.configuration.getProperty("dbman.pass", "");
		int poolTimeout = StringUtils.parseInt(Play.configuration.getProperty("dbman.pool_timeout", ""), 300);
		int poolMaxSize = StringUtils.parseInt(Play.configuration.getProperty("dbman.pool_maxSize", ""), 30);
		int poolMinSize = StringUtils.parseInt(Play.configuration.getProperty("dbman.pool_minSize", ""), 10);

		cpds = new ComboPooledDataSource();
		try {
			cpds.setJdbcUrl(dbUrl);
			cpds.setDriverClass(dbDriver);
			cpds.setUser(dbUser);
			cpds.setPassword(dbPass);
			cpds.setMaxIdleTime(poolTimeout);
			cpds.setMaxPoolSize(poolMaxSize);
			cpds.setMinPoolSize(poolMinSize);
			cpds.setInitialPoolSize(poolMinSize);

			//debug
			//cpds.setDebugUnreturnedConnectionStackTraces(true);
			//cpds.setUnreturnedConnectionTimeout(10);

		} catch (Exception e) {
			Logger.error(e, e.getMessage());
		}

		run = new QueryRunner(cpds);
	}

	public static Connection getConn() throws DBException {
		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public static void closeConn(Connection conn) {
		if (conn != null) {
			DbUtils.closeQuietly(conn);
		}
	}

	public static <T> T findById(Class<T> clazz, Long id) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("select * from %s where id=?", tableName);
		return queryForObject(clazz, sql, id);
	}

	public static <T> T findById(Class<T> clazz, Long id, Integer isLock) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("select * from %s where id=? and is_lock=?", tableName);
		return queryForObject(clazz, sql, id, isLock);
	}

	public static <T> T queryForObjectWithParams(Class<T> clazz, String sql, Object[] params) throws DBException {
		T obj = null;
		try {
			obj = (T) run.query(sql, new BeanHandler(clazz,
					new BasicRowProcessor(new MyBeanProcessor(new AnnotationMatcher(clazz)))), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return obj;
	}

	public static <T> T queryForObject(Class<T> clazz, String sql, Object ...params) throws DBException {
		return queryForObjectWithParams(clazz, sql, params);
	}

	public static Map<String, Object> queryForMap(String sql, Object... params) throws DBException {
		return queryForMapWithParams(sql, params);
	}

	public static Map<String, Object> queryForMapWithParams(String sql, Object[] params) throws DBException {
		try {
			return run.query(sql, new MapHandler(), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public static List<Map<String, Object>> queryForMapList(String sql, Object... params) throws DBException {
		return queryForMapListWithParams(sql, params);
	}

	public static List<Map<String, Object>> queryForMapListWithParams(String sql, Object[] params) throws DBException {
		try {
			return run.query(sql, new MapListHandler(), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}

	public static <T> List<T> queryForListWithParams( Class<T> clazz, String sql, Object[] params) throws DBException {
		List<T> obj = null;
		try {
			obj = (List<T>) run.query(sql, new BeanListHandler(clazz,
					new BasicRowProcessor(new MyBeanProcessor(new AnnotationMatcher(clazz)))), params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return obj;
	}

	public static <T> List<T> queryForList( Class<T> clazz, String sql, Object ...params) throws DBException {
		return queryForListWithParams(clazz, sql, params);
	}

	public static int updateWithParams(String sql, Object[] params) throws DBException {
		Connection conn = getConn();
		if (conn == null) {
			throw new DBException("get db connection failed");
		}
		try {
			return updateWithParams(conn, sql, params);
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeConn(conn);
		}
	}

	public static int updateWithParams(Connection conn, String sql, Object[] params) throws DBException {
		int i = 0;
		try {
			i = run.update(conn, sql, params);
		} catch (SQLException e) {
			throw new DBException(e);
		}
		return i;
	}

	public static int update(Connection conn, String sql, Object ...params) throws DBException {
		return updateWithParams(conn, sql, params);
	}

	public static int update(String sql, Object ...params) throws DBException {
		return updateWithParams(sql, params);
	}

	public static Long insertWithParams(String sql, Object[] params) throws DBException {
		Connection conn = getConn();
		if (conn == null) {
			throw new DBException("get db connection failed");
		}
		try {
			return insertWithParams(conn, sql, params);
		} finally {
			closeConn(conn);
		}
	}

	public static Long insertWithParams(Connection conn, String sql, Object[] params) throws DBException {
		try {
			return run.insert(conn, sql, new ScalarHandler<Long>(), params);
		} catch (SQLException e) {
			if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
				throw new DBDuplicateKeyException(e);
			}
			throw new DBException(e);
		}
	}

	public static Long insert(String sql, Object ...params) throws DBException {
		return insertWithParams(sql, params);
	}

	public static <T> Long insert(T obj) throws DBException {
		Connection conn = getConn();
		if (conn == null) {
			throw new DBException("get db connection failed");
		}
		try {
			return insert(conn, obj);
		} finally {
			closeConn(conn);
		}
	}

	public static <T> Long insert(Connection conn, T obj) throws DBException {
		Class clazz = obj.getClass();
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();

		List<ColumnData> columnValues = getTableColumnValues(obj);
		if (columnValues == null || columnValues.isEmpty()) { // no data field?
			throw new IllegalStateException("No data fields for class " + clazz.getName());
		}
		// 去掉id和is_lock列
		Iterator<ColumnData> iter = columnValues.iterator();
		while (iter.hasNext()) {
			ColumnData columnData = iter.next();
			String columnName = columnData.columnName;
			if (columnName.equalsIgnoreCase("id") || columnName.equalsIgnoreCase("is_lock")) {
				iter.remove();
			}
		}
		if (columnValues.isEmpty()) {
			throw new IllegalStateException("No data fields for class except id and is_lock" + clazz.getName());
		}

		StringBuilder sql = new StringBuilder("insert into `").append(tableName).append("` (");
		for (int i=0; i<columnValues.size(); i++) {
			if (i > 0) {
				sql.append(",");
			}
			sql.append("`").append(columnValues.get(i).columnName).append("`");
		}
		sql.append(") values (");

		for (int i=0; i<columnValues.size(); i++) {
			if (i > 0) {
				sql.append(",");
			}
			sql.append("?");
		}
		sql.append(")");

		Object[] params = new Object[columnValues.size()];
		for (int i=0; i<columnValues.size(); i++) {
			params[i] = columnValues.get(i).value;
		}

		return insertWithParams(conn, sql.toString(), params);
	}

	public static <T> int update(Connection conn, T obj) throws DBException {
		Class clazz = obj.getClass();
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();

		List<ColumnData> columnValues = getTableColumnValues(obj);
		if (columnValues == null || columnValues.isEmpty()) { // no data field?
			//throw new IllegalStateException("No data fields for class " + clazz.getName());
			return 0;
		}

		Long id = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder("update `").append(tableName).append("` set ");
		Iterator<ColumnData> iter = columnValues.iterator();
		int fieldNum = 0;
		while (iter.hasNext()) {
			ColumnData columnData = iter.next();
			String columnName = columnData.columnName;
			if (columnName.equalsIgnoreCase("id")) {
				id = (Long) columnData.value;
				continue;
			}
			fieldNum++;
			if (fieldNum > 1) {
				sql.append(",");
			}
			sql.append("`").append(columnName).append("`=?");
			params.add(columnData.value);
		}
		if (fieldNum == 0 || id == null || id == 0) { // no update field
			return 0;
		}
		sql.append(" where id=?");
		params.add(id);

		return update(conn, sql.toString(), params.toArray());
	}

	public static <T> int update(T obj) throws DBException {
		Connection conn = getConn();
		if (conn == null) {
			throw new DBException("get db connection failed");
		}
		try {
			return update(conn, obj);
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			closeConn(conn);
		}
	}

	public static <T> int delete(Class<T> clazz, Long id) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("delete from %s where id=?", tableName);
		return delete(sql, id);
	}

	public static <T> int delete(Connection conn, Class<T> clazz, Long id) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("delete from %s where id=?", tableName);
		return delete(conn, sql, id);
	}

	public static <T> int deleteLogical(Class<T> clazz, Long id) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("udpate %s set is_lock=1 where id=?", tableName);
		return update(sql, id);
	}

	public static <T> int deleteLogical(Connection conn, Class<T> clazz, Long id) throws DBException {
		if (!clazz.isAnnotationPresent(Table.class)) {
			throw new IllegalStateException("PermCodes Annotation not found for class " + clazz.getName());
		}
		Table table = (Table)clazz.getAnnotation(Table.class);
		String tableName = table.name();
		String sql = String.format("udpate %s set is_lock=1 where id=?", tableName);
		return update(conn, sql, id);
	}

	public static int delete(String sql, Object ...params) throws DBException {
		return update(sql, params);
	}

	public static int delete(Connection conn, String sql, Object ...params) throws DBException {
		return update(conn, sql, params);
	}

	private static <T> List<ColumnData> getTableColumnValues(T obj) {
		List<ColumnData> columnDatas = new ArrayList<ColumnData>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				String columnName = column.name();
				try {
					PropertyDescriptor pd = new PropertyDescriptor(field.getName(), obj.getClass());
					Method getMethod = pd.getReadMethod();
					Object value = getMethod.invoke(obj);
					if (value == null) { // null认为column是允许null的, 使用默认值
						continue;
					}

					ColumnData columnData = new ColumnData();
					columnData.columnName = columnName;
					columnData.value = value;
					columnDatas.add(columnData);
				} catch (Exception e) {
					Logger.error(e, e.getMessage());
					continue;
				}
			}
		}
		return columnDatas;
	}

}
