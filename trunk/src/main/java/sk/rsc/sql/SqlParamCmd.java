package sk.rsc.sql;

import sk.rsc.sql.fields.Field;
import sk.rsc.sql.fields.SqlField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sql parametrized command.
 *
 * @author Radovan Sninsky
 * @since 1.0, 7/14/12 11:04 AM
 */
public abstract class SqlParamCmd extends SqlCmd {

	protected final List<Field> fields = new ArrayList<Field>();

	protected SqlParamCmd(Connection conn, boolean logSql) {
		super(conn, logSql);
	}

	public SqlParamCmd execute() throws SQLException {
		PreparedStatement pst = null;
		try {
			_log(false);
			pst = toStmt();
			pst.executeUpdate();
      return this;
		} finally {
			closeSilent(pst);
		}
	}

	public SqlParamCmd set(String field, Object value) {
		fields.add(new Field(field, value));
		return this;
	}

	public SqlParamCmd setSql(String field, String value) {
		fields.add(new SqlField(field, value));
		return this;
	}
}
