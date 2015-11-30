package sk.rsc.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract SQL command, parent for all other SQL commands.
 *
 * @author Radovan Sninsky
 * @since 2012-09-07 12:18
 */
public abstract class SqlCmd {

  protected final Connection conn;
  protected final boolean logSql;
  protected final boolean isMockMode;

  protected static String schemanizeTable(String schema, String table) {
    return schema != null && !schema.isEmpty() ? schema + "." + table : table;
  }

  SqlCmd(Connection conn, boolean logSql, boolean isMockMode) {
    this.conn = conn;
    this.logSql = logSql;
    this.isMockMode = isMockMode;
  }

  public SqlCmd log() {
    _log(true);
    return this;
  }

  /**
   * Formats sql cmd object state to jdbc form of sql statement (with ? instead of values)
   * ready to use in prepared statement.
   *
   * @return sql command with question marked values
   */
  public abstract String toSql();

  public abstract PreparedStatement toStmt() throws SQLException;

  protected void _log() {
    _log(false);
  }

  protected void _log(boolean important) {
    Sql.getLogger().log(important || logSql, toSql());
  }

  protected void closeSilent(ResultSet rs) {
    if (rs != null) {
      try {
        rs.close();
      } catch (SQLException e) { /* do nothing*/ }
    }
  }

  protected void closeSilent(PreparedStatement pst) {
    if (pst != null) {
      try {
        pst.close();
      } catch (SQLException e) { /* do nothing*/ }
    }
  }
}
