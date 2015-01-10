package sk.rsc.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SqlCallable.
 *
 * @author Radovan Sninsky
 * @since 20.08.2012 23:32
 */
public class SqlCallable extends SqlParamCmd {

	private final String proc;

	public SqlCallable(Connection conn, boolean logSql, String proc) {
		super(conn, logSql);
		this.proc = proc;
	}

	public SqlCallable call(Object... params) {
		for (Object o : params) {
			fields.add(new Field("", o));
		}
		return this;
	}

	@Override
	public String toSql() {
		final StringBuilder sb = new StringBuilder();
		sb.append("call ").append(proc).append("(\n    ");
		int i=0;
		for (Field f : fields) {
			sb.append(f.toValueSql());
			if (++i<fields.size()) { sb.append(", "); }
		}
		sb.append(")\n");
		return sb.toString();
	}

	@Override
	public PreparedStatement toStmt() throws SQLException {
		CallableStatement stmt = conn.prepareCall(toSql());
		int j=0;
		for (Field f : fields) {
			if (!(f instanceof SqlField)) {
				f.setStmtValue(stmt, ++j);
			}
		}
		return stmt;
	}
}
