package sk.rsc.sql;

import sk.rsc.sql.logging.Logger;
import sk.rsc.sql.logging.NullLogger;
import sk.rsc.sql.logging.SoutLogger;

import java.sql.Connection;

/**
 * Sql.
 *
 * @author Radovan Sninsky
 * @since 2012-05-16 22:09
 */
@SuppressWarnings("UnusedDeclaration")
public class Sql<T> {

  private static Logger logger = new NullLogger();

  private final Connection conn;
  private final boolean logSql;

  public Sql(Connection conn) {
    this(conn, false);
  }

  public Sql(Connection conn, boolean logSql) {
    this.conn = conn;
    this.logSql = logSql;
  }

  static Logger getLogger() {
    return logger;
  }

  public static void setSoutLogger() {
    logger = new SoutLogger();
  }

  public static void setCustomLogger(Logger customLogger) {
    logger = customLogger;
  }

  public SqlSelect<T> select(String... columns) {
    return new SqlSelect<T>(conn, logSql, columns);
  }

  public SqlInsert insert(String table) {
    return new SqlInsert(conn, logSql, table);
  }

  public SqlUpdate update(String table) {
    return new SqlUpdate(conn, logSql, table);
  }

  public SqlCallable call(String callable, Object... params) {
    return new SqlCallable(conn, logSql, callable).call(params);
  }
}
