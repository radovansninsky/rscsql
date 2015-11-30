package sk.rsc.sql;

import sk.rsc.sql.fields.Field;
import sk.rsc.sql.fields.OutField;
import sk.rsc.sql.fields.SqlField;

import java.sql.*;

/**
 * Sql command INSERT INTO table (...) VALUES (...).
 *
 * @author Radovan Sninsky
 * @since 2012-06-29 11:55
 */
public final class SqlInsert extends SqlParamCmd {

  private String table;
	private OutField generatedKey = null;


	public SqlInsert(Connection conn, boolean logSql, boolean isMockMode, String table) {
    this(conn, logSql, isMockMode, null, table);
  }

  public SqlInsert(Connection conn, boolean logSql, boolean isMockMode, String schema, String table) {
    super(conn, logSql, isMockMode);
    this.table = schemanizeTable(schema, table);
  }

	public SqlInsert setGeneratedKey(String column, int type) {
		generatedKey = new OutField(column, type);
		return this;
	}

	public OutField getGeneratedKey() {
		return generatedKey;
	}

	public PreparedStatement toStmt() throws SQLException {
    PreparedStatement stmt =
			generatedKey != null ? conn.prepareStatement(toSql(), new String[]{generatedKey.getField()}) : conn.prepareStatement(toSql());
    int j = 0;
    for (Field f : fields) {
      if (!(f instanceof SqlField)) {
        f.setStmtValue(stmt, ++j);
      }
    }
    return stmt;
  }

  public String toSql() {
    final StringBuilder sb = new StringBuilder().append("insert into ").append(table).append("\n  (");
    int i = 0;
    for (Field f : fields) {
      sb.append(f.getField());
      if (++i < fields.size()) {
        sb.append(", ");
      }
    }
    sb.append(")\n  values\n  (");
    i = 0;
    for (Field f : fields) {
      sb.append(f.toValueSql());
      if (++i < fields.size()) {
        sb.append(", ");
      }
    }
    return sb.append(")").toString();
  }

	@Override
	public SqlInsert execute() throws SQLException {
		PreparedStatement pst = null;
		try {
			_log(false);
			if (!isMockMode) {
				pst = toStmt();
				pst.executeUpdate();

				if (generatedKey != null) {
					ResultSet rs = pst.getGeneratedKeys();
					if (rs != null && rs.next()) {
						generatedKey.setValue(rs.getObject(1));
					}
				}
			}
			return this;
		} finally {
			closeSilent(pst);
		}
	}
}
