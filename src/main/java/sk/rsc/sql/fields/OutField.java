package sk.rsc.sql.fields;

import sk.rsc.sql.Utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

import static sk.rsc.sql.Utils.toInt;
import static sk.rsc.sql.Utils.toLong;

/**
 * Out field.
 *
 * @author Radovan Sninsky
 * @since 2013-10-11 12:23
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
    return toInt(value);
  }

  public Long asLong() {
    return toLong(value);
  }

  public Date asDate() {
    return Utils.toDate(value);
  }

  public Instant asInstant() {
    return Utils.toInstant(value);
  }
}
