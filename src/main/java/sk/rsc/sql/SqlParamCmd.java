package sk.rsc.sql;

import sk.rsc.sql.fields.Field;
import sk.rsc.sql.fields.SqlField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Parametrized SQL command, parent INSERT, UPDATE and CALL.
 *
 * @author Radovan Sninsky
 * @since 2012-07-14 11:04
 */
public abstract class SqlParamCmd extends SqlCmd {

  protected final List<Field> fields = new ArrayList<Field>();

  protected SqlParamCmd(Connection conn, boolean logSql, boolean isMockMode) {
    super(conn, logSql, isMockMode);
  }

  public SqlParamCmd execute() throws SQLException {
    PreparedStatement pst = null;
    try {
      _log(false);
      if (!isMockMode  && fields.size() > 0) {
        pst = toStmt();
        pst.executeUpdate();
      }
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
