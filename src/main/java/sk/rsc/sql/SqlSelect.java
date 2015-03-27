package sk.rsc.sql;

import sk.rsc.sql.restrictions.Restriction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Sql select.
 *
 * @author Radovan Sninsky
 * @since 2012-05-16 22:58
 */
public final class SqlSelect<T> extends SqlCmd {

	private final List<String> columns = new ArrayList<String>();
	private String from = null;
	private final List<AbstractJoin> joins = new ArrayList<AbstractJoin>();
	private final List<Restriction> where = new ArrayList<Restriction>();
	private final List<String> group = new ArrayList<String>();
	private final List<Order> order = new ArrayList<Order>();

	SqlSelect(Connection conn, boolean logSql, String... columns) {
		super(conn, logSql);
		this.columns.addAll(Arrays.asList(columns));
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

	public SqlSelect<T> join(String tab, String id1, String id2) {
		this.joins.add(new Join(tab, id1, id2));
		return this;
	}

	public SqlSelect<T> innerJoin(String tab, String id1, String id2) {
		this.joins.add(new InnerJoin(tab, id1, id2));
		return this;
	}

	public SqlSelect<T> leftJoin(String tab, String id1, String id2) {
		this.joins.add(new LeftJoin(tab, id1, id2));
		return this;
	}

	public SqlSelect<T> rightJoin(String tab, String id1, String id2) {
		this.joins.add(new RightJoin(tab, id1, id2));
		return this;
	}

	public SqlSelect<T> where(Restriction... restrictions) {
		this.where.addAll(Arrays.asList(restrictions));
		return this;
	}

	public SqlSelect<T> order(Order... orders) {
		this.order.addAll(Arrays.asList(orders));
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
			pst = toStmt();
			rs = pst.executeQuery();
			return rs.next() ? mapper.toObject(rs) : null;
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
			pst = toStmt();
			rs = pst.executeQuery();
			return rs.next() ? toRow(rs) : Row.EMPTY;
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
			pst = toStmt();
			rs = pst.executeQuery();

			List<T> list = new ArrayList<T>(limit<Integer.MAX_VALUE?limit:1000);
			int i = 0;
			while (rs.next() && i<offset+limit) {
				if (i++>=offset) {
					list.add(mapper.toObject(rs));
				}
			}
			return list;
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

	public void iterate(Mapper<T> mapper, int offset, int limit, RowHandler<T> handler) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			_log();
			pst = toStmt();
			rs = pst.executeQuery();

			int i = 0;
			while (rs.next() && i<offset+limit) {
				if (i++>=offset) {
					handler.handle(mapper.toObject(rs));
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
			pst = toStmt();
			rs = pst.executeQuery();
			List<Row> list = new ArrayList<Row>(limit<Integer.MAX_VALUE?limit:1000);
			int i = 0;
			while (rs.next() && i<limit) {
				if (i++>=offset) {
					list.add(toRow(rs));
				}
			}
			return list;
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
	public void iterate(int offset, int limit, RowHandler<Row> handler) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			_log();
			pst = toStmt();
			rs = pst.executeQuery();

			int i = 0;
			while (rs.next() && i<offset+limit) {
				if (i++>=offset) {
					handler.handle(toRow(rs));
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
		for (int i=1; i<=rsmd.getColumnCount(); i++) {
			row.set(rsmd.getColumnLabel(i).toLowerCase(), rs.getObject(i));
		}
		return row;
	}

	public PreparedStatement toStmt() throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(toSql());
		int j=0;
		for (Restriction r : where) {
			if (r.hasValues()) {
				for (Object val : r.getValues()) {
					if (val instanceof Date) {
						stmt.setTimestamp(++j, new Timestamp(((Date)val).getTime()));
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
		return stmt;
	}

	public SqlSelect toCountSelect() throws SQLException {
		SqlSelect s = new SqlSelect(conn, logSql, "count(1) as cnt");
		s.from = this.from;
		s.joins.addAll(this.joins);
		s.where.addAll(this.where);
		return s;
	}

	public long countRows() throws SQLException {
		return toCountSelect().firstRow().getLong("cnt");
	}

  /**
   * {@inheritDoc}
   */
  @Override
	public String toSql() {
		final StringBuilder sb = new StringBuilder();
		sb.append("select\n");
		for (int i = 0; i < columns.size(); i++) {
			sb.append("    ").append(columns.get(i)).append(i==columns.size()-1?"\n":",\n");
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
				sb.append("    ").append(where.get(i).toSql()).append(i==where.size()-1?"\n":" and\n");
			}
		}
		if (!group.isEmpty()) {
			sb.append("  group by\n");
			for (int i = 0; i < group.size(); i++) {
				sb.append("    ").append(group.get(i)).append(i==group.size()-1?"\n":",\n");
			}
		}
		if (!order.isEmpty()) {
			sb.append("  order by\n");
			for (int i = 0; i < order.size(); i++) {
				sb.append("    ").append(order.get(i).toSql()).append(i==order.size()-1?"\n":",\n");
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

		String toSql() { return getClause()+" "+tab+" on "+id1+" = "+id2; }
	}

	private class Join extends AbstractJoin {
		private Join(String tab, String id1, String id2) {
			super(tab, id1, id2);
		}

		@Override String getClause() { return "join"; }
	}

	private class InnerJoin extends AbstractJoin {
		private InnerJoin(String tab, String id1, String id2) {
			super(tab, id1, id2);
		}

		@Override String getClause() { return "inner join"; }
	}

	private class LeftJoin extends AbstractJoin {
		private LeftJoin(String tab, String id1, String id2) {
			super(tab, id1, id2);
		}

		@Override String getClause() { return "left join"; }
	}

	private class RightJoin extends AbstractJoin {
		private RightJoin(String tab, String id1, String id2) {
			super(tab, id1, id2);
		}

		@Override String getClause() { return "right join"; }
	}
}
