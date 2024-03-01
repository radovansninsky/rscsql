package sk.rsc.sql;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

import static sk.rsc.sql.Utils.makeAliasIfDotField;

/**
 * Maps JDBC {@link ResultSet} to pojo.
 *
 * @author Radovan Sninsky
 * @see SqlSelect#list(Mapper) and simimlar for usage
 * @since 2012-05-24 16:57
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public abstract class Mapper<T> {

  protected String tableName;
  protected ResultSet rs;
  protected List<String> fields = new ArrayList<>();

  public void init(String tableName, ResultSet rs) throws SQLException {
    this.tableName = tableName;
    this.rs = rs;
    this.fields.clear();

    ResultSetMetaData mrs = rs.getMetaData();
    for (int i = 1; i <= mrs.getColumnCount(); i++) {
      fields.add(mrs.getColumnLabel(i).toLowerCase());
    }
  }

  protected abstract T toObject() throws SQLException;

  protected String get(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getString(n1) :
      fields.contains(n2.toLowerCase()) ? rs.getString(n2) : null;
  }

  protected String getString(String name) throws SQLException {
    return get(name);
  }

  protected Integer getInt(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getObject(n1) != null ? rs.getInt(n1) : null :
      fields.contains(n2.toLowerCase()) && rs.getObject(n2) != null ? rs.getInt(n2) : null;
  }

  protected Long getLong(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getObject(n1) != null ? rs.getLong(n1) : null :
      fields.contains(n2.toLowerCase()) && rs.getObject(n2) != null ? rs.getLong(n2) : null;
  }

  protected Boolean getBoolean(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getObject(n1) != null ? rs.getBoolean(n1) : null :
      fields.contains(n2.toLowerCase()) && rs.getObject(n2) != null ? rs.getBoolean(n2) : null;
  }

  protected Double getDouble(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getObject(n1) != null ? rs.getDouble(n1) : null :
      fields.contains(n2.toLowerCase()) && rs.getObject(n2) != null ? rs.getDouble(n2) : null;
  }

  protected BigDecimal getBigDecimal(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? rs.getObject(n1) != null ? rs.getBigDecimal(n1) : null :
      fields.contains(n2.toLowerCase()) && rs.getObject(n2) != null ? rs.getBigDecimal(n2) : null;
  }

  protected Date getDate(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? toDate(rs.getDate(n1)) :
      fields.contains(n2.toLowerCase()) ? toDate(rs.getDate(n2)) : null;
  }

  protected Date getTimestamp(String name) throws SQLException {
    assert name != null : "Field name is null";
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? toDate(rs.getTimestamp(n1)) :
      fields.contains(n2.toLowerCase()) ? toDate(rs.getTimestamp(n2)) : null;
  }

  protected <E extends Enum<E>> E getEnum(String name, Class<E> enumType) throws SQLException {
    assert name != null : "Field name is null";

    Map<String, E> map = new HashMap<>(enumType.getEnumConstants().length);
    for (E e : enumType.getEnumConstants()) {
      map.put(e.name(), e);
    }
    String n1 = makeAliasIfDotField(name);
    String n2 = makeAliasIfDotField(tableName + "." + name);
    return fields.contains(n1.toLowerCase()) ? map.get(rs.getString(n1)) :
      fields.contains(n2.toLowerCase()) ? map.get(rs.getString(n2)) : null;
  }

  public Date toDate(java.sql.Date val) {
    return val != null ? new Date(val.getTime()) : null;
  }

  public Date toDate(java.sql.Timestamp val) {
    return val != null ? new Date(val.getTime()) : null;
  }
}
