package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;

/**
 * Enum field.
 *
 * @author Radovan Sninsky
 * @since 2026-03-21
 */
public class InstantField extends Field {
  public InstantField(String field, Instant value) {
    super(field, value);
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    if (value == null) {
      stmt.setNull(i, java.sql.Types.TIMESTAMP);
    } else {
      stmt.setTimestamp(i, new java.sql.Timestamp(((Instant) value).toEpochMilli()));
    }
  }
}
