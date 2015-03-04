package sk.rsc.sql.fields;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Trieda OutField.
 *
 * @author Radovan Sninsky
 * @since 11.10.2013 12:23
 */
public class OutField extends Field {

	protected int type;

	public OutField(String field, int type) {
		super(field);

    this.type = type;
	}

	@Override
	public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
		if (stmt instanceof CallableStatement) {
			((CallableStatement)stmt).registerOutParameter(i, type);
		}
	}

  @Override
  public String toString() {
    return asString();
  }

  public boolean isNull() {
    return value == null;
  }

  public String asString() {
    return value == null ? null : value.toString();
  }

  public Integer asInt() {
    if (value == null) {
      return null;
    } else if (value instanceof Number) {
      return ((Number)value).intValue();
    } else if (value instanceof String) {
      return Integer.valueOf((String)value);
    } else {
      throw new NumberFormatException("Can't convert to integer");
    }
  }

  public Long asLong() {
    if (value == null) {
      return null;
    } else if (value instanceof Number) {
      return ((Number)value).longValue();
    } else if (value instanceof String) {
      return Long.valueOf((String)value);
    } else {
      throw new NumberFormatException("Can't convert to long");
    }
  }

  public Date asDate() {
    if (value == null) {
      return null;
    } else if (value instanceof java.sql.Date) {
      return new Date(((java.sql.Date)value).getTime());
    } else if (value instanceof java.sql.Timestamp) {
      return new Date(((java.sql.Timestamp)value).getTime());
    } else {
      throw new IllegalArgumentException("Can't convert to date");
    }
  }
}
