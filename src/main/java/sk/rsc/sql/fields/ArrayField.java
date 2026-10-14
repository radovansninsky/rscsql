package sk.rsc.sql.fields;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ArrayField extends Field {
  private final String type;

  public ArrayField(String field, String type, Object[] value) {
    super(field, value);
    this.type = type;
  }

  public ArrayField(String field, String type, Collection<?> value) {
    super(field, value);
    this.type = type;
  }

  @Override
  public void setStmtValue(PreparedStatement stmt, int i) throws SQLException {
    if (value == null) {
      stmt.setObject(i, null);
    } else {
      Array array = stmt.getConnection().createArrayOf(
        type,
        value instanceof Collection ? ((Collection<?>) value).toArray() : (Object[]) value
      );
      stmt.setArray(i, array);
    }
  }
}
