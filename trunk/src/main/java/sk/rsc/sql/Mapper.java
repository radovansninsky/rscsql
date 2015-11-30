package sk.rsc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Maps JDBC {@link ResultSet} to generics object.
 *
 * @author Radovan Sninsky
 * @since 2012-05-24 16:57
 * @see SqlSelect#list(Mapper) and simimlar for usage
 */
public abstract class Mapper<T> {

	protected abstract T toObject(ResultSet rs) throws SQLException;

	public Date toDate(java.sql.Date val) {
		return val != null ? new Date(val.getTime()) : null;
	}

	public Date toDate(java.sql.Timestamp val) {
		return val != null ? new Date(val.getTime()) : null;
	}
}
