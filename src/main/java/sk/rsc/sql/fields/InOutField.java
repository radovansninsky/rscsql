package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * InOut field.
 *
 * @author Radovan Sninsky
 * @since 2015-03-04 21:13
 */
public class InOutField extends OutField {

  public InOutField(String field, Object value, int type) {
    super(field, type);
    this.value = value;
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    if (value instanceof Date) {
      setTimestamp(stmt, i, (Date) value);
    } else {
      if (value != null) {
        stmt.setObject(i, value);
      } else {
        stmt.setNull(i, type);
      }
    }
    super.setStmtValue(stmt, i);
  }
}
