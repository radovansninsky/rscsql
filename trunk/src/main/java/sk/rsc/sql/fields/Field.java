package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * Field.
 *
 * @author Radovan Sninsky
 * @since 2012-07-14 00:09
 */
public class Field {

	protected String field;
	protected Object value;

	public Field(String field) {
    this(field, null);
	}

	public Field(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public Object getValue() {
		return value;
	}

  public void setValue(Object value) {
    this.value = value;
  }

  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
		if (value instanceof Date) {
			setTimestamp(stmt, i, (Date) value);
		} else {
			if (value != null) {
				stmt.setObject(i, value);
			} else {
				stmt.setNull(i, Types.VARCHAR);
			}
		}
	}

	protected void setTimestamp(PreparedStatement stmt, int i, Date d) throws SQLException {
		if (d != null) {
			stmt.setTimestamp(i, new Timestamp(d.getTime()));
		} else {
			stmt.setNull(i, Types.TIMESTAMP);
		}
	}

	public String toValueSql() {
		return "?";
	}
}
