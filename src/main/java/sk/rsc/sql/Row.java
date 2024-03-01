package sk.rsc.sql;

import java.util.*;

import static sk.rsc.sql.Utils.makeAliasIfDotField;

/**
 * Row returned by {@link SqlSelect} when no generic class is provided.
 *
 * @author Radovan Sninsky
 * @since 2012-05-25 11:55
 */
public final class Row {

  public static final Row EMPTY = new Row("");

  private final String tableName;
  private Map<String, Object> nameMap = new HashMap<String, Object>();

  Row(String tableName) {
    this.tableName = tableName;
  }

  void set(String column, Object value) {
    nameMap.put(column, value);
  }

  public Object getObject(String column) {
    String n1 = makeAliasIfDotField(column);
    String n2 = makeAliasIfDotField(tableName + "." + column);
    return nameMap.get(nameMap.containsKey(n1) ? n1 : n2);
  }

  public boolean isNull(String column) {
    return getObject(column) == null;
  }

  public String get(String column) {
    return getString(column);
  }

  public String getString(String column) {
    Object value = getObject(column);
    return value == null ? null : value.toString();
  }

  public Integer getInt(String column) {
    Object value = getObject(column);
    if (value == null) {
      return null;
    } else if (value instanceof Number) {
      return ((Number) value).intValue();
    } else if (value instanceof String) {
      return Integer.valueOf((String) value);
    } else {
      throw new NumberFormatException("Can't convert to integer");
    }
  }

  public Long getLong(String column) {
    Object value = getObject(column);
    if (value == null) {
      return null;
    } else if (value instanceof Number) {
      return ((Number) value).longValue();
    } else if (value instanceof String) {
      return Long.valueOf((String) value);
    } else {
      throw new NumberFormatException("Can't convert to long");
    }
  }

  public Date getDate(String column) {
    Object value = getObject(column);
    if (value == null) {
      return null;
    } else if (value instanceof java.sql.Date) {
      return new Date(((java.sql.Date) value).getTime());
    } else if (value instanceof java.sql.Timestamp) {
      return new Date(((java.sql.Timestamp) value).getTime());
    } else {
      throw new IllegalArgumentException("Can't convert to date");
    }
  }

  public List<String> listColumns() {
    return new ArrayList<>(nameMap.keySet());
  }
}
