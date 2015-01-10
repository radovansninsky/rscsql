package sk.rsc.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Sql Command.
 *
 * @author Radovan Sninsky
 * @since 2012-09-07 12:18
 */
public abstract class SqlCmd {

	protected final Connection conn;
	protected final boolean logSql;
	protected boolean sqlLogged = false;

	SqlCmd(Connection conn, boolean logSql) {
		this.conn = conn;
		this.logSql = logSql;
	}

	public SqlCmd log() {
		_log(true);
		return this;
	}

	protected void _log() {
		_log(false);
	}

  protected void _log(boolean important) {
    Sql.getLogger().log(important || logSql, toSql());
  }
/*
	protected void _log(boolean important) {
		if (!sqlLogged) {
			sqlLogged = true;
			if (Sql.isSoutLoggerEnabled()) {
				System.out.println("Executing SQL:\n"+toSql());
			} else {
				if (important || logSql || Sql.isSl4jInfoLevel()) {
					logger.info("Executing SQL:\n{}", toSql());
				} else if (Sql.isSl4jDebugLevel()) {
					logger.debug("Executing SQL:\n{}", toSql());
				} else {
					logger.trace("Executing SQL:\n{}", toSql());
				}
			}
		}
	}
*/

	public abstract String toSql();

	public abstract PreparedStatement toStmt() throws SQLException;

	protected void closeSilent(ResultSet rs) {
		if (rs != null) {
			try { rs.close(); } catch (SQLException e) { /* do nothing*/ }
		}
	}

	protected void closeSilent(PreparedStatement pst) {
		if (pst != null) {
			try { pst.close(); } catch (SQLException e) { /* do nothing*/ }
		}
	}
}
