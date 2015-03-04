package sk.rsc.sql;

import sk.rsc.sql.fields.Field;
import sk.rsc.sql.fields.InOutField;
import sk.rsc.sql.fields.OutField;
import sk.rsc.sql.fields.SqlField;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SqlCallable.
 *
 * @author Radovan Sninsky
 * @since 20.08.2012 23:32
 */
public final class SqlCallable extends SqlParamCmd {

  private static final String RET_FIELD_NAME = "_$ret_";

  private final String proc;
	private final OutField retField;
  private final Map<String, OutField> outFileds = new HashMap<String, OutField>();

	public SqlCallable(Connection conn, boolean logSql, String proc) {
    this(conn, logSql, proc, null);
	}

	public SqlCallable(Connection conn, boolean logSql, String proc, Integer retType) {
		super(conn, logSql);
		this.proc = proc;
    this.retField = retType != null ? new OutField(RET_FIELD_NAME, retType) : null;
	}

  SqlCallable addParams(Object... params) {
    for (Object o : params) {
      Field f = new Field("", o);
      if (o instanceof OutField) {
        f = (OutField)o;
        outFileds.put(f.getField(), (OutField)f);
      }
      fields.add(f);
    }
    return this;
  }

  public SqlCallable in(Object... params) {
    return addParams(params);
  }

  public SqlCallable inOut(String field, Object value, int type) {
    return addParams(new InOutField(field, value, type));
  }

  public SqlCallable out(String field, int type) {
    return addParams(new OutField(field, type));
  }

	@Override
	public String toSql() {
		final StringBuilder sb = new StringBuilder();
    sb.append("{");
    if (retField != null) {
      sb.append("? = ");
    }
		sb.append("call ").append(proc).append("(");
		int i=0;
		for (Field f : fields) {
			sb.append(f.toValueSql());
			if (++i<fields.size()) { sb.append(", "); }
		}
		sb.append(")}\n");
		return sb.toString();
	}

	@Override
	public PreparedStatement toStmt() throws SQLException {
		CallableStatement stmt = conn.prepareCall(toSql());
    int j=0;
    if (retField != null) {
      retField.setStmtValue(stmt, ++j);
    }
		for (Field f : fields) {
			if (!(f instanceof SqlField)) {
				f.setStmtValue(stmt, ++j);
			}
		}
		return stmt;
	}

  @Override
  public SqlCallable execute() throws SQLException {
    CallableStatement cst = null;
    try {
      _log(false);
      cst = (CallableStatement)toStmt();
      cst.executeUpdate();

      int j=0;
      if (retField != null) {
        retField.setValue(cst.getObject(++j));
      }
      for (Field f : fields) {
        if (!(f instanceof SqlField)) {
          ++j;
          if (f instanceof OutField) {
            f.setValue(cst.getObject(j));
//            OutField of = (OutField) f;
//            if (of.getType() == Types.INTEGER) {
//              of.setValue(cst.getInt(j));
//            } else if (of.getType() == Types.VARCHAR) {
//              of.setValue(cst.getString(j));
//            }
          }
        }
      }
      return this;
    } finally {
      closeSilent(cst);
    }
  }

  public OutField getReturn() {
    return retField;
  }

  public OutField getOut(String name) {
    return outFileds.get(name);
  }
}
