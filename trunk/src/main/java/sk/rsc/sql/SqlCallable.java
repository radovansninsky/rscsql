package sk.rsc.sql;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * SqlCallable.
 *
 * @author Radovan Sninsky
 * @since 20.08.2012 23:32
 */
public class SqlCallable extends SqlParamCmd {

  private static final String RET_FIELD_NAME = "ret";

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
//    if (retField != null) {
//      outFileds.put(retField.field, retField);
//    }
	}

  public SqlCallable call(Object... params) {
    for (Object o : params) {
      if (o instanceof OutField) {
        OutField of = (OutField)o;
        fields.add(of);
        outFileds.put(of.field, of);
      } else {
        fields.add(new Field("", o));
      }
    }
    return this;
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
  public void execute() throws SQLException {
    CallableStatement cst = null;
    try {
      _log(false);
      cst = (CallableStatement)toStmt();
      cst.executeUpdate();

      if (retField != null) {
        retField.setValue(cst.getObject(1));
      }

      for (OutField of : outFileds.values()) {
        if (of.getType() == Types.INTEGER) {
          of.setValue(cst.getInt(of.getField()));
        } else if (of.getType() == Types.VARCHAR) {
          of.setValue(cst.getString(of.getField()));
        }
      }
    } finally {
      closeSilent(cst);
    }
  }

  public String getReturnValue() {
    return retField.getValue() instanceof String ? (String)retField.getValue() : null;
//    return getOutValue(RET_FIELD_NAME);
  }

  public Integer getIntReturnValue() {
    return retField.getValue() instanceof Integer ? (Integer)retField.getValue() : null;
//    return getIntOutValue(RET_FIELD_NAME);
  }

  public String getOutValue(String name) {
    if (outFileds.keySet().contains(name) && outFileds.get(name).getValue() instanceof String) {
      return (String)outFileds.get(name).getValue();
    } else {
      return null;
    }
  }

  public Integer getIntOutValue(String name) {
    if (outFileds.keySet().contains(name) && outFileds.get(name).getValue() instanceof Integer) {
      return (Integer)outFileds.get(name).getValue();
    } else {
      return null;
    }
  }
}
