package sk.rsc.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Mapper.
 *
 * @author Radovan Sninsky
 * @since 24.05.2012 16:57
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
