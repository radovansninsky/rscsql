package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    if (value == null) {
      stmt.setObject(i, null);
    } else {
      stmt.setObject(i, value);
    }
  }

  public String toValueSql() {
    return "?";
  }
}
