package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * Date field.
 *
 * @author Radovan Sninsky
 * @since 2026-03-21
 */
public class DateField extends Field {
  public DateField(String field, Date value) {
    super(field, value);
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    Date d = (Date) value;
    if (d != null) {
      stmt.setTimestamp(i, new Timestamp(d.getTime()));
    } else {
      stmt.setNull(i, Types.TIMESTAMP);
    }
  }
}
