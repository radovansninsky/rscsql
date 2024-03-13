package sk.rsc.sql;

import java.math.BigDecimal;
import java.util.*;

import static sk.rsc.sql.Utils.*;

/**
 * Row returned by {@link SqlSelect} when no generic class is provided.
 *
 * @author Radovan Sninsky
 * @since 2012-05-25 11:55
 */
public final class Row {

  // todo: https://www.baeldung.com/java-optional
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
    return toInt(getObject(column));
  }

  public Long getLong(String column) {
    return toLong(getObject(column));
  }

  public Double getDouble(String column) {
    return toDouble(getObject(column));
  }

  public BigDecimal getBigDecimal(String column) {
    return toBigDecimal(getObject(column));
  }

  public Boolean getBoolean(String column) {
    return toBool(getObject(column));
  }

  public Date getDate(String column) {
    return toDate(getObject(column), "yyyy-MM-dd");
  }

  public Date getTimestamp(String column) {
    return toDate(getObject(column), "yyyy-MM-dd'T'HH:mm:ss");
  }

  public <E extends Enum<E>> E getEnum(String column, Class<E> enumType) {
    Map<String, E> map = new HashMap<>(enumType.getEnumConstants().length);
    for (E e : enumType.getEnumConstants()) {
      map.put(e.name(), e);
    }
    return map.get(get(column));
  }

  public List<String> listColumns() {
    return new ArrayList<>(nameMap.keySet());
  }
}
