package sk.rsc.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps JDBC {@link ResultSet} to generics object.
 *
 * @author Radovan Sninsky
 * @since 2012-05-24 16:57
 * @see SqlSelect#list(Mapper) and simimlar for usage
 */
@SuppressWarnings("unused")
public abstract class Mapper<T> {

	private ResultSet rs;
	private List<String> fields = new ArrayList<String>();

	public void init(ResultSet rs) throws SQLException {
		this.rs = rs;

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

	protected Double getDouble(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? rs.getObject(name) != null ? rs.getDouble(name) : null : null;
	}

	protected Date getDate(String name) throws SQLException {
		assert name != null : "Field name is null";
		return fields.contains(name.toLowerCase()) ? toDate(rs.getDate(name)) : null;
	}

	protected Date getTimestamp(String name) throws SQLException {
		return fields.contains(name.toLowerCase()) ? toDate(rs.getTimestamp(name)) : null;
	}

	public Date toDate(java.sql.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}

	public Date toDate(java.sql.Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
