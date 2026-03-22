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

  protected Field field;

  public InOutField(String field, Object value, int type) {
    super(field, type);
    this.field = Fields.create(field, value);
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    field.setStmtValue(stmt, i);
    super.setStmtValue(stmt, i);
  }
}
