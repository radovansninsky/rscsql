package sk.rsc.sql.fields;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Enum field.
 *
 * @author Radovan Sninsky
 * @since 2026-03-21
 */
public class EnumField extends Field {
  public EnumField(String field, Enum<?> value) {
    super(field, value);
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    if (value == null) {
      stmt.setNull(i, java.sql.Types.VARCHAR);
    } else {
      stmt.setString(i, ((Enum<?>) value).name());
    }
  }
}
