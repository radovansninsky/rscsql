package sk.rsc.sql.restrictions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Native SQL restriction simple or parametrized.
 *
 * @author Radovan Sninsky
 * @since 2012-05-21 17:59
 */
public final class NativeRestriction implements Restriction {

  private final String sql;
  private final Object[] values;

  public NativeRestriction(String simpleSql) {
    this.sql = simpleSql;
    this.values = null;
  }

  public NativeRestriction(String parametrizedSql, Object... values) {
    this.sql = parametrizedSql;
    this.values = values;
  }

  @Override
  public boolean hasValues() {
    return values != null && values.length > 0;
  }

  @Override
  public List<Object> getValues() {
    return values == null ? Collections.emptyList() : Arrays.asList(values);
  }

  @Override
  public String toSql() {
    return sql;
  }
}
