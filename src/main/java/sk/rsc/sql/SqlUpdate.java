package sk.rsc.sql;

import sk.rsc.sql.fields.Field;
import sk.rsc.sql.fields.SqlField;
import sk.rsc.sql.restrictions.Restriction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Sql command UPDATE table SET ... WHERE ....
 *
 * @author Radovan Sninsky
 * @since 2012-07-09 22:12
 */
public final class SqlUpdate extends SqlParamCmd {

  private final String table;
  private final List<Restriction> where = new ArrayList<Restriction>();

  public SqlUpdate(Connection conn, boolean logSql, boolean isMockMode, String table) {
    this(conn, logSql, isMockMode, null, table);
  }

  public SqlUpdate(Connection conn, boolean logSql, boolean isMockMode, String schema, String table) {
    super(conn, logSql, isMockMode);
    this.table = schemanizeTable(schema, table);
  }

  @Override
  public SqlUpdate set(String field, Object value) {
    super.set(field, value);
    return this;
  }

  @Override
  public SqlParamCmd setSql(String field, String value) {
    super.setSql(field, value);
    return this;
  }

  public SqlUpdate where(Restriction... restrictions) {
    this.where.addAll(Arrays.asList(restrictions));
    return this;
  }

  @Override
  public PreparedStatement toStmt() throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(toSql());
    int j = 0;
    for (Field f : fields) {
      if (!(f instanceof SqlField)) {
        f.setStmtValue(stmt, ++j);
      }
    }
    for (Restriction r : where) {
      if (r.hasValues()) {
        for (Object val : r.getValues()) {
          if (val instanceof Date) {
            stmt.setTimestamp(++j, new Timestamp(((Date) val).getTime()));
          } else {
            stmt.setObject(++j, val);
          }
        }
      }
    }
    return stmt;
  }

  @Override
  public String toSql() {
    final StringBuilder sb = new StringBuilder();
    sb.append("update ").append(table).append("\n  set\n    ");
    int i = 0;
    for (Field f : fields) {
      sb.append(f.getField()).append(" = ").append(f.toValueSql());
      if (++i < fields.size()) {
        sb.append(", ");
      }
    }
    sb.append("\n");
    if (!where.isEmpty()) {
      sb.append("  where\n");
      for (i = 0; i < where.size(); i++) {
        sb.append("    ").append(where.get(i).toSql()).append(i == where.size() - 1 ? "\n" : " and\n");
      }
    }
    return sb.toString();
  }
}
