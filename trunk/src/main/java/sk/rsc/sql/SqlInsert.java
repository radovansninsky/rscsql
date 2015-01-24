package sk.rsc.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sql Insert.
 *
 * @author Radovan Sninsky
 * @since 2012-06-29 11:55
 */
public final class SqlInsert extends SqlParamCmd {

	private String table;

	public SqlInsert(Connection conn, boolean logSql, String table) {
		super(conn, logSql);
		this.table = table;
	}

	public SqlInsert(Connection conn, boolean logSql, String schema, String table) {
		super(conn, logSql);
		this.table = schemanizeTable(schema, table);
	}

	public void execute() throws SQLException {
		PreparedStatement pst = null;
		try {
			_log(false);
			pst = toStmt();
			pst.executeUpdate();
		} finally {
			closeSilent(pst);
		}
	}

	public PreparedStatement toStmt() throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(toSql());
		int j=0;
		for (Field f : fields) {
			if (!(f instanceof SqlField)) {
				f.setStmtValue(stmt, ++j);
			}
		}
		return stmt;
	}

	public String toSql() {
		final StringBuilder sb = new StringBuilder().append("insert into ").append(table).append("\n  (");
		int i=0;
		for (Field f : fields) {
			sb.append(f.getField());
			if (++i<fields.size()) { sb.append(", "); }
		}
		sb.append(")\n  values\n  (");
		i=0;
		for (Field f : fields) {
			sb.append(f.toValueSql());
			if (++i<fields.size()) { sb.append(", "); }
		}
		return sb.append(")").toString();
	}
}
