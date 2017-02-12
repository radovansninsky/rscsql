package sk.rsc.sql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Maps JDBC {@link ResultSet} to pojo.
 *
 * @author Radovan Sninsky
 * @since 2012-05-24 16:57
 * @see SqlSelect#list(Mapper) and simimlar for usage
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class Mapper<T> {

	protected ResultSet rs;
	protected List<String> fields = new ArrayList<String>();

	public void init(ResultSet rs) throws SQLException {
		this.rs = rs;
		this.fields.clear();

		ResultSetMetaData mrset = rs.getMetaData();
		for (int i=1; i<=mrset.getColumnCount(); i++) {
			fields.add(mrset.getColumnLabel(i).toLowerCase());
		}
	}

	protected abstract T toObject() throws SQLException;

	protected String get(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getString(name) : null;
	}

	protected String getString(String name) throws SQLException {
		return get(name);
	}

	protected Integer getInt(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getObject(name) != null ? rs.getInt(name) : null : null;
	}

	protected Long getLong(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getObject(name) != null ? rs.getLong(name) : null : null;
	}

	protected Boolean getBoolean(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getObject(name) != null ? rs.getBoolean(name) : null : null;
	}

	protected Double getDouble(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getObject(name) != null ? rs.getDouble(name) : null : null;
	}

	protected BigDecimal getBigDecimal(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) && rs.getObject(name) != null ? rs.getBigDecimal(name) : null ;
	}

	protected Date getDate(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? toDate(rs.getDate(name)) : null;
	}

	protected Date getTimestamp(String name) throws SQLException {
    assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? toDate(rs.getTimestamp(name)) : null;
	}

  protected <E extends Enum<E>> E getEnum(String name, Class<E> enumType) throws SQLException {
    assert name != null : "Field name is null";

    Map<String, E> map = new HashMap<String, E>(enumType.getEnumConstants().length);
    for (E e : enumType.getEnumConstants()) {
      map.put(e.name(), e);
    }
    return fields.contains(name.toLowerCase()) && map.keySet().contains(rs.getString(name)) ? map.get(rs.getString(name)) : null;
  }

  public Date toDate(java.sql.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}

	public Date toDate(java.sql.Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
