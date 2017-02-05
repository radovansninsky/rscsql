package sk.rsc.sql;

import sk.rsc.sql.restrictions.Restriction;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Sql command SELECT ... FROM table JOIN ... WHERE ....
 *
 * @author Radovan Sninsky
 * @since 2012-05-16 22:58
 */
@SuppressWarnings("unused")
public final class SqlSelect<T> extends SqlCmd {

  private final List<String> columns = new ArrayList<String>();
  private String from = null;
  private SqlSelect<T> fromSelect = null;
  private final List<AbstractJoin> joins = new ArrayList<AbstractJoin>();
  private final List<Restriction> where = new ArrayList<Restriction>();
  private final List<String> group = new ArrayList<String>();
  private final List<Order> order = new ArrayList<Order>();

  SqlSelect(Connection conn, boolean logSql, boolean isMockMode, String... columns) {
    this(conn, logSql, isMockMode, columns != null ? Arrays.asList(columns) : null);
  }

  SqlSelect(Connection conn, boolean logSql, boolean isMockMode, List<String> columns) {
    super(conn, logSql, isMockMode);
		this.columns.addAll(columns == null || columns.size() == 0 ? Collections.singletonList("*") : columns);
  }

  public SqlSelect<T> add(String column) {
    this.columns.add(column);
    return this;
  }

  public SqlSelect<T> from(String from) {
    this.from = from;
    return this;
  }

  public SqlSelect<T> from(String schema, String from) {
    this.from = schemanizeTable(schema, from);
    return this;
  }

  public SqlSelect<T> from(SqlSelect<T> select, String as) {
    this.from = "(" + select.toSql() + ") " + as;
    this.fromSelect = select;
    return this;
  }

  public SqlSelect<T> join(String tab, String id1, String id2) {
    this.joins.add(new Join(tab, id1, id2));
    return this;
  }

  public SqlSelect<T> join(String schema, String tab, String id1, String id2) {
    this.joins.add(new Join(schemanizeTable(schema, tab), id1, id2));
    return this;
  }

  public SqlSelect<T> innerJoin(String tab, String id1, String id2) {
    this.joins.add(new InnerJoin(tab, id1, id2));
    return this;
  }

  public SqlSelect<T> innerJoin(String schema, String tab, String id1, String id2) {
    this.joins.add(new InnerJoin(schemanizeTable(schema, tab), id1, id2));
    return this;
  }

  public SqlSelect<T> leftJoin(String tab, String id1, String id2) {
    this.joins.add(new LeftJoin(tab, id1, id2));
    return this;
  }

  public SqlSelect<T> leftJoin(String schema, String tab, String id1, String id2) {
    this.joins.add(new LeftJoin(schemanizeTable(schema, tab), id1, id2));
    return this;
  }

  public SqlSelect<T> rightJoin(String tab, String id1, String id2) {
    this.joins.add(new RightJoin(tab, id1, id2));
    return this;
  }

  public SqlSelect<T> rightJoin(String schema, String tab, String id1, String id2) {
    this.joins.add(new RightJoin(schemanizeTable(schema, tab), id1, id2));
    return this;
  }

  public SqlSelect<T> where(Restriction... restrictions) {
    return where(restrictions != null ? Arrays.asList(restrictions) : null);
  }

  public SqlSelect<T> where(List<Restriction> restrictions) {
    this.where.addAll(restrictions != null ? restrictions : new ArrayList<Restriction>());
    return this;
  }

  public SqlSelect<T> order(Order... orders) {
    return order(orders != null ? Arrays.asList(orders) : null);
  }

  public SqlSelect<T> order(List<Order> orders) {
    this.order.addAll(orders != null ? orders : new ArrayList<Order>());
    return this;
  }

  public SqlSelect<T> group(String... fields) {
    this.group.addAll(Arrays.asList(fields));
    return this;
  }

  public T firstRow(Mapper<T> mapper) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
        mapper.init(rs = pst.executeQuery());
        return rs.next() ? mapper.toObject() : null;
      } else {
        return null;
      }
    } catch (RuntimeException e) {
      throw new SQLException("Mapping failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  public Row firstRow() throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
        rs = pst.executeQuery();
        return rs.next() ? toRow(rs) : Row.EMPTY;
      } else {
        return Row.EMPTY;
      }
    } catch (RuntimeException e) {
      throw new SQLException("Mapping failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  public List<T> list(Mapper<T> mapper) throws SQLException {
    return list(mapper, 0, Integer.MAX_VALUE);
  }

  public List<T> list(Mapper<T> mapper, int limit) throws SQLException {
    return list(mapper, 0, limit);
  }

  public List<T> list(Mapper<T> mapper, int offset, int limit) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
				mapper.init(rs = pst.executeQuery());

        List<T> list = new ArrayList<T>(limit < Integer.MAX_VALUE ? limit : 1000);
        int i = 0;
        while (rs.next() && i < offset + limit) {
          if (i++ >= offset) {
            list.add(mapper.toObject());
          }
        }
        return list;
      } else {
        return new ArrayList<T>();
      }
    } catch (RuntimeException e) {
      throw new SQLException("Mapping failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  public void iterate(Mapper<T> mapper, RowHandler<T> handler) throws SQLException {
    iterate(mapper, 0, Integer.MAX_VALUE, handler);
  }

  public void iterate(Mapper<T> mapper, int limit, RowHandler<T> handler) throws SQLException {
    iterate(mapper, 0, limit, handler);
  }

  @SuppressWarnings("SameParameterValue")
  public void iterate(Mapper<T> mapper, int offset, int limit, RowHandler<T> handler) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
        mapper.init(rs = pst.executeQuery());

        int i = 0;
        while (rs.next() && i < offset + limit) {
          if (i++ >= offset) {
            handler.handle(mapper.toObject());
          }
        }
      }
    } catch (RuntimeException e) {
      throw new SQLException("Iterating result set failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  public List<Row> list() throws SQLException {
    return list(0, Integer.MAX_VALUE);
  }

  public List<Row> list(int limit) throws SQLException {
    return list(0, limit);
  }

  public List<Row> list(int offset, int limit) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
        rs = pst.executeQuery();
        List<Row> list = new ArrayList<Row>(limit < Integer.MAX_VALUE ? limit : 1000); // todo tvrdy limit !!!
        int i = 0;
        while (rs.next() && i < limit) {
          if (i++ >= offset) {
            list.add(toRow(rs));
          }
        }
        return list;
      } else {
        return new ArrayList<Row>();
      }
    } catch (RuntimeException e) {
      throw new SQLException("Mapping failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  public void iterate(RowHandler<Row> handler) throws SQLException {
    iterate(0, Integer.MAX_VALUE, handler);
  }

  public void iterate(int limit, RowHandler<Row> handler) throws SQLException {
    iterate(0, limit, handler);
  }

  @SuppressWarnings("SameParameterValue")
  public void iterate(int offset, int limit, RowHandler<Row> handler) throws SQLException {
    PreparedStatement pst = null;
    ResultSet rs = null;
    try {
      _log();
      if (!isMockMode) {
        pst = toStmt();
        rs = pst.executeQuery();

        int i = 0;
        while (rs.next() && i < offset + limit) {
          if (i++ >= offset) {
            handler.handle(toRow(rs));
          }
        }
      }
    } catch (RuntimeException e) {
      throw new SQLException("Iterating result set failed", e);
    } finally {
      closeSilent(rs);
      closeSilent(pst);
    }
  }

  private Row toRow(ResultSet rs) throws SQLException {
    Row row = new Row();
    ResultSetMetaData rsmd = rs.getMetaData();
    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
      row.set(rsmd.getColumnLabel(i).toLowerCase(), rs.getObject(i));
    }
    return row;
  }

  public PreparedStatement toStmt() throws SQLException {
    PreparedStatement stmt = conn.prepareStatement(toSql());
    int j = fromSelect != null ? setUpStmtParams(stmt, fromSelect.where, 0) : 0;
		setUpStmtParams(stmt, where, j);
    return stmt;
  }

  private int setUpStmtParams(PreparedStatement stmt, List<Restriction> list, int startIdx) throws SQLException {
		int j = startIdx;
		for (Restriction r : list) {
			if (r.hasValues()) {
				for (Object val : r.getValues()) {
					if (val instanceof Date) {
						stmt.setTimestamp(++j, new Timestamp(((Date) val).getTime()));
					} else {
						if (val != null) {
							stmt.setObject(++j, val);
						} else {
							stmt.setNull(++j, Types.VARCHAR);
						}
					}
				}
			}
		}
		return j;
	}

  /**
   * Creates new copy of this select with all columns, joins and where restrictions.
   *
   * @return new copy of this select
   */
  @SuppressWarnings("unchecked")
  public SqlSelect toCopySelect() {
    SqlSelect s = new SqlSelect(conn, logSql, isMockMode, columns);
    s.from = this.from;
    s.joins.addAll(this.joins);
    s.where.addAll(this.where);
    return s;
  }

  /**
   * Creates {@code count(1)} version of this select with all joins and where restrictions.
   *
   * @return count version of this select
   */
  @SuppressWarnings("unchecked")
  public SqlSelect toCountSelect() {
    SqlSelect s = new SqlSelect(conn, logSql, isMockMode, "count(1) as cnt");
    s.from = this.from;
    s.joins.addAll(this.joins);
    s.where.addAll(this.where);
    return s;
  }

  /**
   * Creates new count select ({@link #toCountSelect()} and execute it to count rows returned by this select.
   *
   * @return count of returned by this select
   * @throws SQLException if execution of count select fails
   */
  @SuppressWarnings("ConstantConditions")
  public long countRows() throws SQLException {
    return isMockMode ? 0L : toCountSelect().firstRow().getLong("cnt");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toSql() {
    final StringBuilder sb = new StringBuilder();
    sb.append("select\n");
    for (int i = 0; i < columns.size(); i++) {
      sb.append("    ").append(columns.get(i)).append(i == columns.size() - 1 ? "\n" : ",\n");
    }
    if (from != null && !from.isEmpty()) {
      sb.append("  from\n    ").append(from).append("\n");
      if (!joins.isEmpty()) {
        for (AbstractJoin join : joins) {
          sb.append("      ").append(join.toSql()).append("\n");
        }
      }
    }
    if (!where.isEmpty()) {
      sb.append("  where\n");
      for (int i = 0; i < where.size(); i++) {
        sb.append("    ").append(where.get(i).toSql()).append(i == where.size() - 1 ? "\n" : " and\n");
      }
    }
    if (!group.isEmpty()) {
      sb.append("  group by\n");
      for (int i = 0; i < group.size(); i++) {
        sb.append("    ").append(group.get(i)).append(i == group.size() - 1 ? "\n" : ",\n");
      }
    }
    if (!order.isEmpty()) {
      sb.append("  order by\n");
      for (int i = 0; i < order.size(); i++) {
        sb.append("    ").append(order.get(i).toSql()).append(i == order.size() - 1 ? "\n" : ",\n");
      }
    }
    return sb.toString();
  }

  private abstract class AbstractJoin {
    String tab;
    String id1;
    String id2;

    private AbstractJoin(String tab, String id1, String id2) {
      this.tab = tab;
      this.id1 = id1;
      this.id2 = id2;
    }

		abstract String getClause();

    String toSql() {
      return getClause() + " " + tab + " on " + id1 + " = " + id2;
    }
  }

  private class Join extends AbstractJoin {
    private Join(String tab, String id1, String id2) {
      super(tab, id1, id2);
    }

    @Override
    String getClause() {
      return "join";
    }
  }

  private class InnerJoin extends AbstractJoin {
    private InnerJoin(String tab, String id1, String id2) {
      super(tab, id1, id2);
    }

    @Override
    String getClause() {
      return "inner join";
    }
  }

  private class LeftJoin extends AbstractJoin {
    private LeftJoin(String tab, String id1, String id2) {
      super(tab, id1, id2);
    }

    @Override
    String getClause() {
      return "left join";
    }
  }

  private class RightJoin extends AbstractJoin {
    private RightJoin(String tab, String id1, String id2) {
      super(tab, id1, id2);
    }

    @Override
    String getClause() {
      return "right join";
    }
  }
}
