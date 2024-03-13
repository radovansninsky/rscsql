package sk.rsc.sql;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utils.
 *
 * @author Radovan Sninsky
 * @since 2017-10-11 15:59
 */
public final class Utils {

  public static String makeAliasIfDotField(String column) {
    if (column.contains(".")) {
//      int i = column.indexOf(".");
//      return column.substring(0, i) + "$_$" + column.substring(i+1);
      return column.replace(".", "$_$");
    }
    return column;
  }

  public static Integer toInt(Object value) {
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

  public static Long toLong(Object value) {
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

  public static Double toDouble(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Number) {
      return ((Number) value).doubleValue();
    } else if (value instanceof String) {
      return Double.valueOf((String) value);
    } else {
      throw new NumberFormatException("Can't convert to double");
    }
  }

  public static BigDecimal toBigDecimal(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    } else if (value instanceof Number) {
      return BigDecimal.valueOf(((Number) value).doubleValue());
    } else if (value instanceof String) {
      return new BigDecimal((String) value);
    } else {
      throw new NumberFormatException("Can't convert to big decimal");
    }
  }

  public static Boolean toBool(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof Boolean) {
      return (Boolean) value;
    } else if (value instanceof String) {
      return Boolean.valueOf((String) value);
    } else {
      throw new RuntimeException("Can't convert to boolean");
    }
  }

  public static Date toDate(Object value, String pattern) {
    if (value == null) {
      return null;
    } else if (value instanceof java.sql.Date) {
      return new Date(((java.sql.Date) value).getTime());
    } else if (value instanceof java.sql.Timestamp) {
      return new Date(((java.sql.Timestamp) value).getTime());
    } else if (value instanceof String) {
      try {
        return new SimpleDateFormat(pattern).parse((String)value);
      } catch (ParseException e) {
        throw new RuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException("Can't convert to date");
    }
  }
}
