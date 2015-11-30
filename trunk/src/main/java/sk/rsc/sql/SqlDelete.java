package sk.rsc.sql;

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
 * Sql command DELETE FROM table WHERE ....
 *
 * @author Radovan Sninsky
 * @since 2015-01-23 15:20
 */
public final class SqlDelete extends SqlCmd {

  private final String table;
  private final List<Restriction> where = new ArrayList<Restriction>();

  public SqlDelete(Connection conn, boolean logSql, boolean isMockMode, String table) {
    this(conn, logSql, isMockMode, null, table);
  }

  public SqlDelete(Connection conn, boolean logSql, boolean isMockMode, String schema, String table) {
    super(conn, logSql, isMockMode);
    this.table = schemanizeTable(schema, table);
  }

  public SqlDelete where(Restriction... restrictions) {
    this.where.addAll(Arrays.asList(restrictions));
    return this;
  }

  public void execute() throws SQLException {
    PreparedStatement pst = null;
    try {
      _log(false);
      if (!isMockMode) {
        pst = toStmt();
        pst.executeUpdate();
      }
    } finally {
      closeSilent(pst);
    }
  }

  @Override
  public String toSql() {
    final StringBuilder sb = new StringBuilder();
    sb.append("delete ").append(table);
    sb.append("\n");
    if (!where.isEmpty()) {
      sb.append("  where\n");
      for (int i = 0; i < where.size(); i++) {
        sb.append("    ").append(where.get(i).toSql()).append(i==where.size()-1?"\n":" and\n");
      }
    }
    return sb.toString();
  }

  @Override
  public PreparedStatement toStmt() throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(toSql());
    int j=0;
    for (Restriction r : where) {
      if (r.hasValues()) {
        for (Object val : r.getValues()) {
          if (val instanceof Date) {
            stmt.setTimestamp(++j, new Timestamp(((Date)val).getTime()));
          } else {
            stmt.setObject(++j, val);
          }
        }
      }
    }
    return stmt;
  }
}
