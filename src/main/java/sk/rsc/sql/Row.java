package sk.rsc.sql;

import java.util.*;

/**
 * Row returned by {@link SqlSelect} when no generic class is provided.
 *
 * @author Radovan Sninsky
 * @since 2012-05-25 11:55
 */
public final class Row {

  public static final Row EMPTY = new Row();

  private Map<String, Object> nameMap = new HashMap<String, Object>();

  public Row() {
  }

  public void set(String column, Object value) {
    nameMap.put(column, value);
  }

  public Object getObject(String column) {
    return nameMap.get(column);
  }

  public boolean isNull(String column) {
    return nameMap.get(column) == null;
  }

  public String get(String column) {
    return getString(column);
  }

  public String getString(String column) {
    Object value = nameMap.get(column);
    return value == null ? null : value.toString();
  }

  public Integer getInt(String column) {
    Object value = nameMap.get(column);
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
    Object value = nameMap.get(column);
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
    Object value = nameMap.get(column);
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
		return new ArrayList<String>(nameMap.keySet());
	}
}
