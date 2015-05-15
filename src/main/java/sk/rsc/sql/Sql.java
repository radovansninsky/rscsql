package sk.rsc.sql;

import sk.rsc.sql.logging.Logger;
import sk.rsc.sql.logging.NoLogger;
import sk.rsc.sql.logging.SoutLogger;

import java.sql.Connection;

/**
 * Sql.
 *
 * @author Radovan Sninsky
 * @since 2012-05-16 22:09
 */
@SuppressWarnings("UnusedDeclaration")
public final class Sql<T> {

  private static Logger logger = new NoLogger();

  private static String schema = null;
  private static boolean isMockMode = false;

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

  /**
   * Sets {@link SoutLogger} as logger for library.
   */
  public static void setSoutLogger() {
    logger = new SoutLogger();
  }

  /**
   * Sets custom {@link Logger} as logger for library.
   * <br/>
   * <pre>
   *   Sql.setCustomLogger(new Logger() {
   *     void log(boolean important, String message) {
   *       // custom implementation
   *     }
   *   });
   * </pre>
   *
   * @param customLogger instance of custom logger implementation
   */
  public static void setCustomLogger(Logger customLogger) {
    logger = customLogger;
  }

  public static void setMockMode(boolean isMockMode) {
    Sql.isMockMode = isMockMode;
  }

  public static void enableMockMode() {
    setMockMode(true);
  }

  public static void setSchema(String schema) {
    Sql.schema = schema;
  }

  public SqlSelect<T> select(String... columns) {
    return new SqlSelect<T>(conn, logSql, Sql.isMockMode, columns);
  }

  public SqlInsert insert(String table) {
    return insert(Sql.schema, table);
  }

  public SqlInsert insert(String schema, String table) {
    return new SqlInsert(conn, logSql, Sql.isMockMode, schema, table);
  }

  public SqlUpdate update(String table) {
    return update(Sql.schema, table);
  }

  public SqlUpdate update(String schema, String table) {
    return new SqlUpdate(conn, logSql, Sql.isMockMode, schema, table);
  }

  public SqlDelete delete(String table) {
    return delete(Sql.schema, table);
  }

  public SqlDelete delete(String schema, String table) {
    return new SqlDelete(conn, logSql, Sql.isMockMode, schema, table);
  }

  public SqlCallable call(String callable) {
    return new SqlCallable(conn, logSql, Sql.isMockMode, callable);
  }

  public SqlCallable call(String callable, int retType) {
    return new SqlCallable(conn, logSql, Sql.isMockMode, callable, retType);
  }

  public SqlCallable call(String callable, Object... params) {
    return new SqlCallable(conn, logSql, Sql.isMockMode, callable).addParams(params);
  }

  public SqlCallable call(String callable, int retType, Object... params) {
    return new SqlCallable(conn, logSql, Sql.isMockMode, callable, retType).addParams(params);
  }
}
